FROM centos:7

EXPOSE 5555

RUN yum update -y
RUN yum install java-1.8.0-openjdk -y
RUN mkdir /opt/selenium
RUN yum install wget unzip -y
RUN wget --no-verbose https://selenium-release.storage.googleapis.com/3.9/selenium-server-standalone-3.9.1.jar -O /opt/selenium/selenium-server-standalone.jar


#========================
# Selenium Configuration
#========================
# As integer, maps to "maxInstances"
ENV NODE_MAX_INSTANCES 1
# As integer, maps to "maxSession"
ENV NODE_MAX_SESSION 1
# As integer, maps to "port"
ENV NODE_PORT 5555
# In milliseconds, maps to "registerCycle"
ENV NODE_REGISTER_CYCLE 5000
# In milliseconds, maps to "nodePolling"
ENV NODE_POLLING 5000
# In milliseconds, maps to "unregisterIfStillDownAfter"
ENV NODE_UNREGISTER_IF_STILL_DOWN_AFTER 60000
# As integer, maps to "downPollingLimit"
ENV NODE_DOWN_POLLING_LIMIT 2
# As string, maps to "applicationName"
ENV NODE_APPLICATION_NAME ""

COPY chrome.repo /etc/yum.repos.d/google-chrome.repo

RUN yum install google-chrome-stable -y

ARG CHROME_DRIVER_VERSION="latest"
RUN CD_VERSION=$(if [ ${CHROME_DRIVER_VERSION:-latest} = "latest" ]; then echo $(wget -qO- https://chromedriver.storage.googleapis.com/LATEST_RELEASE); else echo $CHROME_DRIVER_VERSION; fi) \
 && echo "Using chromedriver version: "$CD_VERSION \
 && wget --no-verbose -O /tmp/chromedriver_linux64.zip https://chromedriver.storage.googleapis.com/$CD_VERSION/chromedriver_linux64.zip \
 && rm -rf /opt/selenium/chromedriver \
 && unzip /tmp/chromedriver_linux64.zip -d /opt/selenium \
 && rm /tmp/chromedriver_linux64.zip \
 && mv /opt/selenium/chromedriver /opt/selenium/chromedriver-$CD_VERSION \
 && chmod 755 /opt/selenium/chromedriver-${CD_VERSION} \
 && ln -fs /opt/selenium/chromedriver-${CD_VERSION} /usr/bin/chromedriver

COPY generate_config /opt/bin/generate_config

COPY wrap_chrome_binary /opt/bin/wrap_chrome_binary
RUN /opt/bin/wrap_chrome_binary

RUN sh /opt/bin/generate_config > /opt/selenium/config.json
CMD java -Xmx512m -jar /opt/selenium/selenium-server-standalone.jar -role node -hub http://$HUB_HOST:$HUB_PORT/grid/register -nodeConfig /opt/selenium/config.json
