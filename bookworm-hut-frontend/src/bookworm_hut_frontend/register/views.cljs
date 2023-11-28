(ns bookworm-hut-frontend.register.views
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [clojure.spec.alpha :as spec]
   [bookworm-hut-frontend.components.username-form-field.views :refer [username-form-field]]
   [bookworm-hut-frontend.components.password-form-field.views :refer [password-form-field]]
   [bookworm-hut-frontend.config :as config]
   [bookworm-hut-frontend.register.events :as events]
   [bookworm-hut-frontend.subs :as subs]
   [bookworm-hut-frontend.translations :as tr]))

(spec/def ::username (spec/and string? #(< 2 (count %))))
(defn username-valid? [username]
  (spec/valid? ::username username))

(spec/def ::password (spec/and string? #(< 7 (count %))))
(defn password-valid? [password]
  (spec/valid? ::password password))

(defn submit-button [username password locale]
  [:input.button.is-primary
   {:type "submit"
    :value (tr/tr locale '(:register :submit))
    :style {:align-self "center"}
    :disabled (or (not (username-valid? @username)) (not (password-valid? @password)))
    :on-click #(re-frame/dispatch [::events/register @username @password])}])

(defn register-form []
  (let [username (reagent/atom "")
        password (reagent/atom "")
        locale (re-frame/subscribe [::subs/locale])]
    (fn []
      [:div
       {:style {:display "flex" :flex-direction "column"}}
       [username-form-field
        username
        username-valid?
        (tr/tr @locale '(:register :username-hint))
        (tr/tr @locale '(:register :username-error-hint))]
       [password-form-field
        password
        password-valid?
        (tr/tr @locale '(:register :password-hint))
        (tr/tr @locale '(:register :password-error-hint))]
       [submit-button username password @locale]])))
