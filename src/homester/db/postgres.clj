(ns homester.db.postgres
  (:require [com.stuartsierra.component :as component]
            [plumbing.core :refer [safe-get-in]]
            [hikari-cp.core :as hikari]
            [homester.db :as db]
            [clojure.tools.logging :as log]
            [clojure.java.jdbc :as jdbc]))

(defn- execute*
  [{:keys [conn] :as <db>} sql]
  (log/info sql)
  (if conn
    (jdbc/query conn sql)
    (jdbc/with-db-connection [conn <db>]
                             (jdbc/query conn sql))))

(defn- execute!*
  [{:keys [conn] :as <db>} sql]
  (log/info sql)
  (if conn
    (jdbc/execute! conn sql)
    (jdbc/with-db-connection [conn <db>]
                             (jdbc/execute! conn sql))))

(defn- execute-in-transaction* [this f])
(defn- is-duplicate-ex?* [_ ex])

(defrecord Postgresql []
  component/Lifecycle

  (start [this]
    (let [db-spec (safe-get-in this [:config :database])]
      (assoc this :datasource (hikari/make-datasource db-spec))))

  (stop [this]
    (let [datasource (:datasource this)]
      (hikari/close-datasource datasource))
    (assoc this :datasource nil))

  db/Databaseable
  (execute [this sql]
    (execute* this sql))
  (execute! [this sql]
    (execute!* this sql))
  (execute-in-transaction [this f]
    (execute-in-transaction* this f))
  (is-duplicate-ex? [_ ex]
    (is-duplicate-ex?* _ ex)))

(defn create
  []
  (map->Postgresql {}))