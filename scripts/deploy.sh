#!/bin/bash

export TERM=xterm-256color
echo $TERM

# Reset
export Color_Off='\033[0m'       # Text Reset

# Regular Colors
export Black='\033[0;30m'        # Black
export Red='\033[0;31m'          # Red
export Green='\033[0;32m'        # Green
export Yellow='\033[0;33m'       # Yellow
export Blue='\033[0;34m'         # Blue
export Purple='\033[0;35m'       # Purple
export Cyan='\033[0;36m'         # Cyan

# High Intensity
export IBlack='\033[0;90m'       # Black
export IRed='\033[0;91m'         # Red
export IGreen='\033[0;92m'       # Green
export IYellow='\033[0;93m'      # Yellow
export IBlue='\033[0;94m'        # Blue
export IPurple='\033[0;95m'      # Purple
export ICyan='\033[0;96m'        # Cyan
export IWhite='\033[0;97m'       # White

# Bold High Intensity
export BIBlack='\033[1;90m'      # Black
export BIRed='\033[1;91m'        # Red
export BIGreen='\033[1;92m'      # Green
export BIYellow='\033[1;93m'     # Yellow
export BIBlue='\033[1;94m'       # Blue
export BIPurple='\033[1;95m'     # Purple
export BICyan='\033[1;96m'       # Cyan
export BIWhite='\033[1;97m'      # White

echo -e "${BICyan}Pulling image ${Blue}"
docker pull "$CI_REGISTRY_USER"/"$CI_REGISTRY_REPOSITORY":"$CI_COMMIT_SHORT_SHA"

# TODO: use different ports etc for rolling upgrade
echo -e "${BICyan}Stopping container${IBlue} $APP_CONTAINER_NAME ${Yellow}"
docker container ls -a -s
docker stop "$APP_CONTAINER_NAME" || true
echo -e "${BICyan}Removing${IBlue} $APP_CONTAINER_NAME ${Purple}"
docker rm "$APP_CONTAINER_NAME" || true
echo -e "${Purple}"
docker container ls -a -s

echo -e "${BICyan}Creating internal network bridge for proxy-back-database (if none exsists)${IRed}"
docker network create --driver bridge api-internal-network || true # Create only if none exists

echo -e "${BICyan}Starting new container:${Yellow} $APP_CONTAINER_NAME"
echo -e "${BIPurple}"
docker run -e "SPRING_PROFILES_ACTIVE=prod" \
   --name "$APP_CONTAINER_NAME" \
   --network="api-internal-network" \
   --restart=always \
   -v ~/config:/config \
   -v ~/logs:/logs \
   -v ~/images:/images \
   -d "$CI_REGISTRY_USER"/"$CI_REGISTRY_REPOSITORY":"$CI_COMMIT_SHORT_SHA"
   # -v /home/gitlab-runner/flyway/sql:/flyway/sql \

echo -e "${Green}"
docker container ls -a -s

echo -e "${BICyan}Removing old images${Yellow}"
docker image ls
echo -e "${Purple}"
docker system prune -a -f # Needed for unnamed images / containers / etc
echo -e "${Green}"
docker image ls
echo -e "${Color_Off}"