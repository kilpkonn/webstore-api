# Installation guide for the server 

As we are currently using docker for deployment gitlab-runner's `.gitlab-cy.yml` for installing
as service is located in `/readme/installation-service/.gitlab-ci.yml`. 
Copy it's contents to `.gitlab-ci.yml` (the one that's in project root) to build using this guide!

## Information

Domain: [https://www.flowerstore.ee](https://www.flowerstore.ee)  
Public IP: [13.48.149.235](http://13.48.149.235)  
Public DNS: **ec2-13-48-149-235.eu-north-1.compute.amazonaws.com**

## Table of Contents
* [Initial setup](#initial-setup)
    * [Access the server](#access-the-server)
    * [Update server](#update-server)
    * [Add 2GB of virtual memory]()
* [Backend setup](#backend-setup)
    * [Install java](#install-java)
    * [Install gitlab runner](#install-gitlab-runner)
    * [Register gitlab runner](#register-gitlab-runner)
    * [Install docker](#install-docker)
    * [Define backend as linux service](#define-backend-as-linux-service)
    * [Allow gitlab runner to use sudo](#allow-gitlab-runner-to-use-sudo-for-restarting-service-after-build)
* [Frontend setup](#frontend-setup)
    * [Installing necessary packages](#installing-necessary-packages)
    * [Register new gitlab runner](#register-new-gitlab-runner)
    * [Setup nginx sites-enabled](#setup-nginx-sites-enabled)
    * [Add nginx proxy to backend](#add-nginx-proxy-to-backend)
    * [Make nginx display frontend](#make-nginx-display-frontend)
    * [Make frontend URLs and refreshing work](#make-frontend-urls-and-refreshing-work)
* [Other setup](#other-setup)
    * [Add HTTPS to website](#add-https-to-website)

## Initial setup

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
 
## Backend setup

### Install java
```bash
# Install JDK & JRE for compiling and running backend
sudo apt install openjdk-11-jre openjdk-11-jdk
```
### Install gitlab runner
From [this](https://docs.gitlab.com/runner/install/linux-manually.html) guide

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

### Register gitlab runner
```bash
# Register runner 
# URL for registration: https://gitlab.com/
# Token can be found in backend repo
# Tags: webstore
# Executor is shell
sudo gitlab-runner register
```

### Define backend as linux service
**Create necessary service file**
```bash
cd /etc/systemd/system/
sudo touch webstore.service
```
**Content of the service file** (use sudo nano webstore.service to edit)
```bash
[Unit]
Description=webstore backend service
After=network.target
[Service]
Type=simple
User=gitlab-runner
WorkingDirectory=/home/gitlab-runner/api-deployment
ExecStart=/usr/bin/java -jar webstore-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
Restart=on-abort
[Install]
WantedBy=multi-user.target
```
**Start the service**
```bash
sudo systemctl daemon-reload # Reload configuration
sudo systemctl enable webstore # Enable process
sudo service webstore restart # Start service
sudo service webstore status # Check service status
```

### Allow gitlab-runner to use sudo for restarting service after build
**Modify sudoers file**
```bash
sudo visudo
```
Add following line to the end of the file and save it
```
gitlab-runner ALL = NOPASSWD: /usr/sbin/service webstore *
```

## Frontend setup

### Installing necessary packages
```bash
# Install node
sudo curl -sL https://deb.nodesource.com/setup_12.x | sudo -E bash -
sudo apt install -y nodejs
# Install yarn
sudo npm install -g yarn
# Install nginx
sudo apt install nginx
```

### Register new gitlab runner
Install gitlab runner before this step, [guide here](#install-gitlab-runner).
```bash
# URL for registration: https://gitlab.cs.ttu.ee/
# Token for frontend: 9ufkU4ndPaP3hUDsHKfg
# Executor is shell
sudo gitlab-runner register
```

### Setup nginx sites-enabled

```bash
# Create new config
cd /etc/nginx/sites-available
sudo cp default frontend

# Create symlink from sites-enabled to sites-available
cd /etc/nginx/sites-enabled
sudo ln -s /etc/nginx/sites-available/frontend /etc/nginx/sites-enabled/

#Remove default symlink in sites-enabled
sudo rm default
```

### Add nginx proxy to backend

**Open configuration file**
```bash
cd /etc/nginx/sites-available
sudo nano frontend
```

**Add following configuration** (right after existing conf for location /)
```
location /api/ {  
         proxy_pass   http://localhost:8080;  
}  

```

**Restart nginx**
```bash
sudo service nginx restart
```

### Make nginx display frontend
**Create a symlink from frontend code to /var/www**
```bash
sudo ln -s /home/gitlab-runner/front-deployment/ /var/www/front-deployment
```

**Modify root to point to /var/www/front-deployment**
```bash
cd /etc/nginx/sites-available
sudo nano frontend
# change line "root /var/www/html" to "/var/www/front-deployment"
# save file and restart nginx
sudo service nginx restart
```

### Make frontend URLs and refreshing work
**Open configuration file**
```bash
cd /etc/nginx/sites-available
sudo nano frontend
```
**Replace current location / configuration with following**
```
location / {
        index index.html index.htm;
        if (!-e $request_filename) {
                rewrite ^(.*)$ /index.html break;
        }
}
```
**Save file and restart nginx**
```bash
sudo service nginx restart
```

## Other setup

### Add HTTPS to website
Get a working domain before this step, from [this guide](https://certbot.eff.org/lets-encrypt/ubuntubionic-nginx)  

**Add Certbot PPA**
```bash
sudo apt-get update
sudo apt-get install software-properties-common
sudo add-apt-repository universe
sudo add-apt-repository ppa:certbot/certbot
sudo apt-get update
```

**Install Certbot**
```bash
sudo apt-get install certbot python-certbot-nginx
```

**Get and install certificates**
```bash
sudo certbot --nginx
```