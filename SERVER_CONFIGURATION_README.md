# Server configuration

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


