package org.spoofax.jsglr_layout.client.indentation;

import java.util.Map;

import org.spoofax.jsglr_layout.client.AbstractParseNode;

public class NegationNode extends LogicalNode {

  public NegationNode(BooleanNode operand) {
    super(new BooleanNode[]{operand});
  }

  @Override
  public Boolean evaluate(AbstractParseNode[] kids, Map<String, Object> env,
      boolean parseTime) {
    Boolean value = this.operands[0].evaluate(kids, env, parseTime);
    if (value == null) {
      return null;
    }
    return ! value;
  }

  @Override
  public String getCompiledParseTimeCode() {
    switch (this.operands[0].getParseTimeInvokeType()) {
    case NOT_INVOKABLE:
      return "null";
    case SAFELY_INVOKABLE:
      return "(!"+this.operands[0].getCompiledParseTimeCode()+")";
    case UNSAFELY_INVOKABLE:
      default:
      return "(" + this.operands[0].getCompiledParseTimeCode() + " == null ? null : !" + this.operands[0].getCompiledParseTimeCode() + ")";
    }
  }

  @Override
  public String getCompiledDisambiguationTimeCode() {
    return "(!"+this.operands[0].getCompiledDisambiguationTimeCode()+")";
  }

  @Override
  public ParseTimeInvokeType getParseTimeInvokeType() {
    return this.operands[0].getParseTimeInvokeType();
  }

}
