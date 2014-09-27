(ns cloujera.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cloujera.rest-client :as rest]))

(enable-console-print!)

(def app-state
  (atom
    {:videos [{}]}))

(defn process-response [response]
  (println (str "Search results are: " response)))


;; handlers

(defn handle-input [e owner {:keys [text]}]
  (om/set-state! owner :text (.. e -target -value)))

(defn handle-search [app owner]
  (let [search-query (.-value (om/get-node owner "search-query"))]
    (println (str "Search query is: " search-query))
    (rest/search search-query process-response)))

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
