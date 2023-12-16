FROM openjdk:17-jdk-slim
WORKDIR /app
ARG JAR_FILE=./build/libs/*.jar
COPY ${JAR_FILE} app.jar
COPY settings.sh settings.sh
RUN chmod +x settings.sh
ENTRYPOINT ["./settings.sh"]

#FROM openjdk:17-jdk-slim AS builder
#COPY gradlew .
#COPY gradle gradle
#COPY build.gradle .
#COPY settings.gradle .
#COPY src src
#RUN chmod +x ./gradlew
#RUN ./gradlew clean build
#ENTRYPOINT ["java", "-jar", "/app.jar"]