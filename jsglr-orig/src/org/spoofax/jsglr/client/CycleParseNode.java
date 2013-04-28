package org.spoofax.jsglr.client;

import org.spoofax.jsglr.client.imploder.TopdownTreeBuilder;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class CycleParseNode extends AbstractParseNode {

	private static final AbstractParseNode[] NO_CHILDREN =
		new AbstractParseNode[0];
	
	private ParseNode target;
	
	public CycleParseNode(ParseNode target) {
		this.target = target;
	}

	@Override
	public int getNodeType() {
		return CYCLE;
	}

	@Override
	public AbstractParseNode[] getChildren() {
		return NO_CHILDREN;
	}

	@Override
	public Object toTreeBottomup(BottomupTreeBuilder builder) {
		return builder.buildCycle(getTargetLabel());
	}

	public int getTargetLabel() {
		AbstractParseNode target = this.target;
		while (target.isAmbNode()) {
			target = target.getChildren()[0];
		}
		if (target.isParseNode())
			return ((ParseNode) target).getLabel();
		return -1;
	}

	@Override
	public Object toTreeTopdown(TopdownTreeBuilder builder) {
		return builder.buildTreeCycle(this);
	}
	
	@Override
	public void reject() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toStringShallow() {
		return toString();
	}

	@Override
	public String toString() {
		return "cycle(" + target + ")";
	}

	@Override
	public boolean isParseProductionChain() {
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CycleParseNode other = (CycleParseNode) obj;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}

  @Override
  public int getLabel() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int[] getEnd() {
    // TODO Auto-generated method stub
    return null;
  }
}
