FROM openjdk:26-ea-21-slim
COPY target/*.jar app.jar

EXPOSE 4001
EXPOSE 9001

ENTRYPOINT ["java", "-jar", "/app.jar"]
