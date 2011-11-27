(ns koan-engine.runner
  (:use [koan-engine.freshness :only [setup-freshener]])
  (:require [koan-engine.util :as u]
            [koan-engine.checker :as checker]))

;; TODO: Incorporate each of these custom options in the runner.
;; koan-root: check!
(def default-koan-map
  {:koan-root "src/koans"
   :dojo-resource "dojo"
   :answer-resource "answers"
   :koan-resource "koans"})

(defn -main [task]
  (u/require-version (u/parse-required-version))
  (let [koan-map (merge default-koan-map
                        (:koan (u/read-project)))]
    (case task
      "run"  (setup-freshener koan-map)
      "test" (checker/test koan-map))))
