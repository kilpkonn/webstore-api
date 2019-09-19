FROM openjdk:11
ADD build/libs/webstore-0.0.1-SNAPSHOT.jar webstore.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "webstore.jar"]