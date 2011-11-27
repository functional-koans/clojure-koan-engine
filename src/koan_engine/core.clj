(ns koan-engine.core
  (:require [koan-engine.util :as u]))

(def __ :fill-in-the-blank)
(def ___ (fn [& args] __))

;; TODO: Place these in a required ns:
;; [cascalog.testing :only (test?-)]
;; [cascalog.util :only (defalias)]

(defmacro meditations [& forms]
  (let [pairs (partition 2 forms)
        tests (map (fn [[doc# code#]]
                     `(u/fancy-assert ~code# ~doc#))
                   pairs)]
    `(do ~@tests)))
