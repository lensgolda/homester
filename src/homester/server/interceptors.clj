(ns homester.server.interceptors
  (:require [io.pedestal.interceptor :refer [interceptor]]))

(defn using-component
  [<component>]
  (interceptor
    {:name ::using-component
     :enter (fn [context]
              (assoc-in context [:request :component] <component>))}))
