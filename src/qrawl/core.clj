(ns qrawl.core
  (:require [clojure.core.async :as async :refer [go >! >!! <! chan]]
            [net.cgrand.enlive-html :as html]))

(def WORKER-COUNT 100)

(def link-queue (chan 10000))
(def work-queue (chan WORKER-COUNT))

(defn fetch-url [url]
  (html/html-resource (java.net.URL. url)))

(defn- load-urls
  [urls]
  (go
    (doseq [url urls]
      (>! link-queue url))))

(defn terminate
  [urls]
  (load-urls urls)
  (>!! work-queue true))

(defn- run-handler
  [handler url]
  (try 
    (let [html (fetch-url url)]
      (handler html))

    (catch Exception e
      (println e)
      (terminate []))))

(defn crawl
  "Crawls urls, executing handler on each page.
  
  handler should return a vector of URLs that will
  continuously be fed into consumers that execute handler"
  [handler url]

  (>!! link-queue url)
  (doseq [x (range WORKER-COUNT)]
    (>!! work-queue true))

  (go
    (while true
      (<! work-queue) ;; limits workers
      (go (run-handler handler (<! link-queue))))))

