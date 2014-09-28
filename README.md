###Build and test Locally

First compile the clojurescript

`lein cljsbuild once`

Then build the uberjar

`lein uberjar`

Then try it out locally

`java -jar target/uberjar/cloujera-0.1.0-SNAPSHOT-standalone.jar`

Hit localhost:8080 in your browser.


### Server Configuration

For installation/configuration of Redis used:

https://gist.github.com/bdotdub/714533
