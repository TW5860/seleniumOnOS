FROM centos:7

ARG MAVEN_VERSION=3.5.2

RUN yum update -y
RUN yum install -y java-1.8.0-openjdk-devel wget which
RUN wget http://www-eu.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz
RUN tar xvf apache-maven-$MAVEN_VERSION-bin.tar.gz \
    && mv apache-maven-$MAVEN_VERSION /usr/local/apache-maven

COPY ensure_maven_proxy.sh /app/ensure_maven_proxy.sh
COPY generate_proxy_maven_config.sh /app/generate_proxy_maven_config.sh
RUN /app/ensure_maven_proxy.sh

WORKDIR /app

COPY pom.xml /app/pom.xml
RUN /usr/local/apache-maven/bin/mvn dependency:go-offline

COPY src /app/src

RUN /usr/local/apache-maven/bin/mvn package spring-boot:repackage

RUN cp /app/target/selenium-liveness*.war /app/app.war

EXPOSE 8080

CMD ["java", "-jar", "/app/app.war"]
