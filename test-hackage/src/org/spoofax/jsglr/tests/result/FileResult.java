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
  public String path;
  
  public boolean allSuccess;
  public boolean allNull;

  public boolean skipped;
  public boolean cppPreprocess;
  public boolean makeExplicitLayout;
  public boolean makeImplicitLayout;
  public boolean ambInfix;
  public long referenceTime;

  public DataPoint<Integer> linesOfCode = new DataPoint<Integer>(-1);
  public DataPoint<Integer> byteSize = new DataPoint<Integer>(-1);
  
  public DataPoint<Boolean> parseOk = new DataPoint<Boolean>(false);
  public DataPoint<Boolean> normalizeOk = new DataPoint<Boolean>(false);
  public DataPoint<Long> time = new DataPoint<Long>(-2l);
  public DataPoint<Integer> ambiguities = new DataPoint<Integer>(0);
  public DataPoint<Boolean> stackOverflow = new DataPoint<Boolean>(false);
  public DataPoint<Boolean> outOfMemory = new DataPoint<Boolean>(false);
  public DataPoint<String> parseExceptions = new DataPoint<String>("");
  public DataPoint<String> otherExceptions = new DataPoint<String>("");
  public DataPoint<Boolean> timeout = new DataPoint<Boolean>(false);
  public DataPoint<Long> memoryBefore = new DataPoint<Long>(-1l);
  public DataPoint<Long> memoryAfter = new DataPoint<Long>(-1l);
  
  
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
    writeAsCSV(builder, 1);
    writeAsCSV(builder, 2);
    writeAsCSV(builder, 3);
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
  
  private void write(StringBuilder out, DataPoint<? extends Object> d, int run) {
    write(out, d.get(run));
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
  
  private void writeAsCSV(StringBuilder builder, int run) {
    write(builder, pkg); writeSem(builder);
    write(builder, path); writeSem(builder);
    write(builder, run); writeSem(builder);

    write(builder, allSuccess); writeSem(builder);
    write(builder, allNull); writeSem(builder);

    write(builder, skipped); writeSem(builder);
    write(builder, cppPreprocess); writeSem(builder);
    write(builder, makeExplicitLayout); writeSem(builder);
    write(builder, makeImplicitLayout); writeSem(builder);
    write(builder, ambInfix); writeSem(builder);
    write(builder, referenceTime); writeSem(builder);
    
    write(builder, linesOfCode, run); writeSem(builder);
    write(builder, byteSize, run); writeSem(builder);
    
    write(builder, parseOk, run); writeSem(builder);
    write(builder, normalizeOk, run); writeSem(builder);
    write(builder, time, run); writeSem(builder);
    write(builder, ambiguities, run); writeSem(builder);
    write(builder, stackOverflow, run); writeSem(builder);
    write(builder, outOfMemory, run); writeSem(builder);
    write(builder, timeout, run); writeSem(builder);
    write(builder, memoryBefore, run); writeSem(builder);
    write(builder, memoryAfter, run); writeSem(builder);
    
    write(builder, parseExceptions, run); writeSem(builder);
    write(builder, otherExceptions, run); writeSem(builder);
    
    write(builder, layoutFilterCallsParseTime, run); writeSem(builder);
    write(builder, layoutFilteringParseTime, run); writeSem(builder);
    write(builder, layoutFilterCallsDisambiguationTime, run); writeSem(builder);
    write(builder, layoutFilteringDisambiguationTime, run); writeSem(builder);
    write(builder, enforcedNewlineSkips, run); writeSem(builder);
    
    write(builder, differencesToReferenceParser, run); writeSem(builder);
    
    builder.append('\n');
  }

  private void writeCSVHeader(StringBuilder builder) {
    write(builder, "package"); writeSem(builder);
    write(builder, "path"); writeSem(builder);
    write(builder, "run"); writeSem(builder);

    write(builder, "all success"); writeSem(builder);
    write(builder, "all null"); writeSem(builder);

    write(builder, "skipped"); writeSem(builder);
    write(builder, "cpp ok"); writeSem(builder);
    write(builder, "explicit layout OK"); writeSem(builder);
    write(builder, "implicit layout OK"); writeSem(builder);
    write(builder, "ambInfix error"); writeSem(builder);
    write(builder, "reference time"); writeSem(builder);
    
    write(builder, "lines of code"); writeSem(builder);
    write(builder, "byte size"); writeSem(builder);
    
    write(builder, "parse ok"); writeSem(builder);
    write(builder, "normalize ok"); writeSem(builder);
    write(builder, "time"); writeSem(builder);
    write(builder, "ambiguities"); writeSem(builder);
    write(builder, "stack overflow"); writeSem(builder);
    write(builder, "out of memory"); writeSem(builder);
     write(builder, "timeout"); writeSem(builder);
    write(builder, "used mem before"); writeSem(builder);
    write(builder, "used mem after"); writeSem(builder);
    
    write(builder, "parse exception"); writeSem(builder);
    write(builder, "other exception"); writeSem(builder);
    
    write(builder, "layout filter calls (parse time)"); writeSem(builder);
    write(builder, "layout filtering (parse time)"); writeSem(builder);
    write(builder, "layout filter calls (disambiguation time)"); writeSem(builder);
    write(builder, "layout filtering (disambigutation time)"); writeSem(builder);
    write(builder, "enforced newline skips"); writeSem(builder);
    
    write(builder, "diffs to 1"); writeSem(builder);
    
    builder.append('\n');
  }

  
}
