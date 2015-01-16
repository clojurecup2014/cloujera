#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

sudo -v

echo "==> exporting environment variables"
# Environ setup
# `deploy.sh` exports the environment at deploy time. `lein uberjar` looks into the `:uberjar` profile.
# Not finding anything, `environ` looks into the environment and finds the following values:
# IPs and ports for the elasticsearch and redis containers
export ELASTICSEARCH_HOST=$(sudo docker inspect --format='{{.NetworkSettings.IPAddress}}' elasticsearch)
export ELASTICSEARCH_PORT="9200"
export REDIS_HOST=$(sudo docker inspect --format='{{.NetworkSettings.IPAddress}}' redis)
export REDIS_HOST="6379"

echo "==> done."
