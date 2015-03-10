# -*- coding: utf-8 *-*
__author__ = 'andy'
# create 2015.02.28

import os, sys, time, traceback
import xml.dom.minidom as minidom
import MySQLdb 

from uLogService import uLogService
from uDbFactory import uDbFactory
from uPktModel import uPktModel
from uStaModel import uStaModel

class uPktDbService:
        
    #global log, dbf
    # init
    def __init__(self,log):
        
        try:
            self.log = log
            self.dbf = uDbFactory(self.log)
            self.dbf.open()                
        except Exception as e:
            self.log.error(e)
            traceback.print_exc()

    #def __del__(self):

    # set db password
    def savepktmodel(self,pm = uPktModel()):
        """create a new record"""
        query = "INSERT INTO wlpkt (mac,ssid,rssi,stat,type,subtype,pmac,bssid,pkttime,timefrac,frameproto,chan,RecTime,srcip) values(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)"
        values = (pm.mac, pm.ssid, pm.rssi, pm.stat, pm.type, pm.subtype,pm.pmac, pm.bssid, pm.pkttime, pm.timefrac, pm.frameproto, pm.chan, pm.RecTime, pm.srcip)
        
        #query = "INSERT INTO wlpkt (mac,ssid,rssi,stat,type,subtype,pmac,bssid,pkttime,timefrac,frameproto,chan,RecTime,srcip) values('00:1b:77:1c:92:55', 'Unis', '-51', '0', '', '0x04', 'ff:ff:ff:ff:ff:ff', 'ff:ff:ff:ff:ff:ff', '2014-02-16 17:29:24', '0.562819000', 'radiotap:wlan', '', '2015-03-11 00:07:51.434000', '192.168.0.128')"
        #values = None
        
        self.dbf.execute(query, values)
        #self.dbf.commit()
    
    def savestamodel(self,om = uStaModel()):
        """create a new record"""
        query = "INSERT INTO wlsta (mac, ssid,rssi,stat,firstseen,lastseen,npacket,rectime,srcip)\
            values (%s, %s, %s, %s, %s, %s, %s, %s, %s)"
        values = (om.mac, om.ssid, om.rssi, om.stat, om.firstseen, om.lastseen, \
                  om.npacket, om.rectime, om.srcip)
        self.dbf.execute(query, values)
        #self.dbf.commit()
    
    def updatestamodel(self,om = uStaModel()): 
        
        query = "UPDATE wlsta SET \
            ssid = '" + om.ssid + "'," +\
            "rssi = '" + om.rssi + "'," + \
            "lastseen = " + "'" + om.lastseen  + "'," +\
            "npacket = npacket + 1, " + \
            "recTime = " + "'" + om.rectime +"' " +\
            "where mac ='" + om.mac + "' and  " + \
            "srcip = '" + om.srcip + "'"
        #values = None
        self.dbf.execute(query)
        #self.dbf.commit() 

    def getmacbymacsrcip(self,mac,srcip):
        query = "select mac from wlsta where mac ='" \
            + mac + "' and srcip = '" +  srcip + "'  limit 1"

        smac = self.dbf.fetchone(query)  
        return smac
     
    def getstatfirstseenbymacsrcip(self,mac,srcip):
        query = "select stat,firstseen from wlsta where mac = '" \
            + mac + "' and srcip = '" +  srcip + "'  limit 1"

        statfirstseen = self.dbf.fetchone(query)  
        return statfirstseen    
    
    def updatestat(self,om = uStaModel()): 
        
        query = "UPDATE wlsta SET \
            stat = '200' " +\
            "where mac ='" + om.mac + "' and  " + \
            "srcip = '" + om.srcip + "'"
        #values = None
        self.dbf.execute(query)
        #self.dbf.commit() 
        
    def execute(self,strsql): 
        query = strsql        
        self.dbf.execute(query)
        #self.dbf.commit() 