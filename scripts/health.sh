#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh
source ${ABSDIR}/switch.sh

IDLE_PROFILE=$(find_idle_port)

echo "> Health Check Start"
echo "> IDLE_PORT: $IDLE_PORT"
echo "> curl -s http://localhost:$IDLE_PORT/profile"
sleep 10

for RETRY_COUNT in {1..10}
do
  RESPONSE=$(curl -s http://localhost:$IDLE_PORT/profile)
  UP_COUNT=$(echo ${RESPONSE} | grep 'real' | wc -1)

  if [ ${UP_COUNT} -ge 1 ]
  then # UP_COUNT >= 1 'real' 문자열 있는지 검증 1보다 크다면
    echo "> Health Check Success"
    switch_proxy
    break
  else
    echo "> Health Check 응답을 알 수 없거나 혹은 실행 상태가 아닙니다."
    echo "> Health Check: ${RESPONSE}"
  fi

  if [ ${RETRY_COUNT} -eq 10 ]
  then
    echo "> Health Check failed"
    echo "> Nginx 에 연결하지 않고 배포를 종료합니다."
    exit 1
  fi

  echo "> Health Check 연결 실패 재시도 ..."
  sleep 10
done
