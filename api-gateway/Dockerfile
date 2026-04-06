FROM openjdk:26-ea-21-slim
COPY target/*.jar app.jar
EXPOSE 4004
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app.jar"]