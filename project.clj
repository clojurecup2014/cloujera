(defproject cloujera "0.1.0-SNAPSHOT"
  :description "Full text search for coursera"
  :url "cloujera.clojurecup.com"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2311"]

                 [prismatic/schema "0.3.0"]
                 [compojure "1.1.9"] ;; for routing
                 [ring "1.3.1"]
                 [http-kit "2.1.19"]

                 [enlive "1.1.5"] ;; for scraping


                 [cheshire "5.3.1"] ;; for serializing

                 [clojurewerkz/elastisch "2.1.0-beta7"] ;; for persitance
                 [com.taoensso/carmine "2.7.0" :exclusions [org.clojure/clojure]]] ;; for caching




  :plugins [[lein-cljsbuild "1.0.4-SNAPSHOT"]]

  :source-paths ["src/clj"]
  :resource-paths ["resources"]

  :cljsbuild {
    :builds [{:id "cloujera"
              :source-paths ["src/cljs"]
              :compiler {
                :output-to "resources/public/js/cloujera.js"
                :output-dir "resources/public/js/out"
                :optimizations :none
                :source-map true}}]}
  :main ^:skip-aot cloujera.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
