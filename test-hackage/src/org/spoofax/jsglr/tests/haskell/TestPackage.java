package org.spoofax.jsglr.tests.haskell;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.spoofax.jsglr.tests.result.FileResult;
import org.spoofax.jsglr.tests.result.FileResultObserver;

/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 */
public class TestPackage extends TestCase {
  
  private final static boolean LOGGING = true;
  
  private final static Pattern SOURCE_FILE_PATTERN = Pattern.compile(".*\\.hs");
  
  private File csvFile;
  private FileResultObserver observer;
  
  public void testPackage() throws IOException {
    testPackage("activehs", new FileResultObserver() { public void observe(FileResult result) { } });
    System.out.println(csvFile.getAbsolutePath());
  }
  
  public void testPackage(String pkg, FileResultObserver observer) throws IOException {
    if (LOGGING)
      System.out.println(pkg + " starting");
    
    this.observer = observer;
    
    File dir = new File("hackage-data/" + pkg);
    csvFile = new File(dir + ".csv");
    try {
      new FileResult().writeCSVHeader(csvFile.getAbsolutePath());
    } catch (IOException e) {
      e.printStackTrace();
    }
    testFiles(dir, "", pkg);
    
    if (LOGGING)
      System.out.println(pkg + " done");
    
  }
  
  private void logResult(FileResult result) throws IOException {
    observer.observe(result);
    try {
      result.appendAsCSV(csvFile.getAbsolutePath());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public void testFiles(File dir, String path, String pkg) throws IOException {
    if (dir != null && dir.listFiles() != null)
      for (File f : dir.listFiles())
        if (f.isFile() && SOURCE_FILE_PATTERN.matcher(f.getName()).matches()) {
          logResult(new TestFile().testFile(f, Utilities.extendPath(path, f.getName()), pkg));
        } else if (f.isDirectory()) {
          testFiles(f, Utilities.extendPath(path, f.getName()), pkg);
        }
  }
  
  private File cabalUnpack(String pkg) throws IOException {
    File tmpDir = File.createTempFile(pkg.length() < 3 ? pkg + "aaa" : pkg, "");
    tmpDir.delete();
    tmpDir.mkdirs();
    
    String[] cmds = new String[] {TestConfiguration.CABAL_COMMAND, "unpack", pkg, "-d" + tmpDir.getAbsolutePath()};
    
    if (LOGGING)
      System.out.println("[" + pkg + "] " + CommandExecution.commandLineAsString(cmds));
    
    CommandExecution.SILENT_EXECUTION = !LOGGING;
    CommandExecution.SUB_SILENT_EXECUTION = !LOGGING;
      
    CommandExecution.execute("[" + pkg + "]", cmds);
    return tmpDir;
  }
}
