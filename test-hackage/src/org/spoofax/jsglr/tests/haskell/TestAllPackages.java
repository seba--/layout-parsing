package org.spoofax.jsglr.tests.haskell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import junit.framework.TestCase;

import org.spoofax.jsglr.tests.result.FileResult;
import org.spoofax.jsglr.tests.result.FileResultObserver;

/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 *
 */
public class TestAllPackages extends TestCase {
  
  private File csvDir;
  private File csvFile;
  
  public void warmup() throws IOException {
    String[] warmupPackages = new String[] {
        "matlab",
        "matrix-market",
        "matsuri",
        "maude",
        "maximal-cliques",
        "maybench",
        "mbox",
        "mdo",
    };
    
    for (String pkg : warmupPackages)
      new TestPackage().testPackage(pkg, new FileResultObserver() { public void observe(FileResult result) { } });
  }
  
  public void testAllPackages() throws IOException {
    String path = "all" + System.currentTimeMillis();
    csvDir = new File(path);
    csvDir.mkdirs();
    csvFile = new File(path + ".csv");
    new FileResult().writeCSVHeader(csvFile.getAbsolutePath());
    
    warmup();
    
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
        
        new TestPackage().testPackage(pkg, new MyFileResultObserver(pkg));
      }
      
    } finally { 
      if (in != null)
        in.close();
    }
  }

  private class MyFileResultObserver implements FileResultObserver {
    private File pkgCsv;
    private MyFileResultObserver(String pkg) throws IOException {
      pkgCsv = new File(csvDir, pkg + ".csv");
      new FileResult().writeCSVHeader(pkgCsv.getAbsolutePath());
    }
    public void observe(FileResult packageResult) throws IOException {
      packageResult.appendAsCSV(csvFile.getAbsolutePath());
      packageResult.appendAsCSV(pkgCsv.getAbsolutePath());
      
      System.out.println(csvFile.getAbsolutePath());
    }
  }
}
