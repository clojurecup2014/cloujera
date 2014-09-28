(ns cloujera.burglar.parser.utils
  (:require [clj-http.client :as http]
            [cloujera.cache.core :as cache]))

(def course-api-endpoint "https://api.coursera.org/api/catalog.v1/courses")

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn get-body [url]
  (:body (http/get url)))
(def cached-http-get (cache/persist get-body))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-attr
  "gets the value of attr of tag"
  [tag attr]
  (-> tag first :attrs attr))
