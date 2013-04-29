package org.spoofax.jsglr_layout.client.indentation;

import java.util.Map;

import org.spoofax.jsglr_layout.client.AbstractParseNode;
import org.spoofax.jsglr_layout.client.indentation.LocalVariableManager.LocalVariable;

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
  public String getCompiledCode(LocalVariableManager manager, boolean atParseTime) {
    switch (this.operand.getInvokeState(atParseTime)) {
    case SAFELY_INVOKABLE:
      return this.operand.getCompiledCode(manager, atParseTime)+this.method.getCode();
    case UNSAFELY_INVOKABLE:
      LocalVariable var = manager.getFreeLocalVariable(AbstractParseNode.class);
      String code = "((" + var.getName() + " = " + this.operand.getCompiledCode(manager, atParseTime)+") == null ? Integer.MIN_VALUE : "+var.getName() +this.method.getCode()+")";
      manager.releaseLocalVariable(var);
      return code;
    case NOT_INVOKABLE:
    default:
      return "Integer.MIN_VALUE";
    }
  }

  @Override
  public InvokeState getInvokeState(boolean atParseTime) {
    return this.operand.getInvokeState(atParseTime);
  }

}
