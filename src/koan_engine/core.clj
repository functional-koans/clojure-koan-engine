(ns koan-engine.core
  (:require [koan-engine.util :as u]))

(def __ :fill-in-the-blank)
(def ___ (fn [& args] __))

(defmacro meditations [& forms]
  (let [pairs (partition 2 forms)
        tests (map (fn [[doc# code#]]
                     `(u/fancy-assert ~code# ~doc#))
                   pairs)]
    `(do ~@tests)))
