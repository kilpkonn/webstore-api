# iti0203-2019-project

iti0203 project

### How to run:
cd to project root <br />
buil prjoect with gradle (bootJar task)

```console
docker build -f Dockerfile -t docker-webstore .
docker run -p 8080:8080 docker-webstore
```

To see webpage in browser you need to use ip shown in docker on startup instead of localhost.
Port is 8080


### Analysis
Analysis can be found in [analysis.md](analysis.md)