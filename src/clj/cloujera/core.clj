(ns cloujera.core
  (:gen-class)
  (:require [org.httpkit.server :as server]
            [compojure.handler :as handler]
            [ring.middleware.json :as middleware]
            [cloujera.routes :as routes]))

(def app
  (-> (handler/site routes/app-routes)
      (middleware/wrap-json-response)))

(defn -main []
  (server/run-server app {:port 80}))
