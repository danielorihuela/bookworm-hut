(ns bookworm-hut-frontend.components.username-form-field.views
  (:require
   [reagent.core :as reagent]))

(defn username-form-field [username valid-fn? hint error-hint]
  (let [empty? (reagent/atom true)
        valid? (reagent/atom false)]
    (fn [username valid-fn? hint error-hint]
      [:div.field
       [:p.control.has-icons-left
        [:span.icon.is-small.is-left [:i.fas.fa-user]]
        [:input.input {:type "text"
                       :placeholder hint
                       :name "username"
                       :class (when (not @empty?) (if @valid? "is-success" "is-danger"))
                       :on-change #(let [value (-> % .-target .-value)]
                                     (reset! valid? (valid-fn? value))
                                     (reset! empty? (> 0 (count value)))
                                     (reset! username value))}]]
       [:label.help.is-danger
        {:style {:visibility (when (or @valid? @empty?) "hidden")}}
        error-hint]])))
