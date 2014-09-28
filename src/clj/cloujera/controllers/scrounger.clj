;; controller for dealing with search requests

(ns cloujera.controllers.scrounger
  (:require [cloujera.models.video :as video]))

(defn search [query]
  (let [videos (search/term-matching query)]
    (take 10(map video/valid-video? videos
         ))))



;; stub for API endpoint
#_(defn search [query]
(let [video (video/valid-video? video/fake-video)]
    [video video video]))
