(ns cloujera.routes
  (:require [compojure.route :as route]
            [compojure.core :refer (GET POST defroutes)]
            [ring.util.response :as ring]
            [cloujera.controllers.scrounger :as scrounger]
            [cloujera.controllers.burglar :as burglar]))

(def ^:private empty-search-error
  {:status 400 :body {:code 400 :message "You gave me an empty search query!"}})

(defn- construct-search-response [query]
  {:query query :results (scrounger/search query)})

(defn- raid-commenced-response []
  (ring/response {:status "Aye aye. Commencing the raid, Sir"}))

(defroutes app-routes
  (GET  "/" [] (ring/resource-response "index.html" {:root "public"}))
  (GET "/search" [query] (if (empty? query)
                             empty-search-error
                             (ring/response (construct-search-response query))))

  (GET "/burglar/go" [] (do
                          (future (burglar/go))
                          (raid-commenced-response)))

  (POST "/burglar/raid" {body :body} (do
                                       (future (burglar/raid-url (:url body)))
                                       (raid-commenced-response)))

  (route/resources "/")
  (route/not-found "Keep movin', there ain't nothin' to see here."))
