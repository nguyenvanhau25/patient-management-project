FROM openjdk:26-ea-21-slim
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
COPY target/*.jar app.jar
EXPOSE 4003
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app.jar"]
