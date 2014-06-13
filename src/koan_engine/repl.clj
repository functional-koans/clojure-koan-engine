(ns koan-engine.repl
  (:use [fresh.core :only [clj-files-in freshener]]
        [clojure.java.io :only [file]]
        [koan-engine.core]
        [koan-engine.runner]))

(defn get-koan-filenames
  "Returns a list of koan file names or an empty list on failure."
  [koan-root]
  (try
    (map #(.getName %) (clj-files-in (file koan-root)))
    (catch Exception e '())))

(defn get-koan-ns-strings-from-filenames
  "A helper function for get-koan-ns-from filenames.
  Returns a list of namespace string associated to a list of file names."
  [filenames]
  (map #(str "koans." %)
       (map #(clojure.string/replace % #"_" "-")
            (map #(clojure.string/replace % #"\.clj" "") filenames))))

(defn get-koan-ns-from-filenames
  "Returns a list of namespace symbols associated to a list of file names."
  [filenames]
  (map #(symbol %) (get-koan-ns-strings-from-filenames filenames)))

(def koans-filenames (get-koan-filenames (:koan-root default-koan-map)))
(def koans-ns (get-koan-ns-from-filenames koans-filenames))

(defmacro do-ns-koans
  "Wraps multiple (ns) expressions for the koans with a (do) form,
  the result of which is an evaluation like the following expression:

  (do
    (ns name-1 (:use koan-engine.core))
    ; ...
    (ns name-N (:use koan-engine.core)))"
  []
  (cons 'do
        (for [n koans-ns]
          `(ns ~n (:use koan-engine.core)))))
