package org.spoofax.jsglr.tests.haskell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import junit.framework.TestCase;

import org.spoofax.jsglr.tests.result.FileResult;
import org.spoofax.jsglr.tests.result.FileResultObserver;
import org.spoofax.jsglr_layout.client.indentation.LayoutFilter;

/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 * 
 */
public class TestAllPackages extends TestCase {

  private File csvDir;
  private File csvFile;

  private int warmupCount = 0;
  private static final int NUM_THREADS = 4;

  public void warmup() throws IOException {
    String[] warmupPackages = new String[] { "matlab", "matrix-market",
        "matsuri", "maude", "maximal-cliques", "maybench", "mbox", "mdo",
        "dbf", "dbjava", "dbmigrations", "hakismet", "hakyll", "halfs",
        "halipeto", "haltavista", "hamlet", "hamtmap", "zenc", "zeno" };

    FileResultObserver observer = new FileResultObserver() {
      public void observe(FileResult result) {
        warmupCount++;
      }
    };

    try {
      for (String pkg : warmupPackages)
        new TestPackage().testPackage(pkg, observer);
    } catch (Throwable e) {
      // e.printStackTrace();
    }
    System.out.println("Warmed up with " + warmupCount + " files from "
        + warmupPackages.length + " packages.");
  }

  public void testAllPackages() throws IOException {
    String path = "all" + System.currentTimeMillis();
    csvDir = new File(path);
    csvDir.mkdirs();
    csvFile = new File(path + ".csv");
    new FileResult().writeCSVHeader(csvFile.getAbsolutePath());

    warmup();

    BufferedReader in = null;

    final Semaphore s = new Semaphore(NUM_THREADS);

    try {
      int i = 0;
      in = new BufferedReader(new FileReader(
          ExtractAllCabalPacakges.PACKAGE_LIST_FILE));
      String pkg;
      while ((pkg = in.readLine()) != null
          && !Thread.currentThread().isInterrupted()) {
        i++;
        System.out.println(i + " " + pkg + "...");

        if (TestConfiguration.SKIP_PACKAGES.contains(pkg)) {
          System.out.println("skipped");
          continue;
        }
        if (NUM_THREADS == 1) {
          new TestPackage().testPackage(pkg, new MyFileResultObserver(pkg));
        } else {
          s.acquire();
          final String pkg_name = pkg;
          new Thread() {
            public void run() {
              try {
                new TestPackage().testPackage(pkg_name,
                    new MyFileResultObserver(pkg_name));
              } catch (Exception e) {
                e.printStackTrace();
              } finally {
                s.release();
              }
            }
          }.start();
        }
      }

    } catch (Throwable e) {
      ;
    } finally {
      if (in != null)
        in.close();
    }

    System.out.println(csvFile.getAbsolutePath());
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

      // System.out.println(csvFile.getAbsolutePath());
    }
  }

  public static void main(String[] args) throws IOException {
    TestAllPackages tester = new TestAllPackages();
    int repetition = args.length > 0 ? Integer.parseInt(args[0]) : 1;

    tester.warmup();

    for (int i = 0; i < repetition; i++)
      tester.testAllPackages();
  }

}
