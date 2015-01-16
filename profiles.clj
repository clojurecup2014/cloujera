;; profiles.clj contains the values for the `:dev` profile (`lein repl`).
;; This file is looked up before the actual environment
;; used by environ, merged with project.clj map
{:dev {:env {:elasticsearch-ip "127.0.0.1"
             :elasticsearch-port "9200"
             :redis-ip "127.0.0.1"
             :redis-port 6379}}}
