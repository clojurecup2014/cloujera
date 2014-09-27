(ns cloujera.burglar.parser
  (:require [cloujera.models.video :as video]
            [cloujera.cache.core :as cache]
            [clojure.string :as string]
            [schema.core :as schema]
            [clj-http.client :as http]
            [net.cgrand.enlive-html :as html]
            [cheshire.core :as json]))


(def course-api-endpoint "https://api.coursera.org/api/catalog.v1/courses")
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn get-body [url]
  (:body (http/get url)))
(def cached-http-get (cache/persist get-body))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; UTLIS
(defn- video-_link->lecture-url [video-_link]
  (->> (string/split video-_link #"/")
       (butlast)
       (string/join "/")))

(defn- video-_link->id [video-_link] (last (string/split video-_link #"/")))

(defn video-_link->embedded-video-url [video-_link]
  (let [video-id (video-_link->id video-_link)
        lecture-url (video-_link->lecture-url video-_link)]
    (str lecture-url "/view?lecture_id=" video-id)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; COURSE
(defn- get-course-_link [h]
  (let [attr :data-lecture-coursebase
        tag (html/select h [:div (html/attr-has attr)])]
        (-> tag first :attrs attr)))

(defn- course-_link->id [_link]
  (let [url-tokens (string/split _link #"/")
        session-token (last url-tokens)]
    (first (string/split session-token #"-"))))

(defn- course-id->title [id]
  (let [courses (:elements (json/parse-string (cached-http-get course-api-endpoint) true))
        course (first (filter #(= (:shortName %) id) courses))]
    (:name course)))

(defn extract-course [h]
  (let [_link (get-course-_link h)
        id (course-_link->id _link)
        c {:id id
           :title (course-id->title id)
           :_link (get-course-_link h)}]
   (schema/validate video/Course c)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; VIDEO

(defn- video-list-item->_link [video-list-item]
  (let [tag (html/select video-list-item [:a.lecture-link])]
    (-> tag
        first
        :attrs
        :href)))

(defn- video-list-item->title [video-list-item]
  (-> (html/select video-list-item [:a.lecture-link])
      first
      :content
      first
      string/trim))

(defn- video-_link->transcript-url [video-_link]
  (let [id (video-_link->id video-_link)
        lecture-url (video-_link->lecture-url video-_link)]
    (str lecture-url "/subtitles?q=" id "_en&format=txt")))

(defn- transcript-exists? [video-list-item]
    (not (empty? (html/select video-list-item [(html/attr-contains :href "subtitle")]))))

(defn- needed? [video-list-item]
    (transcript-exists? video-list-item))

;; VideoListItem -> video(without course)
(defn- video-list-item->video [video-list-item]
  (let [_link (video-list-item->_link video-list-item)]
     {:id (video-_link->id _link)
      :title (video-list-item->title video-list-item)
      :transcript (cached-http-get (video-_link->transcript-url _link))
      :_link _link}))


;;;; URL
(defn extract-video-url [html]
  (let [html-soup (html/html-snippet html)
        tag (html/select html-soup [:source (html/attr-contains :type "video/webm")])]
     (-> tag
         first
         :attrs
         :src)))

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
        video-list (extract-video-list html-soup)
        course (extract-course html-soup)]
    (->> video-list
         (filter needed?)
         (map video-list-item->video)
         (map #(assoc % :course course)))))

