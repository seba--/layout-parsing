package org.spoofax.jsglr.tests.haskell;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.spoofax.jsglr.tests.result.FileResult;

/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 */
public class TestDebug extends TestCase {
  
  private TestFile fileTester = new TestFile();
  
  public void test() throws IOException {
    List<String> files = new ArrayList<String>();
    
    files.add("c:/Users/SEBAIN~1.000/AppData/Local/Temp/BASIC3816339167381594818/BASIC-0.1.5.0/Language/BASIC/Interp.hs");
    
    for (String f : files) {
      FileResult result = fileTester.testFile(new File(f), "test");

      String path = new File(f).getAbsolutePath() + ".new.pt";
      Utilities.writeToFile(fileTester.newParserCorrectness.parseTree.toString(), path);
      System.out.println("wrote parse tree to " + path);

      path = new File(f).getAbsolutePath() + ".old.pt";
      Utilities.writeToFile(fileTester.oldParser.parseTree.toString(), path);
      System.out.println("wrote parse tree to " + path);
      
      System.out.println(result.getCSVHeaderString());
      System.out.println(result.getAsCSVString());
    }
  }
}
