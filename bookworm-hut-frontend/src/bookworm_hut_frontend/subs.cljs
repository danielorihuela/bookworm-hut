(ns bookworm-hut-frontend.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::loading?
 (fn [db]
   (:loading? db)))

(re-frame/reg-sub
 ::active-panel
 (fn [db _]
   (:active-panel db)))

(re-frame/reg-sub
 ::locale
 (fn [db _]
   (:locale db)))

(re-frame/reg-sub
 ::read-books
 (fn [db _]
   (:read-books db)))
