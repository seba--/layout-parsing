package org.spoofax.jsglr.tests.haskell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.spoofax.interpreter.terms.IStrategoTerm;

/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 */
public abstract class ChainedTestCase extends TestCase {
  
  
  private List<AssertionFailedError> failures = new ArrayList<AssertionFailedError>();
  private int okCount = 0;
  private int okFailCount = 0;
  private int noParseCount = 0;
  private int ambInfixCount = 0;
  private int timeout = 0;
  private int comparisonFailures = 0;
  
  
  protected ParseComparisonFailure logComparisonFailure(String file, IStrategoTerm expected, IStrategoTerm actual) {
    ParseComparisonFailure f = new ParseComparisonFailure(file, expected, actual);
    failures.add(f);
    comparisonFailures++;
    return f;
  }
  
  protected void addAllFailures(Collection<? extends AssertionFailedError> failures) {
    this.failures.addAll(failures);
  }
  
  protected void addOks(int okCount) {
    this.okCount += okCount;
  }
  
  protected void addAmbInfix(int ambInfixCount) {
    this.ambInfixCount += ambInfixCount;
  }
  
  protected void addTimeout(int timeout) {
    this.timeout += timeout;
  }

  protected void addComparisonFailures(int comparisonFailures) {
    this.comparisonFailures += comparisonFailures;
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

  protected void logTimeout(String f) {
    timeout++;
    failures.add(new TimeoutFailure(f));
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
    return okCount + " ok, " + okFailCount + " okFail, " + timeout + " timeout, " + ambInfixCount + " ambInfix, " + noParseCount + " no parse, " + comparisonFailures + " parse comparison failures";
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
  
  protected int getTimeout() {
    return timeout;
  }

  protected int getComparisonFailures() {
    return comparisonFailures;
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
    timeout = 0;
    comparisonFailures = 0;
  }
  
  protected void raiseFailures() throws AssertionError {
    if (!failures.isEmpty()) {
      StringBuilder builder = new StringBuilder();
      for (Iterator<AssertionFailedError> it = failures.iterator(); it.hasNext(); ) {
        builder.append(it.next().getMessage()).append("\n");
        if (it.hasNext())
          builder.append('\n');
      }
      
      throw new AssertionFailedError(builder.toString());
    }
  }
  
  protected List<AssertionFailedError> getFailures() {
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
