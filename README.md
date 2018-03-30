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

The easiest way to start writing koans for your library is to generate a new project with the `koan-template` leiningen plugin (source code [on github](https://github.com/functional-koans/koan-template). Make sure you have [Leiningen](http://github.com/technomancy/leiningen) installed.  Add the plugin to your Leiningen '~/.lein/profiles.clj' file.

    {:user {:plugins [
      [koan/lein-template "0.1.3"]
    ]}}

Use these commands to great a new project based on the koan template.

    lein new koan <your-project-name>
    cd <your-project-name>
    chmod +x script/*

The README of the resulting project will contain everything your users need to know to get started.


## Adding koan-engine to an existing project

You can also add koans to an existing library without interfering with your source tree. This is a wonderful way to introduce users to your source code.

To get started, add the `lein-koan` plugin (source code [on github](https://github.com/functional-koans/koan-template) ) to your `:dev-dependencies` in `project.clj`:

    [lein-koan "0.1.2"]

The plugin makes two tasks available:

`lein koan run` watches koan files for changes and presents the user with the most advanced unsolved koan.
`lein koan test` checks that all koans fail without answers and pass with answers.

## Customizations

The koan engine looks for koans under `src/koans` by default, but you can customize this by adding a `:koan` entry to `project.clj`. All options are shown here along with defaults:

```clojure
:koan {:koan-root "src/koans"      ;; koan files live at this root
       :dojo-resource "dojo.clj"   ;; The forms in resources/dojo.clj are evaluted before every koan.
       :koan-resource "koans.clj"} ;; answers located at resources/koans.clj
```

For more information on the Dojo see [the wiki page](https://github.com/functional-koans/clojure-koan-engine/wiki/The-Dojo).  ["How to write Koans"](https://github.com/functional-koans/clojure-koan-engine/wiki/How-to-write-koans) provides more information on how to add your own koans and answers.

### License

The use and distribution terms for this software are covered by the
Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
which can be found in the file epl-v10.html at the root of this distribution.
By using this software in any fashion, you are agreeing to be bound by
the terms of this license.
