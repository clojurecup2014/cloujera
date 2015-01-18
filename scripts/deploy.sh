#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

echo "==> pulling most recent version (git)"
# FIXME: it would be nice to checkout to avoid weird
# stuff happening, but it's really easy to run this
# script fortest in the vagrant VM in /vagrant
# and lose work this way...
# git checkout .
git pull

echo "==> Building CLJS"
lein cljsbuild once

echo "==> Uberjarring"
lein uberjar

echo "==> Running cloujera"
java -jar ./target/uberjar/cloujera-0.1.0-SNAPSHOT-standalone.jar > cloujera.log 2>&1
