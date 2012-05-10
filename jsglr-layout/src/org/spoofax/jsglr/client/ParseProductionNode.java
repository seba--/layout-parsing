/*
 * Created on 30.mar.2006
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 *
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr.client;



public class ParseProductionNode extends AbstractParseNode {

	private static final AbstractParseNode[] NO_CHILDREN =
		new AbstractParseNode[0];

	public final int prod;

    public ParseProductionNode(int prod, int line, int column) {
      super(line, column);
        this.prod = prod;
    }
    
    @Override
    public void reject() {
    	throw new UnsupportedOperationException();
    }
    
    @Override
	public boolean isParseProductionChain() {
    	return true;
    }

    @Override
	public Object toTreeBottomup(BottomupTreeBuilder builder) {
    	return builder.buildProduction(prod);
    }
    
    @Override
    public String toString() {
        return "\"" + prod + (prod >= 32 ? (":" + (char) prod) : "").replace("\"", "\\\"") + "\"";
    }

    @Override
    public int getLabel() { return prod; }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ParseProductionNode) ||!super.equals(obj))
            return false;
        return prod == ((ParseProductionNode)obj).prod;
    }

    @Override
    public int hashCode() {
        return 6359 * prod + super.hashCode();
    }

    @Override
    public String toStringShallow() {
        return "prod*(" + prod + ")";
    }

	@Override
	public int getNodeType() {
		return AbstractParseNode.PARSE_PRODUCTION_NODE;
	}

	@Override
	public AbstractParseNode[] getChildren() {
		return NO_CHILDREN;
	}

  @Override
  public boolean isEmpty() {
    return false;
  }
  
  @Override
  public AbstractParseNode getLeft() {
    return this;
  }
  
  @Override
  public boolean isLayout() {
    return false;
  }

  @Override
  public boolean isIgnoreLayout() {
    return false;
  }
}
