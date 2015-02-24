(ns cloujera.burglar.core
  (:require [cloujera.burglar.parser.core :as parser]
            [cloujera.burglar.parser.video :as video-parser]
            [cloujera.burglar.scraper :as scraper]
            [cloujera.cache.core :as cache]
            [cloujera.search.core :as search]
            [cloujera.burglar.parser.utils :as utils]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def username "vise890+cloujera@gmail.com")
(def password "letswinthisthing")
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def get-coursera-page
  (cache/persist (scraper/get-protected-page username password)))

;; Video -> Video
(defn- get-and-add-video-url [video]
  {:pre [(utils/not-nil? video)]}
  (let [embedded-video-url (video-parser/_link->embedded-video-url (:_link video))
        embedded-video-page (get-coursera-page embedded-video-url)
        url (video-parser/extract-video-url embedded-video-page)]
    (assoc video :video-url url)))

;; THE BEAST
(defn raid [lecture-url]
  {:pre [(utils/not-nil? lecture-url)]}
  (let [lecture-page-html (get-coursera-page lecture-url)
        videos-without-urls (parser/extract-videos lecture-page-html)
        all-videos (pmap get-and-add-video-url videos-without-urls)
        useable-videos (remove video-parser/no-embeddable-video? all-videos)]
    (map search/save useable-videos)))
