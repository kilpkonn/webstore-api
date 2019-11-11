#!/bin/bash

docker pull "$CI_REGISTRY_USER"/"$CI_REGISTRY_REPOSITORY":"$CI_COMMIT_SHORT_SHA"
docker stop webstore-back-container || true
docker rm webstore-back-container || true
docker run -e "SPRING_PROFILES_ACTIVE=prod" \
   --name webstore-back-container \
   -p 8080:8080 \
   --network="postgres-network" \
   --restart=always \
   -v /home/gitlab-runner/logs:/logs \
   -d "$CI_REGISTRY_USER"/"$CI_REGISTRY_REPOSITORY"