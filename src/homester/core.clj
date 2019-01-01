(ns homester.core
  (:require [clojure.tools.reader.edn :as edn]
            [environ.core :refer [env]]
            [homester.system :as system]
            [com.stuartsierra.component :as component]
            [clojure.tools.logging :as log]))

(defn -main
  [& args]
  (let [system (-> (slurp (env :app-config))
                   (edn/read-string)
                   (system/->system))]
    (log/info "Starting app...")
    (component/start system)
    (.addShutdownHook (Runtime/getRuntime)
                      (Thread. #(component/stop system)))))