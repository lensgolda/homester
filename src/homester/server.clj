(ns homester.server
  (:gen-class) ; for -main method in uberjar
  (:require [io.pedestal.http :as http]
            [com.stuartsierra.component :as component]
            [plumbing.core :refer [safe-get safe-get-in]]
            [clojure.tools.logging :as log]
            [homester.route :refer [default-routes]]))

(defn- test?
  [service-map]
  (= :test (:env service-map)))

(defn- dev?
  [service-map]
  (= :dev (:env service-map)))

(def default-service
  {:env :prod
   ::http/join? false
   ::http/routes default-routes
   ::http/port 8080
   ::http/type :immutant})

(def dev-service
  {:env :dev
   ;; do not block thread that starts web server
   ::http/join? false
   ;; Routes can be a function that resolve routes,
   ;;  we can use this to set the routes to be reloadable
   ::http/routes default-routes
   ;; all origins are allowed in dev mode
   ::http/allowed-origins {:creds true :allowed-origins (constantly true)}
   ;; Content Security Policy (CSP) is mostly turned off in dev mode
   ::http/secure-headers {:content-security-policy-settings {:object-src "'none'"}}
   ;; Root for resource interceptor that is available by default.
   ::http/resource-path "/public"})
      ;; Wire up interceptor chains))

(defrecord Server [service]
  component/Lifecycle

  (start [this]
    (log/info "Starting server...")
    (let [config (safe-get this :config)
          routes (safe-get-in this [:routing :routes])]
      (if service
        this
        (cond-> default-service
                (dev? config)        (-> (merge dev-service)
                                         (assoc ::http/routes (or routes default-routes))
                                         http/default-interceptors
                                         http/dev-interceptors)
                true                 http/create-server
                (not (test? config)) http/start
                true                 ((partial assoc this :service))))))
  (stop [this]
    (log/info "Stopping server...")
    (let [config (safe-get this :config)]
      (when (and service (not (test? config)))
        (http/stop service)))
    (assoc this :service nil)))

(defn create []
  (map->Server {}))