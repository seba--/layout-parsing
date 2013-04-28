package org.spoofax.jsglr_orig.client;

import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.jsglr_orig.client.imploder.ITokenizer;

/** 
 * A tree builder that always builds a <code>null</code> tree.
 * 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class NullTreeBuilder implements ITreeBuilder {

	public void initializeTable(ParseTable table, int productionCount,
			int labelStart, int labelCount) {
		// Do nothing
	}

	public void initializeLabel(int labelNumber,
			IStrategoAppl parseTreeProduction) {
		// Do nothing
	}

	public void initializeInput(String input, String filename) {
		// Do nothing
	}

	public Object buildTree(AbstractParseNode node) {
		return null;
	}

	public Object buildTreeTop(Object subtree, int ambiguityCount) {
		return null;
	}

	public ITokenizer getTokenizer() {
		return null;
	}

	public void reset() {
		// Do ... nothing
	}

}
