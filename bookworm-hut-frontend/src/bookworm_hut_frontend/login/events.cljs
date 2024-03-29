(ns bookworm-hut-frontend.login.events
  (:require
   [re-frame.core :as re-frame]
   [ajax.core :as ajax]
   [day8.re-frame.http-fx]
   [goog.crypt.base64 :as b64]
   [clojure.string :as str]
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

(re-frame/reg-event-fx
 ::process-login-response
 (fn [{db :db} [_ {token :token}]]
   {:db (assoc db :token token)
    :navigate :read-books
    :fx [[:dispatch [::get-read-books]]]}))

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

(defn claims [base64]
  (-> base64
      (str/split ".")
      (get 1)
      b64/decodeString
      (#(.parse js/JSON %))
      (js->clj :keywordize-keys true)))

(re-frame/reg-event-fx
 ::get-read-books
 (fn
   [{{token :token} :db} [_ username]]
   {:http-xhrio {:method          :get
                 :uri             (str config/url "/users/" (:id (claims token)) "/books")
                 :headers         {"Authorization" (str "Bearer " token)}
                 :format          (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true}) 
                 :on-success      [::process-get-read-books-response]
                 :on-failure      [::bad-get-read-books-response]
                 }
    }))

(re-frame/reg-event-fx
 ::process-get-read-books-response
 (fn [{db :db} [_ {books :books}]]
   {:db (assoc db :read-books books)}))

(re-frame/reg-event-fx
 ::bad-get-read-books-response
 (fn [_ [_ {{errorCode :errorCode} :response}]]
   {:get-read-books-error-alert errorCode}))

(re-frame/reg-fx
 :get-read-books-error-alert
 (fn [error-code]
     (js/alert "Something went wrong with the server")))
