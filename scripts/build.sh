#!/bin/bash

export SCRIPT_DIR=$(dirname $(realpath $0))
export ROOT_DIR="${SCRIPT_DIR}/.."

source "${SCRIPT_DIR}/common.sh"

cd "${ROOT_DIR}/"

docker build -f Dockerfile -t ${IMAGE_NAME} .