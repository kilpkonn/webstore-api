# iti0203-2019-project (team 06)

[![pipeline status](https://gitlab.cs.ttu.ee/taannu/iti0203-2019-project-back/badges/master/pipeline.svg)](https://gitlab.cs.ttu.ee/taannu/iti0203-2019-project-back/commits/master)
[![coverage report](https://gitlab.cs.ttu.ee/taannu/iti0203-2019-project-back/badges/master/coverage.svg)](https://gitlab.cs.ttu.ee/taannu/iti0203-2019-project-back/commits/master)

## Description

A RESTful API backend for a webstore created for ITI0203 course.

**Currently deployed to: [https://www.flowerstore.ee](https://www.flowerstore.ee)**  
Public IP: [13.48.149.235](http://13.48.149.235) 

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
Setup guide for raspberry can be found in
[setup-raspberry.md](readme/setup-raspberry.md)
Installation guide to install with docker can be found in 
[installation-guide-docker.md](readme/installation-guide-docker.md) (Currently in use)  
Installation guide to install as service can be found in 
[installation-guide-service.md](readme/installation-guide-service.md)  
