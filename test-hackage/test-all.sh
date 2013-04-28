#!/bin/sh

JSGLR_ORIG=../jsglr-orig/bin
JSGLR_LAYOUT=../jsglr-layout/bin
NORMALIZE=../haskell-parsetree-normalize/bin
PLUGINS=junit.jar:../org.spoofax.terms_1.0.0.201212201448.jar:strategoxt.jar

java -cp bin:$JSGLR_ORIG:$JSGLR_LAYOUT:$NORMALIZE:$PLUGINS org.spoofax.jsglr.tests.haskell.TestAllPackages