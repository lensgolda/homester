(ns user
  (:require [reloaded.repl :refer [system init start stop go reset reset-all]]
            [homester.system :as system]
            [environ.core :refer [env]]
            [clojure.edn :as edn]))

(reloaded.repl/set-init! #(system/->system (-> (env :app-config)
                                               (slurp)
                                               (edn/read-string))))