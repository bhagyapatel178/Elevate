# syntax=docker/dockerfile:1

#########################
# 1️⃣  Build the JAR
#########################
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /build

# copy build descriptors first to leverage Docker layer cache
COPY pom.xml .
COPY src ./src

# -B = batch mode,   -DskipTests keeps the build fast
RUN mvn -B package -DskipTests

#########################
# 2️⃣  Run stage
#########################
FROM eclipse-temurin:21-jre

# A non-root user is safer (optional but good practice)
RUN useradd -ms /bin/bash appuser
USER appuser
WORKDIR /app

# copy the fat-jar we just built
COPY --from=build /build/target/*.jar app.jar

# Spring Boot listens on 8080 by default
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]