;; controller for dealing with search requests

(ns cloujera.controllers.scrounger
  (:require [cloujera.models.video :as models]
            [schema.core :as s]))

(defn search [query]
  (let [video (s/validate models/Video models/fake-video)]
    [video video video]))
