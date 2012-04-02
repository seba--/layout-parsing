/*
 * Created on 05.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 *
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr.tests.offset;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParserException;

public class TestPython extends ParseTestCase {

  @Override
  public void gwtSetUp() throws FileNotFoundException, IOException,
      ParserException, InvalidParseTableException {
    super.gwtSetUp("python/Python", "test-offside/terms/", "txt");
//    super.sglr.getDisambiguator().setHeuristicFilters(true);
  }

  public void testPython_1() throws FileNotFoundException, IOException {
    doParseTest("Python_1");
  }

  public void testPython_2() throws FileNotFoundException, IOException {
    doParseTest("Python_2");
  }

  public void testPython_3() throws FileNotFoundException, IOException {
    doParseTest("Python_3");
  }

  public void testPython_4() throws FileNotFoundException, IOException {
    doParseTest("Python_4");
  }

  public void testPython_5() throws FileNotFoundException, IOException {
    doParseTest("Python_5");
  }
}
