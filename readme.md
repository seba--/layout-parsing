Layout-sensitive Generalized Parsing
====================================

External dependency
-------------------
JSGLR layout requires the Spoofax term plugin (or any later version):

* org.spoofax.terms_1.0.0.201212201448.jar

The plugin file is available in the root directory of the development
branch.

Scope and motivation
--------------------

The theory of context-free languages is well-understood and context-free
parsers can be used as off-the-shelf tools in practice. In particular, to use
a context-free parser framework, a user does not need to understand its internals but
can specify a language declaratively as a grammar. However, many languages in
practice are not context-free. One particularly important class of such
languages is layout-sensitive languages, in which the structure of code
depends on indentation and whitespace. For example, Python, Haskell, F\#, and
Markdown use indentation instead of curly braces to determine the block
structure of code. Their parsers (and lexers) are not declaratively specified
but hand-tuned to account for layout-sensitivity.

To support _declarative_ specifications of layout-sensitive languages, we
propose a parsing framework in which a user can annotate layout in a grammar as
constraints on the relative positioning of tokens in the parsed subtrees. For
example, a user can declare that a block consists of statements that all start
on the same column. We have integrated layout constraints into SDF and
implemented a layout-sensitive generalized parser as an extension of generalized
LR parsing. We evaluate the correctness and performance of our parser by parsing
33290 open-source Haskell files. Layout-sensitive generalized parsing is easy to
use, and its performance overhead compared to layout-insensitive parsing is
small enough for most practical applications.


A full description of the design and implementation of layout-sensitive
generalized parsing is available
[online as a paper draft](http://sugarj.org/layout-parsing.pdf).


Navigation
----------

* [Layout-sensitive scannerless generalized parser](http://github.com/seba--/layout-parsing/tree/master/jsglr-layout)
* [Haskell grammar](http://github.com/seba--/layout-parsing/tree/master/jsglr-layout/test-offside/grammars/haskell)
* [Python grammar](http://github.com/seba--/layout-parsing/tree/master/jsglr-layout/test-offside/grammars/python)
* [Evaluation tool](http://github.com/seba--/layout-parsing/tree/master/test-hackage)
* [Evaluation subjects from Hackage](http://github.com/seba--/layout-parsing/tree/master/test-hackage/hackage-data)
* [Evaluation results (raw)](http://github.com/seba--/layout-parsing/tree/master/test-hackage/evaluation-data)
