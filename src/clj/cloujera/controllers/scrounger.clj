;; controller for dealing with search requests

(ns cloujera.controllers.scrounger
  (:require [cloujera.models.video :as video]
            [cloujera.burglar.core :as burglar]
            [cloujera.search.core :as search]
            [schema.core :as s]))

(defn search [query]
  (let [video (s/validate video/Video video/fake-video)]
    (map video/valid-video?
         (search/term-matching query))))

