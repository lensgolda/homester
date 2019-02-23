(ns homester.route
  (:require [com.stuartsierra.component :as component]
            [homester.service :as service]
            [io.pedestal.http :as http]
            [io.pedestal.http.body-params :as body-params]
            [homester.server.interceptors :as interceptors]))

(defn default-response
  [{:keys [query-params]}]
  (cond-> {:status 200 :body {}}
    true                 (assoc-in [:body :result] "ok")
    (some? query-params) (assoc-in [:body :params] query-params)))

(def ^:private common-json-interceptors
  [(body-params/body-params) http/json-body http/not-found])

(def ^:private common-html-interceptors
  [(body-params/body-params) http/html-body http/not-found])

;; Map-based default routes
(defn default-routes
  [{<db> :db :as <routing>}]
  {"/" {:interceptors common-json-interceptors
        :get default-response}})

;; Map-based routes
(defn- routes
  [{<db> :db}]
  {"/" {:interceptors common-json-interceptors
        :get `default-response
        "/service" {:interceptors [(interceptors/using-component <db>)]
                    :get `service/test-clojure-version}}})


(defrecord Route [routes-fn]
  component/Lifecycle

  (start [this]
    (assoc this :routes (routes-fn this)))
  (stop [this]
    (assoc this :routes nil)))

(defn create
  []
  (->Route routes))