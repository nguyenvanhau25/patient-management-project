FROM openjdk:26-ea-21-slim
COPY target/*.jar app.jar
EXPOSE 4004
ENTRYPOINT ["java", "-jar", "/app.jar"]