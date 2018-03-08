#!/usr/bin/env bash

echo "Executing firefox in headless"
exec /usr/bin/firefox-bin -headless ${@}