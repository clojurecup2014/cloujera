#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

sudo -v

CLOUJERA_CONTAINER_NAME="cloujera"

echo "==> Removing existing cloujera container"
# FIXME: i don't know if this actually works
container_id=$(sudo docker ps -qa --filter name=$CLOUJERA_CONTAINER_NAME)
sudo docker rm "$container_id" || true

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
sudo docker run -d -P \
   -p 80:8080 \
   --name $CLOUJERA_CONTAINER_NAME \
   --link redis:redis \
   --link elasticsearch:elasticsearch \
   $CLOUJERA_CONTAINER_NAME