;;; profiles.clj contains the values for the `:dev` profile (`lein repl`).
;;; This file is looked up before the actual environment
;;; used by environ, merged with project.clj map

;; we define :elasticsearch-port and :redis-port with weird names and format
;; because that's the the ones Docker exports in linked containers
{:dev {:main cloujera.core/-dev-main
       :env {:elasticsearch-port "tcp://127.0.0.1:9200"
             :redis-port "tcp://127.0.0.1:6379"}}}
