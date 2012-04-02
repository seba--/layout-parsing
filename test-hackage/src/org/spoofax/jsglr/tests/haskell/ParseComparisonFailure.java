package org.spoofax.jsglr.tests.haskell;

import junit.framework.AssertionFailedError;

import org.spoofax.interpreter.terms.IStrategoTerm;

/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 *
 */
public class ParseComparisonFailure extends AssertionFailedError {
  private static final long serialVersionUID = -5087489318213833543L;

  private final String file;
  private final IStrategoTerm expected;
  private final IStrategoTerm actual;
  
  public ParseComparisonFailure(String file, IStrategoTerm expected, IStrategoTerm actual) {
    super("Comparison failed for " + file);
    this.file = file;
    this.expected = expected;
    this.actual = actual;
  }
  
  public String getFile() {
    return file;
  }

  public IStrategoTerm getExpected() {
    return expected;
  }

  public IStrategoTerm getActual() {
    return actual;
  }
}
