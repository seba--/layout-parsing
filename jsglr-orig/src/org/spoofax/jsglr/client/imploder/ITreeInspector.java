package org.spoofax.jsglr.client.imploder;

import org.spoofax.interpreter.terms.ISimpleTerm;

/**
 * @see ITreeFactory  Constructs tree nodes.
 * @see ISimpleTerm      An interface for tree nodes with tokens.
 *
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public interface ITreeInspector<TNode> {
	
	/**
	 * Gets the string value of a string terminal,
	 * or returns null for other node types.
	 */
	String tryGetStringValue(TNode node);
	
	/**
	 * Gets the children of a node.
	 */
	Iterable<TNode> getChildren(TNode node);
	
	/**
	 * Gets the left token of a node, or null if not supported/applicable.
	 */
	IToken getLeftToken(TNode node);
	
	/**
	 * Gets the right token of a node, or null if not supported/applicable.
	 */
	IToken getRightToken(TNode node);
	
	/**
	 * Gets the ambiguous branches of an 'amb' node,
	 * or returns null if the given node is not an 'amb' node.
	 */
	Iterable<TNode> tryGetAmbChildren(TNode node);

}
