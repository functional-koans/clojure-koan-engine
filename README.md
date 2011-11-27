# Koan Engine

* A library that makes "meditations", etc available
* A lein plugin -- `lein-koan` -- that pulls this library into a project and makes two commands available:

     lein koan run
     lein koan test

* Project template with [lein-newnew](https://github.com/Raynes/lein-newnew) to allow for creation of new projects. The template will include the lein plugin as a dev-dependency, a few required resource files (discussed below), an example koan file and a special key-value pair for koans in `project.clj`.

### project.clj entries:
  
These need better description; these can be set for every koan project.

    :koan {:koan-root "src/koans"     ;; koan files live at this root
           :dojo-resource "dojo.clj"      ;; The forms in resources/dojo.clj are evaluted before every koan.
           :koan-resource "koans.clj"}    ;; answers located at resources/koans.clj 

### resources/dojo.clj

Projects need to make their APIs available; this file should contain either forms to be evaluated before each koan, like this:

    (use 'cascalog.api)
    (use '[cascalog.testing :only (test?-)])

### resources/koans.clj

This file should contain a vector of 2-vectors, with each containing a koan name and map of answers:

    [["tuples" {"__" [
                     [["truth."]]
                     [[1]]
                     ]}]
     ["ops/mapper" {"__" [
                         [[true answer 2!"]]
                         ]}]]

`lein koan test` should validate these formats.

## Notes on lein-koan and koan-template

TODO: Document these projects.

### License

The use and distribution terms for this software are covered by the
Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
which can be found in the file epl-v10.html at the root of this distribution.
By using this software in any fashion, you are agreeing to be bound by
the terms of this license.

