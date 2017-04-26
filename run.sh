#!/bin/bash

#add all necessary jars
export JAVA_HOME=/home/eddie/lib/jdk1.8.0_111
export PATH=$JAVA_HOME/bin:$PATH

LIBPATH=../lib/commons-codec-1.6.jar:../lib/commons-logging-1.1.1.jar:../lib/httpclient-4.2.5.jar:../lib/httpcore-4.2.4.jar:../lib/Jama-1.0.3.jar:../lib/junit-4.4.jar:../lib/jython-standalone-2.7.0.jar:../lib/libthrift-0.9.2.jar:../lib/log4j-1.2.14.jar:../lib/org.json.jar:../lib/servlet-api-2.5.jar:../lib/slf4j-api-1.5.8.jar:../lib/slf4j-log4j12-1.5.8.jar

cd /home/eddie/tracing-server/out
setsid java -cp ./:$LIBPATH Server.TracingMaster 2>&1 &