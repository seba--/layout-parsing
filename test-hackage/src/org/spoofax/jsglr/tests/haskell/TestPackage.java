package org.spoofax.jsglr.tests.haskell;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.spoofax.jsglr.tests.haskell.CommandExecution.ExecutionError;
import org.spoofax.jsglr.tests.result.FileResult;

/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 */
public class TestPackage extends TestCase {
  
  private final static boolean LOGGING = false;
  
  public final static String SUCCESS_LOG = "success.log";
  public final static String FAILURE_LOG = "failure.log";
  
  private final static Pattern SOURCE_FILE_PATTERN = Pattern.compile(".*\\.hs");
  
  private List<FileResult> results;
  
  private TestFile fileTester = new TestFile();
  
  private File csvFile;
  
  public void testPackage() throws IOException {
    testPackage("AC-Vector");
    System.out.println(csvFile.getAbsolutePath());
  }
  
  public List<FileResult> testPackage(String pkg) throws IOException {
    results = new LinkedList<FileResult>();
    
    if (LOGGING)
      System.out.println(pkg + " starting");
    
    File dir;
    try { 
      dir = cabalUnpack(pkg);
    } catch (ExecutionError e) {
      String msg = e.getCause() == null ? "unknown cause" : e.getCause().getMessage();
      System.out.println("[" + pkg + "] " + "cabal unpack failed: " + msg);
      return results;
    }
    
    csvFile = new File(dir + ".csv");
    new FileResult().writeCSVHeader(csvFile.getAbsolutePath());
    
    testFiles(dir, pkg);
    
    if (LOGGING)
      System.out.println(pkg + " done");
    
    return results;
  }
  
  private void logResult(FileResult result) throws IOException {
    results.add(result);
    result.appendAsCSV(csvFile.getAbsolutePath());
  }
  
  public void testFiles(File dir, String pkg) throws IOException {
    for (File f : dir.listFiles())
      if (f.isFile() && SOURCE_FILE_PATTERN.matcher(f.getName()).matches())
        logResult(fileTester.testFile(f, pkg));
      else if (f.isDirectory())
        testFiles(f, pkg);
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
