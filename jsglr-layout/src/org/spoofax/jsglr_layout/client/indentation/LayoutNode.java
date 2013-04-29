package org.spoofax.jsglr_layout.client.indentation;

import java.util.Map;

import org.spoofax.jsglr_layout.client.AbstractParseNode;

/**
 * The {@link LayoutNode} is a node in the tree structure to evaluate a layout
 * constraint. The node is able the evaluate the constraints for an array of
 * {@link AbstractParseNode} and a map for environment variables at parse or
 * disambiguation time. This interface does not declare methods respecting
 * children of the node because the subclasses define their type and number of
 * children.
 * 
 * @author moritzlichter
 * 
 * @param <V>
 *          the type of the return value of the evaluation
 */
public interface LayoutNode<V> {

  /**
   * Evaluates whether the given {@link AbstractParseNode} array is valid for
   * the layout constraint this node evaluates.
   * 
   * @param kids
   *          the array of {@link AbstractParseNode} to evaluate
   * @param env
   *          the environment map for variable assignments
   * @param parseTime
   *          whether the evaluation is at parse or disambiguation time.
   * @return the result of the evaluation
   */
  public V evaluate(AbstractParseNode[] kids, Map<String, Object> env,
      boolean parseTime);

}
