(ns qrawl.core
  (:require [clojure.core.async :as async :refer [go go-loop >! <! chan]]))

(def link-queue (chan 10000))
(def work-queue (chan 10))

(defn load-urls
  [urls]
  (go
    (doseq [url urls]
      (>! link-queue url))))

(defn crawl
  "Crawls urls, executing handler on each page.
  
  handler should return a vector of URLs that will
  continuously be fed into consumers that execute handler"
  [urls handler]

  (load-urls urls)

  (go (doseq [x (range 1000)]
    (>! work-queue [x])))

  (go
    (while true
      (let [result-set (<! work-queue)] ;; limits workers
        (load-urls result-set)
        (go (handler (<! link-queue)))))))


(defn x [y]
  (println "got" y)
  (Thread/sleep (rand-int 1000))
  (go (>! work-queue [:a :b :c :d :e :f])))

(crawl ["asd" "qwe"] x)


