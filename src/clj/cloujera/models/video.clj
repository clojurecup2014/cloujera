(ns cloujera.models.video
  (:require [schema.core :as s]))

(def Course
  "The schema for a Coursera Course (embedded in a video)"
  {:id s/Str
   :title s/Str
   :_link s/Str})

(defn valid-course? [c]
  (s/validate Course c))

(def Video
  "The schema for a Coursera Video"
  {:id s/Str
   :title s/Str
   :transcript s/Str
   (s/optional-key :highlighted-transcript) s/Str
   :video-url s/Str
   :_link s/Str
   :course Course})

(defn valid-video? [v]
  (s/validate Video v))
