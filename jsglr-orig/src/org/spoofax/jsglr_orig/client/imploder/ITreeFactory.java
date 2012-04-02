package org.spoofax.jsglr_orig.client.imploder;

import java.util.List;

import org.spoofax.interpreter.terms.ISimpleTerm;
import org.spoofax.jsglr_orig.client.ITreeBuilder;

/**
 * Constructs tree nodes for imploded parse trees.
 * 
 * @see TreeBuilder  An {@link ITreeBuilder} that creates imploded trees.
 * @see ISimpleTerm     An interface for tree nodes with tokens.
 * 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public interface ITreeFactory<TNode> extends ITreeInspector<TNode> {

	/**
	 * Create a new non-terminal node (or a terminal with only a constructor).
	 */
	TNode createNonTerminal(String sort, String constructor, IToken leftToken, IToken rightToken,
			List<TNode> children);
	
	/**
	 * Create a new terminal node for an int value.
	 */
	TNode createIntTerminal(String sort, IToken token, int value);
	
	/**
	 * Create a new terminal node for an real value.
	 */
	TNode createRealTerminal(String sort, IToken token, double value);
	
	/**
	 * Create a new terminal node for a string token.
	 */
	TNode createStringTerminal(String sort, IToken leftToken, IToken rightToken, String value);
	
	TNode createTuple(String elementSort, IToken leftToken, IToken rightToken, List<TNode> children);
	
	/**
	 * Create a new node list. 
	 */
	TNode createList(String elementSort, IToken leftToken, IToken rightToken, List<TNode> children);

	TNode createTop(TNode tree, String filename, int ambiguityCount);

	TNode createAmb(List<TNode> alternatives, IToken leftToken, IToken rightToken);
	
	/**
	 * Creates a new node similar to an existing node,
	 * used for incremental tree construction.
	 */
	TNode recreateNode(TNode oldNode, IToken leftToken, IToken rightToken, List<TNode> children);

	/**
	 * Create an injection node.
	 */
	TNode createInjection(String sort, List<TNode> children);

	void setEnableTokens(boolean enableTokens);
}
