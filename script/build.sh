#!/bin/bash

kill -9 $(pgrep -f "java.*expense")

docker run --name test-RR-db -e MYSQL_DATABASE=mysql-db -e MYSQL_USER=expense -e MYSQL_PASSWORD=manager -e MYSQL_ROOT_PASSWORD=1234 -d -p 33306:3306 -v ./mysql/initdb.d:/docker-entrypoint-initdb.d -v ./mysql/conf.d:/etc/mysql/conf.d mysql --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci
docker run --name test-WW-db -e MYSQL_DATABASE=mysql-db -e MYSQL_USER=expense -e MYSQL_PASSWORD=manager -e MYSQL_ROOT_PASSWORD=1234 -d -p 43306:3306 -v ./mysql/initdb.d:/docker-entrypoint-initdb.d -v ./mysql/conf.d:/etc/mysql/conf.d mysql --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci
docker run --name test-redis -p 36379:6379 -d redis

./gradlew clean build

docker stop test-RR-db
docker stop test-WW-db
docker stop test-redis

docker container remove test-RR-db
docker container remove test-WW-db
docker container remove test-redis

echo 'jar execute'
nohup java -jar ./build/libs/expense-manager-0.0.1-SNAPSHOT.jar &
