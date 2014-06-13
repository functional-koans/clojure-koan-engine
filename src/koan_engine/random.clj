(ns koan-engine.random
  (:require [koan-engine.koans :refer [ordered-koans]]
            [koan-engine.util :as u]
            [clojure.java.io :refer [file]]
            [clojure.string :as s]))

(def success-quotes
  ["Hack and be merry!"
   "You're bound to be unhappy if you optimize everything. -Donald Knuth"
   "Lisp isn't a language, it's a building material. -Alan Kay"
   "The key to performance is elegance. -Jon Bentley and Doug McIlroy"
   "Code long and prosper!"])

(def failure-quotes
  ["If at first you don't succeed; call it version 0.1"
   "I would love to change the world, but they won't give me the source code."
   "Losing is fun. -Dwarf Fortress"
   "Deleted code is debugged code. -Jeff Sickel"])

(defn- solve-koan [koan]
  (println)
  (print "Your solution (using \";\" as a delimiter): ")
  (flush)
  (let [solutions (s/split (read-line) #";")
        filled-koan (reduce (fn [k s] (s/replace-first k #"\b___?\b" s)) koan solutions)]
    (println)
    (println "The koan, filled in with your solution:\n" filled-koan)
    (if
      (or (try
            (load-string filled-koan)
            (catch AssertionError e (prn e) false)
            (catch Exception e (prn e) false))
          (= (symbol "skip-koan") (first solutions))
          (= "skip-koan" (first solutions))
          (= :skip-koan (first solutions)))
      (println "Success! " (-> success-quotes shuffle first)
               "\nNext koan coming right up.\n")
      (do
        (println "Oh no, failed!" (-> failure-quotes shuffle first) "\nTry again!")
        (solve-koan koan)))))

(defmacro random-koan [prefix-forms & forms]
  (let [koan (->> forms
                  (partition 2)
                  shuffle
                  first)]
    ((fn [[doc# code#]]
       (println "-----\nSolve the following koan:")
       (println doc#)
       (println code#)
       (solve-koan (str prefix-forms code#)))
     koan)))

(defn- do-run [opts]
  (let [{:keys [dojo-resource koan-resource koan-root]} opts
        koan-file (-> koan-resource
                      ordered-koans
                      shuffle
                      first)
        form (s/replace (slurp (file koan-root (str koan-file ".clj")))
                        #"\(ns" "(comment")
        form-wo-meditations (-> form
                                (s/replace #"\(meditations[\s\S]*" "")
                                (s/replace "\"" "\\\""))]
    (println "\nMeditating upon: " koan-file)
    (println "The contex of the koan:\n"
             (s/trim form-wo-meditations)
             "\n")
    (u/with-dojo [dojo-resource]
      (-> (s/replace form "(meditations" (format "(random-koan \"%s\""
                                                 form-wo-meditations))
          load-string)))
  (do-run opts))

(defn runner [opts]
  (println
    "You might never reach enlightement, but you can play endlessly until you get bored and start writing your own koans!")
  (println)
  (println
    "Provide your answer or \"skip-koan\" if you want to skip the current one. You will automatically get the next random koan on success. Use \";\" as delimiter if you need to provide multiple answers. If you need to provide multiple answers but only some of those have effect on the given koan you have to provide your solution in the right place; however, what you type for the rest is not important, you can keep them blank or use :skip.")
  (println "-----")
  (do-run opts))
