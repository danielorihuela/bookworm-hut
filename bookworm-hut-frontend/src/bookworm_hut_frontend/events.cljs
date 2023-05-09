(ns bookworm-hut-frontend.events
  (:require
   [re-frame.core :as re-frame]
   [bookworm-hut-frontend.db :as db]
   [ajax.core :as ajax]
   [day8.re-frame.http-fx]))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))
