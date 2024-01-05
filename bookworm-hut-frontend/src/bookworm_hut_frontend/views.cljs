(ns bookworm-hut-frontend.views
  (:require
   [re-frame.core :as re-frame]
   [bookworm-hut-frontend.subs :as subs]
   [bookworm-hut-frontend.events :as events]
   [bookworm-hut-frontend.register.views]
   [bookworm-hut-frontend.login.views]
   [bookworm-hut-frontend.routes :as routes]
   [bookworm-hut-frontend.translations :as tr]))

(defn flex-col-center-vh []
  {:display "flex"
   :flex-direction "column"
   :justify-content "center"
   :align-items "center"})

(defn flex-col-vh []
  {:display "flex"
   :flex-direction "column"
   :align-items "center"})

(defn home-panel []
  (let [locale (re-frame/subscribe [::subs/locale])]
    [:div {:style (conj {:height "100vh"} (flex-col-center-vh))}
     [:div {:style (conj {:flex-grow 1} (flex-col-center-vh))}
      [:label {:style {:font-size "2.5em"}} "Bookworm Hut"]
      [:img {:src "logo.png" :width 150 :height 150}]]
     [:div {:style (conj {:flex-grow 2 :gap "2em"} (flex-col-vh))}
      [:button.button.is-primary  {:style {:width "10rem"} :on-click #(re-frame/dispatch [::events/navigate :register])} (tr/tr @locale '(:register :title))]
      [:button.button.is-primary {:style {:width "10rem"} :on-click #(re-frame/dispatch [::events/navigate :login])} (tr/tr @locale '(:login :title))]]
     ]))

(defn register-panel []
  (let [locale (re-frame/subscribe [::subs/locale])]
    [:div {:style (conj {:height "100vh"} (flex-col-center-vh))}
     [:div {:style (conj {:flex-grow 1} (flex-col-center-vh))}
      [:label {:style {:font-size "2.5em"}} "Bookworm Hut"]
      [:img {:src "logo.png" :width 150 :height 150}]
      [:label {:style {:font-size "1.5em"}} (tr/tr @locale '(:register :title))]]
     [:div {:style {:flex-grow 2}}
      [bookworm-hut-frontend.register.views/register-form]]]))

(defn login-panel []
  (let [locale (re-frame/subscribe [::subs/locale])]
    [:div {:style (conj {:height "100vh"} (flex-col-center-vh))}
     [:div {:style (conj {:flex-grow 1} (flex-col-center-vh))}
      [:label {:style {:font-size "2.5em"}} "Bookworm Hut"]
      [:img {:src "logo.png" :width 150 :height 150}]
      [:label {:style {:font-size "1.5em"}} (tr/tr @locale '(:login :title))]]
     [:div {:style {:flex-grow 2}}
      [bookworm-hut-frontend.login.views/login-form]]]))

(defn panels [panel]
  (case panel
    :home-panel (home-panel)
    :register-panel (register-panel)
    :login-panel (login-panel)
    [:div "404 NOT FOUND"]))

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    (panels @active-panel)))
