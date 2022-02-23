# syntax=docker/dockerfile:1

FROM eclipse-temurin:11-alpine AS base

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw mvnw.cmd pom.xml ./
RUN ./mvnw dependency:go-offline

COPY src ./src

FROM base AS test
RUN ./mvnw test

FROM base AS development
CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000'"]

FROM base AS builder
RUN ./mvnw package -DskipTests

FROM base AS packager
RUN $JAVA_HOME/bin/jlink \
         --add-modules java.base,java.desktop,java.logging,java.management,java.naming,java.security.jgss,java.instrument,java.sql \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /jre

FROM alpine:3.15.0
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH="$PATH:$JAVA_HOME/bin"
COPY --from=packager /jre "$JAVA_HOME"
COPY --from=builder /app/target/docker-java-*.jar /docker-java.jar

EXPOSE 8080

CMD ["java", "-jar", "/docker-java.jar"]