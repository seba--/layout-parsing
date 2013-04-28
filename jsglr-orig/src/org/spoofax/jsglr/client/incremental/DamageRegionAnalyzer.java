package org.spoofax.jsglr.client.incremental;

import static org.spoofax.jsglr.client.imploder.AbstractTokenizer.findLeftMostLayoutToken;
import static org.spoofax.jsglr.client.imploder.AbstractTokenizer.findRightMostLayoutToken;
import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getLeftToken;
import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getRightToken;
import static org.spoofax.jsglr.client.incremental.IncrementalSGLR.isRangeOverlap;
import static org.spoofax.terms.SimpleTermVisitor.tryGetListIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.spoofax.interpreter.terms.ISimpleTerm;
import org.spoofax.jsglr.client.imploder.IToken;

/**
 * Analyzes the trees before and after incremental parsing,
 * determining the tree nodes that are in the damage region.
 * 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class DamageRegionAnalyzer {

	final IncrementalSortSet incrementalSorts;
	
	final int damageStart;

	final int damageEnd;
	
	final int damageSizeChange;

	public DamageRegionAnalyzer(IncrementalSGLR<?> parser, int damageStart, int damageEnd, int damageSizeChange) {
		this.incrementalSorts = parser.incrementalSorts;
		this.damageStart = damageStart;
		this.damageEnd = damageEnd;
		this.damageSizeChange = damageSizeChange;
	}
	
	/**
	 * Gets all non-list tree nodes from the original tree
	 * that are in the damage region according to {@link #isDamageTreeNode}.
	 * Tries to take the innermost {@link #incrementalSorts} tree node
	 * where possible instead of taking an inner non-incremental tree node.
	 */
	public List<ISimpleTerm> getDamageNodes(ISimpleTerm tree) {
		List<ISimpleTerm> results = new ArrayList<ISimpleTerm>();
		getDamageRegionTreeNodes(tree, results, true, 0, null);
		return results;
	}
	
	/**
	 * Gets all non-list tree nodes from the partial result tree
	 * that are in the damage region according to {@link #isDamageTreeNode}.
	 * Tries to take the innermost {@link #incrementalSorts} tree node
	 * where possible instead of taking an inner non-incremental tree node.
	 */
	public List<ISimpleTerm> getDamageNodesForPartialTree(ISimpleTerm tree, int skippedChars) {
		List<ISimpleTerm> results = new ArrayList<ISimpleTerm>();
		getDamageRegionTreeNodes(tree, results, false, skippedChars, null);
		return results;
	}

	/**
	 * Gets all non-list tree nodes from a tree
	 * that are in the damage region according to {@link #isDamageTreeNode}.
	 * Tries to take the innermost {@link #incrementalSorts} tree node
	 * where possible instead of taking an inner non-incremental tree node.
	 * 
	 * @param tree                  Current tree node
	 * @param results               Found tree nodes
	 * @param isOriginalTree        Whether the tree is the input tree of the incremental parser 
	 * @param skippedChars          See {@link IncrementalInputBuilder#getLastSkippedCharsBeforeDamage()}.
	 * @param outerIncrementalNode  The innermost ancestor of the current tree that is in {@link #incrementalSorts}.
	 * 
	 * @return the last ancestral tree node added to the list
	 */
	private ISimpleTerm getDamageRegionTreeNodes(ISimpleTerm tree, List<ISimpleTerm> results,
			boolean isOriginalTree, int skippedChars, ISimpleTerm outerIncrementalNode) {
		
		if (incrementalSorts.isIncrementalNode(tree))
			outerIncrementalNode = tree;
		
		if (!tree.isList() // prefer adding list children
				&& isDamageTreeNode(tree, isOriginalTree, skippedChars))
			return addDamageRegionTreeNode(tree, results, outerIncrementalNode);
		
		// Recurse
		boolean addedChild = false;
		Iterator<ISimpleTerm> iterator = tryGetListIterator(tree); 
		for (int i = 0, max = tree.getSubtermCount(); i < max; i++) {
			ISimpleTerm child = iterator == null ? tree.getSubterm(i) : iterator.next();
			ISimpleTerm addedAncestor = getDamageRegionTreeNodes(child, results, isOriginalTree, skippedChars, outerIncrementalNode);
			if (addedAncestor != null && addedAncestor == outerIncrementalNode)
				return addedAncestor;
			addedChild = true;
		}
		
		if (!addedChild && tree.isList() // add list (outerIncrementalNode), if we must
				&& isDamageTreeNode(tree, isOriginalTree, skippedChars))
			return addDamageRegionTreeNode(tree, results, outerIncrementalNode);
		
		return null;
	}

	private ISimpleTerm addDamageRegionTreeNode(ISimpleTerm tree,
			List<ISimpleTerm> results, ISimpleTerm outerIncrementalNode) {
		assert results.indexOf(outerIncrementalNode) == -1
			|| results.get(results.indexOf(outerIncrementalNode)) != outerIncrementalNode
			: "Exact copy of node to be added already added";
		if (outerIncrementalNode != null) {
			results.add(outerIncrementalNode);
		} else {
			// Will be caught by sanity check in IncrementalSGLR
			// throw new IncrementalSGLRException("Precondition failed: unsafe change to tree node of type "
			// 		+ getSort(tree) + " at line " + getLeftToken(tree).getLine());
			results.add(tree);
		}
		return outerIncrementalNode;
	}

	/**
	 * Determines if the damage region affects a particular tree node,
	 * looking only at those tokens that actually belong to the node
	 * and not to its children.
	 */
	protected boolean isDamageTreeNode(ISimpleTerm tree, boolean isOriginalTree, int skippedChars) {
		IToken current = findLeftMostLayoutToken(getLeftToken(tree));
		IToken last = findRightMostLayoutToken(getRightToken(tree));
		if (current != null && last != null) {
			if (!isDamagedRange(
					current.getStartOffset(), last.getEndOffset(), isOriginalTree, skippedChars))
				return false;
			Iterator<ISimpleTerm> iterator = tryGetListIterator(tree); 
			for (int i = 0, max = tree.getSubtermCount(); i < max; i++) {
				ISimpleTerm child = iterator == null ? tree.getSubterm(i) : iterator.next();
				IToken childLeft = findLeftMostLayoutToken(getLeftToken(child));
				IToken childRight = findRightMostLayoutToken(getRightToken(child));
				if (childLeft != null && childRight != null) {
					if (childLeft.getIndex() > current.getIndex()
							&& isDamagedRange(
									current.getEndOffset() + 1, childLeft.getStartOffset() - 1,
									isOriginalTree, skippedChars)) {
						return true;
					}
					current = childRight;
				}
			}
			return isDamagedRange(
					current.getEndOffset() + 1, last.getEndOffset(), isOriginalTree, skippedChars);
		} else {
			return false;
		}
	}
	
	private boolean isDamagedRange(int startOffset, int endOffset,
			boolean isOriginalTree, int skippedChars) {
		if (isOriginalTree) {
			return /*endOffset >= startOffset
				&&*/ isRangeOverlap(damageStart, damageEnd, startOffset, endOffset);
		} else {
			return /*endOffset >= startOffset
				&&*/ isRangeOverlap(damageStart - skippedChars, damageEnd - skippedChars + damageSizeChange,
						startOffset, endOffset);
		}
	}
}
