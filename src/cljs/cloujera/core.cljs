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
                      :className "form-control"
                      :ref "search-query"
                      :value (:text state)
                      :onChange #(handle-input % owner state)}
      (dom/span #js {:className "input-group-btn"}
           (dom/button #js {:className "btn btn-default"
                            :type "button"
                            :onClick #(handle-search app owner)}
                            "Search"))))))

(om/root
  search-bar-view
  app-state
  {:target (. js/document (getElementById "search-bar"))})


(defn display-info-pane [video]
  (dom/div #js {:className "col-md-4 info-pane"}
      (dom/h3 nil
         (dom/a #js {:href (get-in video ["course" "_link"])
                     :target "_blank"}
                   (get-in video ["course" "title"])))
      (dom/hr nil)
      (dom/p nil
          (dom/a #js {:href (get video "_link")
                      :target "_blank"}
                   (get video "title")))))

;; result pane component
(defn display-video [video]
  (println(str "title: " (get video "title") ", id: " (get video "id")))
  (dom/div #js {:className "row video-item"}
      (dom/div #js {:className "col-md-8"}
         (dom/video #js {:src "http://d396qusza40orc.cloudfront.net/susdev/recoded_videos%2Fsusdev_3_01.da6ff370809111e3af279b48f4519db4.webm"
                         :controls true}))
      (display-info-pane video)))

(defn video-view [video owner]
  (reify
    om/IRender
    (render [this]
            (dom/li nil
              (display-video video))
            )))

(defn result-pane-view [app owner]
  (reify

    om/IRender
    (render [this]
      (apply dom/ul nil
         (om/build-all video-view (:videos app))))))

(om/root
  result-pane-view
  app-state
  {:target (. js/document (getElementById "result-pane"))})
