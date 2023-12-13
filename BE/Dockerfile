FROM gradle:8.4-jdk17-alpine AS builder
WORKDIR /builder

COPY build.gradle settings.gradle /builder/
RUN gradle build -x test --parallel --continue > /dev/null 2>&1 || true

COPY src /builder/src

RUN gradle clean bootJar

FROM openjdk:17-jdk-slim
COPY --from=builder /builder/build/libs/*.jar app/app.jar
COPY --from=builder /builder/build/resources app/resources
ENTRYPOINT ["java", "-jar", "app/app.jar"]