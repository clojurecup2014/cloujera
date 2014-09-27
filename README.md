###Build and test Locally

First compile the clojurescript

`lein cljsbuild once`

Then build the uberjar

`lein uberjar`

Then try it out locally

`java -jar target/cloujera-0.1.0-SNAPSHOT-standalone.jar`

Hit localhost:5000 in your browser.
