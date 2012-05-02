package org.spoofax.jsglr.tests.haskell;

import junit.framework.AssertionFailedError;

/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 *
 */
public class TimeoutFailure extends AssertionFailedError {
  private static final long serialVersionUID = -5087489318213833543L;

  private final String file;

  public TimeoutFailure(String file) {
    super("Parser timed out for " + file);
    this.file = file;
  }
  
  public String getFile() {
    return file;
  }
}
