(ns cloujera.core
  (:gen-class)
  (:require [org.httpkit.server :as server]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [compojure.core :refer (GET defroutes)]
            [ring.util.response :as ring]))

(defroutes application
  (GET  "/" [] (ring/resource-response "index.html" {:root "public"}))
  (route/resources "/")
  (route/not-found "Keep movin', there ain't nothin' to see here."))

(defn -main []
  (server/run-server (handler/api application) {:port 5000}))
