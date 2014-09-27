(ns cloujera.core
  (:gen-class)
  (:require [org.httpkit.server :as server]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [compojure.core :refer (GET defroutes)]
            [ring.util.response :as ring]
            [ring.middleware.json :as middleware]
            [cloujera.models.video :as models]
            [schema.core :as s]))

(def ^:private empty-search-error
  {:status 400 :body {:code 400 :message "You gave me an empty search query!"}})

(defn- search [query]
  (let [video (s/validate models/Video models/fake-video)]
    [video video video]))

(defn- construct-search-response [query]
  {:query query :results (search query)})

(defroutes app-routes
  (GET  "/" [] (ring/resource-response "index.html" {:root "public"}))
  (GET "/search" [query] (if (empty? query)
                             empty-search-error
                             (ring/response (construct-search-response query))))
  (route/resources "/")
  (route/not-found "Keep movin', there ain't nothin' to see here."))

(def app
  (-> (handler/api app-routes)
      (middleware/wrap-json-response)))

(defn -main []
  (server/run-server app {:port 5000}))
