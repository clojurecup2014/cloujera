(ns cloujera.cache.core
  (:require [taoensso.carmine :as redis]
            [environ.core :refer [env]]
            [clojure.string :as string]))

(defn conn []
  (let [redis-tcp-uri (env :redis-port)
        redis-uri (.replaceAll redis-tcp-uri
                               "^tcp://" "")
        [host port] (string/split redis-uri #":")]
    {:host host
     :port (Integer. port)}))

(defn persist [f]
  (let [conn (conn)]
    (fn [k]
      (let [cached-val (redis/wcar conn (redis/get k))]
        (if (nil? cached-val)
           (let [computed-val (f k)]
             (redis/wcar conn (redis/set k computed-val))
             computed-val)
           cached-val)))))
