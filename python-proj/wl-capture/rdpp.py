#!/usr/bin/env python

import os, sys, time, traceback
import xml.dom.minidom as minidom
import MySQLdb,  datetime
import netifaces

from uLogService import uLogService
from uWlcapController import uWlcapController

if __name__ == '__main__':
    
        log = uLogService().getlogger()
        log.info('rdpp service started ....')
        # save pktmodel method
        upc = uWlcapController(log)
        upc.run()
        log.info('rdpp service stoped ....')