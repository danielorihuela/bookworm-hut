(ns bookworm-hut-backend.db.users
  (:require [next.jdbc :as jdbc]
            [honey.sql :as sql]
            [bookworm-hut-backend.config.db :as db-config]))

(defn insert-user-query [username password]
  {:insert-into [:users]
   :columns [:username :password]
   :values [[username password]]})

(defn insert-user [username password]
  (jdbc/execute!
   db-config/db
   (sql/format (insert-user-query username password))))
