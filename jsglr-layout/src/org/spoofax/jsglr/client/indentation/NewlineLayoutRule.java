package org.spoofax.jsglr.client.indentation;

/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 */
public class NewlineLayoutRule implements ILayoutRule {
  public int newIndent(int oldIndent) {
    return 0;
  }
}
