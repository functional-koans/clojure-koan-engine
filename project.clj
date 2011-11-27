(defproject koan-engine "0.1.0"
  :description "Koan Engine for Clojure projects."
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [cascalog "1.8.4"]
                 [fresh "1.0.2"]
                 [jline "0.9.94" :exclusions [junit]]]
  :dev-dependencies [[org.apache.hadoop/hadoop-core "0.20.2-dev"]
                     [swank-clojure "1.4.0-SNAPSHOT"
                      :exclusions [org.clojure/clojure]]]
  :main koan-engine.runner
  :koan {:koan-root "src/koans"
         :dojo-resource "dojo.clj"
         :koan-resource "koans.clj"})
