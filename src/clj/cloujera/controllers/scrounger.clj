;; controller for dealing with search requests

(ns cloujera.controllers.scrounger
  (:require [cloujera.models.video :as video]
            [cloujera.burglar.core :as burglar]
            [cloujera.search.core :as search]
            [schema.core :as s]))

(defn search [query]
  (let [videos (search/term-matching query)]
    (map video/valid-video? videos)))
