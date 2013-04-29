package org.spoofax.jsglr_layout.client.indentation;

public abstract class NodeOperatorNode<V> implements CompilableLayoutNode<V> {

  protected final AbstractParseNodeNode operand;

  public NodeOperatorNode(AbstractParseNodeNode operand) {
    super();
    this.operand = operand;
  }

}
