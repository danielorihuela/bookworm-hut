(ns bookworm-hut-frontend.components.password-form-field.views
  (:require
   [reagent.core :as reagent]))

(defn password-form-field [hint error-hint valid-fn? valid?]
  (let [show-password? (reagent/atom false)
        empty? (reagent/atom true)]
    (fn []
      [:div.field
       [:p.control.has-icons-left.has-icons-right
        [:input.input {:type (if @show-password? "text" "password")
                       :placeholder hint
                       :name "password"
                       :class (when (not @empty?) (if @valid? "is-success" "is-danger"))
                       :on-change #(let [password (-> % .-target .-value)]
                                     (reset! valid? (valid-fn? password))
                                     (reset! empty? (> 0 (count password))))}]
        [:span.icon.is-small.is-left [:i.fas.fa-lock]]
        [:span.icon.is-small.is-right
         [:i.fas
          {:class (if @show-password? "fa-eye-slash" "fa-eye")
           :style {:pointer-events "initial"}
           :on-click #(reset! show-password? (not @show-password?))}]]]
       [:label.help.is-danger
        {:style {:visibility (when (or @valid? @empty?) "hidden")}}
        error-hint]])))
