(ns homester.system
  (:require [com.stuartsierra.component :as component]
            [homester.server :as server]
            [homester.db.postgres :as postgres]
            [homester.route :as route]))

(defn ->system
  [config]
  (component/system-using
    (component/system-map
      :config config
      :db      (postgres/create)
      :server  (server/create)
      :routing (route/create))
    {:server  [:config :routing]
     :db      [:config]
     :routing [:db]}))