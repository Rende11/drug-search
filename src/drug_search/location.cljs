(ns drug-search.location
  (:require [re-frame.core :as rf]
            [clojure.string :as str]))


(rf/reg-cofx
 ::hash
 (fn [cofx _]
   (assoc cofx :hash (let [l (.-location js/document)]
                       (.-hash l)))))

(rf/reg-fx
 ::push-hash
 (fn [value]
   (.pushState js/history nil nil value)))

(def loc-path [:location :hash])

(rf/reg-event-fx
 ::redirect
 (fn [{db :db} [_ hash]]
   {:db (assoc-in db loc-path hash)
    ::push-hash hash}))

(rf/reg-sub
 ::hash
 (fn [db _]
   (get-in db loc-path)))

(defn ->hash [value]
  (str "#" value))

(defn hash->value [value]
  (str/replace value #"#" ""))
