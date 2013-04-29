package org.spoofax.jsglr_layout.client.indentation;

import java.util.Map;

import org.spoofax.jsglr_layout.client.AbstractParseNode;

public class ArithOperatorNode extends BinaryArithNode<Integer> implements
    IntegerNode {

  public static enum Operation {
    ADD {
      @Override
      public int operate(int v1, int v2) {
        return v1 + v2;
      }

      @Override
      public String getSymbol() {
        return "+";
      }
    },
    SUB {
      @Override
      public int operate(int v1, int v2) {
        return v1 - v2;
      }

      @Override
      public String getSymbol() {
        return "-";
      }
    },
    MUL {
      @Override
      public int operate(int v1, int v2) {
        return v1 * v2;
      }

      @Override
      public String getSymbol() {
        return "*";
      }
    },
    DIV {
      @Override
      public int operate(int v1, int v2) {
        return v1 / v2;
      }

      @Override
      public String getSymbol() {
        return "/";
      }
    };

    public abstract int operate(int v1, int v2);

    public abstract String getSymbol();

  }

  private final Operation operation;

  private static Operation fromParseSymbol(String symbol) {
    if (symbol.equals("add")) {
      return Operation.ADD;
    }
    if (symbol.equals("sub")) {
      return Operation.SUB;
    }
    if (symbol.equals("mul")) {
      return Operation.MUL;
    }
    if (symbol.equals("div")) {
      return Operation.DIV;
    }
    return null;
  }

  public ArithOperatorNode(IntegerNode operand1, IntegerNode operand2,
      String parseSymbol) {
    this(operand1, operand2, fromParseSymbol(parseSymbol));
  }

  public ArithOperatorNode(IntegerNode operand1, IntegerNode operand2,
      Operation operation) {
    super(operand1, operand2);
    this.operation = operation;
  }

  @Override
  public Integer evaluate(AbstractParseNode[] kids, Map<String, Object> env,
      boolean parseTime) {
    Integer value1 = this.operands[0].evaluate(kids, env, parseTime);
    if (value1 == null) {
      return null;
    }
    Integer value2 = this.operands[1].evaluate(kids, env, parseTime);
    if (value2 == null) {
      return null;
    }
    return this.operation.operate(value1, value2);
  }

  @Override
  protected String getOperatorString() {
    return this.operation.getSymbol();
  }

  @Override
  protected String convertToInt(String val, LocalVariableManager manager) {
    return "("+val+")";
  }

}
