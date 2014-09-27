(ns cloujera.core
  (:gen-class)
  (:require [org.httpkit.server :as server]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [compojure.core :refer (GET defroutes)]
            [ring.util.response :as ring]
            [cloujera.burglar.core :as burglar]))

(defroutes application
  (GET  "/" [] (ring/resource-response "index.html" {:root "public"}))
  (route/resources "/")
  (route/not-found "Keep movin', there ain't nothin' to see here."))

(defn- coursera-urls []
  ["https://class.coursera.org/modernpoetry-003/lecture"
   "https://class.coursera.org/comparch-003/lecture"
   "https://class.coursera.org/algs4partI-006/lecture"
   "https://class.coursera.org/calcsing-006/lecture"
   "https://class.coursera.org/automata-003/lecture"
   "https://class.coursera.org/crypto-012/lecture"
   "https://class.coursera.org/ml-007/lecture"
   ])

(->> (coursera-urls)
     (map burglar/get-lecture-html))

(defn -main []
  (server/run-server (handler/api application) {:port 5000}))
>>>>>>> Add basic routing and embedded server
