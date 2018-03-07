FROM centos:7

RUN yum install -y java-1.8.0-openjdk maven

WORKDIR /app

COPY pom.xml /app/pom.xml
RUN mvn dependency:go-offline

COPY src /app/src

RUN mvn package spring-boot:repackage

RUN cp /app/target/selenium-liveness*.war /app/app.war

CMD ["java", "-jar", "/app/app.war"]