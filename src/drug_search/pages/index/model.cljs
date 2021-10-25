(ns drug-search.pages.index.model
  (:require [re-frame.core :as rf]
            [clojure.string :as str]
            [day8.re-frame.http-fx]
            [drug-search.sparql :as sparql]
            [ajax.core :as ajax]
            [drug-search.location :as loc]))


(rf/reg-event-fx
 ::set-search-value
 (fn [{db :db} [_ value]]
   {:db (assoc-in db [:form :drug-name :value] value)}))

(defn get-search-value [db]
  (get-in db [:form :drug-name :value]))

(rf/reg-sub
 ::search-value
 (fn [db _]
   (get-search-value db)))

(defn normalize [string]
  (loc/hash->value (str/lower-case string)))

(rf/reg-event-fx
 ::search
 (fn [{db :db} [_ value]]
   (let [search-value (normalize value)]
     {:http-xhrio {:method          :post
                   :uri             sparql/base-url
                   :body            (str "query=" (sparql/drug-query search-value))
                   :response-format (ajax/json-response-format {:keywords? true})
                   :on-success      [::search-success]
                   :on-failure      [::search-fail]}
      :db (-> db
              (assoc-in [:form :drug-name :value] (loc/hash->value value))
              (assoc-in [:search :state] :loading)
              (assoc-in [:search :error] nil))
      :dispatch [::loc/redirect (loc/->hash search-value)]})))



(defn prepare-search-results [{results :results}]
  (map (fn [res]
         {:label (-> res :diseaseLabel :value)
          :desc (-> res :desc :value)
          :uri (-> res :disease :value)}) (:bindings results)))


(rf/reg-event-fx
 ::search-success
 (fn [{db :db} [_ body]]
   {:db (-> db
            (assoc-in [:search :success :raw] body)
            (assoc-in [:search :success :prepared] (prepare-search-results body))
            (assoc-in [:search :state] :done))}))

(rf/reg-event-fx
 ::search-fail
 (fn [{db :db} [_ body]]
   {:db (-> db
            (assoc-in [:search :state] :error)
            (assoc-in [:search :error] body))}))

(rf/reg-sub
 ::search-result
 (fn [db _]
   (get-in db [:search :success :prepared])))

(rf/reg-sub
 ::search-state
 (fn [db _]
   (get-in db [:search :state])))
