# Setup guide for Raspbian

I went with manual installation, so installation wih NOOBS is not tested.

## Table of Contents
* [Install with NOOBS](#install-with-noobs)
    * [Download SD card formatter](#download-sd-card-formatter)
    * [Format card](#format-card)
    * [Download NOOBS](#download-and-extract-noobs)
* [Install it manually](#manual-installation)
    * [Download image](#download-image)
    * [Download balenaEtcher](#download-balenaetcher)
    * [Flash SD card](#flash-sd-card)
    * [Allow ssh connections](#allow-ssh-connections)
* [Connect to Raspberry](#connect-to-raspberry)
    * [Generate and add shh keys](#generate-and-add-ssh-keys-for-easier-access)
    
    
## Install with NOOBS

### Download SD card formatter
Download link: https://www.sdcard.org/downloads/formatter/

### Format card
Simply open previously downloaded formatter, find your card and click `Quick format`

### Download and extract NOOBS
Should be pretty straight forward.


## Manual installation
Some video tutorial containing most of the information: https://www.youtube.com/watch?v=Zi1dIXxu_Qw

### Download image
List of images can be found here: https://www.raspberrypi.org/downloads/  
We are going with Raspbian Buster Lite as this is specially designed for Raspberry and we 
do not need desktop features.
Simply download zip file, no need to extract it.

### Download balenaEtcher
Download link: https://www.balena.io/etcher/
I didn't need to do any installation, worked out of the box.

### Flash SD Card
1. Open balenaEtcher
2. Select image you previously downloaded (zip containing it)
3. Select SD card (if not already selected)
4. Click `Flash`   
**Note that this will erase everything from card!**  
For me the formatting failed on first try, I simply ran again with same settings and everything worked now.

### Allow ssh connections
Open boot partition of SD card. (You might need to reconnect SD card reader)  
Create empty file named `ssh` to allow ssh connections.
```bash
# In boot partition
touch ssh
```

## Connect to Raspberry
Connect on local network
```bash
ssh pi@raspberrypi
# Default password is raspberry
```

### Generate and add ssh-keys for easier access
Generate keys on your computer
```bash
ssh-keygen
```
After entering the command, you should see the following output:
```bash
Output
Generating public/private rsa key pair.
Enter file in which to save the key (/your_home/.ssh/id_rsa):
```
Enter `id_pi-runner` to save the key pair into the .ssh/ subdirectory in your home directory with name `id_pi-runner`  
After that you should then see the following prompt:
```bash
Output
Enter passphrase (empty for no passphrase):
```
Here you optionally may enter a secure passphrase, which we will skip.
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
Copy newly generated keys to pi.
```bash
ssh-copy-id -i ~/.ssh/id_pi-runner pi@raspberrypi.local  
```
Test the new key
```bash
ssh -i ~/.ssh/id_pi-runner pi@raspberrypi.local  
```