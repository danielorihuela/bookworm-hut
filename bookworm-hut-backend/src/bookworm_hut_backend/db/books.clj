(ns bookworm-hut-backend.db.books
  (:require [next.jdbc :as jdbc]
            [honey.sql :as sql]
            [bookworm-hut-backend.config.db :as db-config]))

(defn insert-book-query [username bookname num-pages year month]
  {:insert-into [:books]
   :columns [:username :bookname :pages :year :month]
   :values [[username bookname num-pages year month]]})

(defn insert-book [username bookname num-pages year month]
  (jdbc/execute!
   db-config/db
   (sql/format (insert-book-query username bookname num-pages year month))))
