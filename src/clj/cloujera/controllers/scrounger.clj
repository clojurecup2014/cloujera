;; controller for dealing with search requests

(ns cloujera.controllers.scrounger
  (:require [cloujera.models.video :as video]
            [cloujera.burglar.core :as burglar]
            [schema.core :as s]))

(defn search [query]
  (let [video (s/validate video/Video video/fake-video)]
    (map video/valid-video?
         (take 3 (burglar/raid "https://class.coursera.org/modernpoetry-003/lecture")))))
