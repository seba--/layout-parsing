package org.spoofax.jsglr.tests.haskell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.spoofax.jsglr.tests.result.FileResult;

/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 *
 */
public class TestAllPackages extends TestCase {
  
  private TestPackage packageTester = new TestPackage();
  
  private File csvDir;
  private File csvFile;
  
  public void testAllPackages() throws IOException {
    String path = "all" + System.currentTimeMillis();
    csvDir = new File(path);
    csvDir.mkdirs();
    csvFile = new File(path + ".csv");
    new FileResult().writeCSVHeader(csvFile.getAbsolutePath());
    
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
        
        List<FileResult> packageResults = packageTester.testPackage(pkg);
        logResults(pkg, packageResults);
      }
      
    } finally { 
      if (in != null)
        in.close();
    }
  }
  
  private void logResults(String pkg, List<FileResult> packageResults) throws IOException {
    File pkgCsv = new File(csvDir, pkg + ".csv");
    new FileResult().writeCSVHeader(pkgCsv.getAbsolutePath());
    
    for (FileResult result : packageResults) {
      result.appendAsCSV(csvFile.getAbsolutePath());
      result.appendAsCSV(pkgCsv.getAbsolutePath());
    }
    
    System.out.println(csvFile.getAbsolutePath());
  }
}
