#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

sudo -v

cloujera_container_name="cloujera"

echo "==> Removing existing cloujera container"
sudo docker rm -f $cloujera_container_name || true

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

echo "==> Building container"
sudo docker build -t $cloujera_container_name ./

echo "==> Running container"
sudo docker run -d -P \
   -p 80:8080 \
   --name $cloujera_container_name \
   --link redis:redis \
   --link elasticsearch:elasticsearch \
   $cloujera_container_name
