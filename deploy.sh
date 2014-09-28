#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

sudo -v

git pull

# comment out
sed '%s/;(def ^:private uri "http:\/\/cloujera.clojurecup.com:80")/(def ^:private uri "http:\/\/cloujera.clojurecup.com:80")' ./src/cljs/cloujera/rest-client.cljs

# comment in
sed '%s/(def ^:private uri "http://127.0.0.1:8080")/;(def ^:private uri "http://127.0.0.1:8080")/' ./src/cljs/rest-client.cljs

lein cljsbuild once

sudo lein run &&
