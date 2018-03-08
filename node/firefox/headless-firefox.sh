#!/usr/bin/env bash
set -ex

echo "Executing firefox in headless"
exec /usr/bin/firefox-bin -headless ${@}
