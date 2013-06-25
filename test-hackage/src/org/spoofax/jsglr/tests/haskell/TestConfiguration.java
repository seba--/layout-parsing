package org.spoofax.jsglr.tests.haskell;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 */
public class TestConfiguration {
  public final static String CABAL_COMMAND = "cabal";
  //public final static String CABAL_COMMAND = "d:/Programs/Haskell/lib/extralibs/bin/cabal.exe";
  
  public final static String PP_HASKELL_COMMAND = "/Users/moritzlichter/Library/Haskell/ghc-7.4.2/lib/pp-haskell-0.2/bin/pp-haskell";
      //  "pp-haskell";
  //public final static String PP_HASKELL_COMMAND = "c:/Users/seba.INFORMATIK.001/AppData/Roaming/cabal/bin/pp-haskell.exe";
  
  public final static Collection<String> SKIP_PACKAGES = new ArrayList<String>();
  private final static Collection<String> SKIP_FILES = new ArrayList<String>();
  
  static {
//    SKIP_FILES.add("Agda-2.3.0.1/src/full/Agda/Packaging/Database.hs"); // stack overflow
  }

  
  public final static Collection<String> HASKELL_EXTENSIONS = new ArrayList<String>();
  static {
    HASKELL_EXTENSIONS.add("Haskell2010");
    HASKELL_EXTENSIONS.add("MagicHash");
    HASKELL_EXTENSIONS.add("FlexibleInstances");
    HASKELL_EXTENSIONS.add("FlexibleContexts");
    HASKELL_EXTENSIONS.add("GeneralizedNewtypeDeriving");
    HASKELL_EXTENSIONS.add("ParallelListComp");
    HASKELL_EXTENSIONS.add("PatternGuards");
    HASKELL_EXTENSIONS.add("ViewPatterns");
    HASKELL_EXTENSIONS.add("TemplateHaskell");
    HASKELL_EXTENSIONS.add("QuasiQuotes");
    HASKELL_EXTENSIONS.add("FlexibleContexts");
    HASKELL_EXTENSIONS.add("UnboxedTuples");
    HASKELL_EXTENSIONS.add("TypeOperators");
    HASKELL_EXTENSIONS.add("ScopedTypeVariables");
    HASKELL_EXTENSIONS.add("GADTs");
    HASKELL_EXTENSIONS.add("KindSignatures");
    
    HASKELL_EXTENSIONS.add("ExplicitForAll");
    HASKELL_EXTENSIONS.add("PolymorphicComponents");
  //  HASKELL_EXTENSIONS.add("Rank2Types");
    HASKELL_EXTENSIONS.add("RankNTypes");
    HASKELL_EXTENSIONS.add("TypeFamilies");
    HASKELL_EXTENSIONS.add("TypeSynonymInstances");
    HASKELL_EXTENSIONS.add("MultiParamTypeClasses");
    HASKELL_EXTENSIONS.add("ExistentialQuantification");
  }

 
  
  public static boolean skipFile(File file) {
    String path = file.getAbsolutePath().replace('\\', '/');
    
    for (String skip : SKIP_FILES)
      if (path.endsWith(skip))
        return true;
    
    return false;
  }
}