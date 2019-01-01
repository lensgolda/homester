(defproject homester "0.0.1-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/tools.logging "0.4.1"]
                 [io.pedestal/pedestal.service "0.5.5"]
                 [org.clojure/java.jdbc "0.7.7"]
                 [org.postgresql/postgresql "42.2.2"]
                 [com.stuartsierra/component "0.3.2"]
                 [environ "1.1.0"]
                 [prismatic/plumbing "0.5.5"]
                 [honeysql "0.9.2"]
                 [nilenso/honeysql-postgres "0.2.4"]

                 ;; Remove this line and uncomment one of the next lines to
                 ;; use Immutant or Tomcat instead of Jetty:
                 ;; [io.pedestal/pedestal.jetty "0.5.5"]
                 [io.pedestal/pedestal.immutant "0.5.5"]

                 [ch.qos.logback/logback-classic "1.2.3" :exclusions [org.slf4j/slf4j-api]]
                 [org.slf4j/jul-to-slf4j "1.7.25"]
                 [org.slf4j/jcl-over-slf4j "1.7.25"]
                 [org.slf4j/log4j-over-slf4j "1.7.25"]]
  :plugins [[lein-environ "1.1.0"]]
  :min-lein-version "2.0.0"
  :resource-paths ["config", "resources"]
  ;; If you use HTTP/2 or ALPN, use the java-agent to pull in the correct alpn-boot dependency
  ;:java-agents [[org.mortbay.jetty.alpn/jetty-alpn-agent "2.0.5"]]
  :profiles {:dev {;;:aliases {"run-dev" ["trampoline" "run" "-m" "homester.core/-main"]}
                   :dependencies [[io.pedestal/pedestal.service-tools "0.5.5"]
                                  [reloaded.repl "0.2.4"]]
                   :source-paths ["dev"]
                   :env {:app-config "./config/config.edn"}
                   :repl-options {:init-ns user}}
             :uberjar {:aot [homester.core]}}
  :main ^{:skip-aot true} homester.core)

