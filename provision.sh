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
sudo docker run --name elasticsearch --restart=always -d -p 9200:9200 -p 9300:9300 dockerfile/elasticsearch

echo "==> setting up REDIS"
sudo docker pull dockerfile/redis
sudo docker run --name redis --restart=always -d -p 6379:6379 dockerfile/redis
