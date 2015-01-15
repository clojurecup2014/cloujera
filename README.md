# Cloujera

Cloujera lets you do a fine-grained search for spoken words in Coursera's
videos. It does this by performing full text searches on the transcripts of
videos on [coursera](http://coursera.org).


## Local Setup

1. Bring up Vagrant (elasticsearch + redis)
   `vagrant up`

2. Compile the clojurescript
   `lein cljsbuild once dev`

3. Start the app
   `lein run`

4. On the first run, `http://127.0.0.1:8080/burglar/go` to seed the db
   (it will error out ridiculously if you don't do this!)


### Vagrant Setup
Forwarded ports:
- 9200 : elasticsearch http
- 9300 : elasticsearch
- 6379 : redis

So you can use redis with `redis-cli` for example....


# Deploy (sort of, still TODO)

The first time:
```bash
$ ssh user@cloudmachine
$ git clone https://github.com/vise890/cloujera
$ cd cloujera
$ sudo ./provision.sh
```

Then, to deploy the app:
```bash
$ git pull
$ lein cljs build once prod
$ lein uberjar
$ # TODO: dockerize
$ # TODO: run container
```

# BUGS
- when reloading vagrant, provisioning doesn't get run so redis and elasticsearch contaners don't start
  - move to upstart?
  - convenience script to start them?

- elasticsearch data gets dumped every time the elasticsearch container restarts?
