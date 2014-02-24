(ns koan-engine.core
  (:require [koan-engine.util :as u]))

(def __ :fill-in-the-blank)
(def ___ (fn [& args] __))

(defn ensure-valid-meditation [doc-expression-pairs]
  (doseq [[doc expression] doc-expression-pairs]
    (when-not (string? doc)
      (throw (ex-info (str "Meditations must be alternating doc/expression pairs\n"
                           "Expected " doc " to be a documentation string")
                      {:line (:line (meta doc))}))))
  doc-expression-pairs)

(defmacro meditations [& forms]
  (let [pairs (ensure-valid-meditation (partition 2 forms))
        tests (map (fn [[doc# code#]]
                     `(u/fancy-assert ~code# ~doc#))
                   pairs)]
    `(do ~@tests)))
