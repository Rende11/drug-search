(ns drug-search.pages.index.model
  (:require [re-frame.core :as rf]))


(rf/reg-event-fx
 ::set-search-value
 (fn [{db :db} [_ value]]
   {:db (assoc-in db [:form :drug-name :value] value)}))

(rf/reg-sub
 ::search-value
 (fn [db _]
   (get-in db [:form :drug-name :value])))
