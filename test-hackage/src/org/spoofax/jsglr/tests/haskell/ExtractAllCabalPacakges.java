package org.spoofax.jsglr.tests.haskell;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.spoofax.jsglr.tests.haskell.CommandExecution.ExecutionError;

/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 */
public class ExtractAllCabalPacakges {

  public final static String PACKAGE_LIST_FILE = "src/org/spoofax/jsglr/tests/haskell/all-packages.list";

  /**
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {
    List<String> packages = listCabalPackages();
    packages = filterDuplicates(packages);
    writePackages(packages, PACKAGE_LIST_FILE);
    System.out.println(new File("src/org/spoofax/jsglr/tests/haskell/all-packages.list").getAbsolutePath());
  }

  private static List<String> listCabalPackages() {
    String[] cmds = new String[] {TestConfiguration.CABAL_COMMAND, "list", "--simple-output"};
    
    System.out.println(CommandExecution.commandLineAsString(cmds));
    
    CommandExecution.SILENT_EXECUTION = false;
    CommandExecution.SUB_SILENT_EXECUTION = false;
    
    final ArrayList<String> list = new ArrayList<String>();
    
    OutputStream out = new OutputStream() {
      @Override
      public void write(byte[] b) throws IOException {
        String s = new String(b);
        list.add(s.trim());
      }

      @Override
      public void write(int b) throws IOException {
        throw new UnsupportedOperationException();
      }
    };
    
    try {
      CommandExecution.execute(out, System.err, null, cmds);
    } catch (ExecutionError e) {
      String s = list.isEmpty() ? "" : (": " + list.get(list.size() - 1));
      throw new ExecutionError(e.getMessage() + s, e.getCmds(), e.getExitValue(), e.getMessages(), e);
    }

    try {
      out.close();
    } catch (IOException e) {
    }

    return list;
  }

  private static List<String> filterDuplicates(List<String> list) {
    List<String> result = new ArrayList<String>();
    
    for (int i = 0; i < list.size(); i++) {
      String thisName = list.get(i).split(" ")[0];
      
      if (i + 1 >= list.size())
        result.add(thisName);
      else {
        String nextName = list.get(i+1).split(" ")[0];
        if (!nextName.equals(thisName))
          result.add(thisName);
      }
    }
    
    return result;
  }
  
  private static void writePackages(List<String> list, String file) throws IOException {
    BufferedOutputStream out = null;
    
    try {
      out = new BufferedOutputStream(new FileOutputStream(file));
      for (String s : list) {
        out.write(s.getBytes());
        out.write('\n');
      }
    } finally {
      if (out != null)
        out.close();
    }
  }
}
