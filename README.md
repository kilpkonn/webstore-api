# iti0203-2019-project (team 06)

[![pipeline status](https://gitlab.cs.ttu.ee/taannu/iti0203-2019-project-back/badges/master/pipeline.svg)](https://gitlab.cs.ttu.ee/taannu/iti0203-2019-project-back/commits/master)

### Description

A RESTful API backend for a webstore created for ITI0203 course.

### How to run:

__Make sure you have:__
* Docker (or Docker toolbox for windows)
* Docker-compose
* Java 11 or newer
* Gradle

cd to project root <br />
Start database:

```console
docker-compose up
```
Start backend:
```console
gradle bootRun
```
You can find api at `localhost:8080/api/{api_name}`


### Analysis
Analysis can be found in [analysis.md](readme/business-analysis.md)