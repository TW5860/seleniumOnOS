FROM centos:7

WORKDIR /app

COPY pom.xml /app/pom.xml
RUN mvn dependency:go-offline

COPY src /app/src

RUN mvn package spring-boot:repackage

RUN cp /app/target/selenium-liveness*.war /app/app.war

EXPOSE 8080

CMD ["java", "-jar", "/app/app.war"]
