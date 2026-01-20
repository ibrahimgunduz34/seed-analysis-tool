#!/bin/bash

export APP_SERVICE_ID=$(docker service ps -q --filter "desired-state=running" seed-analysis_app)
export APP_CONTAINER_ID=$(docker inspect ${APP_SERVICE_ID} --format='{{.Status.ContainerStatus.ContainerID}}')


docker exec -it ${APP_CONTAINER_ID} \
java -jar /app/seed-analysis.jar \
--spring.main.web-application-type=none \
--task=PeriodFundTypeComparisonReport \
DEGISKEN_SEMSIYE_FONU
