# -*- coding: utf-8 *-*
__author__ = 'andy'
# create 2015.03.01

import os, sys, time, traceback
import xml.dom.minidom as minidom
import unittest

from uPktModel import uPktModel
from uDbFactory import uDbFactory
from uLogService import uLogService
from uPktDbService import uPktDbService
from uStaModel import uStaModel

class WidgetTestCase(unittest.TestCase):

    # testing the pktdbservice class
    def testPktDbService(self):
        log = uLogService().getlogger()
        log.info("init at test module ....")
        
        # save pktmodel method
        ups = uPktDbService(log)
        opkt = uPktModel()
        opkt.ssid = "fwefosdfsdf"
        opkt.rssi = '75'
        ups.savepktmodel(opkt)
        
        # save stamodel method
        osta = uStaModel()
        osta.mac = time.time()
        ups.savestamodel(osta)
    
        # updae method   
        osta = uStaModel()
        osta.mac = '1425221459.281'
        osta.ssid = '1000'
        ups.updatestamodel(osta)