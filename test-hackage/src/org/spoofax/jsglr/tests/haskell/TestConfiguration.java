package org.spoofax.jsglr.tests.haskell;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 */
public class TestConfiguration {
  public final static String CABAL_COMMAND = "cabal";
  public final static String PP_HASKELL_COMMAND = "pp-haskell";
  
  public final static Collection<String> SKIP_PACKAGES = new ArrayList<String>();
  private final static Collection<String> SKIP_FILES = new ArrayList<String>();
  
  static {
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
    HASKELL_EXTENSIONS.add("ExplicitForAll");
  }

 
  
  public static boolean skipFile(File file) {
    String path = file.getAbsolutePath().replace('\\', '/');
    
    for (String skip : SKIP_FILES)
      if (path.endsWith(skip))
        return true;
    
    return false;
  }
}