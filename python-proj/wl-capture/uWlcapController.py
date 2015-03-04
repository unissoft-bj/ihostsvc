#!/usr/bin/env python
# -*- coding: utf-8 *-*
__author__ = 'andy'
# create 2015.03.01
import os, sys, time, traceback
import xml.dom.minidom as minidom
import MySQLdb,  datetime
import netifaces
from uPktDbService import uPktDbService

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
        
        # "Feb 16, 2014 12:43:18.256717000","ppi:wlan",,"-77","0x04","ff:ff:ff:ff:ff:ff","8c:be:be:fb:7c:38","ff:ff:ff:ff:ff:ff","AP"
        # "Feb 16, 2014 17:29:24.562819000","radiotap:wlan","-51",,"0x04","ff:ff:ff:ff:ff:ff","00:1b:77:1c:92:55","ff:ff:ff:ff:ff:ff","Unis"
        
        # update filterlist 
        #print "filterlist here --------------------------------------"
        for i in range(len(filterlist)-1, -1, -1):
            #print(filterlist[i])
            dtlastseen = datetime.datetime.strptime(filterlist[i][2], "%b %d, %Y %H:%M:%S")
            if (abs(datetime.datetime.now()-dtlastseen).seconds) > int(losting/lf) : # half of losting
                #print('pop :' , (datetime.datetime.now()-dtlastseen).seconds )
                #print(type(filterlist))
                del filterlist[i]
        
        # replace ,, to ,   --double comma to single
        linstr = linstr.replace(",,", ",")
        #find a character as spliter 
        for s in (":", "^", "~", "%", "`", "!", "@", "#","$","*","(",")","-","="):
            r = linstr.find(s)
            if r < 0: break
        
        # replace 
        linstr = linstr.replace("\",\"", s) #set splitter
        linstr = linstr.replace("\"", "")
        linstr = linstr.replace("\'", "")  #to avoid sql statement error
           
        # use  "," for splitter
        dlist=linstr.split(s)
        if len(dlist) < 8 : 
            dlist.append("n/a") #for packets without a ssid name
    
        dlist.append(sip)
    
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
        
        # pre-check 
        rssiisok = 0
        pkttimeisok = 0
        macisok = 0
        typeisok = 0
        
        #dlist[0] string to datetime part and minus part 
        ptime = str(dlist[0])
        ptimelist = ptime.split(".")  #type of ptimelist[0] is string  "%b %d, %Y %H:%M:%S"
        
        try:
            chkpkttime = datetime.datetime.strptime(ptimelist[0], "%b %d, %Y %H:%M:%S")
            if abs(abs(datetime.datetime.now()-chkpkttime).seconds) < maxtimediff : pkttimeisok = 1
        except Exception as e:
            pkttimeisok = 0
            self.log.error(e)
            traceback.print_exc()
        
        try:
            if int(dlist[2]) > chkrssil and int(dlist[2]) < chkrssih : rssiisok = 1
        except Exception as e:
            rssiisok = 0
            self.log.error(e)
            traceback.print_exc()
        
        if dlist[5] not in blklist : macisok = 1
        #print dlist[5], blklist
        if dlist[3] in typelist : typeisok = 1
        #print dlist[3], typelist
    
        #print "macisok, typeisok, rssiisok, pkttimeisok:", macisok,typeisok,rssiisok,pkttimeisok    
        if macisok == 1 and typeisok == 1 and rssiisok == 1 and pkttimeisok == 1:
            #prepare pkttime string    
            structpkttime = datetime.datetime.strptime(ptimelist[0], "%b %d, %Y %H:%M:%S")
            #strpkttime = time.strftime("%Y-%m-%d %X", structpkttime) 
        
            # check for existence
            filteritem=[fitem for fitem in filterlist if ((type(fitem)==list) and (dlist[5] in fitem)) ]
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
                    
                if (structpkttime - structlastseen).seconds > int(win)  or  (dlist[2] > int(bang) and dlist[2] < 0 ):
                    self.dlist2db()
                    filterindex = filterlist.index(filteritem[0])  # findout the filter index  #filteritem is a [[]], filteritem[0] is a []
                    filterlist[filterindex][2] = ptimelist[0]  # update lastseen in the filter
            else:
                self.dlist2db()
                filterlist.append([dlist[5], ptimelist[0], ptimelist[0],""])
    
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
        
        #print(insert_activepacket_sql)
        try:
            odbsvr.savepktmodel(opkt)            
            mac = odbsvr.getmacbymacsrcip(opkt.mac,opkt.srcip) 
            #print "-----------select result--------"
            #print mac
            try:
                # mac in wlsta
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
                    odbsvr.updatestamodel(osta)
                except Exception as e:
                    self.log.error(e)
                    traceback.print_exc()
                
                #update wlact
                row = odbsvr.getstatfirstseenbymacsrcip(str(dlist[5]),str(dlist[8])) 

                dtpkttime = datetime.datetime.strptime(ptimelist[0], "%b %d, %Y %H:%M:%S")
                if (dtpkttime - row[1]).seconds > int(staying) and row[0] <> '200':
                    osta = uStaModel()
                    osta.mac = str(dlist[5])
                    osta.srcip = str(dlist[8])
                    
                    odbsvr.updatestat(osta)
                        
            #mac not in wlsta
            except Exception as e:
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
                    odbsvr.savestamodel(osta)
                except Exception as e:
                    self.log.error(e)
                    traceback.print_exc()                    
                
        except Exception as err:
            print("insert record 'wlpkt' failed.")
            print("Error: {}".format(err.args[1])) 
             
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
        
        strNow = str(datetime.datetime.now())
        #stat: 100-comming 200-staying 300-losting 
        str_gone = "delete from wlsta where \
                TIMESTAMPDIFF(SECOND, lastseen,'" + strNow + "') > '" + str(gone) +"'"
        str_losting = "update wlsta set stat = '300' where \
                TIMESTAMPDIFF(SECOND, lastseen,'" + strNow + "') > '" + str(losting) + "' and \
                TIMESTAMPDIFF(SECOND, lastseen,'" + strNow + "') <= '" + str(gone) + "' and  \
                stat <> '300'"          
        try:
            odbsvr.execute(str_gone)
            odbsvr.execute(str_losting)
        except Exception as err:
            print("insert record 'activepackets' failed.")
            print("Error: {}".format(err.args[1]))   
  
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
            staying = int(node.getAttribute("staying"))
            losting = int(node.getAttribute("losting"))
            lf = float(node.getAttribute("lf"))
            gone = int(node.getAttribute("gone"))
        
        for node in dom.getElementsByTagName("rdpp"):
            comwin = int(node.getAttribute("comwin"))
            stawin = int(node.getAttribute("stawin"))
            bang = int(node.getAttribute("bang"))
            chkfilter = int(node.getAttribute("chkfilter"))
            chksta = int(node.getAttribute("chksta"))
            chkrssil = int(node.getAttribute("chkrssil"))
            chkrssih = int(node.getAttribute("chkrssih"))
            maxtimediff = int(node.getAttribute("maxtimediff"))
            
        #update pre-filter for incoming packets. lists are global, ,updated by main loop periodcally
        blklist = []
        typelist = []
        self.updatefilter()
        
        #prepare filter list [mac, firstseen, lastseen, npacket], global , dynamic in memory
        filterlist=[""]
        filterlist.pop()
        filterindex =""
        
        # 0-frametime 1-protocal 2-rssi 3-subtype 4-da 5-sa 6-bssid 7-ssid ,global
        dlist=[""]*8        
        try:
            pktsrc = open(pktpipe, 'r',  buffering= 1)
        except Exception as e:
            self.log.error(e)
            traceback.print_exc()
            print("Error Opening pktsrc")
            sys.exit(0)
            
        chkfcount = chkfilter
        chkstacount = chksta
        self.updatewlsta()
        
        try:
            while True:    
                #print chkfcount,chkstacount
                chkfcount = chkfcount  - 1
                chkstacount = chkstacount - 1
                try:
                    line = ""
                    while len(line) == 0 :
                        #read line from pipe, untile len(line) > 0.  hold here when a broken pipe occurs (wlcap not running)
                        line = pktsrc.readline()[:-1]
                        #print "line here---------"
                        #print line
                    #parse line to db
                    try:
                        if len(line) >= 90:  #length of line is about 120
                            self.line2db(line,sip)
                    except Exception as e:
                        self.log.error(e)
                        #print filterlist
                        traceback.print_exc()
                except Exception as e:
                    self.log.error(e)
                    traceback.print_exc()
                    cnx.close()
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
            print("\n---End of Loop---") 