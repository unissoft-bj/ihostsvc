#!/usr/bin/env python
# -*- coding: utf-8 *-*
__author__ = 'andy'
# create 2015.03.01
import os, sys, time, traceback
import xml.dom.minidom as minidom
import MySQLdb,  datetime
import netifaces
import subprocess
from uPktDbService import uPktDbService
from uPktModel import uPktModel
from uStaModel import uStaModel

class uWlcapController:
    # 
    log = None
    odbsvr = None    
    def __init__(self,log):
        self.log = log
        self.odbsvr = uPktDbService(self.log)
    
    def line2db (self,linstr,sip):        
        # global variables
        global filterlist,  dlist, filterindex,  comwin,  bang, staying,  losting,  lf, typelist,  blklist
        global chkrssil,  chkrssih,  maxtimediff
        self.log.debug('line2db start')
        self.log.debug("line2db.linstr: %s" % linstr)
        
        # "Feb 16, 2014 12:43:18.256717000","ppi:wlan",,"-77","0x04","ff:ff:ff:ff:ff:ff","8c:be:be:fb:7c:38","ff:ff:ff:ff:ff:ff","AP"
        # "Feb 16, 2014 17:29:24.562819000","radiotap:wlan","-51",,"0x04","ff:ff:ff:ff:ff:ff","00:1b:77:1c:92:55","ff:ff:ff:ff:ff:ff","Unis"
        
        # update self.filterlist 
        #print "self.filterlist here --------------------------------------"
        for i in range(len(self.filterlist)-1, -1, -1):
            #print(self.filterlist[i])
            dtlastseen = datetime.datetime.strptime(self.filterlist[i][2], "%b %d, %Y %H:%M:%S")
            self.log.debug('line2db.dtlastseen: %s' % dtlastseen)
            if (abs(datetime.datetime.now()-dtlastseen).seconds) > int(losting/lf) : # half of losting
                #print('pop :' , (datetime.datetime.now()-dtlastseen).seconds )
                #print(type(self.filterlist))
                del self.filterlist[i]
        
        # replace ,, to ,   --double comma to single
        linstr = linstr.replace(",,", ",")
        self.log.debug("line2db.linstr replaced: %s" % linstr)
        
        #find a character as spliter 
        for s in (":", "^", "~", "%", "`", "!", "@", "#","$","*","(",")","-","="):
            r = linstr.find(s)
            if r < 0: break
        
        # replace 
        linstr = linstr.replace("\",\"", s) #set splitter
        linstr = linstr.replace("\"", "")
        linstr = linstr.replace("\'", "")  #to avoid sql statement error
        
        self.log.debug("line2db.linstr replaced2: %s" % linstr)   
        # use  "," for splitter
        dlist=linstr.split(s)
        if len(dlist) < 8 : 
            dlist.append("n/a") #for packets without a ssid name
    
        dlist.append(sip)
        
        self.log.debug("line2db.dlist: %s" % dlist)
    
        #packet data exceptions
        # 0-frametime 1-protocal 2-rssi 3-subtype 4-da 5-sa 6-bssid 7-ssid 
        try:
            for i in (1, 4, 5, 6): 
                if dlist[i].find(",") > 0:
                    dlist[i]=dlist[i][0:dlist[i].index(",")]
        except Exception as e:
            self.log.error(e)
            traceback.print_exc()
    
        #print(dlist)
        if dlist[3] not in("0x00", "0x02", "0x04") : # ASSOC_REQ REASSOC_REQ PROBE_REQ    
            dlist[4], dlist[5] = dlist[5], dlist[4]
        
        self.log.debug("line2db.dlist checked: %s" % dlist)
        # pre-check 
        rssiisok = 0
        pkttimeisok = 0
        macisok = 0
        typeisok = 0
        
        #dlist[0] string to datetime part and minus part 
        ptime = str(dlist[0])
        self.log.debug("line2db.ptime: %s" % ptime)
        
        ptimelist = ptime.split(".")  #type of ptimelist[0] is string  "%b %d, %Y %H:%M:%S"
        self.log.debug("line2db.ptimelist: %s" % ptimelist)
        
        try:
            chkpkttime = datetime.datetime.strptime(ptimelist[0], "%b %d, %Y %H:%M:%S")
            self.log.debug("line2db.chkpkttime: %s" % chkpkttime)
            if abs(abs(datetime.datetime.now()-chkpkttime).seconds) < self.maxtimediff : pkttimeisok = 1
            self.log.debug("line2db.pkttimeisok: %s" % pkttimeisok)
            
        except Exception as e:
            pkttimeisok = 0
            self.log.error(e)
            traceback.print_exc()
        
        try:
            if int(dlist[2]) > self.chkrssil and int(dlist[2]) < self.chkrssih : rssiisok = 1
            self.log.debug("line2db.rssiisok: %s" % rssiisok)
            
        except Exception as e:
            rssiisok = 0
            self.log.error(e)
            traceback.print_exc()
        
        if dlist[5] not in blklist : macisok = 1
        self.log.debug("line2db.macisok: %s" % macisok)
        
        #print dlist[5], blklist
        if dlist[3] in typelist : typeisok = 1
        self.log.debug("line2db.typeisok: %s" % typeisok)
        #print dlist[3], typelist
    
        self.dlist2db()  #only for test
        
        #print "macisok, typeisok, rssiisok, pkttimeisok:", macisok,typeisok,rssiisok,pkttimeisok    
        if macisok == 1 and typeisok == 1 and rssiisok == 1 and pkttimeisok == 1:
            #prepare pkttime string    
            structpkttime = datetime.datetime.strptime(ptimelist[0], "%b %d, %Y %H:%M:%S")
            self.log.debug("line2db.structpkttime: %s" % structpkttime)
            #strpkttime = time.strftime("%Y-%m-%d %X", structpkttime) 
        
            # check for existence
            filteritem=[fitem for fitem in self.filterlist if ((type(fitem)==list) and (dlist[5] in fitem)) ]
            self.log.debug("line2db.filteritem: %s" % filteritem)
            #print "filteritem here:---------------"
            #print(filteritem)            
            #print '-----------------------------------------ok'
            if len(filteritem) > 0 :  # mac in filter
                
                structfirstseen =  datetime.datetime.strptime(filteritem[0][1], "%b %d, %Y %H:%M:%S")
                structlastseen =  datetime.datetime.strptime(filteritem[0][2], "%b %d, %Y %H:%M:%S")
                
                if (structlastseen - structfirstseen).seconds > staying :
                    win = stawin
                else:
                    win = comwin
                self.log.debug("line2db.win: %s" % win)
                    
                if (structpkttime - structlastseen).seconds > int(win)  or  (dlist[2] > int(bang) and dlist[2] < 0 ):
                    self.dlist2db()
                    filterindex = self.filterlist.index(filteritem[0])  # findout the filter index  #filteritem is a [[]], filteritem[0] is a []
                    self.filterlist[filterindex][2] = ptimelist[0]  # update lastseen in the filter
            else:
                self.dlist2db()
                self.filterlist.append([dlist[5], ptimelist[0], ptimelist[0],""])
    
    def dlist2db (self):    
        
        global filterlist, dlist, filterindex, cnx
            
        #dlist[0] string to datetime part and minus part 
        ptime = str(dlist[0])
        ptimelist = ptime.split(".")
        struct_time = time.strptime(ptimelist[0], "%b %d, %Y %H:%M:%S")
        
        # split packtime to minutes part & seconds part
        timelist=[""]*9
        for i in range(0, 9):
            timelist[i]=struct_time[i]
        secondpart =timelist[5]
        timelist[5]=0              
        timetuple = tuple(timelist)
        timebyminute=time.strftime("%Y-%m-%d %X", timetuple)
        minutepart = timelist[4]
        timelist[4]=0
        timetuple = tuple(timelist)
        timebyhour=time.strftime("%Y-%m-%d %X", timetuple)
        
        #prepare insert sql statement
        opkt = uPktModel()
        opkt.mac = str(dlist[5])
        opkt.ssid = str(dlist[7])
        opkt.rssi = str(dlist[2])
        opkt.stat = '0'
        opkt.type = ''
        opkt.subtype = str(dlist[3])
        opkt.pmac = str(dlist[4])
        opkt.bssid = str(dlist[6])
        opkt.pkttime = time.strftime("%Y-%m-%d %X", struct_time)
        opkt.timefrac = '0.'+str(ptimelist[1])
        opkt.frameproto = str(dlist[1])
        opkt.chan = ''
        opkt.RecTime = str(datetime.datetime.now())
        opkt.srcip = str(dlist[8])
        self.log.debug('dlist2db.opkt: %s' % opkt)
        
        #print(insert_activepacket_sql)
        try:
            self.odbsvr.savepktmodel(opkt)            
            mac = self.odbsvr.getmacbymacsrcip(opkt.mac,opkt.srcip) 
            #print "-----------select result--------"
            #print mac
            try:
                # mac in wlsta
                if mac is None:
                    tmp = 0
                else:
                    tmp =len(mac)
                osta = uStaModel()                
                osta.ssid = str(dlist[7])
                osta.rssi = str(dlist[2])
                osta.lastseen = time.strftime("%Y-%m-%d %X", struct_time) 
                #osta.npacket = str(dlist[7])
                osta.recTime = str(datetime.datetime.now())
                
                # params
                osta.mac = str(dlist[5]) 
                osta.srcip = str(dlist[8]) 
                
                #print "update...."
                #update wlsta
                try:
                    self.odbsvr.updatestamodel(osta)
                except Exception as e:
                    self.log.error(e)
                    traceback.print_exc()
                
                #update wlact
                row = self.odbsvr.getstatfirstseenbymacsrcip(str(dlist[5]),str(dlist[8])) 

                dtpkttime = datetime.datetime.strptime(ptimelist[0], "%b %d, %Y %H:%M:%S")
                if (dtpkttime - row[1]).seconds > int(self.staying) and row[0] <> '200':
                    osta = uStaModel()
                    osta.mac = str(dlist[5])
                    osta.srcip = str(dlist[8])
                    
                    self.odbsvr.updatestat(osta)
                        
            #mac not in wlsta
            except Exception as e:
                self.log.error(e)
                traceback.print_exc()
                self.log.error('traceback :%s' % traceback.print_exc())
                # mac not in wlsta
                #insert into wlsta
                osta = uStaModel()
                osta.mac = str(dlist[5]) 
                osta.ssid = str(dlist[7])
                osta.rssi = str(dlist[2])
                osta.firstseen = time.strftime("%Y-%m-%d %X", struct_time) 
                osta.lastseen = time.strftime("%Y-%m-%d %X", struct_time) 
                osta.npacket = '1'
                osta.recTime = str(datetime.datetime.now())  
                osta.srcip = str(dlist[8]) 
                
                #insert into wlsta
                try:
                    self.odbsvr.savestamodel(osta)
                except Exception as e:
                    self.log.error(e)
                    traceback.print_exc()                    
                
        except Exception as err:
            print("insert record 'wlpkt' failed.")
            self.log.error(err)
            traceback.print_exc() 
             
    def updatefilter(self):
        global blklist, typelist
        # read typelist and blklist from filter.xml , global, static in file 
        dom = minidom.parse("filter.xml")
        blklist = []
        typelist = []
        for node in dom.getElementsByTagName("lt"):
            if node.getAttribute("name") == "address" and node.getAttribute("type") == "macblk":
                blklist.append(node.getAttribute("value"))
            if node.getAttribute("name") == "types" and node.getAttribute("type") == "wlsub":
                typelist.append(node.getAttribute("value"))
    
    def updatewlsta(self):
        global cnx,  gone,  losting
        global idlecount
        
        strNow = str(datetime.datetime.now())
        #stat: 100-comming 200-staying 300-losting 
        str_gone = "delete from wlsta where \
                TIMESTAMPDIFF(SECOND, lastseen,'" + strNow + "') > '" + str(self.gone) +"'"
        str_losting = "update wlsta set stat = '300' where \
                TIMESTAMPDIFF(SECOND, lastseen,'" + strNow + "') > '" + str(self.losting) + "' and \
                TIMESTAMPDIFF(SECOND, lastseen,'" + strNow + "') <= '" + str(self.gone) + "' and  \
                stat <> '300'"          
        try:
            self.odbsvr.execute(str_gone)
            self.odbsvr.execute(str_losting)
        except Exception as err:
            print("insert record 'activepackets' failed.")
            #print("Error: {}".format(err.args[1]))   
            self.log.error(err)
            traceback.print_exc()
  
    #if __name__ == '__main__':
    def run(self):    
        #read configurations here
        dom = minidom.parse("config.xml")
        for node in dom.getElementsByTagName("interface"):
            iftype = node.getAttribute("iftype")
            sip = node.getAttribute("host")
            pktpipe =node.getAttribute("pktpipe")
            
        if iftype == "local":
            interfaces = netifaces.interfaces()
    
            for i in interfaces:
                if i == 'eth0':
                    iface = netifaces.ifaddresses(i).get(netifaces.AF_INET)
                    #print iface
                    if iface != None:
                        sip = iface[0]['addr']
                        #print sip    
        for node in dom.getElementsByTagName("dbconn"):
            user = node.getAttribute("user")
            pwd =node.getAttribute("pwd")
            host = node.getAttribute("host")
            db = node.getAttribute("db")
            
        for node in dom.getElementsByTagName("ppdb"):
            self.staying = int(node.getAttribute("staying"))
            self.losting = int(node.getAttribute("losting"))
            self.lf = float(node.getAttribute("lf"))
            self.gone = int(node.getAttribute("gone"))
        
        for node in dom.getElementsByTagName("rdpp"):
            self.comwin = int(node.getAttribute("comwin"))
            self.stawin = int(node.getAttribute("stawin"))
            self.bang = int(node.getAttribute("bang"))
            self.chkfilter = int(node.getAttribute("chkfilter"))
            self.chksta = int(node.getAttribute("chksta"))
            self.chkrssil = int(node.getAttribute("chkrssil"))
            self.chkrssih = int(node.getAttribute("chkrssih"))
            self.maxtimediff = int(node.getAttribute("maxtimediff"))
            
        #update pre-filter for incoming packets. lists are global, ,updated by main loop periodcally
        blklist = []
        typelist = []
        self.updatefilter()
        
        #prepare filter list [mac, firstseen, lastseen, npacket], global , dynamic in memory
        self.filterlist=[""]
        self.filterlist.pop()
        filterindex =""
        
        # 0-frametime 1-protocal 2-rssi 3-subtype 4-da 5-sa 6-bssid 7-ssid ,global
        dlist=[""]*8        
        try:
            pktsrc = open(pktpipe, 'r',  buffering= 1)
            #pktsrc = '"Feb 16, 2014 12:43:18.256717000","ppi:wlan",,"-77","0x04","ff:ff:ff:ff:ff:ff","8c:be:be:fb:7c:38","ff:ff:ff:ff:ff:ff","AP"'
        except Exception as e:
            self.log.error(e)
            traceback.print_exc()
            print("Error Opening pktsrc")
            sys.exit(0)
            
        chkfcount = self.chkfilter
        chkstacount = self.chksta
        self.updatewlsta()
        
        idlecount = 0
        try:
            while True:    
                #print chkfcount,chkstacount
                chkfcount = chkfcount  - 1
                chkstacount = chkstacount - 1
                try:
                    line = ""
                    #while len(line) == 0 :
                        #read line from pipe, untile len(line) > 0.  hold here when a broken pipe occurs (wlcap not running)
                    line = pktsrc.readline()[:-1]
                    
                    #line = '"Feb 16, 2014 17:29:24.562819000","radiotap:wlan","-51",,"0x04","ff:ff:ff:ff:ff:ff","00:1b:77:1c:92:55","ff:ff:ff:ff:ff:ff","Unis"'
                    self.log.debug('run.line: %s' % line)
                        #print "line here---------"
                        #print line
                    #parse line to db
                    try:
                        self.log.debug('run.line size: %s' % len(line))
                        if len(line) >= 90:  #length of line is about 120
                            idlecount = 0
                            self.line2db(line,sip)                            
                        else:
                            self.log.info('run.line <90: %s' % line)
                            time.sleep(1)
                            idlecount = idlecount + 1
                            self.log.info('run.idlecount[max 30]: %s' % idlecount)
                            if(idlecount >= 30):
                                subprocess.Popen('kill',shell=True)
                                self.log.info('I have long time no packets are received, I have to quit!')
                                sys.exit(0)
                                
                    except Exception as e:
                        self.log.error(e)
                        #print self.filterlist
                        traceback.print_exc()
                    
                    #time.sleep(15)
                except Exception as e:
                    self.log.error(e)
                    traceback.print_exc()
                    sys.exit(0)
                
                # update filter lists from filter.xml
                if chkfcount < 0:
                    #updte typelist and blklist from file
                    print "update filter--- "
                    self.updatefilter()
                    chkfcount = chkfilter
                    
                # update wlsta table and wlact table
                if chkstacount < 0:
                    print "update ---------wlsta--- "
                    self.updatewlsta()
                    chkstacount = chksta
                    
        except KeyboardInterrupt as err:
            traceback.print_exc()
            self.log.erro(err)
            print("\n---End of Loop---") 