FROM openjdk:11
ADD build/libs/webstore-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=/config/$CONFIG_FILE"]