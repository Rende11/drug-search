(ns drug-search.sparql
  (:require [goog.string :as gstring]
            [goog.string.format]))

(def base-url "https://query.wikidata.org/sparql")

(defn drug-query [drug]
  (gstring/urlEncode
   (gstring/format
    "SELECT distinct
     ?disease ?diseaseLabel ?desc
    
     WHERE
     {
       {
         ?drug wdt:P31/wdt:P279* wd:Q12140. 
         ?disease wdt:P31/wdt:P279* wd:Q12136.
         ?disease wdt:P2176 ?drug.
         ?disease schema:description ?desc
       } 
       UNION 
       {
         ?drug wdt:P31/wdt:P279* wd:Q12140.
         ?drug wdt:P3781/wdt:P279* ?ingredient.
         ?disease wdt:P31/wdt:P279* wd:Q12136.
         ?disease wdt:P2176 ?ingredient.
         ?disease schema:description ?desc
       }
       
       FILTER (lang(?desc) = \"en\")                 
       
       SERVICE wikibase:label { bd:serviceParam wikibase:language \"en\". } 
       SERVICE wikibase:mwapi {
         bd:serviceParam wikibase:api \"EntitySearch\" .
         bd:serviceParam wikibase:endpoint \"www.wikidata.org\" .
         bd:serviceParam wikibase:language \"en\" .
         bd:serviceParam mwapi:search \"%s\" .
         bd:serviceParam mwapi:language \"en\" .
         ?drug wikibase:apiOutputItem mwapi:item .
       }
     }"
    drug)))
