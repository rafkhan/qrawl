(ns qrawl.clojure-scrape
  (:require [clojure.core.async :as async :refer [go >! <!]]
            [net.cgrand.enlive-html :as html]
            [qrawl.core :as qrawl]))


(defn clojure-crawler 
  [dom]
  (let [title (html/select dom [:title])
        anchors (html/select dom [:a])]
    (println title)
    (println anchors)
    (map (fn [x]
           (:href
             (:attrs
               (first x))))
         anchors)))


; I'm going to have to change the name, lel
(clojure-crawler (qrawl/fetch-url "http://clojure.github.io/"))
