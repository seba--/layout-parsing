package org.spoofax.jsglr.tests.haskell;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

import org.spoofax.jsglr.tests.haskell.CommandExecution.ExecutionError;

/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 */
public class TestPackage extends ChainedTestCase {
  
  private final static boolean LOGGING = false;
  
  public final static String SUCCESS_LOG = "success.log";
  public final static String FAILURE_LOG = "failure.log";
  
  private final static Pattern SOURCE_FILE_PATTERN = Pattern.compile(".*\\.hs");
  
  private TestFile fileTester = new TestFile();
  
  public void testPackage() throws IOException {
    testPackage("Agda");
    printShortLog();
    raiseFailures();
  }
  
  public void testPackage(String pkg) throws IOException {
    
    File dir;
    try { 
      dir = cabalUnpack(pkg);
    } catch (ExecutionError e) {
      System.out.println("[" + pkg + "] " + "cabal unpack failed");
      return;
    }
    
    testFiles(dir, pkg);
    
    logResult(pkg);
    
    addAllFailures(fileTester.getFailures());
    addOks(fileTester.getOkCount());
    addOkFails(fileTester.getOkFailCount());
    addNoParses(fileTester.getNoParseCount());
    addAmbInfix(fileTester.getAmbInfixCount());
    addTimeout(fileTester.getTimeout());
    addComparisonFailures(fileTester.getComparisonFailures());
    fileTester.reset();
  }
  
  private void logResult(String pkg) throws IOException {
    boolean success = fileTester.getFailures().isEmpty();
    
    BufferedWriter writer = new BufferedWriter(new FileWriter(success ? SUCCESS_LOG : FAILURE_LOG, true));
    writer.write(pkg + ": " + (success ? "success" : "failure") + "\n");
    writer.write("  " + fileTester.getShortLog() + "\n");
    
    writer.close();
  }
  
  public void testFiles(File dir, String pkg) throws IOException {
    for (File f : dir.listFiles())
      if (f.isFile() && SOURCE_FILE_PATTERN.matcher(f.getName()).matches())
        fileTester.testFile(f, pkg);
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
