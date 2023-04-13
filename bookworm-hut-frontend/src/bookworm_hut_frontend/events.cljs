(ns bookworm-hut-frontend.events
  (:require
   [re-frame.core :as re-frame]
   [bookworm-hut-frontend.db :as db]
   [ajax.core :as ajax]
   [day8.re-frame.http-fx]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::update-name
 (fn [db [_ value]]
   (assoc db :name value)))

(re-frame/reg-event-fx
 ::fetch-name
 (fn                ;; <-- the handler function
   [{db :db} _]     ;; <-- 1st argument is coeffect, from which we extract db 
   ;; we return a map of (side) effects
   {:http-xhrio {:method          :get
                  :uri             "http://localhost:3000"
                  :format          (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success      [::fetch-name-success]
                  :on-failure      [:bad-response]}
     :db  (assoc db :loading? true)}))

(re-frame/reg-event-db
 ::fetch-name-success
 (fn [db [_ data]]
   (-> db
     (assoc :loading? false)
     (assoc :name (:name data)))))
