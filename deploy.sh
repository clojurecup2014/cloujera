#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

sudo -v

git pull

# comment out
sed -i 's/;(def ^:private uri "http:\/\/cloujera.clojurecup.com:80")/(def ^:private uri "http:\/\/cloujera.clojurecup.com:80")/' ./src/cljs/cloujera/rest-client.cljs

# comment in
sed -i 's/(def ^:private uri "http:\/\/127.0.0.1:8080")/;(def ^:private uri "http:\/\/127.0.0.1:8080")/' ./src/cljs/cloujera/rest-client.cljs

export LEIN_ROOT="Something"

/home/cloujera/bin/lein cljsbuild once


sudo /home/cloujera/bin/lein run &
