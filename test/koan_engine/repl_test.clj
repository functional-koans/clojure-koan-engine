(ns koan-engine.repl-test
  (:use [clojure.test]
        [koan-engine.repl]))

(def resource-path "test/resource/repl")
(def expected-ns '(koans.test01 koans.test02, koans.test03))

(deftest koan-file-ns-test

  (is (= (sort (get-koan-filenames resource-path))
         (sort '("test01.clj" "test02.clj" "test03.clj"))))

  (is (= (sort (get-koan-ns-from-filenames (get-koan-filenames resource-path)))
         (sort expected-ns))))
