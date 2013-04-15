package org.spoofax.jsglr_layout.client.indentation;

import java.util.Map;

import org.spoofax.jsglr_layout.client.AbstractParseNode;

/**
 * The {@link CompiledLayoutConstraint} is an interface which layout constraint
 * evaluating classes generated at runtime have to implement. It provides two
 * evaluating methods, one for parse time and one for disambiguation time.<br>
 * The methods return an int instead of a boolean to capture the three states:
 * <ul>
 * <li>True: given tree is valid, mapped to 1</li>
 * <li>False: given tree is invalid, mapped to 0</li>
 * <li>Not decidable: it cannot be decided whether the tree is valid, mapped to
 * Integer.MIN_VALUE</li>
 * </ul>
 * This implementation is easier and faster then using the Boolean class,
 * because the Javasisst compiler does not support boxing and unboxing and no
 * objects have to be created but only primitives.
 * 
 * @author moritzlichter
 * 
 */
public interface CompiledLayoutConstraint {

  /**
   * Evaluates whether the given array of kids is valid or not. It checks
   * everything which can be checked at parse time.
   * 
   * @param kids
   *          the array of kids {@link AbstractParseNode}s
   * @param map
   *          map for environment variables
   * @return whether the kids are valid or not
   */
  public int evaluateParseTime(AbstractParseNode[] kids, Map<String, Object> map);

  /**
   * Evaluates whether the given array of kids is valid or not. It checks
   * everything which can be checked at disambiguation time, so evaluates
   * everything which is possible.
   * 
   * @param kids
   *          the array of kids {@link AbstractParseNode}s
   * @param map
   *          map for environment variables
   * @return whether the kids are valid or not
   */
  public int evaluateDisambiguationTime(AbstractParseNode[] kids,
      Map<String, Object> map);

}
