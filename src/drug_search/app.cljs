(ns drug-search.app
  (:require [reagent.dom :as rdom]
            [drug-search.pages.index.layout :as index]
            [re-frame.core :as rf]
            [drug-search.location :as loc]
            [drug-search.pages.index.model :as index-model]))

(rf/reg-event-fx
 ::initialize
 [(rf/inject-cofx ::loc/hash)]
 (fn [{db :db hash :hash} _]
   (if (#{nil "" "/"} hash)
     {:db db}
     {:db db
      :dispatch [::index-model/search hash]})))



(defn app []
  [:div#app.hero
   [:div.hero-body
    [index/view]]])

(defn render []
  (rdom/render [app] (.getElementById js/document "root")))

(defn ^:export ^:dev/after-load init []
  (rf/dispatch-sync [::initialize])
  (render))
