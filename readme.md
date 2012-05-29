Layout-sensitive generalized parsing
====================================

Most languages prescribe a textual syntax. A parser translates from such textual
representation into a structured one and constitutes the first step in
processing a document. Due to the development of parser frameworks such as
ANTLR, PEGs, parsec, or SDF, parsers can be considered off-the-shelf tools
nowadays: Non-experts can use parsers because language specifications are
declarative. Unfortunately, the declarativity of current parser frameworks is
mostly limited to context-free languages.

One particularly relevant class of non-context-free languages is
layout-sensitive languages, first proposed by Landin in 1966. In
layout-sensitive languages, the translation from a textual representation to a
structural one depends on the code's layout and the used indentation. Most
prominently, the _offside rule_ prescribes that all non-whitespace tokens
of a structure must be further to the right than the token that starts the
structure. In other words, a token is offside if it occurs further to the left
than the starting token of a structure; an offside token denotes the start of
the next structure. Accordingly, in languages that employ the offside rule, the
block structure of code is determined by indentation alone, whose use is
considered good style anyway.

The offside rule has been applied in a number of programming languages including
Python, Haskell, F#, and Markdown. For illustration, the following Python code
uses layout to declare the code's block structure.

    if x != y:
        if x > 0:
            y = x
    else:
        y = -x

Similarly, the following Haskell code uses layout to declare the code's block
structure.

    do in <- readInput
       case in of
         Just txt -> do putStrLn "thank you"
                        sendToServer txt
                        return True
         Nothing  -> fail "no input"
    
The layout of the Python program specifies that the `else` belongs to the outer
`if` statement. Similarly, the layout of the Haskell program specifies to which
`do` block each statement belongs. It is a shame that current declarative parser
frameworks do not support layout-sensitive languages such as Python or Haskell.

We propose an extension of SDF that supports the specification of
layout-sensitive languages declaratively. Our extension allows the annotation of
productions with _layout constraints_, which restrict the use of productions to
valid layout. For example, for if-then-else in Python we annotate (among other
things) that the `if` keyword must start on the same column as the `else`
keyword. In general, layout constraints are context-sensitive and thus cannot be
enforced during the execution of a context-free parser. To this end, we use a
generalized parser to generate any possible representation of a source file
ignoring layout. During disambiguation of the resulting parse forest, we discard
any syntax tree that violates a layout constraint. However, in practice, this
scheme is not efficient enough: The parser times out. To improve performance, we
identified a class of layout constraints that in fact is context-free and can be
enforced at parse time. As it turns out, enforcing context-free layout
constraints at parse time and the remaining constraints at disambiguation time
is efficient enough to support layout-sensitive languages in practice.

To validate the correctness and to evaluate the performance of our
layout-sensitive parser, we have build layout-sensitive SDF grammars for Python
and Haskell. In particular, we applied our parser to all 33290 Haskell files in
the open-source package repository Hackage. We compare the result of applying
our parser to applying a traditional generalized parser to the same Haskell
files where block structure has been made explicit through curly braces. Our
study empirically verifies the correctness of our parser and shows that it is
comparably fast.
