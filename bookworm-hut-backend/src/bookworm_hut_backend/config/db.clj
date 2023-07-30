(ns bookworm-hut-backend.config.db
  (:require [next.jdbc :as jdbc]))

(def db-config
  {:dbtype "postgresql"
   :host "localhost"
   :dbname "bookworm_hut"
   :user "bookworm_hut"
   :password "bookworm_hut"})

(def db (jdbc/get-datasource db-config))
