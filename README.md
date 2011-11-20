# Koan Engine

(Notes on a more general version of Clojure koans.) A koan engine needs:

* A library that makes "meditations", etc available
* A lein plugin -- `lein-koan` -- that pulls this library into a project and makes two commands available:

     lein koan run
     lein koan test

* Project template with [lein-newnew](https://github.com/Raynes/lein-newnew) to allow for creation of new projects. The template will include the lein plugin as a dev-dependency, a few required resource files (discussed below), an example koan file and a special key-value pair for koans in `project.clj`.

### project.clj entries:
  
    :koan {:koan-root "src/koans"     ;; koan files live at this root
           :dojo-resource "dojo"      ;; All koans are evaluated within the namespace defined in resources/dojo.clj.
           :answer-resource "answers" ;; answers at resources/answers.clj 
           :koan-resource "koans"}    ;; koans at resources/koans.clj 


### resources/dojo.clj

Projects need to make their APIs available; this file should contain either forms to be evaluated before each koan, like this:

    (use 'cascalog.api)
    (use '[cascalog.testing :only (test?-)])

Or a full-on namespace declaration:

    (ns cascalog.koans
      (:use cascalog.api))

Not sure what's best on this.

*resources/koans.clj*

Should contain a vector of koan paths, like so:

    ["tuples"
     "traps"
     "ops/mapper"]

The "meditations" library will look for the koans at `(format "%s/%s.clj" koan-root vec-entry)`.

### resources/answers.clj

This file should contain a map keyed on koan path with answer values, just as in the current project:

    {"tuples" {"__" [
                     [["truth."]]
                     [[1]]
                     ]}
     "ops/mapper" {"__" [
                         [[true answer 2!"]]
                         ]}
     ...etc...}

Alternatively to prevent duplication, `koans.clj` could hold a vector of 2-vectors, with name and answer:

    [["tuples" {"__" [
                     [["truth."]]
                     [[1]]
                     ]}]
     ["ops/mapper" {"__" [
                         [[true answer 2!"]]
                         ]}]]

`lein koan test` can validate these formats.

# Clojure Koans

The Clojure Koans are a fun and easy way to get started with Clojure - no
experience assumed or required.  Just follow the instructions below to start
making tests pass!


### Getting Started

The easiest and fastest way to get the koans up and running is to [download the
latest zip file from Github](https://github.com/functional-koans/clojure-koans/downloads).
This way, you'll have all the dependencies you need, including Clojure itself
and JLine, and you can skip the rest of this section (skip to "Running the
Koans").

If you're starting from a cloned or forked repo, that's cool too. This way
you'll be able to track your progress in Git, and see how your answers compare
to others, by checking out the project's Network tab. You might want to create your
own branch - that way if you pull back the latest koans from master, it'll be a bit
easier to manage the inevitable conflicts if we make changes to exercises you've already
completed.

The only things you'll need to run the Clojure Koans are:

- JRE 1.5 or higher
- [clojure-1.3.0.jar](http://repo1.maven.org/maven2/org/clojure/clojure/1.3.0/clojure-1.3.0.zip)

clojure-1.3.0.jar needs to be in a directory `lib` under this project.

You can use [Leiningen](http://github.com/technomancy/leiningen) to
automatically install the Clojure jar in the right place. Leiningen will also
get you a couple more jarfiles, including JLine, which allows you some of the
functionality of readline (command-line history, for example).

After you have leiningen installed, run

`lein deps`

which will download all dependencies you need to run the Clojure koans.

### Running the Koans

To run the koans, simply run

`script/run` on Mac/\*nix

`script\run` on Windows

It's an auto-runner, so as you save your files with the correct answers, it will
advance you to the next koan or file.

You'll see something like this:

    Problem in  /home/colin/Projects/clojure-koans/src/koans/equalities.clj
    ---------------------
    Assertion failed!
    We shall contemplate truth by testing reality, via equality.
    (= __ true)

The output is telling you that you have a failing test in equalities.clj.
So open that file up and make it pass!  In general, you just fill in the
blanks to make tests pass.  Sometimes there are several (or even an infinite
number) of correct answers: any of them will work in these cases.

The koans differ from normal TDD in that the tests are already written for you,
so you'll have to pay close attention to the failure messages, because up until
the very end, making a test pass just means that the next failure message comes
up.

While it might be easy (especially at first) to just fill in the blanks making
things pass, you should work thoughtfully, making sure you understand why the
answer is what it is.  Enjoy your path to Clojure enlightenment!


### Trying more things out

There's a REPL (Read-Evaluate-Print Loop) included in the Clojure Koans. Just
run:

`script/repl` on Mac/\*nix

`script\repl` on Windows

Here are some interesting commands you might try, once you're in a running REPL:

```clojure
(find-doc "vec")
(find-doc #"vec$")
(doc vec)
```

And if those still don't make sense:

```clojure
(doc doc)
(doc find-doc)
```

will show you what those commands mean.

You can exit the REPL with `CTRL-d` on any OS.


### Contributing

Patches are encouraged!  Make sure the answer sheet still passes (`script/test`,
or `script\test` on Windows), and send a pull request.

The file ideaboard.txt has lots of good ideas for new koans to start, or things
to add to existing koans.  So write some fun exercises, add your answers to
`src/path_to_answer_sheet.clj`, and we'll get them in there!

Feel free to contact me (Colin Jones / trptcolin) on Github or elsewhere if you
have any questions or want more direction before you start pitching in.


### Contributors (in order of appearance)

- Aaron Bedra
- Colin Jones (maintainer)
- Eric Lavigne
- Nuno Marquez
- Micah Martin
- Michael Kohl
- Ben Lickly
- Alex Robbins
- Jaskirat Singh Veen
- Mark Simpson
- Mike Jansen
- Caleb Phillips
- Doug South


### Credits

These exercises were started by [Aaron Bedra](http://github.com/abedra) of
[Relevance, Inc.](http://github.com/relevance) in early 2010, as a learning
tool for newcomers to functional programming. Aaron's macro-fu makes these
koans extremely simple and fun to use, and to improve upon, and without
Relevance's initiative, this project would not exist.

Using the [koans](http://en.wikipedia.org/wiki/koan) metaphor as a tool for
learning a programming language started with the
[Ruby Koans](http://rubykoans.com) by [EdgeCase](http://github.com/edgecase).


### License

The use and distribution terms for this software are covered by the
Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
which can be found in the file epl-v10.html at the root of this distribution.
By using this software in any fashion, you are agreeing to be bound by
the terms of this license.
