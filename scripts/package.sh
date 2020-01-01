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


echo -e "${BICyan}Building image for linux/amd64... ${IBlue}"
docker build --platform linux/amd64 -t "${CI_REGISTRY_USER}/${CI_REGISTRY_REPOSITORY}:${CI_COMMIT_SHORT_SHA}" .
echo -e "${IGreen}Finished building image!"
echo -e "${BICyan}Pushing image to DockerHub... ${IBlue}"
docker push "${CI_REGISTRY_USER}/${CI_REGISTRY_REPOSITORY}"
echo -e "${IGreen}Done uploading image!"
echo -e "${Green}You can now pull image with: ${Yellow} docker pull
${CI_REGISTRY_USER}/${CI_REGISTRY_REPOSITORY}:${CI_COMMIT_SHORT_SHA}"

echo -e "${BICyan}Removing old images${Yellow}"
docker image ls
echo -e "${Purple}"
# docker system prune -a -f # Needed for unnamed images / containers / etc
docker rmi --force $(docker images -q "$CI_REGISTRY_REPOSITORY" | uniq)
echo -e "${Green}"
docker image ls
echo -e "${Color_Off}"