package org.spoofax.jsglr.client.imploder;

import org.spoofax.jsglr.client.AbstractParseNode;
import org.spoofax.jsglr.client.CycleParseNode;
import org.spoofax.jsglr.client.ITreeBuilder;
import org.spoofax.jsglr.client.ParseNode;
import org.spoofax.jsglr.client.ParseProductionNode;

/**
 * An abstract top-down tree builder implementation.
 * For most uses, the bottom-up one should be more suitable.
 * 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public abstract class TopdownTreeBuilder implements ITreeBuilder {

	public void initialize(int productionCount, int labelCount) {
		// No initialization
	}

	/**
	 * @deprecated
	 *   For a best balance of performance and stack consumption, directly call
	 *   {@link ParseNode#buildTreeBottomup} instead.
	 */
	public Object buildTree(AbstractParseNode node) {
		if (node.isParseNode()) {
			return buildTreeNode((ParseNode) node);
		} else if (node.isParseProductionNode()) {
			return buildTreeProduction((ParseProductionNode) node);
		} else {
			assert node.isAmbNode();
			return buildTreeAmb((ParseNode)node);
		}
	}
	
	public void reset() {
		// Do nothing by default
	}

	public Object buildTreeTop(Object subtree, int ambiguityCount) {
		return subtree;
	}

	public abstract Object buildTreeNode(ParseNode node);

	public abstract Object buildTreeProduction(ParseProductionNode node);

	public abstract Object buildTreeAmb(ParseNode node);
	
	public abstract Object buildTreeCycle(CycleParseNode node);
}
