package org.spoofax.jsglr_layout.client.indentation;

import java.util.HashMap;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr_layout.client.AbstractParseNode;
import org.spoofax.jsglr_layout.client.ParseNode;
import org.spoofax.jsglr_layout.client.ParseTable;

/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 * 
 */
public class LayoutFilter {

  // Config variables for Debugging

  // False: Use compiled constraints, True: Use interpreter
  public static final boolean DEBUG_DO_NOT_COMPILE_LAYOUT_CONSTRAINTS = false;
  // True: Compare result of compiled constraints to the result of the layout tree
  public static final boolean DEBUG_COMPARE_COMPILED_TO_LAYOUT_TREE = false;
  // True: Compare result of compiled constraints to the result of the interpreter
  public static final boolean DEBUG_COMPARE_COMPILED_TO_INTERPRETER = false;
  // True: Parse Table needs to pass the IStrategoTerm source to the ProductionAttributes
  @SuppressWarnings("unused")
  public static final boolean NEEDS_CONSTRAINTS_SOURCE = DEBUG_DO_NOT_COMPILE_LAYOUT_CONSTRAINTS || DEBUG_COMPARE_COMPILED_TO_INTERPRETER
      || DEBUG_COMPARE_COMPILED_TO_LAYOUT_TREE;

  private final boolean atParseTime;
  private ParseTable parseTable;

  private int filterCallCount = 0;

  /**
   * 
   * @param parseTable
   * @param atParseTime
   *          if true, the filter is conservative: it only discards trees whose parser state can be
   *          discarded in general. This is needed when discarding trees at parse time, when a later
   *          join may represent alternative trees by the same parser state.
   */
  public LayoutFilter(ParseTable parseTable, boolean atParseTime) {
    this.parseTable = parseTable;
    this.atParseTime = atParseTime;
  }

  public int getFilterCallCount() {
    return filterCallCount;
  }

  public int getDisambiguationCount() {
    return 0;
  }

  public boolean hasValidLayout(ParseNode t) {
    return hasValidLayout(t.getLabel(), t.getChildren());
  }

  public boolean hasValidLayout(int label, AbstractParseNode[] kids) {
    IStrategoTerm layoutConstraintSource = null;
    if (NEEDS_CONSTRAINTS_SOURCE) {
      layoutConstraintSource = this.parseTable.getLabel(label).getAttributes().getLayoutConstraintSource();
    }
    CompiledLayoutConstraint compiledConstraint = this.parseTable.getLabel(label).getAttributes().getLayoutConstraint();

    if (compiledConstraint == null) {
      return true;
    }
    filterCallCount++;
    Boolean b;
    if (DEBUG_DO_NOT_COMPILE_LAYOUT_CONSTRAINTS) {
      b = new LayoutNodeInterpreter(this.parseTable, this.atParseTime).interpretConstraint(layoutConstraintSource, kids);
    } else {
      // Evaluate the constraint
      int val;
      if (this.atParseTime)
        val = compiledConstraint.evaluateParseTime(kids, null);
      else
        val = compiledConstraint.evaluateDisambiguationTime(kids, null);

      // Convert val to Boolean
      if (val == 1) {
        b = true;
      } else if (val == 0) {
        b = false;
      } else {
        b = null;
      }

      // Debug Method checks the value of the compiled constraint

      // Compares the result of the compiled constraint to the result of the
      // interpreter
      if (DEBUG_COMPARE_COMPILED_TO_INTERPRETER) {
        Boolean check = new LayoutNodeInterpreter(this.parseTable, this.atParseTime).interpretConstraint(layoutConstraintSource, kids);
        if (b != check) {
          System.out.println("Compiled Constraint + " + b + " differes from Interpreter " + check + " for " + layoutConstraintSource
              + " at parse time " + this.atParseTime);
        }
      }
      // Compares the result of the compiled constraint to the result of the
      // layout tree
      if (DEBUG_COMPARE_COMPILED_TO_LAYOUT_TREE) {
        BooleanNode bNode = LayoutNodeCompiler.buildConstraintTree(layoutConstraintSource, BooleanNode.class);
        Boolean check = bNode.evaluate(kids, new HashMap<String, Object>(), this.atParseTime);
        if (b != check) {
          System.out.println("Compiled Constraint + " + b + " differes from Layout Tree " + check + " for " + layoutConstraintSource
              + " at parse time " + this.atParseTime);
        }
      }

    }

    // When b is true, the constraint cannot be evaluated --> return true to
    // move to disambiguation time
    if (b == null)
      return true;
    return b;
  }

}
