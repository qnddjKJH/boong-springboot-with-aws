#!/usr/bin/env bash

ABSPATH=$(readlink -f "$0")
ABSDIR=$(dirname "$ABSPATH")
source "${ABSDIR}"/profile.sh

IDLE_PORT=$(find_idle_port)

echo "> $IDLE_PORT 에서 구동중인 애플리케이션 pid 확인"
IDLE_PID=$(lsof -ti tcp:"${IDLE_PORT}")

echo "> $IDLE_PORT 에서 구동중인 애플리케이션 pid: $IDLE_PID"

if [ -z "${IDLE_PORT}" ]
then
  echo "> kill -15 ${IDLE_{ID}}"
  kill -15 "${IDLE_PID}"
  sleep 5
fi