(ns bookworm-hut-frontend.register.events
  (:require
   [re-frame.core :as re-frame]
   [ajax.core :as ajax]
   [day8.re-frame.http-fx]
   [bookworm-hut-frontend.config :as config]
   [bookworm-hut-frontend.events :as events]))

(re-frame/reg-event-fx
 ::register
 (fn
   [{db :db} [_ username password]]
   {:http-xhrio {:method          :post
                 :uri             (str config/url "/register")
                 :params          {:username username :password password}
                 :format          (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true}) 
                 :on-success      [::process-register-response]
                 :on-failure      [::bad-register-response]}
    }))

(re-frame/reg-event-fx
 ::process-register-response
 (fn [_ _]
   {:navigate :login}))

(re-frame/reg-event-fx
 ::bad-register-response
 (fn [_ [_ {{errorCode :errorCode} :response}]]
   {:register-error-alert errorCode}))

(re-frame/reg-fx
 :register-error-alert
 (fn [error-code]
   (js/alert
    (case error-code
      "USERNAME_ALREADY_EXISTS" "Username already exists"
      "INVALID_DATA_FORMAT" "Username or password format is wrong"
      "Something went wrong with the server"))))
