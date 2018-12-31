(ns koan-engine.koans
  (:use [clojure.java.io :only [file resource]])
  (:require [koan-engine.util :as u]
            [clojure.string :as str]))

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

(defn report-error [file-path line error]
  (let [message (or (.getMessage error) (.toString error))]
    (println "\nNow meditate upon"
             (str (last (str/split file-path #"/"))
                  (when line (str ":" line))))
    (println "---------------------")
    (println "Assertion failed!")
    (println (.replaceFirst message "^Assert failed: " ""))))

(defn tests-pass? [dojo-path file-path]
  (u/with-dojo [dojo-path]
    (flush)
    (try (load-file file-path)
         true
         (catch clojure.lang.Compiler$CompilerException e
           (let [cause (.getCause e)]
             (report-error file-path (:line (ex-data cause)) cause))
           false)
         (catch clojure.lang.ExceptionInfo ei
           (report-error file-path (:line (ex-data ei)) ei)
           false)
         (catch Throwable e
           (report-error file-path nil e)
           false))))

(defn namaste []
  (println "\nYou have achieved clojure enlightenment. Namaste."))
