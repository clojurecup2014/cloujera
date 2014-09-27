(ns cloujera.rest-client
  (:require [cljs.reader :as reader]
            [goog.events :as events])
  (:import [goog.net XhrIo]
           goog.net.EventType
           [goog.events EventType]))


(def ^:private meths
  {:get "GET"})

(def ^:private uri
  "http://127.0.0.1:8080")

(defn json-xhr [{:keys [method url data on-complete]}]
  (let [xhr (XhrIo.)]
    (events/listen xhr goog.net.EventType.COMPLETE
      (fn [e]
        (on-complete (reader/read-string (.getResponseText xhr)))))
    (. xhr
      (send url (meths method) (when data (pr-str data))
        #js {"Content-Type" "application/edn"}))))

(defn search[query callback]
  (json-xhr
   {:method :get
    :url (str uri "/search?query=" query)
    :on-complete (callback)}))
