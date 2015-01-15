# Cloujera

Cloujera lets you do a fine-grained search for spoken words in Coursera's
videos. It does this by performing full text searches on the transcripts of
videos on [coursera](http://coursera.org).

# Build and test Locally

1. Bring up Vagrant (elasticsearch + redis)
   `vagrant up`

2. Compile the clojurescript
   `lein cljsbuild once dev`

3. Start the app
   `lein run`

4. Visit `localhost:8080/burglar/go` to seed the db
   (it will error out ridiculously if you don't do this!)

# Vagrant
Forwarded ports:
- 9200 : elasticsearch http
- 9300 : elasticsearch
- 6379 : redis

So you can use redis with `redis-cli` for example....

# Deploy (sort of)
The first time:
```bash
$ ssh cloudmachine
$ git clone ..
$ cd cloujera
$ sudo ./provision.sh
```

```bash
$ lein cljs build once prod
$ # FIXME: lein uberjar
$ # TODO: dockerize
$ # TODO: run
```

