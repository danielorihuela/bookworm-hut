(ns bookworm-hut-frontend.routes
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [re-frame.core :as re-frame]
            [bookworm-hut-frontend.events :as events]))

(def routes
  ["/" {""      :home
        "register" :register
        "login" :login
        "read-books" :read-books
        "stats" :stats}])

(defn dispatch [route]
  (re-frame/dispatch [::events/set-active-panel route]))

(defn match
  [url]
  (get (bidi/match-route routes url) :handler))

(defn path-to-view-id
  [route-key]
  (keyword (str (name route-key) "-panel")))

;;; Define the routing history of our web app
;;; `match` returns the route id for the given url, nil otherwise
;;; `path-to-view-id` transforms the route id into the view id
;;; `dispatch` gets called when a match is found
(defonce history
  (pushy/pushy dispatch
               match
               {:identity-fn path-to-view-id}))

(defn start!
  "Start event listeners for the routing library"
  []
  (pushy/start! history))

;; We will call this function whenever the user "clicks" or
;; performs any other action that requires the page to
;; change the url and the view.
(re-frame/reg-fx
 :navigate
 (fn [route-key]
   (pushy/set-token! history (bidi/path-for routes route-key))))
