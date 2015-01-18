#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

sudo -v

echo "==> pulling most recent version (git)"
git checkout .
git pull

echo "==> Building CLJS"
/home/cloujera/bin/lein cljsbuild once

export LEIN_ROOT="is set"

export LOGFILE='/var/log/cloujera.log'


echo "==> emptying Logfile"
cat /dev/null > LOGFILE

echo "==> starting"
sudo sh -c '/home/cloujera/bin/lein run > LOGFILE 2>&1 &'
