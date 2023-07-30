(ns bookworm-hut-frontend.register.events
  (:require
   [re-frame.core :as re-frame]
   [ajax.core :as ajax]
   [day8.re-frame.http-fx]
   [bookworm-hut-frontend.config :as config]))

(re-frame/reg-event-fx
 ::register
 (fn
   [{db :db} [_ username password]]
   {:http-xhrio {:method          :post
                 :uri             (str config/url "/register")
                 :params          {:username username :password password}
                 :format          (ajax/url-request-format)
                 :response-format (ajax/json-response-format {:keywords? true}) 
                 :on-success      [::process-register-response]
                 :on-failure      [::bad-register-response]}
    }))

(re-frame/reg-event-db                   
 ::process-register-response
 (fn
   [db [_ {username :username password :password}]]
   (js/alert (str "Username = " username ", Password = " password))))

(re-frame/reg-event-db
 ::bad-register-response
 (fn
   [db [_ response]]
   (let [response (:response response)
         errorCode (:errorCode response)]
     (if (= (compare errorCode "INVALID_DATA_FORMAT") 0)
       (js/alert "Username or password format is wrong")))))

