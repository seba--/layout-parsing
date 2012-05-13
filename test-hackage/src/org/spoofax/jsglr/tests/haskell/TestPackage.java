package org.spoofax.jsglr.tests.haskell;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.spoofax.jsglr.tests.haskell.CommandExecution.ExecutionError;
import org.spoofax.jsglr.tests.result.FileResult;
import org.spoofax.jsglr.tests.result.FileResultObserver;

/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 */
public class TestPackage extends TestCase {
  
  private final static boolean LOGGING = false;
  
  private final static Pattern SOURCE_FILE_PATTERN = Pattern.compile(".*\\.hs");
  
  private File csvFile;
  private FileResultObserver observer;
  
  public void testPackage() throws IOException {
    testPackage("regular", new FileResultObserver() { public void observe(FileResult result) { } });
    System.out.println(csvFile.getAbsolutePath());
  }
  
  public void testPackage(String pkg, FileResultObserver observer) throws IOException {
    if (LOGGING)
      System.out.println(pkg + " starting");
    
    this.observer = observer;
    
    File dir;
    try { 
      dir = cabalUnpack(pkg);
    } catch (ExecutionError e) {
      String msg = e.getCause() == null ? "unknown cause" : e.getCause().getMessage();
      System.out.println("[" + pkg + "] " + "cabal unpack failed: " + msg);
      return;
    }
    
    csvFile = new File(dir + ".csv");
    new FileResult().writeCSVHeader(csvFile.getAbsolutePath());
    
    testFiles(dir, "", pkg);
    
    if (LOGGING)
      System.out.println(pkg + " done");
    
  }
  
  private void logResult(FileResult result) throws IOException {
    observer.observe(result);
    result.appendAsCSV(csvFile.getAbsolutePath());
  }
  
  public void testFiles(File dir, String path, String pkg) throws IOException {
    for (File f : dir.listFiles())
      if (f.isFile() && SOURCE_FILE_PATTERN.matcher(f.getName()).matches())
        logResult(new TestFile().testFile(f, Utilities.extendPath(path, f.getName()), pkg));
      else if (f.isDirectory())
        testFiles(f, Utilities.extendPath(path, f.getName()), pkg);
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
