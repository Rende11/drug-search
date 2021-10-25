(ns drug-search.pages.index.layout
  (:require [re-frame.core :as rf]
            [drug-search.pages.index.model :as model]
            [clojure.string :as str]
            [drug-search.location :as loc]))

(defn search-form []
  (let [search-value @(rf/subscribe [::model/search-value])]
    [:form.box {:on-submit (fn [e]
                             (.preventDefault e)
                             (rf/dispatch [::model/search search-value]))
                :autoComplete "off"}
     [:div.field.is-grouped
      [:div.control.is-expanded
       [:input.input.is-info
        {:id "drug-input"
         :placeholder "place drug name here..."
         :required true
         :type "text"
         :value search-value
         :on-change #(rf/dispatch [::model/set-search-value (-> % .-target .-value)])}]]
      [:div.control
       [:input.button.has-text-weight-medium.is-info
        {:disabled (= :loading @(rf/subscribe [::model/search-state]))
         :type "submit"
         :value "Search"}]]]]))

(defn example-list []
  [:div.examples.is-flex.is-justify-content-space-between.is-align-items-baseline
   (doall
    (for [drug ["Gabapentin" "Ambroxol" "Tylenol" "Albuterol" "Lisinopril"]]
      [:div.is-clickable.box {:key drug
                               :on-click #(rf/dispatch [::model/search drug])} drug]))])


(defn search-results []
  (let [results @(rf/subscribe [::model/search-result])
        state @(rf/subscribe [::model/search-state])
        hash @(rf/subscribe [::loc/hash])]
    [:div.search-results
     (cond
       (= state :loading)
       [:progress.progress.is-small.is-info.mt-6 {:max "100"}]

       (= state :error)
       [:article.message.is-warning.mt-6
        [:div.message-body
         "Something went wrong, please try again later"]]

       (or (empty? hash) (= hash "/"))
       [:div.mt-6
        [:article.message.is-info
         [:div.message-body
          "Try to search something like this:"]]
        [example-list]]
       

       (empty? results)
       [:div.mt-6
        [:article.message.is-info.mt-6
         [:div.message-body
          "Nothing was found, try something else:"]]
        [example-list]]

       :else
       [:div.search-items.mt-6.has-text-left
        (doall
         (for [{:keys [label desc uri] :as item} results]
           [:div.search-item.box.is-info {:key uri}
            [:div.search-item-content.label.is-flex.is-flex-direction-row.is-justify-content-space-between.is-align-items-baseline
             [:span (str/capitalize label)]
             [:a.is-clickable {:href uri :target "_blank"}
              [:i.fa.fa-external-link {:aria-hidden "true"}]]]
            [:div.search-item-content.desc.message-body (str/capitalize desc)]]))])]))


(defn view []
  [:div.column.has-text-centered.is-6.is-offset-3
   [:h1.title.is-clickable {:on-click #(rf/dispatch [::loc/redirect "/"])} "Drug search"]
   [:h2.subtitle.mt-3.mb-6 "Search the diseases that your medicine cures"]
   [search-form]
   [search-results]])
