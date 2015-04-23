# for development.

# build the customized Spring security lib from https://github.com/michaelyin/spring-security.git. 
    You need check out the 3.2.5 release version to build. Steps:
    a. git clone https://github.com/michaelyin/spring-security.git
    b. cd spring-security
    c. git checkout 3.2.5.RELEASE 
    d. gradlew install   (need set up JAVA_HOME to java 7)

1. go to the wms root directory. wms/
2. it needs java 8. set up JAVA_HOME to java 8) or run the set-env.bat in wms/. (you need edit it for your java 8 directory). 
3. then run gradle bootRun
4. the server will be up ready for recording.
5. in your android device, you need configure the ip pointing to the wms server.

#For remote debugging: 
java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n -jar build/libs/wms.war 