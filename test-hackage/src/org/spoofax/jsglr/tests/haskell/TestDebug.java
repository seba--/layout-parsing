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
    
    files.add("c:/Users/seba.INFORMATIK.000/AppData/Local/Temp/4Blocks7755594627937130244/4Blocks-0.2/Core/Brick.hs");
    
    for (String f : files) {
      fileTester.testFile(new File(f), "test");
      String path = new File(f).getAbsolutePath() + ".new.pt";
      Utilities.writeToFile(fileTester.newParser.parseTree.toString(), path);
      System.out.println("wrote parse tree to " + path);
    }
    
    System.out.flush();
    
    fileTester.printShortLog();
    
    for (AssertionFailedError fail : fileTester.getFailures())
      System.out.println("*error*" + fail.getMessage());
    
    fileTester.raiseFailures();
  }
}
