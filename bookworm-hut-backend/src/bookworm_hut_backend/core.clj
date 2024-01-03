(ns bookworm-hut-backend.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.request :as request]
            [ring.middleware.json :as json]
            [ring.middleware.cors :as cors]
            [ring.middleware.params :as params]
            [ring.middleware.keyword-params :as keyword-params]
            [clojure.spec.alpha :as spec]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.pprint :as pprint]
            [buddy.hashers :as hashers]
            [buddy.auth.backends :as backends]
            [buddy.auth.middleware :refer (wrap-authentication)]
            [buddy.sign.jwt :as jwt]
            [cheshire.core :as cjson]
            [bookworm-hut-backend.db.users :as users-repository])
  (:import (java.sql SQLException)))

(def secret "mysecret")
(def backend (backends/jws {:secret secret}))

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
      (println (.getSQLState e))
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
          id (get user :users/id)
          password-hash (get user :users/password)]
      (if (true? (:valid (hashers/verify password password-hash)))
        {:status 200
         :body {:token (jwt/sign {:user id} secret)}
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


(defroutes all-routes
  (POST "/register" {{username :username password :password} :body} (register username password))
  (POST "/login" {{username :username password :password} :body} (login username password))
  (route/not-found "<h1>Page not found</h1>"))

(def app
  (-> all-routes
      (#(json/wrap-json-body % {:keywords? true}))
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

