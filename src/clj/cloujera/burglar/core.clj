(ns cloujera.burglar.core
  (:require [cloujera.burglar.scraper :as scraper]
            [cloujera.cache.core :as cache]
            [cloujera.burglar.parser :as parser]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def username "vise890+cloujera@gmail.com")
(def password "letswinthisthing")
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- coursera-urls []
  ["https://class.coursera.org/modernpoetry-003/lecture"
   "https://class.coursera.org/comparch-003/lecture"
   "https://class.coursera.org/algs4partI-006/lecture"
   "https://class.coursera.org/calcsing-006/lecture"
   "https://class.coursera.org/automata-003/lecture"
   "https://class.coursera.org/crypto-012/lecture"
   "https://class.coursera.org/ml-007/lecture"])

(def get-coursera-page (cache/persist (scraper/get-protected-page username password)))

;; Video -> Video
(defn- add-video-url [video]
  (let [embedded-video-url (parser/video-_link->embedded-video-url (:_link video))
        embedded-video-page (get-coursera-page embedded-video-url)
        url (parser/extract-video-url embedded-video-page)]
    (assoc video :url url)))

;; Video -> Video
;(def add-course [video])

;; THE BEAST
(->> (coursera-urls)
     (map get-coursera-page)
     (map parser/extract-videos)
     (flatten)
     (map add-video-url))
