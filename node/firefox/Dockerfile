FROM centos:7

EXPOSE 4444

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

ARG FIREFOX_VERSION=58.0.1
ENV FIREFOX_DOWNLOAD_URL=https://download-installer.cdn.mozilla.net/pub/firefox/releases/$FIREFOX_VERSION/linux-x86_64/en-US/firefox-$FIREFOX_VERSION.tar.bz2
RUN wget -O /tmp/firefox.tar.bz2 https://download-installer.cdn.mozilla.net/pub/firefox/releases/$FIREFOX_VERSION/linux-x86_64/en-US/firefox-$FIREFOX_VERSION.tar.bz2

RUN tar -C /opt -xjf /tmp/firefox.tar.bz2
RUN mv /opt/firefox /opt/firefox-$FIREFOX_VERSION
RUN ln -fs /opt/firefox-$FIREFOX_VERSION/firefox /usr/bin/firefox
