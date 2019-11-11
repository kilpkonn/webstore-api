#!/bin/bash

export DB_PASS="fudd386a61h2sdsbn3bu3bi37873bdabd3b73ada56yxvnm4y737ihsgf"
#export DB_PASS=$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 32 | head -n 1)

echo "Pulling postgres:latest"
docker pull postgres
echo "Stopping container: $DATABASE_CONTAINER_NAME"
docker stop "$DATABASE_CONTAINER_NAME" || true
echo "Removing container: $DATABASE_CONTAINER_NAME"
docker rm "$DATABASE_CONTAINER_NAME" || true

docker container ls -a -s

echo "Creating network bridge (if none exsists)"
docker network create --driver bridge postgres-network || true # Create only if none exists

echo "Starting $DATABASE_CONTAINER_NAME"
docker run \
   -e "POSTGRES_USER=postgres" \
   -e "POSTGRES_PASSWORD=$DB_PASS" \
   -e "POSTGRES_DB=webstoredb" \
   --name "$DATABASE_CONTAINER_NAME" \
   --network "postgres-network" \
   --restart=always \
   -v /home/gitlab-runner/postgres-data:/var/lib/postgresql/data \
   -d "postgres" \
   -u "postgres" # Will own data folders
