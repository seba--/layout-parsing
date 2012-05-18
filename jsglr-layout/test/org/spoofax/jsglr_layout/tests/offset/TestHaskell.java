/*
 * Created on 05.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 *
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr_layout.tests.offset;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.spoofax.jsglr_layout.client.InvalidParseTableException;
import org.spoofax.jsglr_layout.client.ParserException;

public class TestHaskell extends ParseTestCase {

  @Override
  public void gwtSetUp() throws FileNotFoundException, IOException,
      ParserException, InvalidParseTableException {
    super.gwtSetUp("haskell/Haskell", "test-offside/terms", "txt");
  }

  public void testHaskell_1() throws FileNotFoundException, IOException {
    doParseTest("Haskell_1");
  }

  public void testHaskell_2() throws FileNotFoundException, IOException {
    doParseTest("Haskell_2");
  }

  public void testHaskell_3() throws FileNotFoundException, IOException {
    doParseTest("Haskell_3");
  }

  public void testHaskell_4() throws FileNotFoundException, IOException {
    doParseTest("Haskell_4");
  }

  public void testHaskell_5() throws FileNotFoundException, IOException {
    doParseTest("Haskell_5");
  }

  public void testHaskell_6() throws FileNotFoundException, IOException {
    doParseTest("Haskell_6");
  }

  public void testHaskell_7() throws FileNotFoundException, IOException {
    doParseTest("Haskell_7");
  }

  public void testHaskell_8() throws FileNotFoundException, IOException {
    doParseTest("Haskell_8");
  }

  public void testHaskell_9() throws FileNotFoundException, IOException {
    doParseTest("Haskell_9");
  }

  public void testHaskell_10() throws FileNotFoundException, IOException {
    doParseTest("Haskell_10");
  }
  
  public void testHaskell_11() throws FileNotFoundException, IOException {
    doParseTest("Haskell_11");
  }

  public void testHaskell_12() throws FileNotFoundException, IOException {
    doParseTest("Haskell_12");
  }

  public void testHaskell_13() throws FileNotFoundException, IOException {
    doParseTest("Haskell_13");
  }

  public void testHaskell_14() throws FileNotFoundException, IOException {
    doParseTest("Haskell_14");
  }

  public void testHaskell_15() throws FileNotFoundException, IOException {
    doParseTest("Haskell_15");
  }

  public void testHaskell_16() throws FileNotFoundException, IOException {
    doParseTest("Haskell_16");
  }
}
