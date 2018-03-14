#!/bin/bash

SCRIPTPATH="$( cd "$(dirname "$0")" ; pwd -P )"

if [ ! -z "${HTTP_PROXY}" ]; then
    echo "Generating HTTP proxy config for ${HTTP_PROXY}"
    mkdir -p ${HOME}/.m2
    ${SCRIPTPATH}/generate_proxy_maven_config.sh ${HTTP_PROXY} > ${HOME}/.m2/settings.xml
fi