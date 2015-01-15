# Cloujera

Cloujera lets you do a fine-grained search for spoken words in Coursera's
videos. It does this by performing full text searches on the transcripts of
videos on [coursera](http://coursera.org).


## Local Setup

1. Bring up Vagrant (elasticsearch + redis)
   `vagrant up`

2. Compile the clojurescript
   `lein cljsbuild once`

3. Start the app
   `lein run`

4. On the first run, visit `http://127.0.0.1:8080/burglar/go` to seed the db
   (it will error out ridiculously if you don't do this!)

### Testing docker inside Vagrant
```bash
$ vagrant ssh
$ cd /vagrant
```

Then follow the steps at [Deploying Cloujera](#deploying-cloujera)


### Vagrant Setup
Forwarded ports:
- 9200 : elasticsearch http
- 9300 : elasticsearch
- 6379 : redis

So you can use redis with `redis-cli` for example....

## Scraping courses

Visiting `http://cloujera.whatever/burglar/go` scrapes some 10 courses to get
you started;

To scrape another course, you need to:
0. Visit the cloujera session API
   `https://api.coursera.org/api/catalog.v1/sessions` and choose a course
1. Sign up for the course and agree to honor code **manually** for the
   `vise890+cloujera@gmail.com` user
3. Find the video lecture URL
2. Perform an http `POST http://cloujera.whatever/burglar/raid` with this
   paylod (JSON):
   ```json
   { "url": <video lectures URL> }
   e.g.
   { "url": "https://class.coursera.org/apcalcpart1-001/lecture" }
   ```


## Deploy

### The first time:
```bash
$ ssh user@cloudmachine
$ git clone https://github.com/vise890/cloujera
$ cd cloujera
$ sudo ./provision.sh
```

### Deploying cloujera <a name="deploying-cloujera"></a>

```bash
$ git pull
$ lein cljs build once
$ lein uberjar
$ sudo docker build -t cloujera ./

$ sudo docker run -d -P \
   -p 80:8080 \
   --name cloujera \
   --link redis:redis \
   --link elasticsearch:elasticsearch \
   cloujera
```
