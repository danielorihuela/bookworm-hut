(ns bookworm-hut-frontend.components.months-chart.views
  (:require
   [re-frame.core :as re-frame]
   [reagent.core :as reagent]
   [bookworm-hut-frontend.subs :as subs]
   ["chart.js/auto" :as chartjs]
   [bookworm-hut-frontend.translations :as tr]))

(defn pages-by-month [books year]
  (into (sorted-map-by <)
        (persistent!
         (reduce
          (fn [counts x]
            (if (= (:year x) year)
              (assoc! counts (:month x)
                      (+ (get counts (:month x) 0) (:num-pages x)))
              counts))
          (transient {1 0 2 0 3 0 4 0 5 0 6 0 7 0 8 0 9 0 10 0 11 0 12 0}) books))))


(defn months-chart-chartjs
  [books]
  (let [locale (re-frame/subscribe [::subs/locale])
        year (int (.getFullYear (js/Date.)))
        labels [1 2 3 4 5 6 7 8 9 10 11 12]
        data (into [] (vals (pages-by-month books year)))
        context (.getContext (.getElementById js/document "month-chart") "2d")
        chart-data {:type "line"
                    :data {:labels labels
                           :datasets [{:data data 
                                       :label (tr/tr @locale '(:stats :per-month))
                                       :borderColor "rgb(75, 192, 192)"
                                       :tension 0.5}]}}]
      (chartjs/Chart. context (clj->js chart-data))))

(defn months-chart
  [width height books]
    (reagent/create-class
     {:display-name        "Month Chart"
      :reagent-render      (fn []
                             [:canvas {:id "month-chart" :width width :height height}])
      :component-did-mount #(months-chart-chartjs books)
      }))
