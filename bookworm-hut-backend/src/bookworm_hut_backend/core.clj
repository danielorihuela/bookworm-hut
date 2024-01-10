(ns bookworm-hut-backend.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.request :as request]
            [ring.middleware.json :as json]
            [ring.middleware.cors :as cors]
            [ring.middleware.params :as params]
            [ring.middleware.keyword-params :as keyword-params]
            [clojure.spec.alpha :as spec]
            [clojure.set :as set]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.pprint :as pprint]
            [buddy.hashers :as hashers]
            [buddy.auth :as auth]
            [buddy.auth.backends :as backends]
            [buddy.auth.middleware :refer (wrap-authentication)]
            [buddy.auth.accessrules :refer [wrap-access-rules]]
            [buddy.sign.jwt :as jwt]
            [cheshire.core :as cjson]
            [bookworm-hut-backend.db.users :as users-repository]
            [bookworm-hut-backend.db.books :as books-repository])
  (:import (java.sql SQLException)))

(def secret "mysecret")
(def backend (backends/jws {:secret secret :token-name "Bearer"}))

(spec/def ::username (spec/and string? #(< 2 (count %))))
(defn username-valid? [username]
  (spec/valid? ::username username))

(spec/def ::password (spec/and string? #(< 7 (count %))))
(defn password-valid? [password]
  (spec/valid? ::password password))

(defn register [username password]
  (try
    (if (and (username-valid? username) (password-valid? password))
      (do
        (users-repository/insert-user
         username
         (hashers/derive password {:alg :argon2id}))
        {:status 201
         :body {}
         :headers {"Content-type" "application/json"}})
      (throw (Exception. "Wrong credentials format")))
    (catch java.sql.SQLException e
      (case (.getSQLState e)
        "23505" {:status 409
                 :body {:errorCode "USERNAME_ALREADY_EXISTS"
                        :error "Username already exists"}
                 :headers {"Content-type" "application/json"} }
        {:status 400
         :body {:errorCode "INVALID_DATA_FORMAT"
                :error "Wrong username or password format"}
         :headers {"Content-type" "application/json"} }))
    (catch Exception e
      {:status 400
       :body {:errorCode "INVALID_DATA_FORMAT"
              :error "Wrong username or password format"}
       :headers {"Content-type" "application/json"} })))

(defn login
  [username password]
  (try
    (let [user (get (users-repository/get-user-by-name username) 0)
          id (get user :users/username)
          password-hash (get user :users/password)]
      (if (true? (:valid (hashers/verify password password-hash)))
        {:status 200
         :body {:token (jwt/sign {:id id} secret)}
         :headers {"Content-type" "application/json"}}
        {:status 401
         :body {:errorCode "WRONG_CREDENTIALS"
                :error "Wrong username or password"}
         :headers {"Content-type" "application/json"} }))
    (catch Exception e
      {:status 500
       :body {:errorCode "UNEXPECTED_ERROR"
              :error "Something unexpected went wrong"}
       :headers {"Content-type" "application/json"} })))

(spec/def ::bookname (spec/and string? #(< 0 (count %))))
(defn bookname-valid? [bookname]
  (spec/valid? ::bookname bookname))

(spec/def ::num-pages (spec/and number? #(< 0 %)))
(defn num-pages-valid? [num-pages]
  (spec/valid? ::num-pages num-pages))

(spec/def ::year (spec/and number? #(< 1899 %) #(< % 4001)))
(defn year-valid? [year]
  (spec/valid? ::year year))

(spec/def ::month (spec/and number? #(< 0 %) #(< % 13)))
(defn month-valid? [month]
  (spec/valid? ::month month))

(defn add-book
  [username bookname num-pages year month]
  (try
    (if (and (username-valid? username)
             (bookname-valid? bookname)
             (num-pages-valid? num-pages)
             (year-valid? year)
             (month-valid? month))
      (do
        (books-repository/insert-book
         username bookname num-pages year month)
        {:status 201
         :body {}
         :headers {"Content-type" "application/json"}})
      (throw (Exception. "Book could not be added")))
    (catch Exception e
      {:status 500
       :body {:errorCode "UNKNOWN_ERROR"
              :error "Something went wrong"}
       :headers {"Content-type" "application/json"} })))

(defn get-books
  [username]
  (try
    (let [raw-books (books-repository/get-read-books username)
          books (->> raw-books
                     (map #(select-keys % [:books/bookname :books/pages :books/year :books/month]))
                     (map #(set/rename-keys % {:books/bookname :bookname
                                               :books/pages :num-pages
                                               :books/year :year
                                               :books/month :month})))]
      {:status 200
       :body {:books books}
       :headers {"Content-type" "application/json"}})
    (catch Exception e
      {:status 500
       :body {:errorCode "UNKNOWN_ERROR"
              :error "Something went wrong"}
       :headers {"Content-type" "application/json"} })))

(defroutes all-routes
  (POST "/register" {{username :username password :password} :body} (register username password))
  (POST "/login" {{username :username password :password} :body} (login username password))
  (POST "/users/:id/books" {{id :id} :params {bookname :bookname num-pages :num-pages year :year month :month} :body} (add-book id bookname num-pages year month))
  (GET "/users/:id/books" {{id :id} :params} (get-books id))
  (route/not-found "<h1>Page not found</h1>"))

(def access-rules [{:pattern #"/register"
                    :handler (fn [_] true)}
                   {:pattern #"/login"
                    :handler (fn [_] true)}
                   {:pattern #".*"
                    :handler (fn [request]
                               (auth/authenticated? request))}])

(defn on-error
  [request value]
  {:status 403
   :headers {}
   :body "Unauthenticated"})

;; Print request, useful for printing
;; the transformation in the `app`
;; below
(defn wrap-print-request [handler]
  (fn [request]
    (println request)
    (handler request)))

(def app
  (-> all-routes
      (wrap-access-rules {:rules access-rules :on-error on-error})
      (wrap-authentication backend)
      (json/wrap-json-body {:keywords? true})
      ;;wrap-print-request
      keyword-params/wrap-keyword-params
      params/wrap-params
      json/wrap-json-response
      (cors/wrap-cors
       :access-control-allow-origin [#"http://localhost:8280"]
       :access-control-allow-methods [:get :post])
      ))

(defn start-server []
  (jetty/run-jetty
   (fn [request] (app request))
   {:port 3000 :join? false}))

