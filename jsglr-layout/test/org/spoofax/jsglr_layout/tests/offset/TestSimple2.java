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

import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParserException;

public class TestSimple2 extends ParseTestCase {

  @Override
  public void gwtSetUp() throws FileNotFoundException, IOException,
      ParserException, InvalidParseTableException {
    super.gwtSetUp("Simple2", "test-offside/terms/", "txt");
  }

  public void testSimple2_1() throws FileNotFoundException, IOException {
    doParseTest("Simple2_1");
  }

  public void testSimple2_2() throws FileNotFoundException, IOException {
    doParseTest("Simple2_2");
  }

  public void testSimple2_3() throws FileNotFoundException, IOException {
    doParseTest("Simple2_3");
  }
}
