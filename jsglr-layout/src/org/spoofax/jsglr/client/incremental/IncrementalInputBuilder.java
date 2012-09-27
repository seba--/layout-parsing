package org.spoofax.jsglr.client.incremental;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.spoofax.jsglr.client.imploder.AbstractTokenizer.isErrorInRange;
import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getLeftToken;
import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getRightToken;
import static org.spoofax.jsglr.client.incremental.IncrementalSGLR.DEBUG;
import static org.spoofax.jsglr.client.incremental.IncrementalSGLR.isRangeOverlap;
import static org.spoofax.terms.SimpleTermVisitor.tryGetListIterator;

import java.util.Iterator;
import java.util.Set;

import org.spoofax.interpreter.terms.ISimpleTerm;
import org.spoofax.jsglr.client.imploder.IToken;

/**
 * Constructs an input string for incremental parsing.
 * 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class IncrementalInputBuilder {
	
	private static final boolean INSERT_WHITESPACE = false;

	private final StringBuilder result = new StringBuilder();
	
	private final Set<ISimpleTerm> damageNodes;
	
	private final IncrementalSortSet incrementalSorts;
	
	private final String newInput;

	@SuppressWarnings("unused") // for debugging
	private final String oldInput;
	
	private final int damageStart;
	
	/**
	 * The (inclusive) end offset of the damage region.
	 */
	private final int damageEnd;

	private final int damageSizeChange;
	
	private boolean isSkipping;
	
	private boolean isDamagePrinted;
	
	private int skippedCharsBeforeDamage;
	
	private int skippedCharsAfterDamage;

	/**
	 * Constructs an input string for incremental parsing.
	 * 
	 * @param incrementalSorts
	 *            Sorts that can be incrementally parsed (e.g., MethodDec, ImportDec).
	 *            *Must* be sorts that only occur in lists (such as MethodDec*).
	 */
	public IncrementalInputBuilder(DamageRegionAnalyzer damageAnalyzer, Set<ISimpleTerm> damageNodes,
			String input, String oldInput) {
		
		this.damageNodes = damageNodes;
		this.incrementalSorts = damageAnalyzer.incrementalSorts;
		this.newInput = input;
		this.oldInput = oldInput;
		this.damageEnd = damageAnalyzer.damageEnd;
		this.damageStart = damageAnalyzer.damageStart;
		this.damageSizeChange = damageAnalyzer.damageSizeChange;
	}

	public String buildPartialInput(ISimpleTerm oldTree) throws IncrementalSGLRException {
		result.setLength(0);
		isSkipping = isDamagePrinted = false;
		skippedCharsAfterDamage = skippedCharsBeforeDamage = 0;
		appendTree(oldTree, false);
		try {
			assert result.length() ==
				newInput.length() - skippedCharsBeforeDamage - skippedCharsAfterDamage; 
			return result.toString();
		} finally {
			if (DEBUG) System.out.println();
		}
	}
	
	/**
	 * Gets the number of characters *before* the damaged region
	 * that were in the original input string but not in 
	 * the last incremental input string built by this instance.
	 */
	public int getLastSkippedCharsBeforeDamage() {
		return skippedCharsBeforeDamage;
	}

	/**
	 * @return true if the current node was printed to the {@link #result} string.
	 */
	private boolean appendTree(ISimpleTerm oldTree, boolean disallowSkipping) throws IncrementalSGLRException {
		IToken left = getLeftToken(oldTree);
		IToken right = getRightToken(oldTree);
		int startOffset = 0;
		int endOffset = 0;
		boolean isSkippingStart = false;
		boolean disallowSkippingStart = false;
		
		if (left != null && right != null) {
			startOffset = left.getStartOffset();
			endOffset = right.getEndOffset();
			
			
			if (damageNodes.contains(oldTree)) {
				disallowSkipping = disallowSkippingStart = true;
			} else if (!disallowSkipping && !isSkipping
					&& isSkippableNode(oldTree, startOffset, endOffset)) {
				isSkipping = isSkippingStart = true;
			}

			boolean wasSkipped = false;
			Iterator<ISimpleTerm> iterator = tryGetListIterator(oldTree); 
			for (int i = 0, max = oldTree.getSubtermCount(); i < max; i++) {
				ISimpleTerm child = iterator == null ? oldTree.getSubterm(i) : iterator.next();
				IToken childLeft = getLeftToken(child);
				IToken childRight = getRightToken(child);
				if (childLeft != null)
					appendToken(startOffset, childLeft.getStartOffset() - 1);
				if (wasSkipped)
					isSkipping = false;
				wasSkipped = !appendTree(child, disallowSkipping);
				if (childRight != null)
					startOffset = childRight.getEndOffset() + 1;
			}
			appendToken(startOffset, endOffset);
			if (wasSkipped) isSkipping = false;
		} else {
			assert oldTree.getSubtermCount() == 0 :
				"No tokens for tree with children??";
		}
		
		if (disallowSkippingStart) disallowSkipping = false;
		return !isSkippingStart;
	}

	private boolean isSkippableNode(ISimpleTerm oldTree, int startOffset,
			int endOffset) {
		return !oldTree.isList()
				&& incrementalSorts.isIncrementalNode(oldTree)
				&& !isRangeOverlap(damageStart, damageEnd, startOffset, endOffset)
				&& !isErrorInRange(getLeftToken(oldTree), getRightToken(oldTree));
				// && !isDamagedNodeOrLayout(left, right)) {
	}

	/*
	private boolean isDamagedNodeOrLayout(IToken left, IToken right) {
		int startOffset = findLeftMostLayoutToken(left).getStartOffset();
		int endOffset = findRightMostLayoutToken(right).getEndOffset();
		return isRangeOverlap(damageStart, damageEnd, startOffset, endOffset);
	}
	*/
	
	/**
	 * Appends a token with the given startOffset
	 * and endOffset (inclusive). If the token overlaps
	 * with the damaged region, it is discarded
	 * or merged with the damaged region as necessary.
	 */
	private void appendToken(int startOffset, int endOffset) {
		// TODO: optimize - skip TK_LAYOUT tokens?
		if (isDamagePrinted /* startOffset >= damageStart */) {
			assert startOffset >= damageStart;
			if (endOffset > damageEnd) {
				int newStartOffset = max(damageEnd + damageSizeChange + 1, startOffset + damageSizeChange);
				internalAppendSubstring(newStartOffset, endOffset + damageSizeChange + 1);
			}
			
		} else {
			if (endOffset >= damageStart) {
				int tokenLength = endOffset - startOffset + 1;
				int charsBeforeDamage = damageStart - startOffset;
				int charsAfterDamage = max(0, endOffset - damageEnd);
				assert charsBeforeDamage + charsAfterDamage <= tokenLength
					&& min(charsBeforeDamage, charsAfterDamage) >= 0;
				if (DEBUG) System.out.print('|');
				internalAppendSubstring(startOffset, startOffset + charsBeforeDamage);
				if (DEBUG) System.out.print('$');
				internalAppendSubstring(damageStart, damageEnd + damageSizeChange + 1);
				if (DEBUG) System.out.print('$');
				internalAppendSubstring(damageEnd + damageSizeChange + 1,
							damageEnd + damageSizeChange + 1 + charsAfterDamage);
				if (DEBUG) System.out.print('|');
				isDamagePrinted = true;
			} else {
				internalAppendSubstring(startOffset, endOffset + 1);
			}
		}
 	}
	
	/**
	 * Directly appends a substring with a given 
	 * startIndex and endIndex (exclusive).
	 * 
	 * @param startIndex  The start index or offset of the string.
	 * @param endIndex    The endIndex of the string (exclusive),
	 *                    equal to endOffset + 1.
	 */
	private void internalAppendSubstring(int startIndex, int endIndex) {
		if (isSkipping) {
			for (int i = startIndex; i < endIndex; i++) {
				// if (DEBUG) System.out.print(newInput.charAt(i) == '\n' ? "\n" : "-" + newInput.charAt(i));
				if (newInput.charAt(i) == '\n') {
					result.append('\n');
				} else if (INSERT_WHITESPACE) {
					result.append(' ');
				} else if (i < damageStart) {
					skippedCharsBeforeDamage++;
				} else {
					skippedCharsAfterDamage++;
				}
			}
		} else {
			if (DEBUG) System.out.print(newInput.substring(startIndex, endIndex));
			result.append(newInput, startIndex, endIndex);
		}
	}
}
