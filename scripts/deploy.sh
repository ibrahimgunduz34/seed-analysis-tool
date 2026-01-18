#!/bin/bash
export SCRIPT_DIR=$(dirname $(realpath $0))
export ROOT_DIR="${SCRIPT_DIR}/.."
export STACK="seed-analysis"

source "${SCRIPT_DIR}/common.sh"

docker stack deploy -c "${ROOT_DIR}/app.yaml" ${STACK} --detach