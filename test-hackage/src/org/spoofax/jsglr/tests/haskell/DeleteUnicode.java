package org.spoofax.jsglr.tests.haskell;

import java.io.IOException;

public class DeleteUnicode {

  public static void deleteUnicode(String inFile, String outFile) throws IOException {
    Utilities.writeToFile(deleteUnicode(Utilities.readFileAsString(inFile)), outFile);
  }

  public static String deleteUnicode(String s) {
    StringBuilder builder = new StringBuilder();

    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (c < 128)
        builder.append(c);
    }
    return builder.toString();
  }
}