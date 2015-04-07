# -*- coding: utf-8 *-*
__author__ = 'andy'
# create 2015.02.27
# packet model class

import os, sys, time, traceback, datetime

class uPktModel:
    # init
    def __init__(self):
        self.mac = ''
        self.ssid = ''
        self.rssi = '0'
        self.stat = '0'
        self.type = ''
        self.subtype = ''
        self.pmac = ''
        self.bssid = ''
        self.pkttime = str(datetime.datetime.now()) 
        self.timefrac = '0'
        self.frameproto = ''
        self.chan = ''
        self.RecTime = str(datetime.datetime.now()) 
        self.srcip = 'n/a'
