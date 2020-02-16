# Installation guide for the server 

## Information

Domain: [https://taimetuba.ee](https://taimetuba.ee)  
Public IP: [13.53.135.244](http://13.53.135.244)  
Public DNS: **ec2-13-53-135-244.eu-north-1.compute.amazonaws.com**

## Table of Contents
* [Initial setup](#initial-setup)
    * [Access the server](#access-the-server)
    * [Update server](#update-server)
    * [Add 2GB of virtual memory]()
* [Gitlab runner setup](#gitlab-runner-setup)
    * [Install java](#install-java)
    * [Install node](#install-node)
    * [Install gitlab runner](#install-gitlab-runner)
    * [Register gitlab runner](#register-gitlab-runner)
    * [Install docker](#install-docker)
    * [Fix cd not working](#fix-cd-not-working-for-rasperry)
    * [Enable experimental features for docker](#enable-experimental-features-for-docker)
    * [Generate ssh keys for deployment](#generate-ssh-keys-for-deployment)
* [Deployment server setup](#deployment-server-setup)
    * [Add user to deploy](#add-user-that-will-deploy-app)
    * [Install docker](#install-docker-for-deployment-server)
    * [Add HTTPS to website](#add-https-to-website)
    * [Add gitlab-runner ssh keys](#add-gitlab-runner-ssh-keys)
    * [Create files to persist containers data](#create-files-to-persist-docker-containers-data)
    * [Custom config setup](#custom-config-setup)
    * [Database setup](#database-setup)
    * [Clean up server](#clean-up-server)
* [Gitlab variables setup](#gitlab-variables-setup)

## Initial setup
**This part has to be done on both of the servers before starting next part of the configuration**

### Access the server
```bash
ssh -i KEYFILE ubuntu@PUBLIC_DNS
```

### Update server
  
```bash
# Update and upgrade packages
sudo apt update && sudo apt upgrade
```

### Add 2GB of virtual memory 
From [this](https://itsfoss.com/create-swap-file-linux/) guide.  
```bash
# Check if swap exists
free -h
swapon --show
# Create a 2GB swap file
sudo fallocate -l 2G /swapfile
# Allow only root to read and write to swapfile
sudo chmod 600 /swapfile
# Mark file as swap space
sudo mkswap /swapfile
# Enable the swap file
sudo swapon /swapfile
swapon --show
# Make swap persist after reboot
echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab
# Reboot server and check that swap still exists
sudo reboot
swapon --show
```

<hr>

## Gitlab-runner setup

### Install java
```bash
# Install JDK & JRE for compiling and running backend
sudo apt install openjdk-11-jre openjdk-11-jdk
```

### Install node
```bash
# Install node
sudo curl -sL https://deb.nodesource.com/setup_12.x | sudo -E bash -
sudo apt install -y nodejs
# Install yarn
sudo npm install -g yarn
```

### Install gitlab runner
From [this](https://docs.gitlab.com/runner/install/linux-manually.html) guide  
**For AWS:**
```bash
# Download necessary binary
sudo curl -L --output /usr/local/bin/gitlab-runner https://gitlab-runner-downloads.s3.amazonaws.com/latest/binaries/gitlab-runner-linux-amd64

# Give it permissions to execute
sudo chmod +x /usr/local/bin/gitlab-runner

# Create a GitLab CI user
sudo useradd --comment 'GitLab Runner' --create-home gitlab-runner --shell /bin/bash

# Install and run as service
sudo gitlab-runner install --user=gitlab-runner --working-directory=/home/gitlab-runner
sudo gitlab-runner start
```
**For Rasperry:**
```bash
# Download necessary binary with script
curl -L https://packages.gitlab.com/install/repositories/runner/gitlab-runner/script.deb.sh | sudo bash

# Install gitlab-runner
sudo apt install gitlab-runner
```

### Register gitlab runner
```bash
# Register runner 
# URL for registration: https://gitlab.com/
# Token can be found in backend repo
# Tags: webstore
# Executor is shell
sudo gitlab-runner register
```

### Install docker
**For AWS:**
```bash
# Update packages
sudo apt update
# Install packages to allow apt to use a repository over HTTPS
sudo apt install \
    apt-transport-https \
    ca-certificates \
    curl \
    software-properties-common
# Add Docker’s official GPG key
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
# Setup the repository
sudo add-apt-repository \
   "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
   $(lsb_release -cs) \
   stable"
# Update the package
sudo apt update
# Install the latest version of Docker cE
sudo apt install docker-ce
# Verify
sudo docker run hello-world
# Clean
sudo docker system prune -a
```
**For Rasperry:**
```bash
# Update packages
sudo apt update
# Install packages to allow apt to use a repository over HTTPS
sudo apt install \
    apt-transport-https \
    ca-certificates \
    curl \
    software-properties-common
# Import Docker CPG key.
sudo curl https://download.docker.com/linux/raspbian/gpg
# Download and install docker
curl -fsSL get.docker.com -o get-docker.sh && sh get-docker.sh
# Setup the Docker Repo.
sudo nano /etc/apt/sources.list
# Add the following line and save:
deb https://download.docker.com/linux/raspbian/ buster stable
#  Patch and update your Pi.
sudo apt update && sudo apt upgrade
# Start docker service
sudo systemctl start docker.service
# Verify
sudo docker run hello-world
# Clean
sudo docker system prune -a
```
**Add gitlab-runner to docker group**
```bash
sudo usermod -aG docker gitlab-runner
# Verify that gitlab-runner has access to Docker
sudo -u gitlab-runner -H docker info
```

### Enable experimental features for docker
```bash
# Edit docker daemon service
sudo systemctl edit docker
# Paste following:

# [Service]
# ExecStart=
# ExecStart=/usr/bin/dockerd -H fd:// --experimental=true

# Save and exit
# Reload service
systemctl daemon-reload
sudo service docker restart
```

### Fix cd not working for Rasperry
See: https://gitlab.com/gitlab-org/gitlab-runner/issues/4092
```bash
sudo nano /home/gitlab-runner/.bash_logout
# Comment out the part that clears console
# Save and exit
```

### Generate ssh keys for deployment
Tutorial from [this](https://www.digitalocean.com/community/tutorials/how-to-set-up-ssh-keys-on-ubuntu-1604) guide.  
First enter:
```bash
ssh-keygen
```
After entering the command, you should see the following output:
```bash
Output
Generating public/private rsa key pair.
Enter file in which to save the key (/your_home/.ssh/id_rsa):
```
Enter `id_deploy` to save the key pair into the .ssh/ subdirectory in your home directory with name `id_deploy`  
After that you should then see the following prompt:
```bash
Output
Enter passphrase (empty for no passphrase):
```
Here you optionally may enter a secure passphrase, which we will skip as we will use these keys by gitlab-runner.
Simply press `ENTER` (with empty input)  
You should then see the following output:
```bash
Output
Your identification has been saved in /your_home/.ssh/id_webstore.
Your public key has been saved in /your_home/.ssh/id_webstore.pub.
The key fingerprint is:
a9:49:2e:2a:5e:33:3e:a9:de:4e:77:11:58:b6:90:26 username@remote_host
The key´s randomart image is:
+--[ RSA 2048]----+
|     ..o         |
|   E o= .        |
|    o. o         |
|        ..       |
|      ..S        |
|     o o.        |
|   =o.+.         |
|. =++..          |
|o=++.            |
+-----------------+
```
Later we will copy this public key to deployment server.

<hr>

## Deployment server setup

### Add user that will deploy app
Add user _(we are using pi-runner)_ for simplicity. Make sure to choose strong password.  
Rest of the info can be left blank
```bash
sudo adduser username
```

### Install docker for deployment server
```bash
# Update packages
sudo apt update
# Install packages to allow apt to use a repository over HTTPS
sudo apt install \
    apt-transport-https \
    ca-certificates \
    curl \
    software-properties-common
# Add Docker’s official GPG key
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
# Setup the repository
sudo add-apt-repository \
   "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
   $(lsb_release -cs) \
   stable"
# Update the package
sudo apt update
# Install the latest version of Docker cE
sudo apt install docker-ce
# Verify
sudo docker run hello-world
# Clean
sudo docker system prune -a
```
**Add gitlab-runner (or any other user that will deploy) to docker group**
This is the user we previously created
```bash
sudo usermod -aG docker <user>
# Verify that gitlab-runner has access to Docker
sudo -u <user> -H docker info

```

### Add HTTPS to website
Get a working domain before this step, from 
[this guide](https://www.digitalocean.com/community/tutorials/how-to-use-certbot-standalone-mode-to-retrieve-let-s-encrypt-ssl-certificates-on-ubuntu-16-04)  

**Add Certbot PPA**
```bash
sudo apt update
sudo apt install software-properties-common
sudo add-apt-repository universe
sudo add-apt-repository ppa:certbot/certbot
sudo apt update
```

**Install Certbot**
```bash
sudo apt install certbot
```

**Get and install certificates**  
First open port 80.
Certbot needs to answer a cryptographic challenge issued by the Let’s Encrypt API in order to prove we control our domain. It uses ports 80 (HTTP) or 443 (HTTPS) to accomplish this. Open up the appropriate port in your firewall:
```bash
sudo ufw allow 80
```
After that get the certificates.
```bash
# For website
sudo certbot certonly --standalone --preferred-challenges http -d <your_url.ee>
# Get dhparams
sudo openssl dhparam -out /etc/letsencrypt/ssl-dhparams.pem 2048
```
Make nginx https config
```bash
sudo touch /etc/letsencrypt/options-ssl-nginx.conf
# Copy config from somewhere (https://github.com/simnalamburt/nginx.conf/blob/master/options-ssl-nginx.conf)
sudo nano /etc/letsencrypt/options-ssl-nginx.conf
```
Your certificates should be saved into `/etc/letsencrypt/...` as this is the directory that will be mounted to nginx
proxy container.
If you are using different hostname, don't forget to change nginx config in front end.
 
### Add gitlab-runner ssh keys
Whilst being ssh-d into gitlab-runner server run following:
`user` is the user that will deploy the app (previously created) and `host` is the ip od deployment server.
We will be using gitlab-runner as the user.
```bash
# Only works if password login is enabled
ssh-copy-id -i ~/.ssh/id_deploy user@host  
```
Test the new key
```bash
ssh -i ~/.ssh/id_deploy user@host
```

### Create files to persist docker containers data
User is the user that will deploy thus run the app.  
_For us currently it is gitlab-runner_
```bash
# For logs
mkdir /home/<user>/logs
# For database
mkdir /home/<user>/postgres-data
# For config
mkdir /home/<user>/config
# For images
mkdir /home/<user>/images
```

## Gitlab variables setup
**You also need to set following variables under Settings > CI/CD > Variables:**
 - **CI_REGISTRY_USER** - user that controls docker repository _(currently `kilpkonn`)_
 - **CI_REGISTRY_REPOSITORY** - repository that will contain your docker files 
 _(currently `webstore-api` for back and `webstore-front` for front)_
 - **CI_REGISTRY_TOKEN** - token to sign into docker hub
 - **CI_DEPLOY_USER** - user that deploys app _(pi-runner for example)_
 - **CI_DEPLOY_SERVER** - ip/dns of server your gonna deploy to
 
## Custom config setup
Copy `application.yml` to server
```bash
# Cd into project root and then
scp ./src/main/resources/application.yml user@remote_host:/home/<user>/config/
```
## Database setup
Copy `run_postgres.sh` to server.
```bash
# Cd into project root and then copy the file to somewhere in server
# You can simply copy to `/home/<user>/ for simplicity` but you may aswell choose some other path
scp ./scripts/run_postgres.sh user@remote_host:/home/<some_user>/some/path
```
Start up the database
```bash
# Cd into path where you previously copied `run_postgres.sh`
# Then run the file. user is the user that will be deploying you app
# It is important to run it as that user as postgres data will be saved to home of the user that's
# running this script
# On the last line you should see the password of newly created database.
sudo su <user> -c "./run_postgres.sh"

# If you have database already running you can stop it with
sudo docker stop postgres-container
# You need to delete contents postgres-data after that as it will otherwise persist old password
```
Save the password to custom config
```bash
# Copy the password printed by `run_postgres.sh`
# Then edit previously set up application.yml to save the password (change testPassword to one you copied)
sudo nano /home/<user>/config/application.yml
# Save with CTRL+O, exit with CTRL+X
```
 You can now delete `run_postgres.sh`  
## Clean up server 
Remove all docker containers, images, networks and volumes currently not in use
```bash
sudo docker system prune -a --volumes
```
**You should be good to go now. Run your first pipeline to see if everything works!**