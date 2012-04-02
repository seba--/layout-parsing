package org.spoofax.jsglr.tests.haskell;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class DeleteUnicode {

  public static void deleteUnicode(String inFile, String outFile) throws IOException {
    writeToFile(outFile, deleteUnicode(readFileAsString(inFile)));
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

  public static void writeToFile(String file, String content) throws IOException {
    FileOutputStream fos = new FileOutputStream(file);
    fos.write(content.getBytes());
    fos.close();
  }
}