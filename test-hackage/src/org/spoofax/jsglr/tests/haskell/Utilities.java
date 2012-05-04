package org.spoofax.jsglr.tests.haskell;

import java.io.FileOutputStream;
import java.io.IOException;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.terms.io.InlinePrinter;

public class Utilities {
  public static void writeToFile(IStrategoTerm t, String f) {
    String content;
    if (t == null)
      content = "null";
    else
      content = termToString(t);
    writeToFile(content, f);
  }
  
  public static String termToString(IStrategoTerm t) {
    InlinePrinter printer = new InlinePrinter();
    t.prettyPrint(printer);
    return printer.getString();
  }
  
  public static void writeToFile(String s, String f) {
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(f);
      fos.write(s.getBytes());
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      if (fos != null)
        try {
          fos.close();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
    }
  }
  
}

