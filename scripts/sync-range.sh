#!/bin/bash

export SCRIPT_DIR=$(dirname $(realpath $0))
export ROOT_DIR="${SCRIPT_DIR}/.."
export BACKUP_FILE="${ROOT_DIR}/initial-data/initial-data.dump"
export DB_NAME=seed_analysis
export DB_USER=postgres

export APP_SERVICE_ID=$(docker service ps -q --filter "desired-state=running" seed-analysis_app)
export APP_CONTAINER_ID=$(docker inspect ${APP_SERVICE_ID} --format='{{.Status.ContainerStatus.ContainerID}}')
export DB_SERVICE_ID=$(docker service ps -q --filter "desired-state=running" infra_pgsql)
export DB_CONTAINER_ID=$(docker inspect ${DB_SERVICE_ID} --format='{{.Status.ContainerStatus.ContainerID}}')

docker exec -it ${APP_CONTAINER_ID} \
java -jar /app/seed-analysis.jar \
--spring.main.web-application-type=none \
--task=MetaDataListSync

docker exec -it ${APP_CONTAINER_ID} \
java -jar /app/seed-analysis.jar \
--spring.main.web-application-type=none \
--task=HistoricalDataListSyncAll \
2026-01-18 \
2026-01-20

docker run \
--rm \
--network container:${DB_CONTAINER_ID} \
postgres:17.0 \
pg_dump -h localhost -U ${DB_USER} -F c ${DB_NAME} > ${BACKUP_FILE}

