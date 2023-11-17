(ns bookworm-hut-frontend.views
  (:require
   [re-frame.core :as re-frame]
   [bookworm-hut-frontend.subs :as subs]
   [bookworm-hut-frontend.events :as events]
   [bookworm-hut-frontend.register.views]
   [bookworm-hut-frontend.routes :as routes]))

(defn flex-col-center-vh []
  {:display "flex"
   :flex-direction "column"
   :justify-content "center"
   :align-items "center"})

(defn home-panel []
  [:div
   [:h1 "HOME PANEL"]
   [:button {:on-click #(re-frame/dispatch [::events/navigate :register])} "Register"]])

(defn register-panel []
  [:div {:style (conj {:height "100vh"} (flex-col-center-vh))}
   [:div {:style (conj {:flex-grow 1} (flex-col-center-vh))}
    [:label {:style {:font-size "2.5em"}} "Bookworm Hut"]
    [:img {:src "logo.png" :width 150 :height 150}]]
   [:div {:style {:flex-grow 2}}
    [bookworm-hut-frontend.register.views/register-form]]])

(defn panels [panel]
  (case panel
    :home-panel (home-panel)
    :register-panel (register-panel)
    [:div "404 NOT FOUND"]))

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    (panels @active-panel)))
