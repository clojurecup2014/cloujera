###Build and test Locally

First compile the clojurescript

`lein cljsbuild once`

Then build the uberjar

`lein uberjar`

Then try it out locally

`java -jar target/cloujera-0.1.0-SNAPSHOT-standalone.jar`

Hit localhost:5000 in your browser.


### Server Configuration

For installation/configuration of Redis used:

https://gist.github.com/bdotdub/714533


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

Then create `/etc/init/redis-server.conf` with the following script:

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
sudo start redis-server
sudo restart redis-server
sudo stop redis-server
```



