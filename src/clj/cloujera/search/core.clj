(ns cloujera.search.core
  (:require [clojurewerkz.elastisch.rest          :as esr]
            [clojurewerkz.elastisch.rest.document :as esd]
            [clojurewerkz.elastisch.query         :as q]
            [clojurewerkz.elastisch.rest.response :as esrsp]
            [clojurewerkz.elastisch.rest.index    :as esi]

            [clojure.pprint                       :as pp]

            [cloujera.models.video                :as video]))

(def conn (esr/connect "http://127.0.0.1:9200"))

(defn save [video] (do
  (video/valid-video? video)
  (esd/create conn "videos" "video" video)))

(defn fuzzy [term]
  (esd/search conn "videos" "video" :query (q/fuzzy :transcript {:value term :min_similarity 3})))

(defn all []
  (esd/search conn "videos" "video" :query (q/match-all {})))

;;;;;;;;;;;;;;;;;;;;;;;;;;
;; TEST
;;(esi/create conn "videos")
;;;;;;;;;;;;;;;;;;;;;;;;;;

;;(save-video video/fake-video)
;;(fuzzy "The world")
;;(all)
