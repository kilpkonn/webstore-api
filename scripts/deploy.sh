#!/bin/bash

echo "\033[0;36mPulling image\033[0m"
docker pull "$CI_REGISTRY_USER"/"$CI_REGISTRY_REPOSITORY":"$CI_COMMIT_SHORT_SHA"

docker container ls -a -s
echo "\033[0;36mStopping container \033[43m$APP_CONTAINER_NAME\033[0m"
docker stop "$APP_CONTAINER_NAME" || true
echo "\033[0;36mRemoving $APP_CONTAINER_NAME\033[0m"
docker rm "$APP_CONTAINER_NAME" || true
docker container ls -a -s

echo "\033[0;36mStarting new container: \033[43m$APP_CONTAINER_NAME\033[0m"
docker run -e "SPRING_PROFILES_ACTIVE=prod" \
   --name "$APP_CONTAINER_NAME" \
   -p 8080:8080 \
   --network="postgres-network" \
   --restart=always \
   -v /home/gitlab-runner/logs:/logs \
   -v /home/gitlab-runner/flyway:/flyway/sql \
   -d "$CI_REGISTRY_USER"/"$CI_REGISTRY_REPOSITORY"
docker container ls -a -s