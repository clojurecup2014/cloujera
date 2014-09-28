(ns cloujera.search.core
  (:require [clojurewerkz.elastisch.rest          :as esr]
            [clojurewerkz.elastisch.rest.document :as esd]
            [clojurewerkz.elastisch.query         :as q]
            [clojurewerkz.elastisch.rest.response :as esrsp]
            [clojurewerkz.elastisch.rest.index    :as esi]

            [taoensso.timbre :as timbre]

            [cloujera.models.video                :as video]))

(timbre/refer-timbre) ;; logging aliases

(def conn (esr/connect "http://127.0.0.1:9200"))

;;;;;;;;;;;;;;;;;;;;
;; PRIVATE INTERFACE
;;;;;;;;;;;;;;;;;;;;

(defn- generate-id [video]
  (str (get-in video [:course :id]) "-" (:id video)))

(defn- extract-results [videos]
  (map :_source (get-in videos [:hits :hits])))

;;;;;;;;;;;;;;;;;;;;
;; PUBLIC INTERFACE
;;;;;;;;;;;;;;;;;;;;

(defn save [video]
  (let [identifier (generate-id video)]
    (info (str "START: saving" identifier))
    (video/valid-video? video)
    (esd/put conn "videos" "video" identifier video) video))

(defn term-matching [term]
  (extract-results (esd/search conn "videos" "video" :query (q/match :transcript term))))

(defn all []
  (extract-results (esd/search conn "videos" "video" :query (q/match-all {}))))


