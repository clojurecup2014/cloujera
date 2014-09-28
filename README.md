Cloujera is a fine-grained, spoken word search over Coursera's videos

Cloujera does this by performing full text searches on the transcripts of videos on [coursera](http://coursera.org).

### Build and test Locally

1. Compile the clojurescript
`lein cljsbuild once`

2. Start the app
`lein run`


### Setup Elastic Search Locally

Install elastic search:

```
brew install elasticsearch
```

Install sense which is the Graphical UI to elastic search.
Navigate to install folder likely to be`/usr/local/Cellar/elasticsearch/1.3.2`
This will be avilable from `http://localhost:9200/_plugin/marvel/sense/index.html`

```
./bin/plugin -i elasticsearch/marvel/latest

```

Stop marvel becoming an agent by the following Commands

```
echo 'marvel.agent.enabled: false' >> ./config/elasticsearch.yml

```
Run using
```
elasticsearch

```


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

... Can be found in the [SERVER CONFIGURATION README](./SERVER_CONFIGURATION_README.md)
