# -*- coding: utf-8 *-*
__author__ = 'andy'
# create 2015.02.27

import os, sys, time, traceback
import xml.dom.minidom as minidom
import MySQLdb 

from uLogService import uLogService

FETCH_ONE = 0
FETCH_MANY = 1
FETCH_ALL = 2

class uDbFactory:
        
    #global log
    # init
    def __init__(self,log):
        self.log = log
        
        # db connection strings
        self.user = 'andy'
        self.__pwd = ''
        self.host = ''
        self.db = ''
        
        # db vars
        self.cnx = None
        self.cusor = None
        
        try:
        #read configurations here
            dom = minidom.parse("config.xml")

            for node in dom.getElementsByTagName("dbconn"):
                self.user = node.getAttribute("user")
                self.__pwd = node.getAttribute("pwd")
                self.host = node.getAttribute("host")
                self.db = node.getAttribute("db")
                
                self.log.debug('dbuser: %s host: %s dbname: %s' % (self.user , self.host , self.db))
                
        except Exception as e:
            self.self.log.error(e)
            traceback.print_exc()

    def __del__(self):
        self.close()

    # set db password
    def setdbpassword(self,dbPassword):
        self.__pwd = dbPassword
    
    def open(self):
        try:
            self.cnx = MySQLdb.connect(host=self.host,user=self.user,passwd=self.__pwd,db=self.db)
            self.cusor = self.cnx.cursor()
        except Exception as e:
            self.self.log.error(e)
            traceback.print_exc()
            if self.cnx is not None:
                self.cnx.close()
            
    def close(self):
        try:
            if self.cnx is not None:
                if self.cnx.open == True:
                    self.cnx.close()
        except Exception as e:
            self.log.error(e)
            traceback.print_exc()
            
    def commit(self):
        self.cnx.commit()

    def execute(self,strSql):
        try:
            self.log.debug(strSql)
            '''
            if self.cnx is not None:
                self.Open()
            if self.cnx.open == False:
                self.Open()
            '''
            self.cusor.execute(strSql)
        except Exception as e:
            self.log.error(e)
            traceback.print_exc()
            self.cnx.close()

    def execute(self,strSql,values=''):
        try:
            self.log.debug(strSql)
            self.log.debug(values)
            '''
            if self.cnx is not None:
                self.Open()
            if self.cnx.open == False:
                self.Open()
            '''
            self.cusor.execute(strSql,values)
        except Exception as e:
            self.log.error(e)
            traceback.print_exc()
            self.cnx.close()
                
    def fetch_result(self,strSql,mode=FETCH_ONE,rows=1):
        self.log.debug(strSql)
        if self.cusor == None :
            return
        self.log.debug(strSql)
        self.cusor.excute(strSql)
        if mode == FETCH_ONE :
            return self.cusor.fetchone()
        elif mode == FETCH_MANY :
            return self.cusor.fetchmany(rows)
        elif mode == FETCH_ALL :
            return self.cusor.fetchall()
    
    def fetchone(self,strSql):
        self.log.debug(strSql)
        if self.cusor == None :
            return
        self.log.debug(strSql)
        self.cusor.excute(strSql)
        return self.cusor.fetchone()