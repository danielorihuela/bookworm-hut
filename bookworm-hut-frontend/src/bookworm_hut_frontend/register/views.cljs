(ns bookworm-hut-frontend.register.views
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [clojure.spec.alpha :as spec]
   [bookworm-hut-frontend.components.username-form-field.views :refer [username-form-field]]
   [bookworm-hut-frontend.components.password-form-field.views :refer [password-form-field]]
   [bookworm-hut-frontend.config :as config]
   ))

(spec/def ::username (spec/and string? #(< 2 (count %))))
(defn username-valid? [username]
  (spec/valid? ::username username))

(spec/def ::password (spec/and string? #(< 15 (count %))))
(defn password-valid? [password]
  (spec/valid? ::password password))

(defn submit-button [username-filled? password-filled?]
  [:input.button.is-primary
   {:type "submit"
    :value "Register"
    :style {:align-self "center"}
    :disabled (when (or (not @username-filled?) (not @password-filled?)) true)}])

(defn register-panel []
  (let [username-filled? (reagent/atom false)
        password-filled? (reagent/atom false)]
    (fn []
      [:form {:style {:display "flex" :flex-direction "column"}
              :action (str config/url "/register")
              :method "post"}
       [username-form-field
        "Username"
        "Minimum length is 3"
        username-valid?
        username-filled?]
       [password-form-field
        "Password"
        "Minimum length is 16"
        password-valid?
        password-filled?]
       [submit-button username-filled? password-filled?]])))
