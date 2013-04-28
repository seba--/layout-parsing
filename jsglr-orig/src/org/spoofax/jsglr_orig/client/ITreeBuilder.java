package org.spoofax.jsglr_orig.client;

import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.jsglr_orig.client.imploder.ITokenizer;

public interface ITreeBuilder {

	void initializeTable(ParseTable table, int productionCount, int labelStart, int labelCount);
	void initializeLabel(int labelNumber, IStrategoAppl parseTreeProduction);
	void initializeInput(String input, String filename);
	
	Object buildTree(AbstractParseNode node);
	Object buildTreeTop(Object subtree, int ambiguityCount);
	
	void reset();
	
	/**
	 * Gets the tokenizer, if applicable and initialized, or null.
	 */
	ITokenizer getTokenizer();
}
