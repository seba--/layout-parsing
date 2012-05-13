package org.spoofax.jsglr.tests.result;

import java.io.IOException;

/**
 * @author seba
 *
 */
public interface FileResultObserver {
  public void observe(FileResult result) throws IOException;
}
