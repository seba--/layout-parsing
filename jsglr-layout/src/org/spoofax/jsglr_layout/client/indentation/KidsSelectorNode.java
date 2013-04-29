package org.spoofax.jsglr_layout.client.indentation;

import java.util.Map;

import org.spoofax.jsglr_layout.client.AbstractParseNode;

public class KidsSelectorNode implements AbstractParseNodeNode {

  private final int arrayIndex;

  public KidsSelectorNode(int index) {
    super();
    this.arrayIndex = 2 * (index -1);
  }

  @Override
  public AbstractParseNode evaluate(AbstractParseNode[] kids,
      Map<String, Object> env, boolean parseTime) {
    return kids[this.arrayIndex];
  }

  @Override
  public String getCompiledCode(LocalVariableManager manager, boolean atParseTime) {
    return "$1["+this.arrayIndex+"]";
  }

  @Override
  public InvokeState getInvokeState(boolean atParseTime) {
    // Can always be invoked on parse time
    return InvokeState.SAFELY_INVOKABLE;
  }

}
