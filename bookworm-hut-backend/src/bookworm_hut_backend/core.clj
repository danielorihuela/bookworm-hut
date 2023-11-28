(ns bookworm-hut-backend.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.request :as request]
            [ring.middleware.json :as json]
            [ring.middleware.cors :as cors]
            [ring.middleware.params :as params]
            [ring.middleware.keyword-params :as keyword-params]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.pprint :as pprint]
            [buddy.hashers :as hashers]
            [bookworm-hut-backend.db.users :as users-repository]))

;; In production environments we should handle different types of
;; errors in different maners.
;; For example, 400 for incorrect data and 500 if the backend
;; cannot access the database.
;; For that, we should look into the SQLState that we can obtain
;; with (.getSQLState e).
;; For a side project I don't need it.
(defn register [username password]
  (try
    (do
      (users-repository/insert-user
       username
       (hashers/derive password {:alg :scrypt}))
      {:status 201
       :headers {"Content-type" "application/json"} })
    (catch Exception e
      {:status 400
       :body {:errorCode "INVALID_DATA_FORMAT" :error "Wrong username or password format"}
       :headers {"Content-type" "application/json"} })))

(defroutes all-routes
  (POST "/register" {{username :username password :password} :body} (register username password))
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

