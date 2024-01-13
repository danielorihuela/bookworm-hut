(ns bookworm-hut-frontend.components.years-chart.views
  (:require
   [re-frame.core :as re-frame]
   [reagent.core :as reagent]
   [bookworm-hut-frontend.subs :as subs]
   ["chart.js/auto" :as chartjs]
   [bookworm-hut-frontend.translations :as tr]))

(defn pages-by-year [books]
  (into (sorted-map-by <)
        (persistent!
         (reduce
          (fn [counts x]
            (assoc! counts (:year x)
                    (+ (get counts (:year x) 0) (:num-pages x))))
          (transient {}) books))))

(defn years-chart-chartjs
  [books]
  (let [locale (re-frame/subscribe [::subs/locale])
        data-labels (into [] (keys (pages-by-year books)))
        data-values (into [] (vals (pages-by-year books)))
        context (.getContext (.getElementById js/document "year-chart") "2d")
        chart-data {:type "line"
                    :data {:labels data-labels
                           :datasets [{:data data-values
                                       :label (tr/tr @locale '(:stats :per-year))
                                       :borderColor "rgb(75, 192, 192)"
                                       :tension 0.5}]}}]
      (chartjs/Chart. context (clj->js chart-data))))

(defn years-chart
  [width height books]
  (reagent/create-class
    {:component-did-mount #(years-chart-chartjs books)
     :display-name        "Year Chart"
     :reagent-render      (fn []
                            [:canvas {:id "year-chart" :width width :height height}])}))
