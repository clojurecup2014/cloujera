(ns cloujera.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cloujera.rest-client :as rest-client]))

(enable-console-print!)

(def app-state
  (atom
    {:videos []}))


;; handlers
(defn handle-input [e owner {:keys [text]}]
  (om/set-state! owner :text (.. e -target -value)))

(defn handle-search [app owner]
  (let [search-query (.-value (om/get-node owner "search-query"))]
    (println (str "Search query is: " search-query))
    (rest-client/search search-query
                        (fn [response]
                           (println "Server response:" response)
                           (swap! app-state assoc :videos (get response "results"))))))

;; search bar component
(defn search-bar-view [app owner]
  (reify

    om/IInitState
    (init-state [_]
      {:text ""})

    om/IRenderState
    (render-state [this state]
      (dom/input #js {:type "text"
                      :ref "search-query"
                      :value (:text state)
                      :onChange #(handle-input % owner state)}
      (dom/button #js {:onClick #(handle-search app owner)}
                      "Search")))))

(om/root
  search-bar-view
  app-state
  {:target (. js/document (getElementById "search-bar"))})


;; result pane component
(defn display-video [video]
  (str "title: " (get video "title") ", id: " (get video "id")))

(defn video-view [video owner]
  (reify
    om/IRender
    (render [this]
            (dom/li nil
              (dom/span nil (if-not (nil? video)
                              (display-video video)))))))

(defn result-pane-view [app owner]
  (reify

    om/IRender
    (render [this]
      (dom/h2 nil "Results")
      (apply dom/ul nil
         (om/build-all video-view (:videos app))))))

(om/root
  result-pane-view
  app-state
  {:target (. js/document (getElementById "result-pane"))})
