package org.spoofax.jsglr.tests.result;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr.tests.haskell.Utilities;

/**
 * @author seba
 *
 */
public class FileResult {
  
  public String pkg;
  public String file;
  
  public boolean makeExplicitLayout;
  public boolean makeImplicitLayout;
  
  public boolean ambInfix;
  
  public DataPoint<Integer> linesOfCode;
  public DataPoint<Integer> byteSize;
  
  public DataPoint<IStrategoTerm> syntaxTrees;
  public DataPoint<Integer> ambiguities;
  public DataPoint<Boolean> stackOverflow;
  public DataPoint<SGLRException> parseExceptions;
  public DataPoint<Boolean> timeout;
  
  public DataPoint<Integer> layoutFilterCallsParseTime;
  public DataPoint<Integer> layoutFilteringParseTime;
  public DataPoint<Integer> layoutFilterCallsDisambiguationTime;
  public DataPoint<Integer> layoutFilteringDisambiguationTime;
  public DataPoint<Integer> enforcedNewlineSkips;

  public DataPoint<Integer> differencesToReferenceParser;
  
  
  public void writeAsCSV(String file) throws IOException {
    FileOutputStream fos = new FileOutputStream(file);
    try {
      writeAsCSV(fos);
    } finally {
      fos.close();
    }
  }
  
  public void appendAsCSV(String file) throws IOException {
    OutputStream out = new BufferedOutputStream(new FileOutputStream(file, true));
    try {
      writeAsCSV(out);
    } finally {
      out.close();
    }
  }
  
  private void writeAsCSV(OutputStream out) throws IOException {
    write(out, pkg); writeSem(out);
    write(out, this.file); writeSem(out);
    
    write(out, makeExplicitLayout); writeSem(out);
    write(out, makeImplicitLayout); writeSem(out);
    
    write(out, ambInfix); writeSem(out);
    
    write(out, linesOfCode); writeSem(out);
    write(out, byteSize); writeSem(out);
    
    write(out, syntaxTrees); writeSem(out);
    write(out, ambiguities); writeSem(out);
    write(out, stackOverflow); writeSem(out);
    write(out, parseExceptions); writeSem(out);
    write(out, timeout); writeSem(out);
    
    write(out, layoutFilterCallsParseTime); writeSem(out);
    write(out, layoutFilteringParseTime); writeSem(out);
    write(out, layoutFilterCallsDisambiguationTime); writeSem(out);
    write(out, layoutFilteringDisambiguationTime); writeSem(out);
    write(out, enforcedNewlineSkips); writeSem(out);
    
    write(out, differencesToReferenceParser); writeSem(out);
  }
  
  private void write(OutputStream out, SGLRException e) throws IOException {
    writeString(out, e.getMessage());
  }
  

  private void write(OutputStream out, IStrategoTerm t) throws IOException {
    writeString(out, Utilities.termToString(t));
  }

  private void write(OutputStream out, Object o) throws IOException {
    if (o instanceof SGLRException)
      write(out, (SGLRException) o);
    else if (o instanceof IStrategoTerm)
      write(out, (IStrategoTerm) o);
    else 
      writeString(out, o.toString());
  }
  
  private void write(OutputStream out, DataPoint<? extends Object> d) throws IOException {
    write(out, d.t1); writeSem(out);
    write(out, d.t2); writeSem(out);
    write(out, d.t3); writeSem(out);
  }

  private void writeSem(OutputStream out) throws IOException {
    writeString(out, ";");
  }
  
  private void writeString(OutputStream out, String s) throws IOException {
    out.write(escapeString(s).getBytes());
  }
  
  private String escapeString(String s) {
    return "\"" + s.replace("\"", "\"\"") + "\"";
  }
}
