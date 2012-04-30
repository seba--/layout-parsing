package org.spoofax.jsglr.tests.haskell;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 *
 */
public class TestAllPackages extends ChainedTestCase {
  
  private TestPackage packageTester = new TestPackage();
  
  public TestAllPackages() {
    BufferedWriter  writer = null;
  
    try {
      writer = new BufferedWriter(new FileWriter(TestPackage.SUCCESS_LOG, true));
      writer.write("\n\n\n\nTest all packages at " + new Date(System.currentTimeMillis()) + "\n");
      writer.close();
      
      writer = new BufferedWriter(new FileWriter(TestPackage.FAILURE_LOG, true));
      writer.write("\n\n\n\nTest all packages at " + new Date(System.currentTimeMillis()) + "\n");
      writer.close();
    } catch (IOException e) {
      if (writer != null) 
        try { writer.close(); }
        catch (IOException e2) {
        }
      throw new RuntimeException(e);
    }
  }
  
  public void testAllPackages() throws IOException {
    BufferedReader in = null;
    
    try {
      int i = 0;
      in = new BufferedReader(new FileReader(ExtractAllCabalPacakges.PACKAGE_LIST_FILE));
      String pkg;
      while ((pkg = in.readLine()) != null && !Thread.currentThread().isInterrupted()) {
        i++;
        System.out.println(i + " " + pkg + "...");
        
        if (TestConfiguration.SKIP_PACKAGES.contains(pkg)) {
          System.out.println("skipped");
          continue;
        }
        
        packageTester.testPackage(pkg);
        
        addAllFailures(packageTester.getFailures());
        addOks(packageTester.getOkCount());
        addOkFails(packageTester.getOkFailCount());
        addNoParses(packageTester.getNoParseCount());
        addAmbInfix(packageTester.getAmbInfixCount());
        addTimeout(packageTester.getTimeout());
        packageTester.reset();
        
        printShortLog();
      }
      
    } finally { 
      if (in != null)
        in.close();
    }
    
    printShortLog();
    raiseFailures();
  }
}
