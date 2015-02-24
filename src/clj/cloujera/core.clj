(ns cloujera.core
  (:require [cloujera.routes :as routes]
            [org.httpkit.server :as server]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :as json])
  (:gen-class))

(def app
  (-> (wrap-defaults routes/app-routes api-defaults)
      (json/wrap-json-response)
      (json/wrap-json-body {:keywords? true :bigdecimals? true})))

(defn -main []
  (do
    (println "Listening on port 8080...")
    (server/run-server app {:port 8080})))
