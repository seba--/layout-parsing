package org.spoofax.jsglr_layout.client.indentation;

import java.util.Map;

import org.spoofax.jsglr_layout.client.AbstractParseNode;

public class ConstantNode implements IntegerNode {
  
  private final int value;
  
  public ConstantNode(int value) {
    super();
    this.value = value;
  }

  @Override
  public Integer evaluate(AbstractParseNode[] kids, Map<String, Object> env,
      boolean parseTime) {
    return this.value;
  }

  @Override
  public String getCompiledCode(LocalVariableManager manager, boolean atParseTime) {
    return Integer.toString(this.value);
  }

  @Override
  public InvokeState getInvokeState(boolean atParseTime) {
    return InvokeState.SAFELY_INVOKABLE;
  }

}
