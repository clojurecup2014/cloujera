(ns cloujera.cache.core
  (:require [taoensso.carmine :as redis]
            [environ.core :refer [env]]))

(def conn
  (let [host (env :redis-host)
        port (env :redis-port)]
    {:spec {:host host
            :port port}}))

(defmacro wcar* [& body] `(redis/wcar conn ~@body))

(defn persist [f]
  (fn [k]
    (wcar*
      (let [cached-val (redis/get k)]
         (if (nil? cached-val)
             (let [computed-val (f k)]
                  (redis/set k computed-val)
             computed-val)
             cached-val)))))
