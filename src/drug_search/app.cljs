(ns drug-search.app
  (:require [reagent.dom :as rdom]
            [drug-search.pages.index.layout :as index]))


(defn app []
  [:div#app
   [index/view]])

(defn render []
  (rdom/render [app] (.getElementById js/document "root")))

(defn ^:export ^:dev/after-load init []
  (render))
