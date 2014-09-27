(ns cloujera.rest-client
  (:require [ajax.core :refer [GET POST]]))


(def ^:private meths
  {:get "GET"})

(def ^:private uri
  "http://127.0.0.1:8080")

(defn search [query handler]
  (let [search-endpoint (str uri "/search")]
    (GET search-endpoint
        {:params {:query query}
         :handler handler })))

