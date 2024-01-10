(ns bookworm-hut-frontend.read-books.events
  (:require
   [re-frame.core :as re-frame]
   [ajax.core :as ajax]
   [day8.re-frame.http-fx]
   [goog.crypt.base64 :as b64]
   [clojure.string :as str]
   [bookworm-hut-frontend.config :as config]))

(defn claims [base64]
  (-> base64
      (str/split ".")
      (get 1)
      b64/decodeString
      (#(.parse js/JSON %))
      (js->clj :keywordize-keys true)))
      
(re-frame/reg-event-fx
 ::add-book-alert
 (fn [{{token :token} :db} [_ bookname num-pages year month]]
   (println (:id (claims token)))
   (js/alert (str bookname num-pages year month))))

(re-frame/reg-event-fx
 ::add-book
 (fn
   [{{token :token} :db} [_ bookname num-pages year month]]
   {:http-xhrio {:method          :post
                 :uri             (str config/url "/users/" (:id (claims token)) "/books")
                 :headers         {"Authorization" (str "Bearer " token)}
                 :params          {:bookname bookname :num-pages num-pages :year year :month month}
                 :format          (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true}) 
                 :on-success      [::process-add-book-response]
                 :on-failure      [::bad-add-book-response]
                 }
    }))

(re-frame/reg-event-fx
 ::process-add-book-response
 (fn [_]
   {:fx [[:dispatch [::get-read-books]]]}))

(re-frame/reg-event-fx                  
 ::bad-add-book-response
 (fn [_ [_ {{errorCode :errorCode} :response}]]
   {:add-book-error-alert errorCode}))

(re-frame/reg-fx
 :add-book-error-alert
 (fn [error-code]
     (js/alert "Something went wrong with the server")))

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
