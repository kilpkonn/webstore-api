# Setup guide for AWS

## Table of Contents
* [Get EC2 server](#get-ec2-server)
    * [Choose region](#choose-region)


## Get EC2 server

### Choose region
Choose preferred region from top-right corner.

### Setup EC2
1. Search for `EC2`.
2. Click on `Launch instance`
**Note that you can have 1 instance of EC2 running for free.**
3. Choose `Ubuntu Server 18.04 LTS (HVM), SSD Volume Type` or newer
4. Choose `t3.micro`
5. Untick `unlimited` option to be sure of maintaining free tier. (optional)
6. Set storage to something in range 16-30.
7. Next until security groups
8. Add security groups
* Add -> HTTP -> Anywhere (0.0.0.0/0, ::/0)
This will allow the world to see your web page.
* Add -> HTTPS -> Anywhere (0.0.0.0/0, ::/0)
**Keep the ssh security group!**
9. Click review and launch.
10. Launch
    * Create new key pair
    * Download key and store it at `~/.ssh/id_webstore_server` or similar location