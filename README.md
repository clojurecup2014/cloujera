# Cloujera

Cloujera lets you do a fine-grained search for spoken words in Coursera's
videos. It does this by performing full text searches on the transcripts of
videos on [coursera](http://coursera.org).


## Local Setup

1. Bring up Vagrant (elasticsearch + redis):
   `vagrant up`

2. Compile the clojurescript:
   `lein cljsbuild once`

3. Start the app:
   `lein run`

4. On the first run, visit `http://127.0.0.1:8080/burglar/go` to seed the db
   (it will error out ridiculously with an `IndexMissingException` from
   elasticsearch if you don't do this!)


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

   ```
   { "url": videoLecturesURL }
   ```
   For example:
   ```
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


### (Re)-Deploying cloujera

```bash
# in the cloujera directory...
$ ./scripts/deploy.sh
```

**NOTE**: `deploy.sh` pulls the most recent version of cloujera from the repo


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
$ sudo docker exec cloujera cat /var/cloujera.log
```

### Checking elasticsearch health

Visit `http://localhost:9200/`, you should see `status: 200`


### Checking redis Running

`redis-cli` will drop you into a redis shell. Some useful commands are: `INFO`,
`MONITOR`, `HELP`, `HELP @server`.


### Dropping into a shell inside a container
```bash
$ vagrant ssh || ssh user@cloudbox
$ sudo docker exec -i -t cloujera bash
```


# BUGS
- `lein run` doesn't give any output initially
- `lein run` doens't reload
