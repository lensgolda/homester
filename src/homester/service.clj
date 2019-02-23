(ns homester.service
  (:require [ring.util.response :refer [response]]
            [honeysql.core :as sql]
            [homester.db :as db]))

(defn test-clojure-version
  [req]
  (response (format "Clojure %s" (clojure-version))))

(defn test-db
  [{<db> :component}]
  (let [data (-> (sql/build {:select :*
                             :from :cities})
                 (sql/format)
                 (->> (db/fetch-one <db>)))]
    (response data)))