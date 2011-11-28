(ns koan-engine.checker
  (:refer-clojure :exclude [test])
  (:use [clojure.java.io :only (file resource)]
        [koan-engine.koans :only [ordered-koans]]
        [clojure.string :only [join split trim] :as string])
  (:require [koan-engine.util :as u]))

(defn mk-answers [koan-resource]
  (into {} (u/try-read (resource koan-resource))))

(defn replace-with [s k replacements]
  (let [unreplaced-texts (split s (re-pattern (str "\\b" k "\\b")))]
    (join (butlast
           (interleave unreplaced-texts
                       (concat (map pr-str replacements)
                               (repeat k)))))))

(defn koan-text [koan-root koan]
  (slurp (file koan-root (str koan ".clj"))))

(defn answers-for [koan-resource koan sym]
  (let [answers (mk-answers koan-resource)]
    ((answers koan {}) sym [])))

(defn fill-in-answers [koan-resource text koan sym]
  (replace-with text sym (answers-for koan-resource koan sym)))

(defn print-non-failing-error [koan]
  (println (str "\n" koan ".clj is passing without filling in the blanks")))

(defmacro ensure-failure [& forms]
  (let [pairs (partition 2 forms)
        tests (map (fn [[doc# code#]]
                     `(if (try
                            (u/fancy-assert ~code# ~doc#)
                            false
                            (catch AssertionError e# true)
                            (catch Exception e# true))
                        :pass
                        (throw (AssertionError. (pr-str ~doc# ~code#)))))
                   pairs)]
    `(do ~@tests)))

(defn ensure-failing-without-answers [opts]
  (let [{:keys [dojo-resource koan-resource koan-root]} opts]
    (when (every?
           (fn [koan]
             (let [form (koan-text koan-root koan)
                   form (string/replace form "(meditations" "(ensure-failure")
                   fake-err (java.io.PrintStream. (java.io.ByteArrayOutputStream.))
                   real-err System/err
                   result (try (u/with-dojo [dojo-resource]
                                 (load-string form))
                               true
                               (catch AssertionError e (prn e) false)
                               (catch Exception e (prn e) false))]
               (if result
                 :pass
                 (print-non-failing-error koan))))
           (ordered-koans koan-resource))
      (println "\nTests all fail before the answers are filled in."))))

(defn ensure-passing-with-answers [opts]
  (let [{:keys [dojo-resource koan-resource koan-root]} opts
        filler (partial fill-in-answers koan-resource)]
    (try (dorun
          (map
           (fn [koan]
             (u/with-dojo [dojo-resource]
               (load-string (-> (koan-text koan-root koan)
                                (filler koan "__")
                                (filler koan "___")))))
           (ordered-koans koan-resource)))
         (println "\nAll tests pass after the answers are filled in.")
         (catch Exception e
           (println "\nAnswer sheet fail: " e)
           (.printStackTrace e)
           (println "Answer sheet fail")))))

(defn test [opts]
  (ensure-failing-without-answers opts)
  (ensure-passing-with-answers opts))
