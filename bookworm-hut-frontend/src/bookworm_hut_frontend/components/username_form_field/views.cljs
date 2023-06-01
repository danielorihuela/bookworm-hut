(ns bookworm-hut-frontend.components.username-form-field.views
  (:require
   [reagent.core :as reagent]))

(defn username-form-field [hint error-hint valid-fn? valid? username]
  (let [empty? (reagent/atom true)]
    (fn []
      [:div.field
       [:p.control.has-icons-left
        [:input.input {:type "text"
                       :placeholder hint
                       :name "username"
                       :class (when (not @empty?) (if @valid? "is-success" "is-danger"))
                       :on-change #(let [value (-> % .-target .-value)]
                                     (reset! valid? (valid-fn? value))
                                     (reset! empty? (> 0 (count value)))
                                     (reset! username value))}]
        [:span.icon.is-small.is-left
         [:i.fas.fa-user]]]
       [:label.help.is-danger
        {:style {:visibility (when (or @valid? @empty?) "hidden")}}
        error-hint]])))
