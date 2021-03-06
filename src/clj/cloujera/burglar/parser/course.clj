(ns cloujera.burglar.parser.course
  (:require [cheshire.core :as json]
            [clojure.string :as string]
            [cloujera.burglar.parser.utils :as utils]
            [cloujera.models.video :as video-model]
            [net.cgrand.enlive-html :as html]
            [schema.core :as schema]))

(defn- soup->_link
  "gets the course._link from an HTML soup of the lecture page"
  [soup]
  {:pre [(utils/not-nil? soup)]}
  (let [attr :data-lecture-coursebase
        tag (html/select soup [:div (html/attr-has attr)])
        _link (utils/get-attr tag attr)]
    (if (nil? _link)
      (throw "unable to extract course _link from HTML soup of lecture page")
      _link)))

(defn- _link->id [_link]
  {:pre [(utils/not-nil? _link)]}
  (let [url-tokens (string/split _link #"/")
        session-token (last url-tokens)]
    (first (string/split session-token #"-"))))

;; TITLE
;; FIXME: this feels too hacky (even for this...)
(defn courses []
  (let [course-api-endpoint "https://api.coursera.org/api/catalog.v1/courses"
        courses-json (utils/cached-http-get course-api-endpoint)
        courses (json/parse-string courses-json true)]
    (:elements courses)))

(defn- id->title [id]
  {:pre [(utils/not-nil? id)]}
  (let [right-course? #(= (:shortName %) id)
        course (first (filter right-course? (courses)))]
    (:name course)))

;; HTMLSoup(LecturePage) -> Course
(defn soup->course [soup]
  {:pre [(utils/not-nil? soup)]}
  (let [_link (soup->_link soup)
        id (_link->id _link)
        c {:id id
           :title (id->title id)
           :_link _link}]
   (schema/validate video-model/Course c)))
