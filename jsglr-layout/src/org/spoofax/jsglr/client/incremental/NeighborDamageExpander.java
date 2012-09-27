package org.spoofax.jsglr.client.incremental;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getLeftToken;
import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getRightToken;
import static org.spoofax.jsglr.client.incremental.IncrementalSGLR.isRangeOverlap;
import static org.spoofax.terms.SimpleTermVisitor.tryGetListIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.spoofax.interpreter.terms.ISimpleTerm;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr.client.imploder.Tokenizer;
import org.spoofax.terms.attachments.ParentAttachment;

/**
 * Expands the damage region to the two
 * neighboring tree nodes, if they 
 * correspond to the set of incremental sorts.
 * 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class NeighborDamageExpander {

	private final IncrementalSortSet incrementalSorts;
	
	private final int oldDamageStart;

	private final int oldDamageEnd;

	private final List<ISimpleTerm> damageNodes;
	
	private ISimpleTerm leftNeighbor;
	
	private ISimpleTerm rightNeighbor;

	public NeighborDamageExpander(DamageRegionAnalyzer damageAnalyzer, List<ISimpleTerm> damageNodes, ISimpleTerm oldTree) {
		this.incrementalSorts = damageAnalyzer.incrementalSorts;
		this.oldDamageStart = damageAnalyzer.damageStart;
		this.oldDamageEnd = damageAnalyzer.damageEnd;
		this.damageNodes = initExpandedDamageNodes(damageNodes, oldTree);
	}
	
	public int getExpandedDamageStart() {
		if (leftNeighbor == null) return oldDamageStart;
		return min(oldDamageStart, getLeftToken(leftNeighbor).getStartOffset());
	}
	
	public int getExpandedDamageEnd() {
		if (rightNeighbor == null) return oldDamageEnd;
		return max(oldDamageEnd, getRightToken(rightNeighbor).getEndOffset());
	}
	
	public List<ISimpleTerm> getExpandedDamageNodes() {
		return damageNodes;
	}
	
	private List<ISimpleTerm> initExpandedDamageNodes(List<ISimpleTerm> damageNodes, ISimpleTerm tree) {
		if (damageNodes.size() == 0)
			return Collections.unmodifiableList(damageNodes);
		
		ISimpleTerm first = damageNodes.get(0);
		ISimpleTerm last = damageNodes.get(damageNodes.size() - 1);
		final ISimpleTerm SEEN_LAST = null;
		
		ISimpleTerm parent = ParentAttachment.tryTraverseGetParent(first, tree);
		
		List<ISimpleTerm> results = new ArrayList<ISimpleTerm>(damageNodes.size() + 2);
		ISimpleTerm previous = null;
		
		Iterator<ISimpleTerm> iterator = tryGetListIterator(parent); 
		for (int i = 0, max = parent.getSubtermCount(); i < max; i++) {
			ISimpleTerm child = iterator == null ? parent.getSubterm(i) : iterator.next();
			if (child == first) {
				if (previous != null && incrementalSorts.isIncrementalNode(previous)
						&& !isLeftCollateralDamage(first)) {
					results.add(leftNeighbor = previous);
				} else {
					leftNeighbor = first;
				}
				results.addAll(damageNodes);
			} else if (child == last) {
				if (isRightCollateralDamage(last)) {
					rightNeighbor = last;
				} else {
					last = SEEN_LAST;
				}
			} else if (last == SEEN_LAST) {
				if (incrementalSorts.isIncrementalNode(child))
					results.add(rightNeighbor = child);
				break;
			}
			
			previous = child;
		}
		
		assert results.size() >= damageNodes.size();
		return results;
	}

	boolean isDamagedNode(ISimpleTerm node, boolean considerLeftLayout, boolean considerRightLayout) {
		IToken left = getLeftToken(node);
		IToken right = getRightToken(node);
		if (left == null || right == null) return false;
		if (considerLeftLayout)
			left = Tokenizer.findLeftMostLayoutToken(left);
		if (considerRightLayout)
			right = Tokenizer.findRightMostLayoutToken(right);
		int startOffset = left.getStartOffset();
		int endOffset = right.getEndOffset();
		return isRangeOverlap(oldDamageStart, oldDamageEnd, startOffset, endOffset);
	}

	private boolean isLeftCollateralDamage(ISimpleTerm child) {
		assert isDamagedNode(child, true, true);
		return !isDamagedNode(child, true, false);
	}

	private boolean isRightCollateralDamage(ISimpleTerm child) {
		assert isDamagedNode(child, true, true);
		return !isDamagedNode(child, false, true);
	}
}