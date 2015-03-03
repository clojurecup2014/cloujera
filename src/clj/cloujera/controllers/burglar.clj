(ns cloujera.controllers.burglar
  (:require [cloujera.burglar.core :as burglar]))

(def lecture-urls
  ["https://class.coursera.org/modernpoetry-003/lecture"
   "https://class.coursera.org/comparch-003/lecture"
   "https://class.coursera.org/algs4partI-006/lecture"
   "https://class.coursera.org/calcsing-006/lecture"
   "https://class.coursera.org/automata-003/lecture"
   "https://class.coursera.org/crypto-012/lecture"
   "https://class.coursera.org/ml-007/lecture"
   "https://class.coursera.org/introastro-002/lecture"
   "https://class.coursera.org/religionandtolerance-001/lecture"
   "https://class.coursera.org/microecon-017/lecture"
   "https://class.coursera.org/sysbio-003/lecture"
   ])

(defn go [] (-> (pmap burglar/raid lecture-urls) (flatten)))

(defn raid-url [lecture-url] (burglar/raid lecture-url))
