(ns koan-engine.runner
  (:use [koan-engine.freshness :only [setup-freshener]])
  (:require [koan-engine.util :as u]
            [koan-engine.checker :as checker]
            [koan-engine.random :as random]))

(def default-koan-map
  {:koan-root "src/koans"
   :dojo-resource "dojo.clj"
   :koan-resource "koans.clj"})

;; TODO: Run validations on koan-map.
;; Do all resources exist? Does this matter?
(defn exec [task]
  (u/require-version (u/parse-required-version))
  (let [koan-map (merge default-koan-map
                        (:koan (u/read-project)))]
    (case task
      "run"  (setup-freshener koan-map)
      "test" (checker/test koan-map)
      "random" (random/runner koan-map))))
