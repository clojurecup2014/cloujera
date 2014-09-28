(ns cloujera.burglar.parser.video
  (:require [cloujera.burglar.parser.utils :as utils]
            [clojure.string :as string]
            [net.cgrand.enlive-html :as html]))

(defn- _link->base-url [_link]
  (->> (string/split _link #"/")
       (butlast)
       (string/join "/")))

(defn- _link->id [_link]
  (last (string/split _link #"/")))

(defn _link->embedded-video-url [_link]
  (let [video-id (_link->id _link)
        base-url (_link->base-url _link)]
    (str base-url "/view?lecture_id=" video-id)))

(defn- _link->transcript-url [_link]
  (let [id (_link->id _link)
        base-url (_link->base-url _link)]
    (str base-url "/subtitles?q=" id "_en&format=txt")))

(defn- soup->title [soup]
  (-> (html/select soup [:a.lecture-link])
      first
      :content
      first
      string/trim))

(defn- transcript-exists? [soup]
    (not (empty?
      (html/select soup
                   [(html/attr-contains :href "subtitle")]))))

(defn can-index? [soup]
    (transcript-exists? soup))

(defn- soup->_link
  "extracts the video._link from an the html soup"
  [soup]
  (let [tag (html/select soup [:a.lecture-link])]
    (-> tag
        first
        :attrs
        :href)))

;; HtmlSoup(Video) -> video(without course)
(defn soup->video
  "parses an HTML soup of a video into a Video (w/o course)"
  [soup]
  (let [_link (soup->_link soup)]
     {:id (_link->id _link)
      :title (soup->title soup)
      :transcript (utils/cached-http-get (_link->transcript-url _link))
      :_link _link}))

;;;; URL
;; HTML(EmbeddedVideoPage) -> EmbeddedVideoURL
(defn extract-video-url
  "returns the URL from the embedded video page"
  [html]
  (let [soup (html/html-snippet html)
        tag (html/select soup
                         [:source (html/attr-contains :type "video/webm")])]
     (utils/get-attr tag :src)))
