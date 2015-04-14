# for development.
1. go to the wms root directory. wms/

2. it needs java 8
3.1 run the set-env.bat in wms/. (you need edit it for your java 8 directory). 
3.2 build the customized Spring security lib from https://github.com/michaelyin/spring-security.git. You need check out the 3.2.5 release version to build.
4. then run gradle bootRun
5. the server will be up ready for recording.
6. in your android device, you need configure the ip pointing to the wms server.
7. remote debugging: java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n -jar build/libs/wms.war 