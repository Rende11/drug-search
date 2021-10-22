(ns drug-search.pages.index.layout
  (:require [re-frame.core :as rf]
            [drug-search.pages.index.model :as model]))

(defn search-form []
  [:form {:on-submit (fn [e]
                       (.preventDefault e)
                       (rf/dispatch [::model/search]))}
   [:label {:for "drug-input"} "Drug name"]
   [:input {:id "drug-input"
            :required true
            :type "text"
            :value @(rf/subscribe [::model/search-value])
            :on-change #(rf/dispatch [::model/set-search-value (-> % .-target .-value)])}]
   [:input {:type "submit" :value "Search"}]])


(defn search-results []
  [:div.search-results
   (doall
    (for [{:keys [label desc uri] :as item} @(rf/subscribe [::model/search-result])]
      [:div.search-item {:key uri}
       [:div.search-item-content.label label]
       [:div.search-item-content.desc desc]
       [:div.search-item-content.uri uri]]))])


(defn view []
  [:<>
   [search-form]
   [search-results]])
