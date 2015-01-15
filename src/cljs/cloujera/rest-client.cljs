(ns cloujera.rest-client
  (:require [cognitect.transit :as transit]
            [goog.events :as events])
  (:import [goog.net XhrIo]))

(def ^:private json-reader (transit/reader :json))

(defn search [search-query call-back]
  (let [xhr (XhrIo.)
        backend-uri (.origin js/location)]
    (events/listen xhr goog.net.EventType.COMPLETE
      (fn [e]
        (call-back (transit/read json-reader (.getResponseText xhr)))))
    (. xhr
      (send (str backend-uri "/search?query=" search-query) "GET"))))
