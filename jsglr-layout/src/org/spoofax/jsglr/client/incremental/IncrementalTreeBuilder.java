package org.spoofax.jsglr.client.incremental;

import static java.lang.Math.min;
import static org.spoofax.jsglr.client.imploder.AbstractTokenizer.findLeftMostLayoutToken;
import static org.spoofax.jsglr.client.imploder.AbstractTokenizer.findRightMostLayoutToken;
import static org.spoofax.jsglr.client.imploder.AbstractTokenizer.getTokenAfter;
import static org.spoofax.jsglr.client.imploder.AbstractTokenizer.getTokenBefore;
import static org.spoofax.jsglr.client.imploder.IToken.TK_EOF;
import static org.spoofax.jsglr.client.imploder.IToken.TK_ERROR;
import static org.spoofax.jsglr.client.imploder.IToken.TK_ERROR_EOF_UNEXPECTED;
import static org.spoofax.jsglr.client.imploder.IToken.TK_ERROR_KEYWORD;
import static org.spoofax.jsglr.client.imploder.IToken.TK_UNKNOWN;
import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getLeftToken;
import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getRightToken;
import static org.spoofax.terms.SimpleTermVisitor.tryGetListIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.spoofax.interpreter.terms.ISimpleTerm;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr.client.imploder.ITokenizer;
import org.spoofax.jsglr.client.imploder.ITreeFactory;
import org.spoofax.jsglr.client.imploder.Tokenizer;

/**
 * Constructs the output tree based on the old tree and the list of repaired tree nodes.
 * 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class IncrementalTreeBuilder<TNode extends ISimpleTerm> {
	
	private static final int NO_STOP_OFFSET = Integer.MAX_VALUE;

	private final ITreeFactory<TNode> factory;
	
	private final IncrementalSortSet incrementalSorts;

	private final int damageStart;

	private final int damageEnd;

	private final int skippedChars;
	
	private final int damageSizeChange;

	private final Set<ISimpleTerm> damageNodes;

	private final List<ISimpleTerm> repairedNodes;

	private final Tokenizer newTokenizer;

	private final DamageRegionAnalyzer damageAnalyzer;
	
	private final List<TNode> reconstructedNodes = new ArrayList<TNode>();
	
	private boolean isRepairedNodesInserted;

	/**
	 * @param skippedChars @see {@link IncrementalInputBuilder#getLastSkippedCharsBeforeDamage()}
	 */
	public IncrementalTreeBuilder(IncrementalSGLR<TNode> parser, DamageRegionAnalyzer damageAnalyzer,
			String input, String filename, Set<ISimpleTerm> damageNodes,
			List<ISimpleTerm> repairedTreeNodes, int skippedChars) {
		this.damageAnalyzer = damageAnalyzer;
		this.damageNodes = damageNodes;
		this.factory = parser.factory;
		this.incrementalSorts = damageAnalyzer.incrementalSorts;
		this.damageStart = damageAnalyzer.damageStart;
		this.damageEnd = damageAnalyzer.damageEnd;
		this.skippedChars = skippedChars;
		this.damageSizeChange = damageAnalyzer.damageSizeChange;
		this.repairedNodes = repairedTreeNodes;
		this.newTokenizer = new Tokenizer(input, filename, parser.parser.getParseTable().getKeywordRecognizer());
	}
	
	/**
	 * Builds the output tree based on the old tree and the list of repaired tree nodes.
	 */
	public TNode buildOutput(ISimpleTerm oldTreeNode) throws IncrementalSGLRException {
		isRepairedNodesInserted = false;
		TNode result = buildOutputSubtree(oldTreeNode, 0, false, false);
		assert result != null;
		if (!isRepairedNodesInserted)
			throw new IncrementalSGLRException("Postcondition failed: unable to insert repaired tree nodes in original tree: " + repairedNodes);
		newTokenizer.makeToken(newTokenizer.getStartOffset() - 1, TK_EOF, true);
		newTokenizer.setAst(result);
		boolean assertionsEnabled = false;
		assert assertionsEnabled = true;
		if (assertionsEnabled)
			newTokenizer.initAstNodeBinding(); // eagerly initialize and trigger asserts
		return result;
	}
	
	public List<TNode> getLastReconstructedNodes() {
		return reconstructedNodes;
	}
	
	/**
	 * @return
	 *   A reconstructed subtree, or null if the input tree was damaged
	 *   and could not be repaired. 
	 */
	private TNode buildOutputSubtree(ISimpleTerm treeNode, int offsetChange, boolean expectDamagedNode, boolean isRepairedNode) throws IncrementalSGLRException {
		final List<ISimpleTerm> children;
		final IToken beforeStartToken = newTokenizer.currentToken();
		IToken startToken = getLeftToken(treeNode);
		
		sanityCheckOldTreeNode(treeNode);
		
		if (!isRepairedNode && treeNode.isList() && incrementalSorts.isIncrementalContainerNode(treeNode)) {
			children = buildDamagedSubtree(treeNode, offsetChange, startToken);
		} else if (!isRepairedNode && damageAnalyzer.isDamageTreeNode(treeNode, true, skippedChars)) {
			if (!expectDamagedNode)
				throw new IncrementalSGLRException(new IllegalStateException(
						"Constructing resulting tree failed; unexpected damaged tree node: " + treeNode + " should be in a list of one of the sorts: " + incrementalSorts));
			return null;
		} else {
			children = copyChildrenToList(treeNode);
			for (int i = 0; i < children.size(); i++) {
				ISimpleTerm child = children.get(i);
				
				int myOffsetChange = offsetChange + (isRepairedNodesInserted ? damageSizeChange : 0);
				copyTokens(startToken, findLeftMostLayoutToken(getLeftToken(child)), NO_STOP_OFFSET/*getLeftToken(child).getEndOffset() + 1*/, myOffsetChange);
				startToken = getTokenAfter(getRightToken(child));

				TNode child2 = buildOutputSubtree(child, offsetChange, expectDamagedNode, isRepairedNode);
				if (child2 == null) return null;
				children.set(i, child2);
			}
		}
		int myOffsetChange = offsetChange + (isRepairedNodesInserted ? damageSizeChange : 0);
		IToken stopToken = getTokenAfter(getRightToken(treeNode));
		copyTokens(startToken, stopToken, NO_STOP_OFFSET/*stopToken.getEndOffset() + 1*/, myOffsetChange);
		return buildOutputNode(treeNode, children, beforeStartToken);
	}

	private List<ISimpleTerm> buildDamagedSubtree(ISimpleTerm treeNode, int offsetChange, IToken startToken)
			throws IncrementalSGLRException {
		
		// UNDONE: assert offsetChange == 0 : "Nested incrementalSorts lists?";
		List<ISimpleTerm> results = new ArrayList<ISimpleTerm>(treeNode.getSubtermCount() + repairedNodes.size());
		
		Iterator<ISimpleTerm> iterator = tryGetListIterator(treeNode);
		for (int i = 0, max = treeNode.getSubtermCount(); i < max; i++) {
			ISimpleTerm child = iterator == null ? treeNode.getSubterm(i) : iterator.next();

			if (damageNodes.contains(child)) {
				copyTokensAndTryAddRepairedNodes(treeNode, results, startToken,
						getLeftToken(child), getRightToken(child));
			} else {
				startToken = getTokenAfter(getRightToken(child));
				TNode child2 = buildOutputSubtree(child, offsetChange, true, false);
				if (child2 != null)
					results.add(child2);
			}
		}
		IToken stopToken = getTokenAfter(findRightMostLayoutToken(getRightToken(treeNode)));
		copyTokensAndTryAddRepairedNodes(treeNode, results, startToken, stopToken, stopToken);
		return results;
	}

	private void sanityCheckOldTreeNode(ISimpleTerm oldTreeNode) {
		// (Also checked in IncrementalSGLR precondition.)
		// TODO: incremental parsing with ambiguous trees
		@SuppressWarnings("unchecked")
		TNode tOldTreeNode = (TNode) oldTreeNode;
		assert factory.tryGetAmbChildren(tOldTreeNode) == null :
			"Incremental tree building with ambiguities not implemented";
	}

	@SuppressWarnings("unchecked")
	private TNode buildOutputNode(ISimpleTerm oldTreeNode, List<ISimpleTerm> children, IToken beforeStartToken) {
		IToken startToken;
		if (newTokenizer.currentToken() == beforeStartToken) {
			startToken = newTokenizer.makeToken(newTokenizer.getStartOffset() - 1, TK_UNKNOWN, true);
		} else {
			startToken = getTokenAfter(beforeStartToken);
		}
		return factory.recreateNode((TNode) oldTreeNode, startToken, newTokenizer.currentToken(), (List<TNode>) children);
	}

	private void copyTokensAndTryAddRepairedNodes(ISimpleTerm oldTreeNode, List<ISimpleTerm> results,
			IToken firstToken, IToken stopToken, IToken childEndToken) throws IncrementalSGLRException {
		
		if (!isRepairedNodesInserted && childEndToken.getEndOffset() >= damageStart) {
			//copyTokens(firstToken, stopToken, damageStart, -skippedChars);
			copyTokens(firstToken, stopToken, damageStart, 0);
			insertRepairedNodes(oldTreeNode, results);
			copyTokens(firstToken, stopToken, damageEnd + damageSizeChange - skippedChars + 1, damageSizeChange);
		} else {
			copyTokens(firstToken, stopToken, damageStart, 0);
		}
	}

	private void insertRepairedNodes(ISimpleTerm oldTreeNode, List<ISimpleTerm> results) throws IncrementalSGLRException {
		if (repairedNodes.size() > 0) {
			IToken firstToken = getTokenBefore(getLeftToken(repairedNodes.get(0)));
	
			for (ISimpleTerm node : repairedNodes) {
				copyTokens(firstToken, getLeftToken(node), NO_STOP_OFFSET/*getLeftToken(node).getEndOffset() + 1*/, skippedChars);
				firstToken = getTokenAfter(getRightToken(node));
				
				TNode reconstructed = buildOutputSubtree(node, skippedChars, false, true);
				reconstructedNodes.add(reconstructed);
				results.add(reconstructed);
			}
		}
		isRepairedNodesInserted = true;
	}
	
	/**
	 * Copies tokens to {@link #newTokenizer}.
	 * 
	 * @param startToken
	 *           Start copying at this token.
	 * @param stopToken
	 *           Stop copying just before this token.
	 * @param stopOffset
	 *           Stop copying just before this offset in the original token stream.
	 * @param offsetChange
	 *           The change in offset between the given tokens and the copied tokens in the new tokenizer.
	 */
	private void copyTokens(IToken startToken, IToken stopToken, int stopOffset, int offsetChange) {
		ITokenizer fromTokenizer = startToken.getTokenizer();
		assert fromTokenizer == stopToken.getTokenizer();
		int oldStartOffset = newTokenizer.getStartOffset();
		for (int i = findLeftMostLayoutToken(startToken).getIndex(), last = stopToken.getIndex(); i < last; i++) {
			IToken fromToken = fromTokenizer.getTokenAt(i);
			int myEndOffset = min(stopOffset, fromToken.getEndOffset()) + offsetChange;
			if (myEndOffset < oldStartOffset)
				continue;
			IToken toToken = newTokenizer.makeToken(myEndOffset, fromToken.getKind(), isEssentialToken(fromToken));
			assert toToken == null || myEndOffset < fromToken.getEndOffset() + offsetChange
				|| // toToken.toString().equals(fromToken.toString())
				fromToken.toString().equals(newTokenizer.getInput().substring(
						fromToken.getStartOffset() + offsetChange, fromToken.getEndOffset() + offsetChange + 1))
				: "Expected '" + fromToken + "' in copied tokenstream, not '" + toToken + "'";
		}
	}
	
	private static boolean isEssentialToken(IToken token) {
		switch (token.getKind()) {
			case TK_ERROR: case TK_ERROR_EOF_UNEXPECTED: case TK_ERROR_KEYWORD:
				return true;
			default:
				return false;
		}
	}

	private static List<ISimpleTerm> copyChildrenToList(ISimpleTerm tree) {
		List<ISimpleTerm> results = new ArrayList<ISimpleTerm>(tree.getSubtermCount());
		Iterator<ISimpleTerm> iterator = tryGetListIterator(tree); 
		for (int i = 0, max = tree.getSubtermCount(); i < max; i++) {
			ISimpleTerm child = iterator == null ? tree.getSubterm(i) : iterator.next();
			results.add(child);
		}
		return results;
	}
}
