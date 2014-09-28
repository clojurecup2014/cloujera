### Build and test Locally

1. Compile the clojurescript
`lein cljsbuild once`

2. Start the app
`lein run`

### Deploying to production

*important* our local code references our API at 127.0.0.1:8080 but for production we want that reference to be cloujera.clojurecup.com:80 so go to `rest-client.cljs` and uncomment the appropriate line like so:

```
#_(def ^:private uri "http://127.0.0.1:8080")
(def ^:private uri "http://cloujera.clojurecup.com:80")

```
Now do a cljs compile with `lein cljsbuild once`

then, build the uberjar locally

`lein uberjar`

Once you've done this you can reverse the comments in the code to go back to deving.
(Yes, we need a config file for this, but I've parked it until later)

An uberjar will magically appear in ./target
Now scp it to the production server

`scp -i ~/.ssh/id_rsa_clojurecup target/cloujera-0.1.0-SNAPSHOT-standalone.jar cloudsigma@178.22.65.147:/tmp `

This will put the file in the tmp directory, now ssh to the box

`ssh cloudsigma@178.22.65.147 -i ~/.ssh/id_rsa_clojurecup`

and move the file from /tmp to /home/cloujera (password is in slack)

`sudo mv /tmp/cloujera-0.1.0-SNAPSHOT-standalone.jar /home/cloujera`

become the cloujera user

`sudo su cloujera`

go to home and chown the file

`sudo chown cloujera:cloujera cloujera-0.1.0-SNAPSHOT-standalone.jar`

run it!

```
sudo start cloujera
sudo restart cloujera
sudo stop cloujera
```

check cloujera.clojurecup.com

### Server Configuration

#### The JAR

Create `/etc/init/cloujera.conf` with the following script:

```
description "Cloujera Service"  
author "ThoughtWorkers"  
  
start on runlevel [3]  
stop on shutdown  
  
expect fork  
  
script  
    cd /home/cloujera/  
    java -jar /home/cloujera/cloujera-0.1.0-SNAPSHOT-standalone.jar > /var/log/cloujera.log 2>&1  
    emit cloujera_running  
end script  
```

Commands are:

```
sudo start cloujera
sudo restart cloujera
sudo stop cloujera
```

#### Redis

For installation/configuration of Redis I used: https://gist.github.com/bdotdub/714533

To install:

```
sudo apt-get install redis-server
```

To disable the default `init.d` script for redis:

```
sudo update-rc.d redis-server disable
```

Then create `/etc/init/redis-server.conf` with the following script:

```
description "redis server"

start on runlevel [23]
stop on shutdown

exec sudo -u redis /usr/bin/redis-server /etc/redis/redis.conf

respawn
```

Commands are:

```
sudo start redis-server
sudo restart redis-server
sudo stop redis-server
```

#### Elastic Search

Download and install the Public Signing Key

```
wget -qO - http://packages.elasticsearch.org/GPG-KEY-elasticsearch | sudo apt-key add -
```

Add the following to your /etc/apt/sources.list to enable the repository

```
deb http://packages.elasticsearch.org/elasticsearch/1.3/debian stable main
```

Run apt-get update and the repository is ready for use. You can install it with:

```
sudo apt-get update && sudo apt-get install elasticsearch
```

Disable the init script

```
sudo update-rc.d -f elasticsearch remove
```

Then create `/etc/init/elasticsearch.conf` with the following script:

```
# ElasticSearch upstart script

description     "ElasticSearch service"

start on (net-device-up
          and local-filesystems
          and runlevel [2345])

stop on runlevel [016]

respawn

respawn limit 10 30

# NB: Upstart scripts do not respect
# /etc/security/limits.conf, so the open-file limits
# settings need to be applied here.
limit nofile 32000 32000

setuid elasticsearch
setgid elasticsearch
exec /usr/share/elasticsearch/bin/elasticsearch -f -Des.default.config=/etc/elasticsearch/elasticsearch.yml -Des.default.path.home=/usr/share/elasticsearch/ -Des.default.path.logs=/var/log/elasticsearch/ -Des.default.path.data=/var/lib/elasticsearch/ -Des.default.path.work=/tmp/elasticsearch -Des.default.path.conf=/etc/elasticsearch
```

Commands are:

```
sudo start elasticsearch
sudo restart elasticsearch
sudo stop elasticsearch
```
