package org.spoofax.jsglr_layout.client.imploder;

import java.util.ArrayList;
import java.util.Iterator;

import org.spoofax.interpreter.terms.ISimpleTerm;

/**
 * A special tokenizer that has only one token.
 * 
 * @author Lennart Kats <lennart add lclnet.nl>
 * @author Karl Trygve Kalleberg <karltk near strategoxt dot org>
 */
public class NullTokenizer extends AbstractTokenizer {
	
	private final IToken onlyToken;
	
	public NullTokenizer(String input, String filename, Token onlyToken) {
		super(input, filename);
		this.onlyToken = onlyToken;
		assert onlyToken.getTokenizer() == null || onlyToken.getTokenizer() == this;
		onlyToken.setTokenizer(this);
	}
		
	public NullTokenizer(String input, String filename) {
		super(input, filename);
		onlyToken = new Token(this, 0, 0, 0, 0,
				input == null ? 0 : input.length() - 1, IToken.TK_UNKNOWN);
	}

	public int getStartOffset() {
		return 0;
	}

	public void setStartOffset(int startOffset) {
		// Do nothing		
	}

	public IToken currentToken() {
		return onlyToken;
	}

	public int getTokenCount() {
		return 1;
	}

	public IToken getTokenAt(int i) {
		return onlyToken;
	}
	
	public IToken getTokenAtOffset(int o) {
		return onlyToken;
	}

	@Override
	public IToken makeToken(int endOffset, LabelInfo label, boolean allowEmptyToken) {
		return onlyToken;
	}

	public IToken makeToken(int endOffset, int kind, boolean allowEmptyToken) {
		return onlyToken;
	}
	
	@Override
	protected void setErrorMessage(IToken leftToken, IToken rightToken, String message) {
		if (leftToken != onlyToken || rightToken != onlyToken)
			throw new IllegalArgumentException("Argument tokens do not belong to this NullTokenizer");
		// Do nothing
	}

	public void tryMakeSkippedRegionToken(int endOffset) {
		// Do nothing
	}

	@Override
	public void tryMakeLayoutToken(int endOffset, int lastOffset, LabelInfo label) {
		// Do nothing
	}
	
	@Override
	public void markPossibleSyntaxError(LabelInfo label, IToken firstToken,
			int endOffset, ProductionAttributeReader prodReader) {
		// Do nothing
	}

	public Iterator<IToken> iterator() {
		ArrayList<IToken> result = new ArrayList<IToken>(1);
		result.add(onlyToken);
		return result.iterator();
	}

	public void setAst(ISimpleTerm ast) {
		// no tokens, no ast-token binding
	}

	public void initAstNodeBinding() {
		// no tokens, no ast-token binding
	}
}
