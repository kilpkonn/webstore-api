# iti0203-2019-project (team 06)

iti0203 project

### How to run:

__Make sure you have:__
- [x] Docker (or Docker toolbox for windows)
- [x] Docker-compose
- [x] Java 11 or newer

cd to project root <br />
Start database:

```console
docker-compose up
```
Start backend:
```console
./gradlew bootRun
```
You can find api at localhost:8080/api/{api_name}

**Currently spring app is NOT packed into docker container.**
Will be aviable soon :)

### Analysis
Analysis can be found in [analysis.md](analysis.md)