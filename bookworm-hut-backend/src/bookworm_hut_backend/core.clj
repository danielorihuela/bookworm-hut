(ns bookworm-hut-backend.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.json]
            [ring.middleware.cors :as cors]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.pprint :as pprint]))

(defn get-username-handler [request]
  {:status 200
   :body {:name "elodin" :description "Naming professor"}
   :headers {"Content-type" "application.json"} })

(defroutes all-routes
  (GET "/" [] get-username-handler)
  (route/not-found "<h1>Page not found</h1>"))


(defn cors-headers []
  {"Access-Control-Allow-Origin"  "http://localhost:8280"})

(defn wrap-cors [handler]
  (fn [req]
    (let [response (handler req)]
      (assoc response :headers (merge (:headers response) (cors-headers))))))

(defn print-response [handler]
  (fn [req]
      (pprint/pprint (handler req))
      (handler req)))

(def app
  (-> all-routes
      ring.middleware.json/wrap-json-response
      (cors/wrap-cors :access-control-allow-origin [#"http://localhost:8280"]
                      :access-control-allow-methods [:get])
      ;;wrap-cors
      print-response
      ))

(defn start-server []
  (jetty/run-jetty
   (fn [request] (app request))
   {:port 3000 :join? false}))

