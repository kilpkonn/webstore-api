#!/bin/bash

export DB_PASS="fudd386a61h2sdsbn3bu3bi37873bdabd3b73ada56yxvnm4y737ihsgf"
#export DB_PASS=$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 32 | head -n 1)

docker pull postgres
docker stop postgres-container || true
docker rm postgres-container || true

docker network create --driver bridge postgres-network || true # Create only if none exists

docker run \
   -e "POSTGRES_USER=postgres" \
   -e "POSTGRES_PASSWORD=$DB_PASS" \
   --name postgres-container \
   --network "postgres-network" \
   --restart=always \
   -v /home/gitlab-runner/postgres-data:/var/lib/postgresql/data \
   -d "postgres" \
   -u "postgres" # Will own data folders
