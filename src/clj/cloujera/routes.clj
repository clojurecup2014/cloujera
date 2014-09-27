(ns cloujera.routes
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [compojure.core :refer (GET defroutes)]
            [ring.util.response :as ring]
            [cloujera.controllers.scrounger :as scrounger]))

(def ^:private empty-search-error
  {:status 400 :body {:code 400 :message "You gave me an empty search query!"}})

(defn- construct-search-response [query]
  {:query query :results (scrounger/search query)})

(defroutes app-routes
  (GET  "/" [] (ring/resource-response "index.html" {:root "public"}))
  (GET "/search" [query] (if (empty? query)
                             empty-search-error
                             (ring/response (construct-search-response query))))
  (route/resources "/")
  (route/not-found "Keep movin', there ain't nothin' to see here."))
