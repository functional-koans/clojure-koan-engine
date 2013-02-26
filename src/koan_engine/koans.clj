(ns koan-engine.koans
  (:use [clojure.java.io :only [file resource]])
  (:require [koan-engine.util :as u]))

;; TODO: Proper koan validation. Accept the path as an argument.
(defn ordered-koans [answer-path]
  (map first (u/try-read (.getPath (resource answer-path)))))

(defn ordered-koan-paths [koan-root answer-path]
  (map (fn [koan-name]
         (.getCanonicalPath (file koan-root (str koan-name ".clj"))))
       (ordered-koans answer-path)))

(defn among-paths? [files]
  (into #{} (map #(.getCanonicalPath %) files)))

(defn next-koan-path [koan-path-seq last-koan-path]
  (loop [[this-koan & more :as koan-paths] koan-path-seq]
    (when (seq more)
      (if (= last-koan-path this-koan)
        (first more)
        (recur more)))))

(defn tests-pass? [dojo-path file-path]
  (u/with-dojo [dojo-path]
    (print "Considering" (str file-path "..."))
    (flush)
    (try (load-file file-path)
         (do (println) true)
         (catch Exception e
           (println)
           (let [actual-error (or (.getCause e) e)
                 message (or (.getMessage actual-error)
                             (.toString actual-error))
                 ; TODO: use ex-info or something to clean this up, once we're
                 ; upgraded to clojure 1.4+
                 line (when-let [groups (first (re-seq #"^\[LINE (\d+)\] "
                                                       message))]
                        (last groups))]
             (println "\nNow meditate upon"
                      (str file-path
                           (when line (str ":" line))))
             (println "---------------------")
             (println "Assertion failed!")
             (println (.replaceFirst
                        (.replaceFirst message "^Assert failed: " "")
                        "^\\[LINE \\d+\\] " "")))
           false))))

(defn namaste []
  (println "\nYou have achieved clojure enlightenment. Namaste."))
