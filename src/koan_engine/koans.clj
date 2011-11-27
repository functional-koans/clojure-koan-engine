(ns koan-engine.koans
  (:use [clojure.java.io :only [file resource]])
  (:require [koan-engine.util :as u]))

;; TODO: Proper koan validation. Accept the path as an argument.
(defn ordered-koans []
  (if-let [conf-path (resource "koans.clj")]
    (try (let [conf (-> conf-path slurp read-string)]
           (u/safe-assert (map? conf) "Koans aren't valid!")
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

(defn tests-pass? [dojo-path file-path]
  (u/with-dojo [dojo-path]
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
