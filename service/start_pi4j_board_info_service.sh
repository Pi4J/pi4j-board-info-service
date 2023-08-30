#!/bin/bash

JAVA_HOME=/home/ft/.sdkman/candidates/java/current
WORKDIR=/home/ft/pi4j-board-info-service/
JAVA_OPTIONS="-Dspring.profiles.active=production"
APP_OPTIONS=""

cd $WORKDIR
"${JAVA_HOME}/bin/java" $JAVA_OPTIONS -jar pi4j-board-info-service.jar $APP_OPTIONS &