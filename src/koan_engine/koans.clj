(ns koan-engine.koans
  (:use [clojure.java.io :only [file resource]])
  (:require [koan-engine.util :as u]))

;; Add more koan namespaces here.
(defn ordered-koans []
  (if-let [conf-path (resource "job-conf.clj")]
    (try (let [conf (-> conf-path slurp read-string)]
           (u/safe-assert (map? conf)
                          "job-conf.clj must produce a map of config parameters!")
           conf)
         (catch RuntimeException e
           (throw (Exception. "Error reading job-conf.clj!\n\n") e)))
    {}))

(def ordered-koans
  ["tuples"])

(defn ordered-koan-paths  []
  (map (fn [koan-name]
         (.getCanonicalPath (file "src" "koans" (str koan-name ".clj"))))
       ordered-koans))

(defn among-paths? [files]
  (into #{} (map #(.getCanonicalPath %) files)))

(defn next-koan-path [last-koan-path]
  (loop [[this-koan & more :as koan-paths] (ordered-koan-paths)]
    (when (seq more)
      (if (= last-koan-path this-koan)
        (first more)
        (recur more)))))

(defn tests-pass? [file-path]
  (binding [*ns* (create-ns (gensym "koans"))]
    (refer 'clojure.core)
    (use 'cascalog.api) ;; TODO: Move out other namespace.
    (use '[koan-engine.core :only [meditations ?= __ ___]])
    (try (load-file file-path)
         true
         (catch Exception e
           (println)
           (println "Problem in" file-path)
           (println "---------------------")
           (println "Assertion failed!")
           (let [actual-error (or (.getCause e) e)
                 message (or (.getMessage actual-error)
                             (.toString actual-error))]
             (println (.replaceFirst message "^Assert failed: " "")))
           false))))

(defn namaste []
  (println "\nYou have achieved clojure enlightenment. Namaste."))
