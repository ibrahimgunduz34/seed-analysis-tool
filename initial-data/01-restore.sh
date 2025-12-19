#!/bin/bash
set -e

echo "Restoring custom-format backup..."

pg_restore \
  --verbose \
  --no-owner \
  --username="$POSTGRES_USER" \
  --dbname="$POSTGRES_DB" \
  /docker-entrypoint-initdb.d/initial-data.dump
