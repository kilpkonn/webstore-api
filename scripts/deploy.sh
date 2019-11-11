#!/bin/bash

echo "Pulling image"
docker pull "$CI_REGISTRY_USER"/"$CI_REGISTRY_REPOSITORY":"$CI_COMMIT_SHORT_SHA"

docker container ls -a -s
echo "Stopping container $APP_CONTAINER_NAME"
docker stop "$APP_CONTAINER_NAME" || true
echo "Removing $APP_CONTAINER_NAME"
docker rm "$APP_CONTAINER_NAME" || true
docker container ls -a -s

echo "Starting new container: $APP_CONTAINER_NAME"
docker run -e "SPRING_PROFILES_ACTIVE=prod" \
   --name "$APP_CONTAINER_NAME" \
   -p 8080:8080 \
   --network="postgres-network" \
   --restart=always \
   -v /home/gitlab-runner/logs:/logs \
   -v /home/gitlab-runner/flyway/sql:/flyway/sql \
   -d "$CI_REGISTRY_USER"/"$CI_REGISTRY_REPOSITORY"

docker container ls -a -s