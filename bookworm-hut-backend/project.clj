(defproject bookworm-hut-backend "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [ring/ring-core "1.10.0"]
                 [ring/ring-jetty-adapter "1.10.0"]
                 [ring/ring-json "0.5.1"]
                 [ring-cors "0.1.13"]
                 [compojure "1.7.0"]
                 [com.github.seancorfield/next.jdbc "1.3.883"]
                 [org.postgresql/postgresql "42.6.0"]
                 [com.github.seancorfield/honeysql "2.4.1045"]
                 [buddy/buddy-hashers "2.0.167"]]
  :repl-options {:init-ns bookworm-hut-backend.core})
