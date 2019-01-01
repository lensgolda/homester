(ns homester.db)

(defprotocol Databaseable
  (execute [this sql])
  (execute! [this sql])
  (execute-in-transaction [this f])
  (is-duplicate-ex? [this ex]))

(defn fetch-one
  [<db> sql]
  (first (execute <db> sql)))