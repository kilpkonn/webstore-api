#!/bin/bash

docker pull "$CI_REGISTRY_USER"/"$CI_REGISTRY_REPOSITORY":"$CI_COMMIT_SHORT_SHA"
docker stop "$APP_CONTAINER_NAME" || true
docker rm "$APP_CONTAINER_NAME" || true
docker run -e "SPRING_PROFILES_ACTIVE=prod" \
   --name "$APP_CONTAINER_NAME" \
   -p 8080:8080 \
   --network="postgres-network" \
   --restart=always \
   -v /home/gitlab-runner/logs:/logs \
   -d "$CI_REGISTRY_USER"/"$CI_REGISTRY_REPOSITORY"