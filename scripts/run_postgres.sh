#!/bin/bash

# export DB_PASS="fudd386a61h2sdsbn3bu3bi37873bdabd3b73ada56yxvnm4y737ihsgf"
export DATABASE_CONTAINER_NAME="postgres-container"

docker container ls -a -s

if [ ! "$(docker ps -q -f name="$DATABASE_CONTAINER_NAME")" ]; then
    if [ "$(docker ps -aq -f status=exited -f name="$DATABASE_CONTAINER_NAME")" ]; then
        # cleanup
        docker rm "$DATABASE_CONTAINER_NAME"
    fi

    echo "Pulling postgres"
    docker pull postgres

    docker container ls -a -s
    echo "Stopping container: $DATABASE_CONTAINER_NAME"
    docker stop "$DATABASE_CONTAINER_NAME" || true
    echo "Removing container: $DATABASE_CONTAINER_NAME"
    docker rm "$DATABASE_CONTAINER_NAME" || true

    docker container ls -a -s

    echo "Creating internal network bridge for proxy-back-database (if none exsists)"
    docker network create --driver bridge api-internal-network || true # Create only if none exists

    export DB_PASS=$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 32 | head -n 1)

    echo "Starting $DATABASE_CONTAINER_NAME"
    docker run \
        -e "POSTGRES_USER=postgres" \
        -e "POSTGRES_PASSWORD=$DB_PASS" \
        -e "POSTGRES_DB=webstoredb" \
        --name "$DATABASE_CONTAINER_NAME" \
        --network "api-internal-network" \
        --restart=always \
        -v ~/postgres-data:/var/lib/postgresql/data \
        -d "postgres" \
        # -u "postgres" # Will own data folders

    docker container ls -a -s
    echo "Generated new password for database, make sure to write it down!"
    echo "$DB_PASS"
else
    echo "$DATABASE_CONTAINER_NAME seems to be running."
fi

