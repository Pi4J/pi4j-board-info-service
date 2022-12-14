#!/bin/bash

JAVA_HOME=/home/ubuntu/.sdkman/candidates/java/current
WORKDIR=/home/ubuntu/pi4j-board-info-service/
#JAVA_OPTIONS="-Xms256m -Xmx512m -server"
JAVA_OPTIONS=""
#APP_OPTIONS="-c /path/to/app.config -d /path/to/datadir"
APP_OPTIONS=""

cd $WORKDIR
"${JAVA_HOME}/bin/java" $JAVA_OPTIONS -jar pi4j-board-info-service.jar $APP_OPTIONS &