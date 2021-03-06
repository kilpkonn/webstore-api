# webstore-api
_Originally created in ITI0203 course (2019)_

[![pipeline status](https://gitlab.com/kilpkonn/webstore-api/badges/master/pipeline.svg)](https://gitlab.com/kilpkonn/webstore-api/commits/master)
[![coverage report](https://gitlab.com/kilpkonn/webstore-api/badges/master/coverage.svg)](https://gitlab.com/kilpkonn/webstore-api/commits/master)
## Description

A RESTful API backend for a webstore created for ITI0203 course.
Frontend can be found [here](https://github.com/kilpkonn/webstore-front)

**Currently deployed to: [https://taimetuba.ee](https://taimetuba.ee)**  
Public IP: [13.53.135.244](http://13.53.135.244) 

## How to run locally:

__Make sure you have:__
* Java 11 or newer
* Gradle

cd to project root  
  
Start backend:
```console
./gradlew bootRun
```
You can find api at `localhost:8080/api/{api_name}`


### Analysis
Analysis can be found in [business-analysis.md](readme/business-analysis.md)

## Installation Guide for server / gitlab runner
Setup guide for Raspberry can be found in
[setup-guide-raspberry.md](readme/setup-guide-raspberry.md)  
Setup guide for AWS can be found in
[setup-guide-aws.md](readme/setup-guide-aws.md)  
Installation guide to install with docker can be found in 
[installation-guide-docker.md](readme/installation-guide-docker.md) (Currently in use)  
Installation guide to install as service can be found in 
[installation-guide-service.md](readme/installation-guide-service.md)  

## Contributors
* Tavo Annus
* Taavi Jõõts
