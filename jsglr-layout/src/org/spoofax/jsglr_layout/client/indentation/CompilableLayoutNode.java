package org.spoofax.jsglr_layout.client.indentation;

import java.util.Map;

import org.spoofax.jsglr_layout.client.AbstractParseNode;

/**
 * A {@link CompilableLayoutNode} is a {@link LayoutNode} which can be compiled.
 * That means that the node is able to produce a single (!) statement of Java
 * code which takes an array of {@link AbstractParseNode}s and a {@link Map} for
 * environment variables which evaluates whether the kids are valid.<br>
 * It is very important that the
 * {@link #getCompiledCode(LocalVariableManager, boolean)} Method does only a
 * single statement returning its result. This makes concatenating of statements
 * of child nodes very easy. The methods may use local variables to cache
 * calculation results. The declaration of these local variables must not be
 * included in the generated code. The names of these variables have to be
 * required from the {@link LocalVariableManager}. Please make sure that all
 * variables required in a generation method are released before the method
 * returns. <br>
 * The {@link CompilableLayoutNode} supports code generation for parse and
 * disambiguation time because some constraints cannot be evaluated at parse
 * time and to optimize the code. This behavior is modeled by the
 * {@link InvokeState} interface which distinguishes the tree cases: invokable
 * at parse time, not invokable at parse time and unsafely invokable parse time,
 * which means that the code has to check whether its input value are valid (ex.
 * not null).
 * 
 * @author moritzlichter
 * 
 * @param <V>
 *          the return value of the LayoutNode for evaluation the tree at
 *          runtime. This value may differ from the return type of the generated
 *          code!
 */
public interface CompilableLayoutNode<V> extends LayoutNode<V> {

  /**
   * The {@link InvokeState} enumeration models the three different states,
   * {@link CompilableLayoutNode}s may have respecting their invocation.
   * 
   * @author moritzlichter
   * 
   */
  public static enum InvokeState {

    /**
     * A {@link CompilableLayoutNode} is safely invokable, when it is able to
     * evaluate its given {@link AbstractParseNode} array under all
     * circumstances to a non null (or similar) value.
     */
    SAFELY_INVOKABLE {

      @Override
      public InvokeState combine(InvokeState type) {
        // return the given type
        return type;
      }

    },
    /**
     * A {@link CompilableLayoutNode} is unsafely invokable, when it may not
     * evaluate its given {@link AbstractParseNode} array to a nun null (or
     * similar) value. This may be causes by parse time restriction or by the
     * {@link InvokeState} of children.
     */
    UNSAFELY_INVOKABLE {

      @Override
      public InvokeState combine(InvokeState type) {
        // Stays UNSAFLEY when SAFLY is given, otherwise given type
        if (type == SAFELY_INVOKABLE) {
          return this;
        } else {
          return type;
        }
      }

    },
    /**
     * A {@link CompilableLayoutNode} is not invokable, when it is not able to
     * evaluate its given {@link AbstractParseNode} array to a nun null (or
     * similar) value. This may be caused by parse time restrictions or the
     * {@link InvokeState} of children.
     */
    NOT_INVOKABLE {

      @Override
      public InvokeState combine(InvokeState type) {
        // Stays not invokable
        return this;
      }

    };

    /**
     * Combines the current {@link InvokeState} with the given one and returns
     * the less worthy one: {@link #SAFELY_INVOKABLE} more valuable than
     * {@link #UNSAFELY_INVOKABLE} more valuable than {@link #NOT_INVOKABLE}.
     * 
     * @param state
     *          the state to combine with
     * @return the less worthy {@link InvokeState}
     */
    public abstract InvokeState combine(InvokeState state);

  }

  /**
   * Generates the code to evaluate the {@link CompilableLayoutNode}. The code
   * needs to refer to the argument {@link AbstractParseNode} array as "$1" and
   * to the environment variable map as "$2".<br>
   * This method should generate code for parse time or disambiguation time
   * depending on the atParseTime argument. The given
   * {@link LocalVariableManager} may be used to get local variables for the
   * code. All local variables required must be released before returning.<br>
   * This method returns a SINGLE statement which evaluates the node. So the
   * results from different child nodes can be combined easily! No "return"
   * statement.
   * 
   * @param manager
   *          the {@link LocalVariableManager} to manager local variables
   * @param atParseTime
   *          whether to generate code for parse or disambiguation time
   * @return the generated statement to evaluate the node
   */
  public String getCompiledCode(LocalVariableManager manager,
      boolean atParseTime);

  /**
   * Returns the {@link InvokeState} for the current node.
   * 
   * @param atParseTime
   *          whether to return the state for parse or disambiguation time
   * @return the {@link InvokeState} of the node
   */
  public InvokeState getInvokeState(boolean atParseTime);

}
