#!/bin/bash

docker compose up -d

mvn spring-boot:run \
-DskipTests=true \
-Dspring-boot.run.profiles=prod \
-Dspring-boot.run.jvmArguments="-Dtask=MetaDataListSync"

mvn spring-boot:run \
-DskipTests=true \
-Dspring-boot.run.profiles=prod \
-Dspring-boot.run.jvmArguments="-Dtask=HistoricalDataListSync"

docker run --rm \
--network container:$(docker compose ps -q pgsql) \
postgres:17.0 \
pg_dump -h localhost -U appuser -F c appdb > initial-data/initial-data.dump