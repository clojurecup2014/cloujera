(ns cloujera.burglar.parser
  (:require [cloujera.models.video :as video]
            [clojure.string :as string]
            [schema.core :as schema]
            [clj-http.client :as http]
            [net.cgrand.enlive-html :as html]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; COURSE

(defn- get-course-_link [h]
  (let [attr :data-lecture-coursebase
        tag (html/select h [:div (html/attr-has attr)])
        _link (-> tag
                  first
                  :attrs
                  attr)]
    _link))

;; TODO: implement
(defn- course-_link->id [_link] "susdev-002")

;; TODO: implement
(defn- course-_link->title [_link] "The Age of Sustainable Development")

(defn extract-course [h]
  (let [_link (get-course-_link h)
        c {:id (course-_link->id _link)
           :title (course-_link->title _link)
           :_link (get-course-_link h)}]
  (schema/validate video/Course c)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; VIDEO

(defn- video-list-item->_link [video-list-item]
  (-> video-list-item
      :content
      first ;; the first element is the anchor with the link
      :attrs
      :href))

;; TODO: neet to http/get the video._link
(defn- video-_link->url [video-_link])

(defn- video-list-item->title [video-list-item]
  (-> video-list-item
      :content
      first
      :content
      first
      (string/trim)))

(defn- video-_link->id [video-_link] (last (string/split video-_link #"/")))
(defn- video-_link->lecture-url [video-_link]
  (->> (string/split video-_link #"/")
       (butlast)
       (string/join "/")))

(defn- video-_link->transcript-url [video-_link]
  (let [id (video-_link->id video-_link)
        lecture-url (video-_link->lecture-url video-_link)]
    (str lecture-url "/subtitles?q=" id "_en&format=txt")))

(defn- transcript-exists? [video-list-item]
    (html/any-node video-list-item [(html/attr-contains :href "subtitle")]))

(defn- needed? [video-list-item]
    (transcript-exists? video-list-item))

;; VideoListItem -> video(without course)
(defn- video-list-item->video [video-list-item]
  (let [_link (video-list-item->_link video-list-item)]
     {:id (video-_link->id _link)
      :title (video-list-item->title video-list-item)
      :transcript (:body (http/get (video-_link->transcript-url _link)))
      :video-url (video-_link->url _link)
      :_link _link}))

;;; TEST
;;; TEST
;(def course-html (slurp "resources/fixtures/course.html"))
;(def html-soup (html/html-snippet course-html))
;; (extract-course html-soup)
;(-> (extract-video-list html-soup)
 ;   (first)
  ;  (video-list-item->video))

(defn extract-video-url [h]
  "foo")
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; VIDEOS

;; HTMLSoup -> [VideoListItem]
(defn- extract-video-list [h]
  (let [video-list (->> (html/select h [:ul.course-item-list-section-list])
                                 (map :content)
                                 (flatten))]
    video-list))

;; HTML -> [Videos]
(defn extract-videos [html]
  (let [html-soup (html/html-snippet html)
        video-list (extract-video-list html-soup)]
    (println "Shit my soiled pants...")
    (->> (filter needed? video-list)
         (map video-list-item->video))))

