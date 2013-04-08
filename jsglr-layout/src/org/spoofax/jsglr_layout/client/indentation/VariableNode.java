package org.spoofax.jsglr_layout.client.indentation;

import java.util.Map;

import org.spoofax.jsglr_layout.client.AbstractParseNode;

public class VariableNode<V> implements CompilableLayoutNode<V> {

  private final String name;
  
  public VariableNode(String name) {
    super();
    this.name = name;
  }

  @Override
  public V evaluate(AbstractParseNode[] kids, Map<String, Object> env,
      boolean parseTime) {
    return (V) env.get(this.name);
  }

}
