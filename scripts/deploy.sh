#!/bin/bash

echo "Pulling image"
docker pull "$CI_REGISTRY_USER"/"$CI_REGISTRY_REPOSITORY":"$CI_COMMIT_SHORT_SHA"

echo "Moving container $APP_CONTAINER_NAME to $APP_CONTAINER_NAME-old"
docker rename "$APP_CONTAINER_NAME" "$APP_CONTAINER_NAME-old"

# TODO: use different ports etc for rolling upgrade
echo "Stopping container $APP_CONTAINER_NAME-old"
docker container ls -a -s
docker stop "$APP_CONTAINER_NAME-old" || true
echo "Removing $APP_CONTAINER_NAME-old"
docker rm "$APP_CONTAINER_NAME-old" || true
docker container ls -a -s

echo "Creating internal network bridge for proxy-back-database (if none exsists)"
docker network create --driver bridge api-internal-network || true # Create only if none exists

echo "Starting new container: $APP_CONTAINER_NAME"
docker run -e "SPRING_PROFILES_ACTIVE=prod" \
   --name "$APP_CONTAINER_NAME" \
   --network="api-internal-network" \
   --restart=always \
   -v /home/gitlab-runner/logs:/logs \
   -v /home/gitlab-runner/config:/config \
   -d "$CI_REGISTRY_USER"/"$CI_REGISTRY_REPOSITORY":"$CI_COMMIT_SHORT_SHA"

docker container ls -a -s

echo "Removing old images"
docker image ls
# shellcheck disable=SC2046
docker rmi $(docker images | grep "$CI_REGISTRY_USER"/"$CI_REGISTRY_REPOSITORY" | awk '{print $3}')
docker image ls