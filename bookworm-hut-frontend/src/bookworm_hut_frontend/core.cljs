(ns bookworm-hut-frontend.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [bookworm-hut-frontend.events :as events]
   [bookworm-hut-frontend.views :as views]
   [bookworm-hut-frontend.config :as config]
   [bookworm-hut-frontend.routes :as routes]))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-panel] root-el)))

(defn init []
  (routes/start!)
  (re-frame/dispatch-sync [::events/initialize-db])
  (re-frame/dispatch [::events/set-locale (keyword (. js/navigator -language))])
  (mount-root))
