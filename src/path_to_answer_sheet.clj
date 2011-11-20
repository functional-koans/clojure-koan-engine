(ns path-to-answer-sheet
  (:use cascalog.api
        [runner.koans :only [ordered-koans]]
        [path-to-enlightenment :only [meditations fancy-assert ?= __ ___]]
        [clojure.string :only [join split trim] :as string]))

(def answers
  {"tuples" {"__" [
                   [["truth."]]
                   [[1]]
                   ]}
   })

(defn replace-with [s k replacements]
  (let [unreplaced-texts (split s (re-pattern (str "\\b" k "\\b")))]
    (join
      (butlast
        (interleave
          unreplaced-texts
          (concat (map pr-str replacements) (repeat k)))))))

;;
(defn koan-text [koan]
  (slurp (str "src/koans/" koan ".clj")))

(defn answers-for [koan sym]
  ((answers koan {}) sym []))

(defn fill-in-answers [text koan sym]
  (replace-with text sym (answers-for koan sym)))

(defn print-non-failing-error [koan]
  (println (str "\n" koan ".clj is passing without filling in the blanks")))

(defmacro ensure-failure [& forms]
  (let [pairs (partition 2 forms)
        tests (map (fn [[doc# code#]]
                     `(if (try
                           (fancy-assert ~code# ~doc#)
                           false
                           (catch AssertionError e# true)
                           (catch Exception e# true))
                       :pass
                       (throw (AssertionError. (pr-str ~doc# ~code#)))))
                   pairs)]
    `(do ~@tests)))

(defn ensure-failing-without-answers []
  (if (every?
        (fn [koan]
          (let [form (koan-text koan)
                form (string/replace form "(meditations" "(ensure-failure")
                fake-err (java.io.PrintStream. (java.io.ByteArrayOutputStream.))
                real-err System/err
                result (try
                         (load-string form)
                         true
                         (catch AssertionError e (prn e) false)
                         (catch Exception e (prn e) false))]
            (if result
              :pass
              (print-non-failing-error koan))))
        ordered-koans)
    (println "\nTests all fail before the answers are filled in.")))

(defn ensure-passing-with-answers []
  (try
    (dorun (map
            (fn [koan]
              (load-string
               (-> (koan-text koan)
                   (fill-in-answers koan "__")
                   (fill-in-answers koan "___"))))
            ordered-koans))
    (println "\nAll tests pass after the answers are filled in.")

    (catch Exception e
      (println "\nAnswer sheet fail: " e)
      (.printStackTrace e)
      (println "Answer sheet fail"))))

(defn run []
  (ensure-failing-without-answers)
  (ensure-passing-with-answers))
