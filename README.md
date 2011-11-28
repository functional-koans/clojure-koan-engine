# Koan Engine

Clojure-Koan-Engine is the library behind [clojure-koans](https://github.com/functional-koans/clojure-koans). It exposes the `meditations` macro and makes it possible to write koans of this form for any Clojure project:

```clojure
(meditations
  "We shall contemplate truth by testing reality, via equality."
  (= __ true)

  "To understand reality, we must compare our expectations against reality."
  (= __ (+ 1 1)))
```
The student' job is to fill in the blanks in such a way that the code form throws no exceptions and returns a truthy value.

## Writing your own koans

Koans are a wonderful way to teach users by example. If you maintain or use a library in Clojure, consider putting together a custom Koan project! 

The easiest way to start writing koans for your library is to generate a new project with the `koan-template` leiningen plugin (source code [on github](https://github.com/functional-koans/koan-template). Make sure you have [Leiningen](http://github.com/technomancy/leiningen) installed, then run the following commands:

    lein plugin install lein-newnew 0.1.2   # Skip if you already have lein-newnew installed!
    lein plugin install koan-template 0.1.0
    lein new koan <your-project-name>
    cd <your-project-name>
    chmod +x script/*

The README of the resulting project will contain everything your users need to know to get started.


## Adding koan-engine to an existing project

You can also add koans to an existing library without interfering with your source tree. This is a wonderful way to introduce users to your source code.

To get started, add the `lein-koan` plugin (source code [on github](https://github.com/functional-koans/koan-template)) to your `:dev-dependencies` in `project.clj`:

    [lein-koan "0.1.0"]

The plugin makes two tasks available:

`lein koan run` watches koan files for changes and presents the user with the most advanced unsolved koan.
`lein koan test` checks that all koans fail without answers and pass with answers.


### Adding Koans

Koans live by default in the `src/koans` directory. (See "Customizations" below to change this.) To create a set of clojure koans about equality, you would add the following forms to `src/koans/equality.clj`:

```clojure
(defn truthy? [x] (boolean x))

(meditations
  "We shall contemplate truth by testing reality, via equality."
  (= __ true)

  "Non-boolean values have a sense of truthiness about them."
  (= __ (truthy? "cake")))
```

First come any definitions you want to use in your koans, followed by a call to the `meditations` macro with any number of koans.

Each koan consists of:

- a descriptive string
- a code form containing blanks (`__`).

The student' job is to fill in the blanks in such a way that the code form throws no exceptions and returns a truthy value.


### Defining Solutions

To run properly every set of koans must maintain an entry in the `:koan-resource` file, located by default at `resources/koans.clj`. The koan-resource file contains a vector of 2-vectors, with each of these holding a koan name and map of answers:

```clojure
[["equality" {"__" [true
                    true]}]
 ["atoms"    {"__" ["jail"]}]
]
```

The answer values are substituted into the koan file in the order they appear. `lein koan test` will check that every koan fails without and passes with answer substitution.


### The Dojo

By default, koans are executed in a bare namespace with `clojure.core`, the `meditations` macro, and the `__` and `___` symbols. Most koan projects will need to customize this namespace to make APIs and custom functions available.

To do this, simply add code forms to `resources/dojo.clj`. Each of these forms will be evaluated before every koan. Here's the dojo for the [Cascalog-Koans](https://github.com/sritchie/cascalog-koans):

    ;; in resources/dojo.clj
    (use 'cascalog.api
         '[cascalog.testing :only (test?-)]
         '[cascalog.util :only (defalias)])
    
    (defalias ?= test?-)

This dojo makes the cascalog API available, along with a custom equality operator that doesn't exist in the API but made koans very concise. Now koans of this form will run just fine:

    "Some queries transform nothing."
    (?= __ (<- [?x] ([["tuple."]] ?x))

You can customize the `:dojo-resource` sub-path and name inside of `resources` by tweaking its entry in `project.clj`, as discussed above:

    :koan {:dojo-resource "helpers/dojo_file.clj"} ;; dojo located at resources/helpers/dojo_file.clj


## Customizations

The koan engine looks for koans under `src/koans` by default, but you can customize this by adding a `:koan` entry to `project.clj`. All options are shown here along with defaults:

    :koan {:koan-root "src/koans"      ;; koan files live at this root
           :dojo-resource "dojo.clj"   ;; The forms in resources/dojo.clj are evaluted before every koan.
           :koan-resource "koans.clj"} ;; answers located at resources/koans.clj 

### License

The use and distribution terms for this software are covered by the
Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
which can be found in the file epl-v10.html at the root of this distribution.
By using this software in any fashion, you are agreeing to be bound by
the terms of this license.
