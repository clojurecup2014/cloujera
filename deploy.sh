#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

sudo -v

cloujera_container_name="cloujera"

echo "==> exporting environment variables"
# Environ setup
# `deploy.sh` exports the environment at deploy time. `lein uberjar` looks into the `:uberjar` profile.
# Not finding anything, `environ` looks into the environment and finds the following values:
# IPs and ports for the elasticsearch and redis containers
export ELASTICSEARCH_HOST=$(sudo docker inspect --format='{{.NetworkSettings.IPAddress}}' elasticsearch)
export ELASTICSEARCH_PORT="9200"
export REDIS_HOST=$(sudo docker inspect --format='{{.NetworkSettings.IPAddress}}' redis)
export REDIS_HOST="6379"

echo "==> Removing existing cloujera container"
sudo docker rm -f $cloujera_container_name || true

echo "==> pulling most recent version (git)"
git checkout .
git pull

echo "==> Building CLJS"
lein cljsbuild once

echo "==> Uberjarring"
lein uberjar

echo "==> Building container"
sudo docker build -t $cloujera_container_name ./

echo "==> Running container"
# FIXME: containers can't talk to each other!!!
#        - linking seems to work?
sudo docker run -d -P \
   -p 80:8080 \
   --name $cloujera_container_name \
   --link redis:redis \
   --link elasticsearch:elasticsearch \
   $cloujera_container_name
