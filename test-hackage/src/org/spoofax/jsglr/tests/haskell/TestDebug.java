package org.spoofax.jsglr.tests.haskell;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 */
public class TestDebug extends TestCase {
  
  private TestFile fileTester = new TestFile();
  
  public void test() throws IOException {
    List<String> files = new ArrayList<String>();
    
    files.add("/Users/seba/tmp/scale.hs");
    
    for (String f : files)
      fileTester.testFile(new File(f), "test");
    
    System.out.flush();
    
    fileTester.printShortLog();
    
    for (AssertionFailedError fail : fileTester.getFailures())
      System.out.println("*error*" + fail.getMessage());
    
    fileTester.raiseFailures();
  }
}
