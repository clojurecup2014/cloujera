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
  (fn [& args]
    (let [conn (conn)
          redis-key (string/join "::" args)
          cached-val (redis/wcar conn (redis/get redis-key))]
      (if (nil? cached-val)
         (let [computed-val (apply f args)]
           (redis/wcar conn (redis/set redis-key computed-val))
           computed-val)
         cached-val))))
