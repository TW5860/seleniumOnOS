FROM centos:7

EXPOSE 4444

RUN yum update -y
RUN yum install java-1.8.0-openjdk -y
RUN mkdir /opt/selenium
RUN yum install wget -y
RUN wget --no-verbose https://selenium-release.storage.googleapis.com/3.9/selenium-server-standalone-3.9.1.jar -O /opt/selenium/selenium-server-standalone.jar

CMD java -jar /opt/selenium/selenium-server-standalone.jar   -role hub
