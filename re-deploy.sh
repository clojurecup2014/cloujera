#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

sudo -v

echo "==> pulling most recent version (git)"
git checkout .
git pull

echo "==> switching to production mode"
# remove from commented state the prod setting
sed -i 's/;(def ^:private uri "http:\/\/cloujera.clojurecup.com:80")/(def ^:private uri "http:\/\/cloujera.clojurecup.com:80")/' ./src/cljs/cloujera/rest-client.cljs

# comment out the dev setting
sed -i 's/(def ^:private uri "http:\/\/127.0.0.1:8080")/;(def ^:private uri "http:\/\/127.0.0.1:8080")/' ./src/cljs/cloujera/rest-client.cljs

echo "==> Building CLJS"
/home/cloujera/bin/lein cljsbuild once

export LEIN_ROOT="is set"

export LOGFILE='/var/log/cloujera.log'


echo "==> emptying Logfile"
cat /dev/null > LOGFILE

echo "==> starting"
sudo sh -c '/home/cloujera/bin/lein run > LOGFILE 2>&1 &'