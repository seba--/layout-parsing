package org.spoofax.jsglr_layout.client.indentation;

/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 *
 */
public class SkipToLayoutRule implements ILayoutRule {
  
  private final int skipTo;
  
  public SkipToLayoutRule(int skipTo) {
    this.skipTo = skipTo;
  }
  
  public int newIndent(int oldIndent) {
    int diff = oldIndent % skipTo;
    return oldIndent + (diff == 0 ? skipTo : diff);
  }
}
