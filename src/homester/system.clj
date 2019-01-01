(ns homester.system
  (:require [com.stuartsierra.component :as component]
            [homester.server :as server]))

(defn ->system
  [config]
  (component/system-using
    (component/system-map
      :config config
      :server (server/create))
    {:server [:config]}))