#!/bin/bash

set -e

echo "> 최신 JAR 파일을 nyang 프로젝트 디렉토리로 이동"
cp /home/ubuntu/togetnyang/*.jar /home/ubuntu/togetnyang/server/nyang/*.jar

cd /home/ubuntu/togetnyang/server/nyang

if [ -f .env ]; then
  echo "> .env 파일 로드"
  export $(cat .env | xargs)
else
  echo "❌ .env 파일이 존재하지 않습니다. 배포 중단."
  exit 1
fi

echo "> 기존 컨테이너 종료"
docker compose down

echo "> 새 컨테이너 빌드 및 실행"
docker compose up -f docker-compose-nyang.yml -d --build