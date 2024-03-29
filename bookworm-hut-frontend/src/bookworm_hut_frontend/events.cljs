(ns bookworm-hut-frontend.events
  (:require
   [re-frame.core :as re-frame]
   [bookworm-hut-frontend.db :as db]))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::set-locale
 (fn [db [_ locale]]
   (assoc db :locale locale)))

(re-frame/reg-event-fx
 ::navigate
 (fn [_ [_ handler]]
   {:navigate handler}))

(re-frame/reg-event-fx
 ::set-active-panel
 (fn [{:keys [db]} [_ active-panel]]
   {:db (assoc db :active-panel active-panel)}))
