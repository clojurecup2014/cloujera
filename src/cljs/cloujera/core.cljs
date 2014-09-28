(ns cloujera.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cloujera.rest-client :as rest-client]))

(enable-console-print!)

(def app-state
  (atom
    {:videos []}))

(defn hide-main-msg []
  (let [main-msg (.getElementById js/document (name "main-message"))]
      (set! (-> main-msg .-style .-display ) "none")))

(defn show-not-found-msg []
  (let [main-msg (.getElementById js/document (name "no-results-found"))]
      (set! (-> main-msg .-style .-display ) "block")))

;; this is shity, we know, but is 20:44, less than 4 hours to go :/
(defn hide-not-found-msg []
  (let [main-msg (.getElementById js/document (name "no-results-found"))]
      (set! (-> main-msg .-style .-display ) "none")))


(defn no-results-msg []
  ())

;; handlers
(defn handle-input [e owner {:keys [text]}]
  (om/set-state! owner :text (.. e -target -value)))

(defn handle-search [app owner]
  (let [search-query (.-value (om/get-node owner "search-query"))]
    (println (str "Search query is: " search-query))
    (hide-main-msg)
    (hide-not-found-msg) ;; this is terrible, sorry
    (rest-client/search search-query
                        (fn [response]
                           (let [videos (get response "results")]
                             (if (empty? videos)
                                 (show-not-found-msg)
                                 (swap! app-state assoc :videos (get response "results"))))))))

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
                      :onKeyPress #(when (== (.-keyCode %) 13)
                                (handle-search app owner))
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
                   (get video "title")))
      (dom/p #js {:dangerouslySetInnerHTML #js {:__html (get video "highlighted-transcript")}} nil)))

;; result pane component
(defn display-video [video]
  (println(str "title: " (get video "title") ", id: " (get video "id") ", url: " (get video "video-url")))
  (dom/div #js {:className "row video-item"}
      (dom/div #js {:className "col-md-8"}
         (dom/video #js {:src (get video "video-url")
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
