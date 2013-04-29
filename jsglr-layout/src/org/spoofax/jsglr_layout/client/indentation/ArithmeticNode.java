package org.spoofax.jsglr_layout.client.indentation;

public abstract class ArithmeticNode<V> implements CompilableLayoutNode<V> {

  protected final IntegerNode[] operands;
  
  public ArithmeticNode(IntegerNode[] operands) {
    super();
    this.operands = operands;
  }
  
}
