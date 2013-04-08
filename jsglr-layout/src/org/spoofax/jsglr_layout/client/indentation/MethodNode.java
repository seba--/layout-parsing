package org.spoofax.jsglr_layout.client.indentation;

import java.util.Map;

import org.spoofax.jsglr_layout.client.AbstractParseNode;

public class MethodNode extends NodeOperatorNode<Integer> implements
    IntegerNode {

  public static enum Method {
    GET_COLUMN {

      @Override
      public int invoke(AbstractParseNode node) {
        return node.getColumn();
      }

      @Override
      public String getCode() {
        return ".getColumn()";
      }
      
    },
    GET_LINE {

      @Override
      public int invoke(AbstractParseNode node) {
        return node.getLine();
      }

      @Override
      public String getCode() {
        return ".getLine()";
      }
      
    };
    
    public abstract int invoke(AbstractParseNode node);
    
    public abstract String getCode();
  }
  
  private Method method;
  
  public MethodNode(AbstractParseNodeNode operand, Method method) {
    super(operand);
    this.method = method;
  }

  @Override
  public Integer evaluate(AbstractParseNode[] kids, Map<String, Object> env,
      boolean parseTime) {
    AbstractParseNode r = this.operand.evaluate(kids, env, parseTime);
    if (r == null) {
      return null;
    } else {
      return this.method.invoke(r);
    }
  }

  @Override
  public String getCompiledParseTimeCode() {
    switch (this.operand.getParseTimeInvokeType()) {
    case SAFELY_INVOKABLE:
      return this.operand.getCompiledParseTimeCode()+this.method.getCode();
    case UNSAFELY_INVOKABLE:
      return this.operand.getCompiledParseTimeCode()+" == null ? null : "+this.operand.getCompiledParseTimeCode() +this.method.getCode();
    case NOT_INVOKABLE:
    default:
      return "null";
    }
  }

  @Override
  public String getCompiledDisambiguationTimeCode() {
    return this.operand.getCompiledParseTimeCode()+this.method.getCode();
  }

  @Override
  public ParseTimeInvokeType getParseTimeInvokeType() {
    return this.operand.getParseTimeInvokeType();
  }

}
