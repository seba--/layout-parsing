package org.spoofax.jsglr.tests.haskell;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 */
public class TestConfiguration {
//  public final static String CABAL_COMMAND = "/usr/local/bin/cabal";
  public final static String CABAL_COMMAND = "d:/Programs/Haskell/lib/extralibs/bin/cabal.exe";
  
//  public final static String PP_HASKELL_COMMAND = "/Users/seba/.cabal/bin/pp-haskell";
  public final static String PP_HASKELL_COMMAND = "c:/Users/seba.INFORMATIK.000/AppData/Roaming/cabal/bin/pp-haskell.exe";
  
  public final static Collection<String> SKIP_PACKAGES = new ArrayList<String>();
  public final static Collection<String> SKIP_FILES = new ArrayList<String>();
  
  static {
//    SKIP_FILES.add("Agda-2.2.10/dist/build/Agda/Syntax/Parser/Parser.hs"); // out of stack memory
//    SKIP_FILES.add("BNFC-meta-0.2.1/Language/LBNF/Grammar.hs"); // out of stack memory, 5000 LOCs
//    SKIP_FILES.add("CHXHtml-0.1.4/Text/CHXHtml/XHtml1_frameset.hs"); // out of stack memory
//    SKIP_FILES.add("CHXHtml-0.1.4/Text/CHXHtml/XHtml1_strict.hs"); // out of stack memory
//    SKIP_FILES.add("CHXHtml-0.1.4/Text/CHXHtml/XHtml1_transitional.hs"); // out of stack memory
//    SKIP_FILES.add("Crypto-4.2.3/Codec/Encryption/AESAux.hs"); // out of stack memory
//    SKIP_FILES.add("CCA-0.1.3/dist/build/ccap/ccap-tmp/Parser.hs"); // out of heap memory
//    SKIP_FILES.add("Crypto-4.2.3/Codec/Encryption/DESAux.hs"); // out of heap memory
//    SKIP_FILES.add("DOM-2.0.1/Data/DOM/CSS2Properties.hs"); // out of heap memory
//    SKIP_FILES.add("DSH-0.5.5/tests/Main.hs"); // out of heap memory
//    SKIP_FILES.add("Eq-1.1/EqManips/Renderer/Placer.hs"); // out of heap memory
//    SKIP_FILES.add("FM-SBLEX-3.0.1/src/dalin/CommandsSw.hs"); // out of stack memory
    SKIP_FILES.add(""); // out of heap memory
    SKIP_FILES.add(""); // out of heap memory
    SKIP_FILES.add(""); // out of heap memory
    SKIP_FILES.add(""); // out of heap memory
    SKIP_FILES.add(""); // out of heap memory
  }

  
  public final static Collection<String> HASKELL_EXTENSIONS = new ArrayList<String>();
  static {
    HASKELL_EXTENSIONS.add("Haskell2010");
    HASKELL_EXTENSIONS.add("MagicHash");
    HASKELL_EXTENSIONS.add("FlexibleInstances");
    HASKELL_EXTENSIONS.add("FlexibleContexts");
    HASKELL_EXTENSIONS.add("GeneralizedNewtypeDeriving");
  }

  
}