(ns koan-engine.freshness
  (:use [fresh.core :only [clj-files-in freshener]]
        [clojure.java.io :only [file]]
        [koan-engine.koans :only [among-paths?
                                  namaste
                                  next-koan-path
                                  ordered-koan-paths
                                  tests-pass?]])
  (:import [java.util.concurrent ScheduledThreadPoolExecutor TimeUnit]))

(defn files-to-keep-fresh [koan-root]
  (constantly
   (clj-files-in (file koan-root))))

(defn report-refresh [opt-map report]
  (when-let [refreshed-files (seq (:reloaded report))]
    (let [{:keys [dojo-resource koan-resource koan-root]} opt-map
          path-seq    (ordered-koan-paths koan-root koan-resource)
          these-koans (filter (among-paths? refreshed-files)
                              path-seq)]
      (when (every? (partial tests-pass? dojo-resource)
                    these-koans)
        (if-let [next-koan-file (file (next-koan-path path-seq
                                                      (last these-koans)))]
          (report-refresh opt-map {:reloaded [next-koan-file]})
          (namaste))))
    (println))
  :refreshed)

(defn refresh!
  [{:keys [koan-root] :as opts}]
  (freshener (files-to-keep-fresh koan-root)
             (partial report-refresh opts)))

(def scheduler (ScheduledThreadPoolExecutor. 1))

(defn setup-freshener [koan-map]
  (println "Starting auto-runner...")
  (.scheduleWithFixedDelay scheduler
                           (refresh! koan-map)
                           0 500 TimeUnit/MILLISECONDS)
  (.awaitTermination scheduler Long/MAX_VALUE TimeUnit/SECONDS))
