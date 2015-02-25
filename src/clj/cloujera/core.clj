(ns cloujera.core
  (:require [cloujera.routes :as routes]
            [compojure.handler :as handler]
            [org.httpkit.server :as server]
            [ring.middleware.json :as middleware]
            [ring.middleware.reload :as reload])
  (:gen-class))

(def app
  (-> (handler/site routes/app-routes)
      (middleware/wrap-json-response)
      (middleware/wrap-json-body {:keywords? true :bigdecimals? true})))

(defn -main []
  (do
    (println "Listening on port 8080...")
    (server/run-server app {:port 8080})))

(defn -dev-main []
  (do
    (println "DEV-SERVER: Listening on port 8080...")
    (server/run-server (reload/wrap-reload #'app) {:port 8080})))
