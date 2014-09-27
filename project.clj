(defproject cloujera "0.1.0-SNAPSHOT"
  :description "Full text search for coursera"
  :url "cloujera.clojurecup.com"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2311"]

                 [prismatic/schema "0.3.0"]
                 [http-kit "2.1.16"]

                 [enlive "1.1.5"] ;; for scraping

                 [compojure "1.1.9"] ;; for routing

                 [clojurewerkz/elastisch "2.1.0-beta7"] ;; for persitance
                 [com.taoensso/carmine "2.7.0"]] ;; for caching


  :plugins [[lein-cljsbuild "1.0.4-SNAPSHOT"]]

  :source-paths ["src"]

  :cljsbuild {
    :builds [{:id "cloujera"
              :source-paths ["src"]
              :compiler {
                :output-to "cloujera.js"
                :output-dir "out"
                :optimizations :none
                :source-map true}}]})
