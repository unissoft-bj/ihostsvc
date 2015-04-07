# -*- coding: utf-8 *-*
__author__ = 'andy'
# create 2015.03.01
# packet model class

import os, sys, time, traceback, datetime

class uStaModel:
    # init
    def __init__(self):
        self.mac = ''
        self.ssid = ''
        self.rssi = '0'
        self.stat = '0'
        self.firstseen = str(datetime.datetime.now()) 
        self.lastseen = str(datetime.datetime.now()) 
        self.npacket = '0'
        self.rectime = str(datetime.datetime.now()) 
        self.srcip = 'n/a'
