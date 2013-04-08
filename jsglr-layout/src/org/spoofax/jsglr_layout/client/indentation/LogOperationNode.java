package org.spoofax.jsglr_layout.client.indentation;

import java.util.Map;

import org.spoofax.jsglr_layout.client.AbstractParseNode;

import static org.spoofax.jsglr_layout.client.indentation.CompilableLayoutNode.ParseTimeInvokeType.*;

public class LogOperationNode extends LogicalNode {

  public static enum Operator {

    AND {
      @Override
      public boolean operate(boolean b1, boolean b2) {
        return b1 && b2;
      }

      @Override
      public Boolean operateSingle(boolean b) {
        return b ? null : b;
      }

      @Override
      public String getSymbol() {
        return "&&";
      }

      @Override
      public String getOperateSingleCode(String argument) {
        return "(!" + argument + "? false : null)"; 
      }
    },
    OR {
      @Override
      public boolean operate(boolean b1, boolean b2) {
        return b1 || b2;
      }

      @Override
      public Boolean operateSingle(boolean b) {
        return b ? b : null;
      };

      @Override
      public String getSymbol() {
        return "||";
      }

      @Override
      public String getOperateSingleCode(String argument) {
        return "("+argument+"? true : null)";
      }
    };

    public abstract boolean operate(boolean b1, boolean b2);
    
    public abstract Boolean operateSingle(boolean b);

    public abstract String getSymbol();
    
    public abstract String getOperateSingleCode(String argument);

  }

  private Operator operator;

  private static Operator fromParseSymbol(String symbol) {
    if (symbol.equals("and")) {
      return Operator.AND;
    }
    if (symbol.equals("or")) {
      return Operator.OR;
    }
    return null;
  }

  public LogOperationNode(BooleanNode operand1, BooleanNode operand2,
      String parseSymbol) {
    this(operand1, operand2, fromParseSymbol(parseSymbol));
  }

  public LogOperationNode(BooleanNode operand1, BooleanNode operand2,
      Operator operator) {
    super(new BooleanNode[] { operand1, operand2 });
    this.operator = operator;
  }

  @Override
  public Boolean evaluate(AbstractParseNode[] kids, Map<String, Object> env,
      boolean parseTime) {
    // Evaluate first operand
    Boolean b1 = this.operands[0].evaluate(kids, env, parseTime);
    // Check whether the result is determined only by first value
    if (b1 != null) {
      Boolean result = this.operator.operateSingle(b1);
      if (result != null) {
        return result;
      }
    }
    // Need to evaluate second operand
    Boolean b2 = this.operands[1].evaluate(kids, env, parseTime);
    // Check whether both operand are null
    if (b1 == null && b2 == null) {
      return null;
    } else if (b1 == null) {
      // Check whether operand result determines result
      return this.operator.operateSingle(b2);
    } else {
      // Operate
      return this.operator.operate(b1, b2);
    }
  }

  @Override
  public String getCompiledParseTimeCode() {
    
  }

  @Override
  public String getCompiledDisambiguationTimeCode() {
    return "(" + this.operands[0].getCompiledDisambiguationTimeCode()
        + this.operator.getSymbol()
        + this.operands[1].getCompiledDisambiguationTimeCode() + ")";
  }

  @Override
  public ParseTimeInvokeType getParseTimeInvokeType() {
    // Cannot combine because sometimes the operator does not need to evaluate
    // one operand
    ParseTimeInvokeType type0 = this.operands[0].getParseTimeInvokeType();
    ParseTimeInvokeType type1 = this.operands[1].getParseTimeInvokeType();
    // Check whether one is safely invokable
    if (type0 == SAFELY_INVOKABLE || type1 == SAFELY_INVOKABLE) {
      // When both are safe, everything is safe
      if (type0 == SAFELY_INVOKABLE && type1 == SAFELY_INVOKABLE) {
        return SAFELY_INVOKABLE;
      }
      // Otherwise it is always unsafe
      return UNSAFELY_INVOKABLE;
    }
    // Now both are not safely
    if (type0 == UNSAFELY_INVOKABLE || type1 == UNSAFELY_INVOKABLE) {
      // One is unsafe, so is is unsafe
      return UNSAFELY_INVOKABLE;
    }
    // Now it is not invokable
    return NOT_INVOKABLE;
  }

}
