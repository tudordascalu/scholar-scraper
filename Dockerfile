FROM openjdk:11

COPY build/libs/*.jar app.jar

RUN mkdir -p /app

ENTRYPOINT ["java", "-jar", "app.jar"]
