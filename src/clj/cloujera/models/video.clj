(ns cloujera.models.video
  (:require [schema.core :as s]))

(def fake-video
  {:id "29"
   :title "Lecture 3 - Chapter 2: The industrial revolution starts in England"
   :transcript "The world we've seen is divided...(no timestamps!!)"
   :video-url "http://d396qusza40orc.cloudfront.net/susdev/recoded_videos%2Fsusdev_3_01.da6ff370809111e3af279b48f4519db4.webm"
   :_link "https://class.coursera.org/susdev-002/lecture/29"
   :course {:id "susdev-002"
            :title "The Age of Sustainable Development"
            :_link "https://class.coursera.org/susdev-002"}})


(def Course
  "The schema for a Coursera Course (embedded in a video)"
  {:id s/Str
   :title s/Str
   :_link s/Str})

(defn valid-course? [c]
  (s/validate Course c))

(def Video
  "The schema vor a Coursera Video"
  {:id s/Str
   :title s/Str
   :transcript s/Str
   :video-url s/Str
   :_link s/Str
   :course Course})

(defn valid-video? [v]
  (s/validate Video v))
