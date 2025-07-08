#!/bin/bash

cd nyang

./gradlew clean build

docker compose -f docker-compose-nyang.yml up -d --build