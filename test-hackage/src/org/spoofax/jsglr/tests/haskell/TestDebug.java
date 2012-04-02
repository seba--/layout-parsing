package org.spoofax.jsglr.tests.haskell;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 */
public class TestDebug extends TestCase {
  
  private TestFile fileTester = new TestFile();
  
  public void test() throws IOException {
    List<String> files = new ArrayList<String>();
    
    files.add("/var/folders/aw/aw2pcGAuGEyvWvKgy3h3GU+++TM/-Tmp-/AC-Vector7228716350751816011/AC-Vector-2.3.2/Data/BoundingBox/B1.hs");
    
    for (String f : files)
      fileTester.testFile(new File(f), "test");
    
    System.out.flush();
    
    fileTester.printShortLog();
    
    for (ParseComparisonFailure fail : fileTester.getFailures())
      System.out.println("*error*" + fail.getMessage());
    
    fileTester.raiseFailures();
  }
}
