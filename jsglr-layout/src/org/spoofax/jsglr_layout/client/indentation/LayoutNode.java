package org.spoofax.jsglr_layout.client.indentation;

import java.util.Map;

import org.spoofax.jsglr_layout.client.AbstractParseNode;

public interface LayoutNode<V> {

  public V evaluate(AbstractParseNode[] kids, Map<String, Object> env, boolean parseTime);
  
}
