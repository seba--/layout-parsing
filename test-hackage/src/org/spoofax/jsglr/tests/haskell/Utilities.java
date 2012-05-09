package org.spoofax.jsglr.tests.haskell;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

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
  
  @SuppressWarnings("deprecation")
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


  public static String readFileAsString(String file) throws IOException {
    StringBuilder fileData = new StringBuilder(1000);
    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8")));
    char[] buf = new char[1024];
    int numRead = 0;
    while ((numRead = reader.read(buf)) != -1)
      fileData.append(buf, 0, numRead);

    reader.close();
    return fileData.toString();
  }
  
  public static String extendPath(String path, String name) {
    return path.isEmpty() ? name : (path + "/" + name);
  }
}

