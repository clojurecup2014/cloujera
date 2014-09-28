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
  (let [attr :data-lecture-coursebase
        tag (html/select soup [:div (html/attr-has attr)])]
        (utils/get-attr tag attr)))

(defn- _link->id [_link]
  (let [url-tokens (string/split _link #"/")
        session-token (last url-tokens)]
    (first (string/split session-token #"-"))))

;; TITLE
;; FIXME: this feels too hacky (even for this...)
(def course-api-endpoint "https://api.coursera.org/api/catalog.v1/courses")
(def courses (:elements (json/parse-string
                           (utils/cached-http-get course-api-endpoint)
                                                  true)))

(defn- id->title [id]
  (let [right-course? #(= (:shortName %) id)
        course (first (filter right-course? courses))]
    (:name course)))

;; HTMLSoup(LecturePage) -> Course
(defn soup->course [soup]
  (let [_link (soup->_link soup)
        id (_link->id _link)
        c {:id id
           :title (id->title id)
           :_link _link}]
   (schema/validate video-model/Course c)))
