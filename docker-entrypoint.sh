#!/bin/sh
export JAVA_BIN="/opt/java/openjdk/bin/java"
#export JAVA_BIN="/usr/bin/java"

${JAVA_BIN} $JAVA_OPTS -DREDIS_HOST=$REDIS_HOST -jar /app/seed-analysis.jar