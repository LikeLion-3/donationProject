#FROM openjdk:17-jdk-slim
#ARG JAR_FILE=build/libs/*.jar
#COPY ${JAR_FILE} app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]

FROM openjdk:17-jdk-slim AS builder
WORKDIR /BE
COPY env-back.env .
RUN export $(cat env-back.env | xargs)
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew clean build
ENTRYPOINT ["java", "-jar", "/app.jar"]