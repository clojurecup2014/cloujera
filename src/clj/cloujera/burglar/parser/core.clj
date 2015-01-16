(ns cloujera.burglar.parser.core
  (:require [cloujera.burglar.parser.course :as course]
            [cloujera.burglar.parser.video :as video]
            [net.cgrand.enlive-html :as html]
            [cloujera.burglar.parser.utils :as utils]))

;; HTMLSoup(LecturePage) -> [HTMLSoup(Video)]
(defn- lectures-soup->video-soups
  "splits the soup of the lecture page into HtmlSoup(Video)"
  [soup]
  {:pre [(utils/not-nil? soup)]}
  (->> (html/select soup [:ul.course-item-list-section-list])
       (map :content)
       (flatten))) ;; there are multiple sections...

;; HTML(LecturePage) -> [Videos]
(defn extract-videos
  "Returns a list of Videos from the HTML(LecturePage)
   NOTE: Videos don't have a url (yet)"
  [html]
  {:pre [(utils/not-nil? html)]}
  (let [soup (html/html-snippet html)
        video-soups (lectures-soup->video-soups soup)
        course (course/soup->course soup)
        add-course #(assoc % :course course)]
    (->> video-soups
         (filter video/can-index?)
         (map video/soup->video)
         (map add-course))))
