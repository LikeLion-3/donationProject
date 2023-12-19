FROM openjdk:17-jdk-slim
ARG JAR_FILE=./build/libs/*.jar
COPY ${JAR_FILE} app.jar
COPY settings.sh .
RUN chmod +x settings.sh
ENTRYPOINT ["./settings.sh"]