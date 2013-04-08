package org.spoofax.jsglr_layout.client.indentation;

import java.util.Map;

import org.spoofax.jsglr_layout.client.AbstractParseNode;

public class ArithComparatorNode extends BinaryArithNode<Boolean> implements
    BooleanNode {

  public static enum Comperator {
    GREATER {
      @Override
      public boolean comparate(int v1, int v2) {
        return v1 > v2;
      }
      @Override
      public String getSymbol() {
        return ">";
      }
    }, GREATER_EQUAL {
      @Override
      public boolean comparate(int v1, int v2) {
        return v1 >= v2;
      }
      @Override
      public String getSymbol() {
        return ">=";
      }
    }, EQUAL {
      @Override
      public boolean comparate(int v1, int v2) {
        return v1 == v2;
      }
      @Override
      public String getSymbol() {
        return "==";
      }
    }, LESS_EQUAL {
      @Override
      public boolean comparate(int v1, int v2) {
        return v1 <= v2;
      }
      @Override
      public String getSymbol() {
        return "<=";
      }
    }, LESS {
      @Override
      public boolean comparate(int v1, int v2) {
        return v1 < v2;
      }
      @Override
      public String getSymbol() {
        return "<";
      }
    }, NOT_EQUAL {
      @Override
      public boolean comparate(int v1, int v2) {
        return v1 != v2;
      }
      @Override
      public String getSymbol() {
        return "!=";
      }
    };
    
    public abstract boolean comparate(int v1, int v2);
    
    public abstract String getSymbol();
    
  }

  private final Comperator comperator;
  
  private static Comperator fromParseSymbol(String symbol) {
    if (symbol.equals("eq")) {
      return Comperator.EQUAL;
    }
    if (symbol.equals("gt")) {
      return Comperator.GREATER;
    }
    if (symbol.equals("ge")) {
      return Comperator.GREATER_EQUAL;
    }
    if (symbol.equals("lt")) {
      return Comperator.LESS;
    }
    if (symbol.equals("le")) {
      return Comperator.LESS_EQUAL;
    }
    return null;
  }

  public ArithComparatorNode(IntegerNode operand1, IntegerNode operand2, String comperatorSymbol) {
    this (operand1, operand2, fromParseSymbol(comperatorSymbol));
  }
  
  public ArithComparatorNode(IntegerNode operand1, IntegerNode operand2, Comperator comperator) {
    super(operand1, operand2);
    this.comperator = comperator;
  }

  @Override
  public Boolean evaluate(AbstractParseNode[] kids, Map<String, Object> env,
      boolean parseTime) {
    Integer value1 = this.operands[0].evaluate(kids, env, parseTime);
    if (value1 == null) {
      return null;
    }
    Integer value2 = this.operands[1].evaluate(kids, env, parseTime);
    if (value2 == null) {
      return null;
    }
    return this.comperator.comparate(value1, value2);
  }

  @Override
  protected String getOperatorString() {
    return this.comperator.getSymbol();
  }

}
