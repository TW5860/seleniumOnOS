FROM maven:3-jdk-8 AS build

WORKDIR /app

COPY pom.xml /app/pom.xml
RUN mvn dependency:go-offline

COPY src /app/src

RUN mvn package spring-boot:repackage

FROM openjdk:8

COPY --from=build /app/target/selenium-liveness*.war /app/app.war

CMD ["java", "-jar", "/app/app.war"]