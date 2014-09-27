(ns cloujera.cache.core
  (:require [taoensso.carmine :as redis]))

(defn persist [f]
  (fn [k]
    (let [cached-val (redis/wcar {} (redis/get k))]
         (if (nil? cached-val)
           (let [computed-val (f k)]
             (redis/wcar {} (redis/set k computed-val))
             computed-val)
           cached-val))))


