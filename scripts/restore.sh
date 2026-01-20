#!/bin/bash
export DB_NAME=seed_analysis
export DB_USER=postgres
export SERVICE_ID=$(docker service ps -q --filter=desired-state=running infra_pgsql)
export CONTAINER_ID=$(docker inspect ${SERVICE_ID} --format='{{.Status.ContainerStatus.ContainerID}}')
export BACKUP_FILE="initial-data/initial-data.dump"

docker run \
--rm \
--network container:${CONTAINER_ID} \
-v "$(pwd)/${BACKUP_FILE}:/backup.dump" \
postgres:17.0 \
pg_restore -h localhost -U postgres -d seed_analysis --no-owner --no-privileges /backup.dump
