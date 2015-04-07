# -*- coding: utf-8 *-*
__author__ = 'andy'
# create 2015.02.28
# logging class

import os, sys, time, traceback
import xml.dom.minidom as minidom
import logging
from logging.handlers import RotatingFileHandler

CRITICAL = 50
ERROR = 40
WARNING = 30
INFO = 20
DEBUG = 10
NOTSET = 0

class uLogService:

    def __init__(self):
        
        self.CRITICAL = 50
        self.ERROR = 40
        self.WARNING = 30
        self.INFO = 20
        self.DEBUG = 10
        self.NOTSET = 0
        
        self.filesizelimit = 1024*1024*10                   # log file size limited 10MB
        
        self.logger = None        
        filename = os.path.join(os.getcwd(),'wlcaplog')     # log file name set default as 'wlcaplog'
        
        if os.path.exists(filename):        
            filesize = os.path.getsize(filename)
     
            if filesize > self.filesizelimit:
                os.rename(filename, filename+"_"+time.strftime('%Y%m%d%H%M%S')+".bak")
        
        self.logger = logging.getLogger()
        self.logger.setLevel(logging.DEBUG)
     
        formatter = logging.Formatter('%(asctime)s %(filename)s %(funcName)s [line:%(lineno)d] %(levelname)s %(message)s')
        
        sh = logging.StreamHandler(sys.stderr)
        sh.setFormatter(formatter)
        self.logger.addHandler(sh)
        
        fh = logging.FileHandler(filename)
        fh.setFormatter(formatter)
        self.logger.addHandler(fh)
        
        self.logger.info('Logging service Start')
        self.logger.info('Logging file name is %s',filename)
    
    def setlevel(self,level=INFO):
        self.logger.setLevel(level)

    def getlogger(self):
        return self.logger