(ns bookworm-hut-frontend.views
  (:require
   [re-frame.core :as re-frame]
   [bookworm-hut-frontend.subs :as subs]
   [bookworm-hut-frontend.events :as events]
   ))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])
        loading? (re-frame/subscribe [::subs/loading?])]
    [:div
     [:h1
      "Hello from " @name]
     (when @loading? "Loading ...")
     [:button
      {:on-click #(re-frame/dispatch [::events/update-name "Elodin UI"])} "Update name"]
     [:button
      {:on-click #(re-frame/dispatch [::events/fetch-name])} "Update name"]
     ]))
