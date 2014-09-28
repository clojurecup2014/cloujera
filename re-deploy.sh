#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

sudo -v

git checkout .
git pull

# remove from commented state the prod setting
sed -i 's/;(def ^:private uri "http:\/\/cloujera.clojurecup.com:80")/(def ^:private uri "http:\/\/cloujera.clojurecup.com:80")/' ./src/cljs/cloujera/rest-client.cljs

# comment out the dev setting
sed -i 's/(def ^:private uri "http:\/\/127.0.0.1:8080")/;(def ^:private uri "http:\/\/127.0.0.1:8080")/' ./src/cljs/cloujera/rest-client.cljs

/home/cloujera/bin/lein cljsbuild once

export LEIN_ROOT="is set"

export LOGFILE='/var/log/cloujera.log'

# empty the logfile
cat /dev/null > LOGFILE

# start it up
sudo `/home/cloujera/bin/lein run > LOGFILE 2>&1 &`
