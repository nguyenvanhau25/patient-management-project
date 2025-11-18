FROM openjdk:26-ea-21-slim
COPY target/*.jar app.jar

EXPOSE 4005
ENTRYPOINT ["java", "-jar", "/app.jar"]