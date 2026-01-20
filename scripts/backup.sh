#!/bin/bash
export DB_NAME=seed_analysis
export DB_USER=postgres
export SERVICE_ID=$(docker service ps -q --filter=desired-state=running infra_pgsql)
export CONTAINER_ID=$(docker inspect ${SERVICE_ID} --format='{{.Status.ContainerStatus.ContainerID}}')
export BACKUP_FILE="backup.dump"

docker run \
--rm \
--network container:${CONTAINER_ID} \
postgres:17.0 \
pg_dump -h localhost -U ${DB_USER} -F c ${DB_NAME} > ${BACKUP_FILE}
