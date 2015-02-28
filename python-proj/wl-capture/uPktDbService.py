# -*- coding: utf-8 *-*
__author__ = 'andy'
# create 2015.02.28

import os, sys, time, traceback
import xml.dom.minidom as minidom
import MySQLdb 

from uLogService import uLogService
from uDbFactory import uDbFactory
from uPktModel import uPktModel

class uPktDbService:
        
    #global log, dbf
    # init
    def __init__(self,log):
        
        try:
            self.log = log
            self.dbf = uDbFactory(self.log)
            self.dbf.open()                
        except Exception as e:
            log.error(e)
            traceback.print_exc()

    #def __del__(self):

    # set db password
    def save(self,pm = uPktModel()):
        """create a new record"""
        query = "INSERT INTO wlpkt (mac, ssid,rssi,stat,type,subtype,pmac,bssid,\
            pkttime,timefrac,frameproto,chan,RecTime,srcip)\
            values (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)"
        values = (pm.mac, pm.ssid, pm.rssi, pm.stat, pm.type, pm.subtype, \
                  pm.pmac, pm.bssid, pm.pkttime, pm.timefrac, pm.frameproto, \
                  pm.chan, pm.RecTime, pm.srcip)
        self.dbf.execute(query, values)
        #self.dbf.commit()