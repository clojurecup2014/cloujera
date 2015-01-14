#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

echo "==> upgrading system"
sudo apt-get --assume-yes update
sudo apt-get --assume-yes upgrade

echo "==> installing docker"
curl -sSL https://get.docker.com/ubuntu/ | sudo sh

echo "==> setting up ElasticSearch"
sudo docker pull dockerfile/elasticsearch
sudo docker run -d -p 9200:9200 -p 9300:9300 dockerfile/elasticsearch

echo "==> setting up REDIS"
docker run --name cloujera-redis -d redis redis-server --appendonly yes
