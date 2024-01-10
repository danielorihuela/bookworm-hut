(ns bookworm-hut-frontend.read-books.views
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [bookworm-hut-frontend.read-books.events :as events]
   [bookworm-hut-frontend.subs :as subs]
   [bookworm-hut-frontend.translations :as tr]))

(defn bookname-form-field [bookname hint error-hint]
  (fn [bookname hint error-hint]
    [:div.field
     [:p.control.has-icons-left
      [:span.icon.is-small.is-left [:i.fas.fa-book]]
      [:input.input {:type "text"
                     :placeholder hint
                     :name "book name"
                     :class (if (empty? @bookname) "is-danger" "is-success")
                       :on-change #(let [value (-> % .-target .-value)]
                                     (reset! bookname value))}]]
     [:label.help.is-danger
      {:style {:visibility (when (not (empty? @bookname)) "hidden")}} error-hint]]))

(defn num-pages-form-field [num-pages hint error-hint]
  (fn [num-pages hint error-hint]
    [:div.field
     [:p.control.has-icons-left
      [:span.icon.is-small.is-left [:i.fas.fa-hashtag]]
      [:input.input {:type "number"
                     :placeholder hint
                     :name "number of pages"
                     :min 0
                     :class (if (< @num-pages 1) "is-danger" "is-success")
                     :on-change #(let [value (-> % .-target .-value)]
                                   (reset! num-pages (int value)))}]]
     [:label.help.is-danger
      {:style {:visibility (when (not (< @num-pages 1)) "hidden")}} error-hint]]))

(defn select-number-form-field [value min max hint]
  (fn [value min max hint]
    [:div.control.has-icons-left
     [:span.icon.is-small.is-left [:i.fas.fa-calendar]]
     [:div.select
      {:class (if (= 0 @value) "is-danger" "is-success")
       :style {:width "100%"}}
      [:select
       {:style {:width "100%"}
        :on-change #(let [selected (-> % .-target .-value)]
                      (reset! value (int selected)))}
       [:option {:label hint :style {:display "none"}} ""]
       (for [x (range min max)]
         [:option {:key (str x)} x])]]]))

(defn submit-button [bookname num-pages year month locale]
  [:input.button.is-primary
   {:type "add"
    :value (tr/tr locale '(:read-books :add))
    :disabled (or (empty? @bookname) (< @num-pages 1) (= 0 @month) (= 0 @year))
    :on-click #(re-frame/dispatch [::events/add-book @bookname @num-pages @year @month])}])

(defn add-book-form []
  (let [bookname (reagent/atom "")
        num-pages (reagent/atom 0)
        month (reagent/atom 0)
        year (reagent/atom 0)
        locale (re-frame/subscribe [::subs/locale])]
    (fn []
      [:div
       {:style {:display "flex" :flex-direction "row" :justify-content "center" :gap "2em"}}
       [:div
        [bookname-form-field
         bookname
         (tr/tr @locale '(:read-books :bookname-hint))
         (tr/tr @locale '(:read-books :bookname-error-hint))]]
       [:div
        [num-pages-form-field
         num-pages 
         (tr/tr @locale '(:read-books :num-pages-hint))
         (tr/tr @locale '(:read-books :num-pages-error-hint))]]
       [:div
        [select-number-form-field
         year 1900 4001
         (tr/tr @locale '(:read-books :year-hint))]]
       [:div
        [select-number-form-field
         month 1 13
         (tr/tr @locale '(:read-books :month-hint))]]
       [:div
        [submit-button bookname num-pages year month @locale]]])))

(defn read-books-table []
  (let [books (re-frame/subscribe [::subs/read-books])
        locale (re-frame/subscribe [::subs/locale])]
    (fn []
      [:table.table.is-bordered.is-fullwidth
       [:thead
        [:tr
         [:th (tr/tr @locale '(:read-books :bookname-hint))]
         [:th (tr/tr @locale '(:read-books :num-pages-hint))]
         [:th (tr/tr @locale '(:read-books :year-hint))]
         [:th (tr/tr @locale '(:read-books :month-hint))]]]
       [:tbody
        (for [book @books]
          (let [name (:bookname book)
                num-pages (:num-pages book)
                year (:year book)
                month (:month book)]
          [:tr
           [:td name]
           [:td num-pages]
           [:td year]
           [:td month]]))]])))
