# Cloujera

Cloujera lets you do a fine-grained search for spoken words in Coursera's videos. It does this by performing full text searches on the transcripts of videos on [coursera](http://coursera.org).

# Build and test Locally

1. Compile the clojurescript
`lein cljsbuild once`

2. Start the app
`lein run`


## Setup Elastic Search Locally

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
# Vagrant
```bash
$ vagrant up
```
Forwarded ports:
- 9200 : elasticsearch http
- 9300 : elasticsearch
- 6379 : redis

So you can use redis with `redis-cli` for example....
