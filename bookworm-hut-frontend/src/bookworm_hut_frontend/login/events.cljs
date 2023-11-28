(ns bookworm-hut-frontend.login.events
  (:require
   [re-frame.core :as re-frame]
   [ajax.core :as ajax]
   [day8.re-frame.http-fx]
   [bookworm-hut-frontend.config :as config]))

(re-frame/reg-event-fx
 ::login
 (fn
   [{db :db} [_ username password]]
   {:http-xhrio {:method          :post
                 :uri             (str config/url "/login")
                 :params          {:username username :password password}
                 :format          (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true}) 
                 :on-success      [::process-login-response]
                 :on-failure      [::bad-login-response]}
    }))

(re-frame/reg-event-db                   
 ::process-login-response
 (fn
   [db [_ {username :username password :password}]]
   (js/alert (str "Username = " username ", Password = " password))))

(re-frame/reg-event-fx                  
 ::bad-login-response
 (fn [_ [_ {{errorCode :errorCode} :response}]]
   {:login-error-alert errorCode}))

(re-frame/reg-fx
 :login-error-alert
 (fn [error-code]
   (case error-code
     "INVALID_DATA_FORMAT" (js/alert "Username or password format is wrong")
     (js/alert "Something went wrong with the server"))))
