package org.spoofax.jsglr.client.indentation;

/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 */
public interface ILayoutRule {
  public int newIndent(int oldIndent);
}
