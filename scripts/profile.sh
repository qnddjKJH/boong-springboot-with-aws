#!/usr/bin/env bash

# 쉬고 있는 profile 찾기: real1 이 사용 중이면 real2 가 쉬고 있으면 반대면 real1 이 쉼
# bash 는 return value 가 안되니 *제일 마지막줄에 echo 로 해서 결과 출력*후, 클라이언트에서 값을 사용한다

function find_idle_profile() {
  RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/profile)

 # 400 보다 크면 즉, 40x/50x 에러 모두 포함
  if [ ${RESPONSE_CODE} -ge 400 ]
  then
    CURRENT_PROFILE=real2
  else
    CURRENT_PROFILE=$(curl -s http://localhost/profile)
  fi

  echo "${CURRENT_PROFILE}"

  if [ ${CURRENT_PROFILE} == real1 ]
  then
    IDLE_PROFILE=real2
  else
    IDLE_PROFILE=real1
  fi

  echo "${IDLE_PROFILE}"
}

# 쉬고 있는 profile 의 port 찾기
function find_idle_port() {
     IDLE_PROFILE=$(find_idle_profile)

     if [ ${IDLE_PROFILE} == real1 ]
     then
       echo "8081"
     else
       echo "8082"
     fi
}