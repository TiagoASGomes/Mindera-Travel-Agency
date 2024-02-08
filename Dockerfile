FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn install -DskipTests

FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/Mindera-Travel-Agency-0.0.1-SNAPSHOT.jar .

ENTRYPOINT ["java", "-jar", "Mindera-Travel-Agency-0.0.1-SNAPSHOT.jar", "--host", "0.0.0.0"]