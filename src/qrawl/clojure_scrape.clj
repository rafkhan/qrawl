(ns qrawl.clojure-scrape
  (:require [clojure.core.async :as async :refer [go >! <!]]
            [net.cgrand.enlive-html :as html]
            [qrawl.core :as qrawl]))


(defn extract-links
  [dom]
  (map #(-> % :attrs :href)
       (html/select dom [:a])))

(defn extract-title
  [dom]
  (:content
    (first
      (html/select dom [:title]))))

(defn clojure-crawler 
  [dom]
  (let [title (extract-title dom)
        links (extract-links dom)]
    (println title)
    (qrawl/terminate links)))

(qrawl/crawl clojure-crawler "http://clojure.github.io/")


; I'm going to have to change the name, lel
#_(clojure-crawler (qrawl/fetch-url "http://clojure.github.io/"))
