package org.spoofax.jsglr.tests.result;

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
    FileOutputStream out = new FileOutputStream(file);
    try {
      out.write(getAsCSVString().getBytes());
    } finally {
      out.close();
    }
  }
  
  public void appendAsCSV(String file) throws IOException {
    OutputStream out = new FileOutputStream(file, true);
    try {
      out.write(getAsCSVString().getBytes());
    } finally {
      out.close();
    }
  }
  
  public String getAsCSVString() {
    StringBuilder builder = new StringBuilder();
    writeAsCSV(builder);
    return builder.toString();
  }
  
  public void writeCSVHeader(String file) throws IOException {
    FileOutputStream out = new FileOutputStream(file);
    try {
      out.write(getCSVHeaderString().getBytes());
    } finally {
      out.close();
    }
  }
  
  public void appendCSVHeader(String file) throws IOException {
    OutputStream out = new FileOutputStream(file, true);
    try {
      out.write(getCSVHeaderString().getBytes());
    } finally {
      out.close();
    }
  }
  
  public String getCSVHeaderString() {
    StringBuilder builder = new StringBuilder();
    writeCSVHeader(builder);
    return builder.toString();
  }
  
  private void write(StringBuilder out, SGLRException e) {
    writeString(out, e.getMessage());
  }
  

  private void write(StringBuilder out, IStrategoTerm t) {
    writeString(out, Utilities.termToString(t));
  }

  private void write(StringBuilder out, Object o) {
    if (o instanceof SGLRException)
      write(out, (SGLRException) o);
    else if (o instanceof IStrategoTerm)
      write(out, (IStrategoTerm) o);
    else if (o == null)
      writeString(out, "null");
    else
      writeString(out, o.toString());
  }
  
  private void write(StringBuilder out, DataPoint<? extends Object> d) {
    write(out, d.t1); writeSem(out);
    write(out, d.t2); writeSem(out);
    write(out, d.t3); writeSem(out);
  }

  private void writeSem(StringBuilder out) {
    writeString(out, ";");
  }
  
  private void writeString(StringBuilder out, String s) {
    out.append(escapeString(s));
  }
  
  private String escapeString(String s) {
    return "\"" + s.replace("\"", "\"\"") + "\"";
  }
  
  private void writeAsCSV(StringBuilder builder) {
    write(builder, pkg); writeSem(builder);
    write(builder, this.file); writeSem(builder);
    
    write(builder, makeExplicitLayout); writeSem(builder);
    write(builder, makeImplicitLayout); writeSem(builder);
    
    write(builder, ambInfix); writeSem(builder);
    
    write(builder, linesOfCode); writeSem(builder);
    write(builder, byteSize); writeSem(builder);
    
    write(builder, syntaxTrees); writeSem(builder);
    write(builder, ambiguities); writeSem(builder);
    write(builder, stackOverflow); writeSem(builder);
    write(builder, parseExceptions); writeSem(builder);
    write(builder, timeout); writeSem(builder);
    
    write(builder, layoutFilterCallsParseTime); writeSem(builder);
    write(builder, layoutFilteringParseTime); writeSem(builder);
    write(builder, layoutFilterCallsDisambiguationTime); writeSem(builder);
    write(builder, layoutFilteringDisambiguationTime); writeSem(builder);
    write(builder, enforcedNewlineSkips); writeSem(builder);
    
    write(builder, differencesToReferenceParser); writeSem(builder);
  }

  private void writeCSVHeader(StringBuilder builder) {
    write(builder, "package"); writeSem(builder);
    write(builder, "source file"); writeSem(builder);
    
    write(builder, "explicit layout OK"); writeSem(builder);
    write(builder, "implicit layout OK"); writeSem(builder);
    
    write(builder, "ambInfix error"); writeSem(builder);
    
    write(builder, "lines of code"); writeSem(builder);
    write(builder, "byte size"); writeSem(builder);
    
    writeDataPointDesc(builder, "AST"); writeSem(builder);
    writeDataPointDesc(builder, "ambiguities"); writeSem(builder);
    writeDataPointDesc(builder, "stack overflow"); writeSem(builder);
    writeDataPointDesc(builder, "parse exception"); writeSem(builder);
    writeDataPointDesc(builder, "timeout"); writeSem(builder);
    
    writeDataPointDesc(builder, "layout filter calls (parse time)"); writeSem(builder);
    writeDataPointDesc(builder, "layout filtering (parse time)"); writeSem(builder);
    writeDataPointDesc(builder, "layout filter calls (disambiguation time)"); writeSem(builder);
    writeDataPointDesc(builder, "layout filtering (disambigutation time)"); writeSem(builder);
    writeDataPointDesc(builder, "enforced newline skips"); writeSem(builder);
    
    write(builder, differencesToReferenceParser); writeSem(builder);
  }
  
  private void writeDataPointDesc(StringBuilder builder, String desc) {
    write(builder, "expl layout: "); write(builder, desc); writeSem(builder);
    write(builder, "impl layout: "); write(builder, desc); writeSem(builder);
    write(builder, "impl layout (preprocessed): "); write(builder, desc);
  }
  
}
