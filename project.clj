(defproject cloujera "0.1.0-SNAPSHOT"
  :description "Full text search for coursera"
  :url "cloujera.clojurecup.com"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2665"]
                 [om "0.7.3"]
                 [com.cognitect/transit-cljs "0.8.199"]

                 [prismatic/schema "0.3.3"]
                 [compojure "1.3.1"] ;; for routing
                 [ring "1.3.2"]
                 [ring/ring-json "0.3.1"] ;; for serializing
                 [http-kit "2.1.19"]

                 [clj-http "1.0.1"] ;; for http getting and cookieing

                 [enlive "1.1.5"] ;; for scraping

                 [cheshire "5.4.0"] ;; JSON deserializing

                 [clojurewerkz/elastisch "2.1.0"] ;; for persitance
                 [com.taoensso/carmine "2.9.0"]] ;; for caching

  :plugins [[lein-cljsbuild "1.0.4-SNAPSHOT"]]

  :min-lein-version "2.0.0"

  :source-paths ["src/clj"]
  :resource-paths ["resources"]

  :cljsbuild {:builds [{:id "cloujera"
                        :source-paths ["src/cljs"]
                        :compiler {
                          :output-to "resources/public/js/cloujera.js"
                          :output-dir "resources/public/js/out"
                          :optimizations :none
                          :source-map true}}]}
  :main ^:skip-aot cloujera.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
             :dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                                  [ring-mock "0.1.5"]]})
