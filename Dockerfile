FROM maven:3-jdk-8

COPY src /app/src
COPY pom.xml /app/pom.xml

WORKDIR /app

RUN mvn compile

CMD ["mvn", "spring-boot:run"]