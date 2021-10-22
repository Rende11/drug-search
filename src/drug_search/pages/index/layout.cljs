(ns drug-search.pages.index.layout
  (:require [re-frame.core :as rf]
            [drug-search.pages.index.model :as model]))


(defn view []
  [:form {:on-submit (fn [e]
                       (.preventDefault e))}
   [:label {:for "drug-input"} "Drug name"]
   [:input {:id "drug-input"
            :type "text"
            :value @(rf/subscribe [::model/search-value])
            :on-change #(rf/dispatch [::model/set-search-value (-> % .-target .-value)])}]
   [:input {:type "submit" :value "Search"}]])
