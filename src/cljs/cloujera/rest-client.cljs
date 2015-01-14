(ns cloujera.rest-client
  (:require [cognitect.transit :as transit]
            [goog.events :as events]
            [cloujera-config :as config])
  (:import [goog.net XhrIo]))

(def ^:private json-reader (transit/reader :json))

(defn search [search-query call-back]
  (let [xhr (XhrIo.)]
    (events/listen xhr goog.net.EventType.COMPLETE
      (fn [e]
        (call-back (transit/read json-reader (.getResponseText xhr)))))
    (. xhr
      (send (str config/cloujera-backend-uri "/search?query=" search-query) "GET"))))
