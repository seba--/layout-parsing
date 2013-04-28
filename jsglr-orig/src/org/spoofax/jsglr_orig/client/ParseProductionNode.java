/*
 * Created on 30.mar.2006
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 *
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr_orig.client;

import org.spoofax.jsglr_orig.client.imploder.TopdownTreeBuilder;



public class ParseProductionNode extends AbstractParseNode {

	private static final AbstractParseNode[] NO_CHILDREN =
		new AbstractParseNode[0];

	public final int prod;
	public final int line;
	public final int column;

    public ParseProductionNode(int prod, int line, int column) {
        this.prod = prod;
        this.line = line;
        this.column = column;
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
    public Object toTreeTopdown(TopdownTreeBuilder builder) {
      return builder.buildTreeProduction(this);
    }

    @Override
    public String toString() {
        return "" + prod + (prod >= 32 ? (":" + (char) prod) : "");
    }

    @Override
    public int getLabel() { return prod; }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ParseProductionNode))
            return false;
        return prod == ((ParseProductionNode)obj).prod && line == ((ParseProductionNode)obj).line && column == ((ParseProductionNode)obj).column;
    }

    @Override
    public int hashCode() {
        return 6359 * prod + line * 9192 + column >> 2;
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
  public int[] getEnd() {
     return new int[] {line, column};
  }
}
