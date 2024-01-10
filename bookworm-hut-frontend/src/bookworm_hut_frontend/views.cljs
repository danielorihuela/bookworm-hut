(ns bookworm-hut-frontend.views
  (:require
   [re-frame.core :as re-frame]
   [bookworm-hut-frontend.subs :as subs]
   [bookworm-hut-frontend.events :as events]
   [bookworm-hut-frontend.register.views]
   [bookworm-hut-frontend.login.views]
   [bookworm-hut-frontend.read-books.views]
   [bookworm-hut-frontend.routes :as routes]
   [bookworm-hut-frontend.translations :as tr]))

(defn flex-col-center-vh []
  {:display "flex"
   :flex-direction "column"
   :justify-content "center"
   :align-items "center"})

(defn flex-col-center-h []
  {:display "flex"
   :flex-direction "column"
   :align-items "center"})

(defn home-panel []
  (let [locale (re-frame/subscribe [::subs/locale])]
    [:div {:style (conj {:height "100vh"} (flex-col-center-vh))}
     [:div {:style (conj {:flex-grow 1} (flex-col-center-vh))}
      [:label {:style {:font-size "2.5rem"}} "Bookworm Hut"]
      [:img {:src "logo.png" :width 150 :height 150}]]
     [:div {:style (conj {:flex-grow 2 :gap "2em"} (flex-col-center-h))}
      [:button.button.is-primary  {:style {:width "10em"} :on-click #(re-frame/dispatch [::events/navigate :register])} (tr/tr @locale '(:register :title))]
      [:button.button.is-primary {:style {:width "10em"} :on-click #(re-frame/dispatch [::events/navigate :login])} (tr/tr @locale '(:login :title))]]]))

(defn register-panel []
  (let [locale (re-frame/subscribe [::subs/locale])]
    [:div {:style (conj {:height "100vh"} (flex-col-center-vh))}
     [:div {:style (conj {:flex-grow 1} (flex-col-center-vh))}
      [:label {:style {:font-size "2.5rem"}} "Bookworm Hut"]
      [:img {:src "logo.png" :width 150 :height 150}]
      [:label {:style {:font-size "1.5rem"}} (tr/tr @locale '(:register :title))]]
     [:div {:style {:flex-grow 2}}
      [bookworm-hut-frontend.register.views/register-form]]]))

(defn login-panel []
  (let [locale (re-frame/subscribe [::subs/locale])]
    [:div {:style (conj {:height "100vh"} (flex-col-center-vh))}
     [:div {:style (conj {:flex-grow 1} (flex-col-center-vh))}
      [:label {:style {:font-size "2.5rem"}} "Bookworm Hut"]
      [:img {:src "logo.png" :width 150 :height 150}]
      [:label {:style {:font-size "1.5rem"}} (tr/tr @locale '(:login :title))]]
     [:div {:style {:flex-grow 2}}
      [bookworm-hut-frontend.login.views/login-form]]]))

(defn heading []
  (let [locale (re-frame/subscribe [::subs/locale])]
    [:div
     {:style {:display "flex" :gap "2em"}}
     [:button.button.is-primary
      {:style {:min-width "10em"}
       :on-click #(re-frame/dispatch [::events/navigate :read-books])}
      (tr/tr @locale '(:heading :read-books))]
     [:label {:style {:font-size "1.5rem"}} "Bookworm Hut"]
     [:button.button.is-primary
      {:style {:min-width "10em"}
       :on-click #(re-frame/dispatch [::events/navigate :stats])}
      (tr/tr @locale '(:heading :stats))]]))

(defn read-books-panel []
  (let [locale (re-frame/subscribe [::subs/locale])]
    [:div {:style (conj {:height "100vh" :gap "2em"} (flex-col-center-h))}
     [:div {:style {:display "flex" :align-items "center" :margin-top "2em"}}
     [heading]]
     [:div {:style {:display "flex" :align-items "center"}}
      [bookworm-hut-frontend.read-books.views/add-book-form]]
     [:div {:style {:overflow "auto" :margin-bottom "2em"}}
      [bookworm-hut-frontend.read-books.views/read-books-table]]]))

(defn stats-panel []
  (let [locale (re-frame/subscribe [::subs/locale])]
    [:div {:style (conj {:height "100vh" :gap "2em"} (flex-col-center-h))}
     [:div {:style {:display "flex" :align-items "center" :margin-top "2em"}}
     [heading]]]))

(defn panels [panel]
  (case panel
    :home-panel (home-panel)
    :register-panel (register-panel)
    :login-panel (login-panel)
    :read-books-panel (read-books-panel)
    :stats-panel (stats-panel)
    [:div "404 NOT FOUND"]))

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    (panels @active-panel)))
