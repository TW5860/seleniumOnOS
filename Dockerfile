FROM maven:3-jdk-8 AS build

COPY src /app/src
COPY pom.xml /app/pom.xml

WORKDIR /app

RUN mvn package spring-boot:repackage

FROM openjdk:8

COPY --from=build /app/target/bootwildfly-1.0.war /app/app.war

CMD ["java", "-jar", "/app/app.war"]