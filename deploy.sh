#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

sudo -v

CLOUJERA_CONTAINER_NAME="cloujera"

# export the IPs for the elasticsearch and redis containers
export ELASTICSEARCH_CONTAINER_IP=$(sudo docker inspect --format='{{.NetworkSettings.IPAddress}}' elasticsearch)
export REDIS_CONTAINER_IP=$(sudo docker inspect --format='{{.NetworkSettings.IPAddress}}' redis)

echo "==> Removing existing cloujera container"
sudo docker rm -f cloujera || true

echo "==> pulling most recent version (git)"
git checkout .
git pull

echo "==> Building CLJS"
lein cljsbuild once

echo "==> Uberjarring"
lein uberjar

echo "==> Building container"
sudo docker build -t $CLOUJERA_CONTAINER_NAME ./

echo "==> Running container"
# FIXME: containers can't talk to each other!!!
#        - linking seems to work?
sudo docker run -d -P \
   -p 80:8080 \
   --name $CLOUJERA_CONTAINER_NAME \
   --link redis:redis \
   --link elasticsearch:elasticsearch \
   $CLOUJERA_CONTAINER_NAME
