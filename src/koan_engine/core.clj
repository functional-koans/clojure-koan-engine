(ns koan-engine.core
  (:use [cascalog.testing :only (test?-)]
        [cascalog.util :only (defalias)])
  (:require [koan-engine.util :as u]))

(def __ :fill-in-the-blank)
(def ___ (fn [& args] __))

;; TODO: Place these in a required ns:
;; [cascalog.testing :only (test?-)]
;; [cascalog.util :only (defalias)]

;; TODO: Move out!
;; More concise for koans.
(defalias ?= test?-)

(defmacro meditations [& forms]
  (let [pairs (partition 2 forms)
        tests (map (fn [[doc# code#]]
                     `(u/fancy-assert ~code# ~doc#))
                   pairs)]
    `(do ~@tests)))
