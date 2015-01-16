(ns cloujera.cache.core
  (:require [taoensso.carmine :as redis]
            [environ.core :refer [env]]
            [clojure.string :as string]))

(defn conn []
  (let [redis-tcp-uri (env :redis-port)
        redis-uri (.replaceAll redis-tcp-uri
                               "^tcp://" "")
        [host port] (string/split redis-uri #":")]
    {:spec {:host host
            :port (Integer. port)}}))
(defmacro wcar* [& body] `(redis/wcar conn ~@body))

(defn persist [f]
  (fn [k]
    (wcar* (let [cached-val (redis/get k)]
                   (if (nil? cached-val)
                       (let [computed-val (f k)]
                          (redis/set k computed-val)
                          computed-val)
                       cached-val)))))
