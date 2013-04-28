package org.spoofax.jsglr_orig.client.incremental;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.spoofax.jsglr_orig.client.imploder.ImploderAttachment.getLeftToken;
import static org.spoofax.jsglr_orig.client.imploder.ImploderAttachment.getSort;
import static org.spoofax.terms.attachments.ParentAttachment.getParent;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.spoofax.NotImplementedException;
import org.spoofax.interpreter.terms.ISimpleTerm;
import org.spoofax.jsglr_orig.client.ParseException;
import org.spoofax.jsglr_orig.client.SGLR;
import org.spoofax.jsglr_orig.client.imploder.ITreeFactory;
import org.spoofax.jsglr_orig.client.imploder.ImploderAttachment;
import org.spoofax.jsglr_orig.client.imploder.TermTreeFactory;
import org.spoofax.jsglr_orig.client.imploder.TreeBuilder;
import org.spoofax.jsglr_orig.shared.BadTokenException;
import org.spoofax.jsglr_orig.shared.SGLRException;
import org.spoofax.jsglr_orig.shared.TokenExpectedException;
import org.spoofax.terms.attachments.ParentTermFactory;

/**
 * An incremental parsing extension of SGLR.
 * 
 * Should be used with a tree builder that constructs {@link ISimpleTerm}s
 * that have {@link ImploderAttachment} attachments, like {@link TreeBuilder}.
 * 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class IncrementalSGLR<TNode extends ISimpleTerm> {
	
	public static boolean DEBUG = false;
	
	final SGLR parser;

	final ITreeFactory<TNode> factory;
	
	IncrementalSortSet incrementalSorts;
	
	private final CommentDamageExpander comments;

	private TNode lastAst;
	
	private List<TNode> lastReconstructedNodes = Collections.emptyList();

	/**
	 * Creates a new, reusable IncrementalSGLR instance.
	 * 
	 * @see IncrementalSortSet#getInjectionsTo()
	 *            Can be used to determine the injections for incrementalSorts.
	 * 
	 * @param incrementalSorts
	 *            Sorts that can be incrementally parsed (e.g., MethodDec, ImportDec).
	 *            *Must* be sorts that only occur in lists (such as MethodDec*).
	 */
	public IncrementalSGLR(SGLR parser, CommentDamageExpander comments, ITreeFactory<TNode> factory,
			IncrementalSortSet incrementalSorts) {
		this.parser = parser;
		this.comments = comments == null ? CommentDamageExpander.ONLY_LINE_COMMENTS : comments;
		this.factory = factory;
		this.incrementalSorts = incrementalSorts;
		if (factory instanceof TermTreeFactory) {
			if (!ParentTermFactory.isParentTermFactory(((TermTreeFactory) factory).getOriginalTermFactory()))
				throw new IllegalArgumentException("Requires a ParentTermFactory-based term factory");
		} else {
			throw new NotImplementedException("Can't check for parent capability of ITreeFactory");
		}
	}
	
	/**
	 * Incrementally parse an input.
	 * 
	 * Uses the last parsed abstract syntax tree as a template,
	 * or the tree set using {@link #setLastAst()}
	 * 
	 * @see #getLastReconstructedNodes()
	 *             Gets the list of tree nodes that were reconstructed
	 *             after running {@link #parseIncremental}.
	 * 
	 * @throws IncrementalSGLRException
	 *             If the input could not be incrementally parsed.
	 *             It may still be possible to parse it non-incrementally.
	 * @throws InterruptedException 
	 */
	@SuppressWarnings("unchecked")
	public TNode parseIncremental(String newInput, String filename, String startSymbol)
			throws TokenExpectedException, BadTokenException, ParseException, SGLRException, IncrementalSGLRException, InterruptedException {
		
		lastReconstructedNodes = Collections.emptyList();

		if (lastAst == null || incrementalSorts.isEmpty()) {
			lastAst = (TNode) parser.parse(newInput, filename, startSymbol);
			return lastAst;
		}

		// Determine damage size
		String oldInput = getLeftToken(lastAst).getTokenizer().getInput();
		int damageStart = getDamageStart(newInput, oldInput);
		int damageSizeChange = newInput.length() - oldInput.length();
		int damageEnd = getDamageEnd(newInput, oldInput, damageStart, damageSizeChange);
		sanityCheckDiff(oldInput, newInput, damageStart, damageEnd, damageSizeChange);
		
		if (damageSizeChange == 0 && damageEnd == damageStart - 1) {
			assert newInput.equals(oldInput);
			return lastAst;
		}

		// Expand damage size for comments
		damageStart = comments.getExpandedDamageStart(newInput, damageStart, damageEnd, damageSizeChange);
		damageEnd = comments.getExpandedDamageEnd(newInput, damageStart, damageEnd, damageSizeChange);
		
		// Analyze current damage
		DamageRegionAnalyzer neighborAnalyzer = new DamageRegionAnalyzer(this, damageStart, damageEnd, damageSizeChange);
		List<ISimpleTerm> neighborDamageNodes = neighborAnalyzer.getDamageNodes(lastAst);
		if (DEBUG) System.out.println("Damaged excluding neighbours: " + neighborDamageNodes);
		sanityCheckDamageNodes(neighborDamageNodes);
		
		// Expand damage size for neighbors
		NeighborDamageExpander neighbors = new NeighborDamageExpander(neighborAnalyzer, neighborDamageNodes, lastAst);
		List<ISimpleTerm> damageNodes = neighbors.getExpandedDamageNodes();
		damageStart = neighbors.getExpandedDamageStart();
		damageEnd = neighbors.getExpandedDamageEnd();
		
		// Pre-conditions
		if (DEBUG) System.out.println("Damaged including neighbours: " + damageNodes);
		sanityCheckDiff(oldInput, newInput, damageStart, damageEnd, damageSizeChange);
		sanityCheckOldTree(lastAst);
		sanityCheckDamageNodes(damageNodes);

		// Construct and parse partial input
		Set<ISimpleTerm> damageNodesSet = toIdentitySet(damageNodes);
		DamageRegionAnalyzer damageAnalyzer = new DamageRegionAnalyzer(this, damageStart, damageEnd, damageSizeChange);
		IncrementalInputBuilder inputBuilder =
			new IncrementalInputBuilder(damageAnalyzer, damageNodesSet, newInput, oldInput);
		String partialInput = inputBuilder.buildPartialInput(lastAst);
		int skippedChars = inputBuilder.getLastSkippedCharsBeforeDamage();
		ISimpleTerm partialTree = (ISimpleTerm) parser.parse(partialInput, filename, startSymbol);
		List<ISimpleTerm> repairedNodes = damageAnalyzer.getDamageNodesForPartialTree(partialTree, skippedChars);
		
		// Post-condition
		sanityCheckRepairedTree(repairedNodes);
		
		// Combine old tree with new partial tree
		IncrementalTreeBuilder<TNode> treeBuilder = 
			new IncrementalTreeBuilder<TNode>(this, damageAnalyzer, newInput, filename, damageNodesSet, repairedNodes, skippedChars);
		TNode result = treeBuilder.buildOutput(lastAst);
		lastReconstructedNodes = treeBuilder.getLastReconstructedNodes();
		lastAst = result;
		return result;
	}
	
	/**
	 * Returns the list of tree nodes that was reconstructed
	 * for the last incremental parse, or an empty list
	 * if it was not reconstructed incrementally.
	 */
	public List<TNode> getLastReconstructedNodes() {
		return lastReconstructedNodes;
	}
	
	public final TNode getLastAst() {
		return lastAst;
	}
	
	public void setLastAst(TNode ast) {
		lastReconstructedNodes = Collections.emptyList();
		this.lastAst = ast;
	}
	
	public final SGLR getParser() {
		return parser;
	}
	
	public final IncrementalSortSet getIncrementalSorts() {
		return incrementalSorts;
	}
	
	public void setIncrementalSorts(IncrementalSortSet incrementalSorts) {
		this.incrementalSorts = incrementalSorts;
	}

	private static<T> Set<T> toIdentitySet(List<T> list) {
		Map<T, T> resultMap = new IdentityHashMap<T, T>(list.size());
		for (T item : list)
			resultMap.put(item, null);
		return resultMap.keySet();
	}

	private void sanityCheckDiff(String oldInput, String newInput,
			int damageStart, int damageEnd, int damageSizeChange) throws IncrementalSGLRException {
		if (!(damageStart <= damageEnd + 1
			&& damageStart <= damageEnd + damageSizeChange + 1)) {
			throw new IncrementalSGLRException("Precondition failed: unable to determine valid diff");
		}
		assert (oldInput.substring(0, damageStart)
			+ newInput.substring(damageStart, damageEnd + damageSizeChange + 1)
			+ oldInput.substring(damageEnd + 1)).equals(newInput);
	}

	private void sanityCheckOldTree(ISimpleTerm oldTree) throws IncrementalSGLRException {
		
		if (isAmbiguous(oldTree)) {
			// Not yet supported by IncrementalTreeBuilder
			throw new IncrementalSGLRException("Postcondition failed: old tree is ambiguous");
		}
	}

	private void sanityCheckDamageNodes(List<ISimpleTerm> damageNodes) throws IncrementalSGLRException {
		for (ISimpleTerm node : damageNodes) {
			if (!incrementalSorts.isIncrementalNode(node))
				throw new IncrementalSGLRException("Precondition failed: unsafe change to tree node of type "
						+ getSort(node) + " at line " + getLeftToken(node).getLine());
		}
		if (!isSameParent(damageNodes))
			throw new IncrementalSGLRException("Precondition failed: unable to reconcile changes at multiple levels in the tree");
	}
	
	private void sanityCheckRepairedTree(List<ISimpleTerm> repairedTreeNodes)
			throws IncrementalSGLRException {
		
		if (DEBUG) System.out.println("\nRepaired: " + repairedTreeNodes);
		
		for (ISimpleTerm node : repairedTreeNodes) {
			if (!incrementalSorts.isIncrementalNode(node)) {
				throw new IncrementalSGLRException("Postcondition failed: unsafe tree parsed of type "
						+ getSort(node)  + " at line " + getLeftToken(node).getLine());
			} else if (isAmbiguous(node)) {
				// Not yet supported by IncrementalTreeBuilder
				throw new IncrementalSGLRException("Postcondition failed: updated tree is ambiguous");
			}
		}
		
		if (!isSameParent(repairedTreeNodes))
			throw new IncrementalSGLRException("Postcondition failed: unable to reconcile changes at multiple levels in the tree");
	}

	private static boolean isSameParent(List<ISimpleTerm> nodes) {
		if (nodes.size() == 0) return true;
		ISimpleTerm parent = getParent(nodes.get(0));
		if (parent == null) // non-parenttermfactory?
			throw new IllegalStateException("Could not determine parent of tree node");
		for (int i = 1, max = nodes.size(); i < max; i++)
			if (getParent(nodes.get(i)) != parent)
				return false;
		return true;
	}
	
	private static boolean isAmbiguous(ISimpleTerm tree) {
		return getLeftToken(tree).getTokenizer().isAmbigous();
	}

	protected static boolean isRangeOverlap(int start1, int end1, int start2, int end2) {
		return start1 <= end2 && start2 <= end1; // e.g. testJava55
	}

	private int getDamageStart(String input, String oldInput) {
		int limit = min(input.length(), oldInput.length());
		for (int i = 0; i < limit; i++) {
			if (input.charAt(i) != oldInput.charAt(i)) return i;
		}
		return limit - 1;
	}

	private int getDamageEnd(String input, String oldInput, int damageStart, int damageSizeChange) {
		int limit = max(damageStart, damageStart - damageSizeChange) -1;
		for (int i = oldInput.length() - 1; i > limit; i--) {
			if (oldInput.charAt(i) != input.charAt(i + damageSizeChange))
				return i;
		}
		return limit;
	}
}
