# Installation guide for the server 
  
## Initial setup
  
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
# URL for registration: https://gitlab.cs.ttu.ee/
# Token for backend: Xg9t4qmnggHzJEdTtxur
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
ExecStart=/usr/bin/java -jar webstore-0.0.1-SNAPSHOT.jar
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