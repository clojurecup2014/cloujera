(ns cloujera.rest-client
  (:require [cognitect.transit :as transit]
            [goog.events :as events])
  (:import [goog.net XhrIo]))


(def ^:private uri "http://127.0.0.1:8080")
(def ^:private json-reader (transit/reader :json))

(defn search [search-query call-back]
  (let [xhr (XhrIo.)]
    (events/listen xhr goog.net.EventType.COMPLETE
      (fn [e]
        (call-back (transit/read json-reader (.getResponseText xhr)))))
    (. xhr
      (send (str uri "/search?query=" search-query) "GET"))))
