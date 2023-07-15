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

(spec/def ::password (spec/and string? #(< 15 (count %))))
(defn password-valid? [password]
  (spec/valid? ::password password))

(defn submit-button [username-filled? username password-filled? password]
  [:input.button.is-primary
   {:type "submit"
    :value "Register"
    :style {:align-self "center"}
    :disabled (when (or (not @username-filled?) (not @password-filled?)) true)
    :on-click #(re-frame/dispatch [::events/register @username @password])}])

(defn register-form []
  (let [username-filled? (reagent/atom false)
        username (reagent/atom "")
        password-filled? (reagent/atom false)
        password (reagent/atom "")
        locale (re-frame/subscribe [::subs/locale])]
    (fn []
      [:div
       {:style {:display "flex" :flex-direction "column"}}
       [username-form-field
        (tr/tr @locale '(:register :username-hint))
        (tr/tr @locale '(:register :username-error-hint))
        username-valid?
        username-filled?
        username]
      [password-form-field
        (tr/tr @locale '(:register :password-hint))
        (tr/tr @locale '(:register :password-error-hint))
        password-valid?
        password-filled?
        password]
       [submit-button
        username-filled?
        username
        password-filled?
        password]])))
