;; controller for dealing with search requests

(ns cloujera.controllers.scrounger
  (:require [cloujera.models.video :as video]
            [cloujera.search.core :as search]))

(defn search [query]
  (let [videos (search/term-matching query)]
    (map video/valid-video? videos)))
