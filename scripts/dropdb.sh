#!/bin/bash
export DB_NAME=seed_analysis
export DB_USER=postgres
export SERVICE_ID=$(docker service ps -q infra_pgsql)
export CONTAINER_ID=$(docker inspect ${SERVICE_ID} --format='{{.Status.ContainerStatus.ContainerID}}')

docker run \
--rm \
--network container:${CONTAINER_ID} \
postgres:17.0 \
dropdb -h localhost -U postgres ${DB_NAME}