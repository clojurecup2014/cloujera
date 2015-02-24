(ns cloujera.cache.core
  (:require [clojure.string :as string]
            [environ.core :refer [env]]
            [taoensso.carmine :as redis]))

(defn conn []
  (let [redis-tcp-uri (env :redis-port)
        redis-uri (.replaceAll redis-tcp-uri
                               "^tcp://" "")
        [host port] (string/split redis-uri #":")]
    {:spec {:host host
            :port (Integer. port)}}))

(defn persist [f]
  (fn [k]
    (let [cached-val (redis/wcar (conn) (redis/get k))]
      (if (nil? cached-val)
        (let [computed-val (f k)]
          (redis/wcar (conn) (redis/set k computed-val))
          computed-val)
        cached-val))))

