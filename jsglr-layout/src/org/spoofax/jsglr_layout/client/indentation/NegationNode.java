package org.spoofax.jsglr_layout.client.indentation;

import java.util.Map;

import org.spoofax.jsglr_layout.client.AbstractParseNode;
import org.spoofax.jsglr_layout.client.indentation.LocalVariableManager.LocalVariable;

public class NegationNode extends LogicalNode {

  public NegationNode(BooleanNode operand) {
    super(new BooleanNode[] { operand });
  }

  @Override
  public Boolean evaluate(AbstractParseNode[] kids, Map<String, Object> env,
      boolean parseTime) {
    Boolean value = this.operands[0].evaluate(kids, env, parseTime);
    if (value == null) {
      return null;
    }
    return !value;
  }

  @Override
  public String getCompiledParseTimeCode(LocalVariableManager manager) {
    switch (this.operands[0].getParseTimeInvokeType()) {
    case NOT_INVOKABLE:
      return "Integer.MIN_VALUE";
    case SAFELY_INVOKABLE:
      return "((" + this.operands[0].getCompiledParseTimeCode(manager) + ") == 0 ? 1 : 0";
    case UNSAFELY_INVOKABLE:
    default:
      LocalVariable var = manager.getFreeLocalVariable(Integer.class);
      return "(" + "(" + var.getName() + " " + this.operands[0].getCompiledParseTimeCode(manager)
          + ")" +" == Integer.MIN_VALUE ? Integer.MIN_VALUE : ((" + this.operands[0].getCompiledParseTimeCode(manager)
          + ") == 0 ? 1: 0)";
    }
  }

  @Override
  public String getCompiledDisambiguationTimeCode(LocalVariableManager manager) {
    return "((" + this.operands[0].getCompiledDisambiguationTimeCode(manager) + ") == 0 ? 1 : 0)";
  }

  @Override
  public ParseTimeInvokeType getParseTimeInvokeType() {
    return this.operands[0].getParseTimeInvokeType();
  }

}
