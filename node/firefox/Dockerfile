FROM centos:7

EXPOSE 5555

RUN yum install java-1.8.0-openjdk -y
RUN mkdir /opt/selenium
RUN yum install wget -y
RUN wget --no-verbose https://selenium-release.storage.googleapis.com/3.9/selenium-server-standalone-3.9.1.jar -O /opt/selenium/selenium-server-standalone.jar

RUN yum install bzip2 -y
RUN yum install gtk3 -y

ENV SCREEN_WIDTH 1360
ENV SCREEN_HEIGHT 1020
ENV SCREEN_DEPTH 24
ENV DISPLAY :99.0

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

# Following line fixes https://github.com/SeleniumHQ/docker-selenium/issues/87
ENV DBUS_SESSION_BUS_ADDRESS=/dev/null

RUN yum install firefox -y
RUN yum remove firefox -y

ARG FIREFOX_VERSION=58.0.2
ENV FIREFOX_DOWNLOAD_URL=https://download-installer.cdn.mozilla.net/pub/firefox/releases/$FIREFOX_VERSION/linux-x86_64/en-US/firefox-$FIREFOX_VERSION.tar.bz2
RUN wget -O /tmp/firefox.tar.bz2 https://download-installer.cdn.mozilla.net/pub/firefox/releases/$FIREFOX_VERSION/linux-x86_64/en-US/firefox-$FIREFOX_VERSION.tar.bz2

RUN tar -C /opt -xjf /tmp/firefox.tar.bz2
RUN mv /opt/firefox /opt/firefox-$FIREFOX_VERSION
RUN ln -fs "/opt/firefox-$FIREFOX_VERSION/firefox" /usr/bin/firefox-bin
COPY headless-firefox.sh /usr/bin/firefox

ARG GECKODRIVER_VERSION=latest
RUN GK_VERSION=$(if [ ${GECKODRIVER_VERSION:-latest} = "latest" ]; then echo $(wget -qO- "https://api.github.com/repos/mozilla/geckodriver/releases/latest" | grep '"tag_name":' | sed -E 's/.*"v([0-9.]+)".*/\1/'); else echo $GECKODRIVER_VERSION; fi) \
  && echo "Using GeckoDriver version: "$GK_VERSION \
  && wget --no-verbose -O /tmp/geckodriver.tar.gz https://github.com/mozilla/geckodriver/releases/download/v$GK_VERSION/geckodriver-v$GK_VERSION-linux64.tar.gz \
  && rm -rf /opt/geckodriver \
  && tar -C /opt -zxf /tmp/geckodriver.tar.gz \
  && rm /tmp/geckodriver.tar.gz \
  && mv /opt/geckodriver /opt/geckodriver-$GK_VERSION \
  && chmod 755 /opt/geckodriver-$GK_VERSION \
  && ln -fs /opt/geckodriver-$GK_VERSION /usr/bin/geckodriver

RUN yum install nc -y

COPY generate_config /opt/bin/generate_config

RUN sh /opt/bin/generate_config > /opt/selenium/config.json
CMD java -Xmx512m -jar /opt/selenium/selenium-server-standalone.jar -role node -hub http://$HUB_HOST:$HUB_PORT/grid/register -nodeConfig /opt/selenium/config.json
