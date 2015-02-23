(ns cloujera.search.core
  (:require [clojure.string :as string]
            [clojurewerkz.elastisch.query :as q]
            [clojurewerkz.elastisch.rest :as esr]
            [clojurewerkz.elastisch.rest.document :as esd]
            [environ.core :refer [env]]
            [cloujera.models.video :as video]))

(defn conn []
  (let [elasticsearch-tcp-uri (env :elasticsearch-port)
        elasticsearch-uri (.replaceAll elasticsearch-tcp-uri
                                       "^tcp:" "http:")]
    (esr/connect elasticsearch-uri)))

;;;;;;;;;;;;;;;;;;;;
;; PRIVATE INTERFACE
;;;;;;;;;;;;;;;;;;;;

(defn- generate-id [video]
  (str (get-in video [:course :id]) "-" (:id video)))

(defn- elastic-highlight->highlighted-transcript [eh]
  (string/join " ... " eh))

(defn- extract-video-result [elastic-video]
  (let [video (:_source elastic-video)
        elastic-highlight (get-in elastic-video [:highlight :transcript])
        hl-transcript (elastic-highlight->highlighted-transcript elastic-highlight)
        video+hl-transcript (assoc video
                                   :highlighted-transcript
                                   hl-transcript)]
    (dissoc video :transcript)))

(defn- extract-results [videos]
  (let [found-videos (get-in videos [:hits :hits])]
    (map extract-video-result found-videos)))

;;;;;;;;;;;;;;;;;;;;
;; PUBLIC INTERFACE
;;;;;;;;;;;;;;;;;;;;

(defn save [video]
  (let [identifier (generate-id video)]
    (esd/put (conn) "videos" "video" identifier video) video))


(defn term-matching [term]
  (extract-results (esd/search (conn)
                               "videos"
                               "video"
                               :query (q/match :transcript
                                               term
                                               {:type "phrase"})
                               :highlight {:fields {:transcript {}}})))

(defn all []
  (extract-results (esd/search (conn) "videos" "video" :query (q/match-all {}))))


