package org.spoofax.jsglr.tests.haskell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

import org.spoofax.interpreter.terms.IStrategoTerm;

/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 */
public abstract class ChainedTestCase extends TestCase {
  
  
  private List<ParseComparisonFailure> failures = new ArrayList<ParseComparisonFailure>();
  private int okCount = 0;
  private int okFailCount = 0;
  private int noParseCount = 0;
  private int ambInfixCount = 0;
  
  
  protected ParseComparisonFailure logComparisonFailure(String file, IStrategoTerm expected, IStrategoTerm actual) {
    ParseComparisonFailure f = new ParseComparisonFailure(file, expected, actual);
    failures.add(f);
    return f;
  }
  
  protected void addAllFailures(Collection<? extends ParseComparisonFailure> failures) {
    this.failures.addAll(failures);
  }
  
  protected void addOks(int okCount) {
    this.okCount += okCount;
  }
  
  protected void addAmbInfix(int ambInfixCount) {
    this.ambInfixCount += ambInfixCount;
  }
  
  protected void addOkFails(int okFailCount) {
    this.okFailCount += okFailCount;
  }
  
  protected void logOk() {
    okCount++;
  }
  
  protected void logAmbInfix() {
    ambInfixCount++;
  }
  
  protected void logOkFail() {
    okFailCount++;
  }
  
  protected void addNoParses(int noParseCount) {
    this.noParseCount += noParseCount;
  }
  
  protected void logNoParse() {
    noParseCount++;
  }
  
  protected String getShortLog() {
    return okCount + " ok, " + okFailCount + " okFail, " + ambInfixCount + " ambInfix, " + noParseCount + " no parse, " + failures.size() + " parse comparison failures";
  }
  
  protected void printShortLog() {
    System.out.println(getShortLog());
  }
  
  protected int getOkCount() {
    return okCount;
  }
  
  protected int getAmbInfixCount() {
    return ambInfixCount;
  }
  
  protected int getOkFailCount() {
    return okFailCount;
  }
  
  protected int getNoParseCount() {
    return noParseCount;
  }

  protected boolean hasFailures() {
    return !failures.isEmpty();
  }
  
  protected void reset() {
    failures.clear();
    okCount = 0;
    okFailCount = 0;
    noParseCount = 0;
    ambInfixCount = 0;
  }
  
  protected void raiseFailures() throws AssertionError {
    if (!failures.isEmpty())
      throw failures.get(0);
  }
  
  protected List<ParseComparisonFailure> getFailures() {
    return failures;
  }
  
  /*
   * Similar to assertEquals but does not throw an exception.
   */
  protected static boolean equals(Object expected, Object actual) {
    if (expected == null && actual == null)
      return true;
    if (expected != null && expected.equals(actual))
      return true;
    return false;
  }
  
  
}
