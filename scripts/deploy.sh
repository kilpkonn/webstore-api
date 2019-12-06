#!/bin/bash

echo "Pulling image"
docker pull "$CI_REGISTRY_USER"/"$CI_REGISTRY_REPOSITORY":"$CI_COMMIT_SHORT_SHA"

# TODO: use different ports etc for rolling upgrade
echo "Stopping container $APP_CONTAINER_NAME"
docker container ls -a -s
docker stop "$APP_CONTAINER_NAME" || true
echo "Removing $APP_CONTAINER_NAME"
docker rm "$APP_CONTAINER_NAME" || true
docker container ls -a -s

echo "Creating internal network bridge for proxy-back-database (if none exsists)"
docker network create --driver bridge api-internal-network || true # Create only if none exists

echo "Starting new container: $APP_CONTAINER_NAME"
docker run -e "SPRING_PROFILES_ACTIVE=prod" \
   --name "$APP_CONTAINER_NAME" \
   --network="api-internal-network" \
   --restart=always \
   -v ~/config:/config \
   -v ~/logs:/logs \
   -v ~/images:/images \
   -d "$CI_REGISTRY_USER"/"$CI_REGISTRY_REPOSITORY":"$CI_COMMIT_SHORT_SHA"
   # -v /home/gitlab-runner/flyway/sql:/flyway/sql \

docker container ls -a -s

echo "Removing old images"
docker image ls
docker system prune -a -f # Needed for unnamed images / containers / etc
docker image ls