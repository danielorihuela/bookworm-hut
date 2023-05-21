(ns bookworm-hut-frontend.routes
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [re-frame.core :as re-frame]
            [bookworm-hut-frontend.events :as events]))

(defmulti panels identity)
(defmethod panels :default [] [:div "404 NOT FOUND"])

(def routes
  (atom
    ["/" {""      :home
          "register" :register}]))

(defn parse [url]
  (bidi/match-route @routes url))

(defn url-for [& args]
  (apply bidi/path-for (into [@routes] args)))

(defn dispatch [route]
  (let [panel (keyword (str (name (:handler route)) "-panel"))]
    (re-frame/dispatch [::events/set-active-panel panel])))

(defonce history
  (pushy/pushy dispatch parse))

(defn navigate! [handler]
  (pushy/set-token! history (url-for handler)))

(defn start! []
  (pushy/start! history))

(re-frame/reg-fx
 :navigate
 (fn [handler]
   (navigate! handler)))
