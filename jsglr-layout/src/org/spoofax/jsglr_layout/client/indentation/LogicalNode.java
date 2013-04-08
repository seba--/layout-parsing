package org.spoofax.jsglr_layout.client.indentation;

public abstract class LogicalNode implements BooleanNode {
  
  protected BooleanNode[] operands;
  
  public LogicalNode(BooleanNode[] operands) {
    super();
    this.operands = operands;
  }

}
