(ns cloujera.core
  (:require [cloujera.burglar.core :as burglar]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def username "vise890+cloujera@gmail.com")
(def password "letswinthisthing")
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- coursera-urls []
  ["https://class.coursera.org/modernpoetry-003/lecture"
   "https://class.coursera.org/comparch-003/lecture"
   "https://class.coursera.org/algs4partI-006/lecture"
   "https://class.coursera.org/calcsing-006/lecture"
   "https://class.coursera.org/automata-003/lecture"
   "https://class.coursera.org/crypto-012/lecture"
   "https://class.coursera.org/ml-007/lecture"
   ])

(def get-lectures-page (burglar/authenticated-get username password))

(->> (coursera-urls)
     (map get-lectures-page)
     (map :body))



