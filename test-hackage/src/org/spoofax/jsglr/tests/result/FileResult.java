package org.spoofax.jsglr.tests.result;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.tests.haskell.Utilities;

/**
 * @author seba
 *
 */
public class FileResult {
  
  public String pkg;
  public String file;
  public boolean success;

  public boolean skipped;
  public boolean cppPreprocess;
  public boolean makeExplicitLayout;
  public boolean makeImplicitLayout;
  public boolean ambInfix;

  public DataPoint<Integer> linesOfCode = new DataPoint<Integer>(-1);
  public DataPoint<Integer> byteSize = new DataPoint<Integer>(-1);
  
  public DataPoint<Boolean> parseOk = new DataPoint<Boolean>(false);
  public DataPoint<Integer> time = new DataPoint<Integer>(-2);
  public DataPoint<Integer> ambiguities = new DataPoint<Integer>(0);
  public DataPoint<Boolean> stackOverflow = new DataPoint<Boolean>(false);
  public DataPoint<String> parseExceptions = new DataPoint<String>("");
  public DataPoint<String> otherExceptions = new DataPoint<String>("");
  public DataPoint<Boolean> timeout = new DataPoint<Boolean>(false);
  
  public DataPoint<Integer> layoutFilterCallsParseTime = new DataPoint<Integer>(-1);
  public DataPoint<Integer> layoutFilteringParseTime = new DataPoint<Integer>(-1);
  public DataPoint<Integer> layoutFilterCallsDisambiguationTime = new DataPoint<Integer>(-1);
  public DataPoint<Integer> layoutFilteringDisambiguationTime = new DataPoint<Integer>(-1);
  public DataPoint<Integer> enforcedNewlineSkips = new DataPoint<Integer>(-1);

  public DataPoint<Integer> differencesToReferenceParser = new DataPoint<Integer>(-1);
  
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
  
  private void write(StringBuilder out, IStrategoTerm t) {
    writeString(out, Utilities.termToString(t));
  }

  private void write(StringBuilder out, Object o) {
    if (o instanceof IStrategoTerm)
      write(out, (IStrategoTerm) o);
    else if (o instanceof Integer)
      writeInt(out, (Integer) o);
    else if (o instanceof Long)
      writeLong(out, (Long) o);
    else if (o == null)
      writeString(out, "null");
    else
      writeString(out, o.toString());
  }
  
  private void write(StringBuilder out, DataPoint<? extends Object> d) {
    write(out, d.t1); writeSem(out);
    write(out, d.t2); writeSem(out);
    write(out, d.t3);
  }

  private void writeSem(StringBuilder out) {
    out.append(';');
  }
  
  private void writeString(StringBuilder out, String s) {
    out.append(escapeString(s));
  }
  
  private void writeLong(StringBuilder out, Long l) {
    if (l == null)
      out.append("null");
    out.append(l);
  }
  
  private void writeInt(StringBuilder out, Integer i) {
    if (i == null)
      out.append("null");
    out.append(i);
  }
  
  private String escapeString(String s) {
    return "\"" + s.replace("\"", "\"\"") + "\"";
  }
  
  private void writeAsCSV(StringBuilder builder) {
    write(builder, pkg); writeSem(builder);
    write(builder, file); writeSem(builder);
    write(builder, success); writeSem(builder);
    
    write(builder, skipped); writeSem(builder);
    write(builder, cppPreprocess); writeSem(builder);
    write(builder, makeExplicitLayout); writeSem(builder);
    write(builder, makeImplicitLayout); writeSem(builder);
    write(builder, ambInfix); writeSem(builder);
    
    write(builder, linesOfCode); writeSem(builder);
    write(builder, byteSize); writeSem(builder);
    
    write(builder, parseOk); writeSem(builder);
    write(builder, time); writeSem(builder);
    write(builder, ambiguities); writeSem(builder);
    write(builder, stackOverflow); writeSem(builder);
    write(builder, timeout); writeSem(builder);
    write(builder, parseExceptions); writeSem(builder);
    write(builder, otherExceptions); writeSem(builder);
    
    write(builder, layoutFilterCallsParseTime); writeSem(builder);
    write(builder, layoutFilteringParseTime); writeSem(builder);
    write(builder, layoutFilterCallsDisambiguationTime); writeSem(builder);
    write(builder, layoutFilteringDisambiguationTime); writeSem(builder);
    write(builder, enforcedNewlineSkips); writeSem(builder);
    
    write(builder, differencesToReferenceParser); writeSem(builder);
    
    builder.append('\n');
  }

  private void writeCSVHeader(StringBuilder builder) {
    write(builder, "package"); writeSem(builder);
    write(builder, "source file"); writeSem(builder);
    write(builder, "success"); writeSem(builder);
    
    write(builder, "skipped"); writeSem(builder);
    write(builder, "cpp ok"); writeSem(builder);
    write(builder, "explicit layout OK"); writeSem(builder);
    write(builder, "implicit layout OK"); writeSem(builder);
    write(builder, "ambInfix error"); writeSem(builder);
    
    writeDataPointDesc(builder, "lines of code"); writeSem(builder);
    writeDataPointDesc(builder, "byte size"); writeSem(builder);
    
    writeDataPointDesc(builder, "parse ok"); writeSem(builder);
    writeDataPointDesc(builder, "time"); writeSem(builder);
    writeDataPointDesc(builder, "ambiguities"); writeSem(builder);
    writeDataPointDesc(builder, "stack overflow"); writeSem(builder);
    writeDataPointDesc(builder, "timeout"); writeSem(builder);
    writeDataPointDesc(builder, "parse exception"); writeSem(builder);
    writeDataPointDesc(builder, "other exception"); writeSem(builder);
    
    writeDataPointDesc(builder, "layout filter calls (parse time)"); writeSem(builder);
    writeDataPointDesc(builder, "layout filtering (parse time)"); writeSem(builder);
    writeDataPointDesc(builder, "layout filter calls (disambiguation time)"); writeSem(builder);
    writeDataPointDesc(builder, "layout filtering (disambigutation time)"); writeSem(builder);
    writeDataPointDesc(builder, "enforced newline skips"); writeSem(builder);
    
    writeDataPointDesc(builder, "differences to expl layout"); writeSem(builder);
    
    builder.append('\n');
  }
  
  private void writeDataPointDesc(StringBuilder builder, String desc) {
    write(builder, "expl layout: " + desc); writeSem(builder);
    write(builder, "impl layout: " + desc); writeSem(builder);
    write(builder, "impl layout (preprocessed): " + desc);
  }
  
}
