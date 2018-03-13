#!/bin/bash

set -e

URL=$1

URL_FIELDS="$(echo $URL | grep :// | sed -E -e's#^(https?)://((.*):(.*)@)?(.+):([0-9]+)#\1 \2 \3 \4 \5 \6#')"
PROTO=$(echo $URL_FIELDS | cut -d' ' -f 1)
AUTH=$(echo $URL_FIELDS | cut -d' ' -f 2)
if echo $AUTH | grep -q @; then
    USER=$(echo $URL_FIELDS | cut -d' ' -f 3)
    PASSWORD=$(echo $URL_FIELDS | cut -d' ' -f 4)
    HOST=$(echo $URL_FIELDS | cut -d' ' -f 5)
    PORT=$(echo $URL_FIELDS | cut -d' ' -f 6)
else
    USER="@@REMOVE@@"
    PASSWORD="@@REMOVE@@"
    HOST=$(echo $URL_FIELDS | cut -d' ' -f 2)
    PORT=$(echo $URL_FIELDS | cut -d' ' -f 3)
fi

cat <<SETTINGS_XML | grep -v "@@REMOVE@@"
<settings>
  <proxies>
   <proxy>
      <id>example-proxy</id>
      <active>true</active>
      <protocol>${PROTO}</protocol>
      <host>${HOST}</host>
      <port>${PORT}</port>
      <username>${USER}</username>
      <password>${PASSWORD}</password>
    </proxy>
  </proxies>
</settings>
SETTINGS_XML
