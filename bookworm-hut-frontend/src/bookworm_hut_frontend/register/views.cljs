(ns bookworm-hut-frontend.register.views
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]))

(defn username-field []
  [:div.field
   [:p.control.has-icons-left
    [:input.input {:type "text" :placeholder "Username"}]
    [:span.icon.is-small.is-left
     [:i.fas.fa-user]]]])

(defn password-field []
  (let [show-password? (reagent/atom false)]
    (fn []
      [:div.field
       [:p.control.has-icons-left.has-icons-right
        [:input.input {:type (if @show-password? "text" "password") :placeholder "Password"}]
        [:span.icon.is-small.is-left
         [:i.fas.fa-lock]]
        [:span.icon.is-small.is-right
         [(if @show-password?
            :i.fas.fa-eye-slash
            :i.fas.fa-eye)
          {:style
           {:pointer-events "initial"}
           :on-click #(reset! show-password? (not @show-password?))}]]]])))

(defn register-panel []
  [:form
   [username-field]
   [password-field]
   [:input.button.is-primary {:type "submit" :value "Register"}]])
