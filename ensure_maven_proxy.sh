#!/usr/bin/env bash

if [ ! -z "${HTTP_PROXY}" ]; then
    mkdir -p ${HOME}/.m2
    ./generate_proxy_maven_config.sh ${HTTP_PROXY} > ${HOME}/.m2/settings.xml
fi