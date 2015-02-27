# -*- coding: utf-8 *-*
__author__ = 'andy'
# create 2015.02.27

import os, sys, time, traceback
import xml.dom.minidom as minidom
import MySQLdb 

class uDbFactory:    

    # init
    def __init__(self):
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
                
                print self.user
                print self.host
                print self.db
                                
        except Exception as e:
            print str(e)
            traceback.print_exc()
            cnx.close()    

    def __del__(self):
        self.Close()

    # set db password
    def SetDbPassword(self,dbPassword):
        self.__pwd = dbPassword
    
    def Open(self):
        try:
            self.cnx = MySQLdb.connect(host=self.host,user=self.user,passwd=self.__pwd,db=self.db)
            self.cusor = self.cnx.cursor()
        except Exception as e:
            print str(e)
            traceback.print_exc()
            self.cnx.close()
            
    def Close(self):
        try:
            if self.cnx is not None:
                self.cnx.close()
        except Exception as e:
            print str(e)
            traceback.print_exc()

    def Execute(self,strSql):
        try:
            self.cusor.execute(strSql)
        except Exception as e:
            print str(e)
            traceback.print_exc()
            self.cnx.close()
