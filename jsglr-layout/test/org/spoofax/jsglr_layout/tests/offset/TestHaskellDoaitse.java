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

public class TestHaskellDoaitse extends ParseTestCase {

  @Override
  public void gwtSetUp() throws FileNotFoundException, IOException,
      ParserException, InvalidParseTableException {
    super.gwtSetUp("haskell/Haskell", "test-offside/terms/doaitse", "hs");
  }

  public void test_layout1() throws FileNotFoundException, IOException {
    doParseTest("layout1");
  }
  public void test_layout2() throws FileNotFoundException, IOException {
    doParseTest("layout2");
  }
  public void test_layout3() throws FileNotFoundException, IOException {
    doParseTest("layout3");
  }
  public void test_layout4() throws FileNotFoundException, IOException {
    doParseTest("layout4");
  }
  public void test_layout5() throws FileNotFoundException, IOException {
    doParseTest("layout5");
  }
  public void test_layout6() throws FileNotFoundException, IOException {
    doParseTest("layout6");
  }
  public void test_layout7() throws FileNotFoundException, IOException {
    doParseTest("layout7");
  }
  public void test_layout8() throws FileNotFoundException, IOException {
    doParseTest("layout8");
  }
  public void test_layout9() throws FileNotFoundException, IOException {
    doParseTest("layout9");
  }
  public void test_layout10() throws FileNotFoundException, IOException {
    doParseTest("layout10");
  }
  public void test_layout11() throws FileNotFoundException, IOException {
    doParseTest("layout11");
  }
  public void test_layout12() throws FileNotFoundException, IOException {
    doParseTest("layout12");
  }
  public void test_layout13() throws FileNotFoundException, IOException {
    doParseTest("layout13");
  }
  public void test_error1() throws FileNotFoundException, IOException {
    doParseTest("error1");
  }
  public void test_error2() throws FileNotFoundException, IOException {
    doParseTest("error2");
  }
  public void test_error3() throws FileNotFoundException, IOException {
    doParseTest("error3");
  }
  public void test_error4() throws FileNotFoundException, IOException {
    doParseTest("error4");
  }
  public void test_error5() throws FileNotFoundException, IOException {
    doParseTest("error5");
  }
  public void test_error6() throws FileNotFoundException, IOException {
    doParseTest("error6");
  }
  public void test_error7() throws FileNotFoundException, IOException {
    doParseTest("error7");
  }
  public void test_error8() throws FileNotFoundException, IOException {
    doParseTest("error8");
  }
}
