# TODO
Docker already exports this crap in the container linked with others:
```
REDIS_PORT=tcp://172.17.0.3:6379
REDIS_PORT_6379_TCP=tcp://172.17.0.3:6379
REDIS_PORT_6379_TCP_ADDR=172.17.0.3
REDIS_PORT_6379_TCP_PORT=6379
REDIS_PORT_6379_TCP_PROTO=tcp
REDIS_NAME=/cloujera/redis
ELASTICSEARCH_PORT=tcp://172.17.0.2:9200
ELASTICSEARCH_PORT_9200_TCP=tcp://172.17.0.2:9200
ELASTICSEARCH_PORT_9200_TCP_ADDR=172.17.0.2
ELASTICSEARCH_PORT_9200_TCP_PORT=9200
ELASTICSEARCH_PORT_9200_TCP_PROTO=tcp
ELASTICSEARCH_PORT_9300_TCP=tcp://172.17.0.2:9300
ELASTICSEARCH_PORT_9300_TCP_ADDR=172.17.0.2
ELASTICSEARCH_PORT_9300_TCP_PORT=9300
ELASTICSEARCH_PORT_9300_TCP_PROTO=tcp
ELASTICSEARCH_NAME=/cloujera/elasticsearch
ELASTICSEARCH_ENV_JAVA_HOME=/usr/lib/jvm/java-7-oracle
ELASTICSEARCH_ENV_ES_PKG_NAME=elasticsearch-1.4.1
```
- investigate if I can use `ELASTICSEARCH_PORT` and `REDIS_PORT`

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


### Testing dockerized cloujera inside Vagrant
```bash
$ vagrant ssh
$ cd /vagrant
$ ./scripts/deploy.sh
```

**NOTE:** the address to access the dockerized cloujera is
`http://127.0.0.1:8081` (see `Vagrantfile`)


## Scraping courses

Visiting `http://cloujera.whatever/burglar/go` scrapes some 10 courses to get
you started;

To scrape another course, you need to:
0. Visit the cloujera session API
   `https://api.coursera.org/api/catalog.v1/sessions` and choose a course
1. Sign up for the course and agree to honor code **manually** for the
   `vise890+cloujera@gmail.com` user
3. Find the video lecture URL (`videoLecturesURL`)
2. Perform an http `POST http://cloujera.whatever/burglar/raid` with this
   paylod (JSON):
   ```json
   { "url": videoLecturesURL }
   ```
   For example:
   ```json
   { "url": "https://class.coursera.org/apcalcpart1-001/lecture" }
   ```


## Deployment

### Provisioning (The first time)
```bash
$ ssh user@cloudmachine
$ git clone https://github.com/vise890/cloujera
$ cd cloujera
$ sudo ./scripts/provision.sh
```


### Deploying cloujera

```bash
# in the cloujera directory...
$ ./scripts/deploy.sh
```


## Troubleshooting

Ensure that all the containers are running:
```bash
$ vagrant ssh
$ sudo docker ps -a
```
You should see `redis`, `elasticsearch` and `cloujera` running


### Checking the cloujera logs

```bash
$ vagrant ssh

$ sudo docker exec cloujera cat /var/cloujera_stdout.log
$ sudo docker exec cloujera cat /var/cloujera_stderr.log
```

### Checking elasticsearch health

Visit `http://localhost:9200/, you should see `status: 200`


### Checking redis Running

`redis-cli` will drop you into a redis shell. Some useful commands are:
`INFO`, `MONITOR`, `HELP`, `HELP @server`.

# BUGS
- `lein run` doesn't give any output initially
