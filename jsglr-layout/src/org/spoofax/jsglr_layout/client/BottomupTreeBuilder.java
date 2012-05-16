package org.spoofax.jsglr_layout.client;

import java.util.List;


/**
 * An abstract bottom-up tree builder implementation.
 *
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public abstract class BottomupTreeBuilder implements ITreeBuilder {

	public void initialize(int productionCount, int labelCount) {
		// Do nothing by default
	}
	
	public Object buildTree(AbstractParseNode node) {
		return node.toTreeBottomup(this);
	}
	
	public void visitLabel(int labelNumber) {
		// Do nothing by default
	}
	
	public void endVisitLabel(int labelNumber) {
		// Do nothing by default
	}
	
	public void reset() {
		// Do nothing by default
	}

	public Object buildTreeTop(Object subtree, int ambiguityCount) {
		return subtree;
	}

	public abstract Object buildNode(int labelNumber, List<Object> subtrees);
	public abstract Object buildProduction(int productionNumber);
	public abstract Object buildAmb(List<Object> alternatives);
	public abstract Object buildCycle(int labelNumber);

}
