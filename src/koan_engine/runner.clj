(ns koan-engine.runner
  (:use [koan-engine.freshness :only [setup-freshener]])
  (:require [koan-engine.util :as u]
            [koan-engine.checker :as checker]))

;; TODO: Incorporate each of these custom options in the runner.
;; koan-root:       check!
;; dojo-resource:   NOPE
;; answer-resource: NOPE
;; koan-resource:   NOPE

(def default-koan-map
  {:koan-root "src/koans"
   :dojo-resource "dojo.clj"
   :answer-resource "answers.clj"
   :koan-resource "koans.clj"})

;; TODO: Run validations on koan-map.
;; Do all resources exist?
(defn -main [task]
  (u/require-version (u/parse-required-version))
  (let [koan-map (merge default-koan-map
                        (:koan (u/read-project)))]
    (case task
      "run"  (setup-freshener koan-map)
      "test" (checker/test koan-map))))
