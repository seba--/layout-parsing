April 30, 2013
--------------

 * all1367191170785.csv  --  reference run on seba's MacBook Pro with interpreted constraints
 * all1367252772974.csv  --  run on seba's MacBook Pro with *compiled constraints*


June 11, 2012
-------------
The files "all1339314685378.csv", "all1339377065526.csv", and
"all1339404021207.csv" contain the raw data of three consecutive runs of our
layout-sensitive parser with a Haskell grammar on all Haskell source files in
HackageDB (http://hackage.haskell.org).  These measurements have been performed
from June 10, 2012 to June 11, 2012 using the grammar and parser as of git
commit "77d12eea2553064156c34a38fff4428766fe9999" of the repository
https://github.com/seba--/layout-parsing.
 
May 31, 2012
------------
The file "all1338447943348.csv" contains the raw data of evaluating our
layout-sensitive parser with a Haskell grammar on all Haskell source files in
HackageDB (http://hackage.haskell.org). The measurement has been performed on
May 31, 2012 with parser and grammar of git commit
"e7302297e4086e01cb1905d5d2de0845365e1427" of the repository
https://github.com/seba--/layout-parsing.

Format
------
The format of the raw data is as follows:
 * Column "run" specifies which input our parser was run on. "1" means the input
   was preprocessed to contain explicit layout only. "2" means original input as
   found on HackageDB. "3" means the input was preprocessed to contain implicit
   layout only.
 * Column "time" is in nanoseconds.
 * Column "ambiguities" specifies the number of ambiguities that occurred in the
   parser result. We count a parser result with any ambiguity as a failed parse.
 * Column "diffs to 1" shows the number of differences between this row's parser
   result and the result of parser run "1". Since "1" uses explicit layout only,
   we use it as a reference parser for comparing parse results.
