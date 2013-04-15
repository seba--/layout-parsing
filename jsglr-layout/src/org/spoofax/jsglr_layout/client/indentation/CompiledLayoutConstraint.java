package org.spoofax.jsglr_layout.client.indentation;

import java.util.Map;

import org.spoofax.jsglr_layout.client.AbstractParseNode;

public interface CompiledLayoutConstraint {
  
  public int evaluateParseTime(AbstractParseNode[] kids, Map<String,Object> maps);
  public int evaluateDisambiguationTime(AbstractParseNode[] kids, Map<String, Object> maps);
  
}
