(ns cloujera.search.core
  (:require [clojure.string :as string]
            [clojurewerkz.elastisch.query :as q]
            [clojurewerkz.elastisch.rest :as esr]
            [clojurewerkz.elastisch.rest.document :as esd]
            [cloujera.models.video :as video]))

(def conn (esr/connect "http://127.0.0.1:9200"))

;;;;;;;;;;;;;;;;;;;;
;; PRIVATE INTERFACE
;;;;;;;;;;;;;;;;;;;;

(defn- generate-id [video]
  (str (get-in video [:course :id]) "-" (:id video)))

(defn- elastic-highlight->highlighted-transcript [eh]
  (string/join " ... " eh))

(defn- extract-video-result [elastic-video]
  (let [video (:_source elastic-video)
        elastic-highlight (get-in elastic-video [:highlight :transcript])]
    (assoc video
           :highlighted-transcript
           (elastic-highlight->highlighted-transcript elastic-highlight))))

(defn- extract-results [videos]
  (let [found-videos (get-in videos [:hits :hits])]
    (map extract-video-result found-videos)))

;;;;;;;;;;;;;;;;;;;;
;; PUBLIC INTERFACE
;;;;;;;;;;;;;;;;;;;;

(defn save [video]
  (let [identifier (generate-id video)]
    (esd/put conn "videos" "video" identifier video) video))


(defn term-matching [term]
  (extract-results (esd/search conn "videos" "video"
                               :query (q/match :transcript term)
                               :highlight {:fields {:transcript {}}})))

(defn all []
  (extract-results (esd/search conn "videos" "video" :query (q/match-all {}))))


