package org.spoofax.jsglr_layout.client.indentation;

import static org.spoofax.jsglr_layout.client.indentation.CompilableLayoutNode.ParseTimeInvokeType.NOT_INVOKABLE;
import static org.spoofax.jsglr_layout.client.indentation.CompilableLayoutNode.ParseTimeInvokeType.SAFELY_INVOKABLE;
import static org.spoofax.jsglr_layout.client.indentation.CompilableLayoutNode.ParseTimeInvokeType.UNSAFELY_INVOKABLE;

import java.util.Map;

import org.spoofax.jsglr_layout.client.AbstractParseNode;
import org.spoofax.jsglr_layout.client.indentation.LocalVariableManager.LocalVariable;

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
        return "&";
      }

      @Override
      public String getOperateSingleCode(String argument) {
        return "((" + argument + ") == 0 ? 0 : Integer.MIN_VALUE)";
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
        return "|";
      }

      @Override
      public String getOperateSingleCode(String argument) {
        return "((" + argument + ") == 1 ? 1 : Integer.MIN_VALUE)";
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
  public String getCompiledParseTimeCode(LocalVariableManager manager) {
    // First check to own type
    switch (this.getParseTimeInvokeType()) {
    case NOT_INVOKABLE:
      return "null";
    case SAFELY_INVOKABLE:
      return "(" + this.operands[0].getCompiledParseTimeCode(manager)
          + this.operator.getSymbol()
          + this.operands[1].getCompiledParseTimeCode(manager) + ")";
    case UNSAFELY_INVOKABLE:
    default:
      // Wee need to check which parts are unsafe
      ParseTimeInvokeType type0 = this.operands[0].getParseTimeInvokeType();
      ParseTimeInvokeType type1 = this.operands[1].getParseTimeInvokeType();
      if (type0 == NOT_INVOKABLE) {
        if (type1 == SAFELY_INVOKABLE) {
          return this.getCompiledCodeForOneSafe(this.operands[1], manager);
        } else {
          return this.getCompiledCodeForOneUnsafe(this.operands[1], manager);
        }
      }
      if (type1 == NOT_INVOKABLE) {
        if (type0 == SAFELY_INVOKABLE) {
          return this.getCompiledCodeForOneSafe(this.operands[0], manager);
        } else {
          return this.getCompiledCodeForOneUnsafe(this.operands[0], manager);
        }
      }
      if (type0 == SAFELY_INVOKABLE) {
        return this.getCompiledCodeForOneUnsafeOneSafe(1,0, manager);
      }
      if (type1 == SAFELY_INVOKABLE) {
        return this.getCompiledCodeForOneUnsafeOneSafe(0, 1, manager);
      }
      return this.getCompiledCodeForTwoUnsafe(manager);
    }
  }
  
  private String getCompiledCodeForOneSafe(BooleanNode safe, LocalVariableManager manager) {
    return this.operator.getOperateSingleCode(safe.getCompiledParseTimeCode(manager));
  }

  private String getCompiledCodeForOneUnsafe(BooleanNode unsafe,
      LocalVariableManager manager) {
    LocalVariable var = manager.getFreeLocalVariable(Boolean.class);
    String code = "((" + var.getName() + " = "
        + unsafe.getCompiledParseTimeCode(manager) + ") == Integer.MIN_VALUE ? Integer.MIN_VALUE : "
        + this.operator.getOperateSingleCode(var.getName()) + ")";
    manager.releaseLocalVariable(var);
    return code;
  }

  private String getCompiledCodeForOneUnsafeOneSafe(int unsafe,
      int safe, LocalVariableManager manager) {
    LocalVariable var = manager.getFreeLocalVariable(Boolean.class);
    String code = "("
        + "("
        + var.getName()
        + " = "
        + this.operands[unsafe].getCompiledParseTimeCode(manager)
        + ") == Integer.MIN_VALUE ? "
        + this.operator.getOperateSingleCode(this.operands[safe]
            .getCompiledParseTimeCode(manager)) + " : ";
    if (unsafe < safe) {
      code += "("+ var.getName()  + this.operator.getSymbol() + this.operands[safe].getCompiledParseTimeCode(manager) +")";
    } else {
      code += "(" + this.operands[safe].getCompiledParseTimeCode(manager) + this.operator.getSymbol() + var.getName() +")";
    }
    code += ")";
    manager.releaseLocalVariable(var);
    return code;
  }
  
  private String getCompiledCodeForTwoUnsafe(LocalVariableManager manager) {
    LocalVariable var0 = manager.getFreeLocalVariable(Boolean.class);
    LocalVariable var1 = manager.getFreeLocalVariable(Boolean.class);
    String code = "(" + "(" + var0.getName() + " = " + this.operands[0].getCompiledParseTimeCode(manager) + ") == Integer.MIN_VALUE ? " +
            "(" + "(" + var1.getName() + " = " + this.operands[1].getCompiledParseTimeCode(manager) + ") == Integer.MIN_VALUE ? Integer.MIN_VALUE : " +
            this.operator.getOperateSingleCode(var1.getName()) + ")" + ":" +
            "(" + "(" + var1.getName() + " = " + this.operands[1].getCompiledParseTimeCode(manager) + ") == Integer.MIN_VALUE ? " +
            this.operator.getOperateSingleCode(var0.getName()) + ":" +
            "(" + var0.getName() + this.operator.getSymbol() + var1.getName() +")" +")" +")";
    manager.releaseLocalVariable(var0);
    manager.releaseLocalVariable(var1);
    return code;
  }

  @Override
  public String getCompiledDisambiguationTimeCode(LocalVariableManager manager) {
    return "(" + this.operands[0].getCompiledDisambiguationTimeCode(manager)
        + this.operator.getSymbol()
        + this.operands[1].getCompiledDisambiguationTimeCode(manager) + ")";
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
