package org.spoofax.jsglr_layout.client.imploder;

import java.util.Map;

import org.spoofax.interpreter.terms.ISimpleTerm;
import org.spoofax.jsglr_layout.client.KeywordRecognizer;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public interface ITokenizer extends Iterable<IToken> {
	
	public static final String ERROR_SKIPPED_REGION =
		"Syntax error, unexpected construct(s)";
	
	public static final String ERROR_UNEXPECTED_EOF =
		"Syntax error, unexpected end of input";
	
	public static final String ERROR_WATER_PREFIX =
		"Syntax error, not expected here";
	
	public static final String ERROR_INSERT_PREFIX =
		"Syntax error, expected";
	
	public static final String ERROR_INSERT_END_PREFIX =
		"Syntax error, unterminated construct";
	
	public static final String ERROR_GENERIC_PREFIX =
		"Syntax error";

	public static final String ERROR_WARNING_PREFIX =
		"Warning";

	String getInput();

	int getStartOffset();

	void setStartOffset(int startOffset);

	IToken currentToken();

	int getTokenCount();
	
	int getEndLine();
	
	int getEndColumn();

	IToken getTokenAt(int index);

	IToken getTokenAtOffset(int offset);
	
	int getLineAtOffset(int offset);

	IToken makeToken(int endOffset, LabelInfo label, boolean allowEmptyToken);

	IToken makeToken(int endOffset, int kind, boolean allowEmptyToken);
	
	/**
	 * Gets a token at the given offset, or creates an adjunct
	 * token with that offset, used for error reporting.
	 */
	IToken getErrorTokenOrAdjunct(int offset);
	
	/**
	 * Creates artificial token at keyword boundaries
	 * inside skipped regions of code when
	 * invoked for each character in a skipped/erroneous region of code.
	 * Required for keyword highlighting with {@link KeywordRecognizer}.
	 * 
	 * @param offset
	 *           The offset of the 
	 */
	void tryMakeSkippedRegionToken(int endOffset);

	/**
	 * Creates an artificial token for every water-based recovery
	 * and for comments within layout.
	 */
	void tryMakeLayoutToken(int endOffset, int lastOffset, LabelInfo label);
	
	/**
	 * Marks a possible syntax error (if indicated by the given label),
	 * starting *after* the given token, ending at the given offset.
	 */
	void markPossibleSyntaxError(LabelInfo label, IToken firstToken, int endOffset, ProductionAttributeReader prodReader);
	
	boolean isSyntaxCorrect();

	String toString(IToken left, IToken right);

	String toString(int startOffset, int endOffset);

	String getFilename();
	
	/**
	 * Determines if the tokenizer is ambiguous.
	 * If it is, tokens with subsequent indices may not
	 * always have matching start/end offsets.
	 * 
	 * @see Tokenizer#getTokenAfter(IToken)   Gets the next token with a matching offset.
	 * @see Tokenizer#getTokenBefore(IToken)  Gets the previous token with a matching offset.
	 */
	boolean isAmbigous();
	
	void setAmbiguous(boolean isAmbiguous);

	void setAst(ISimpleTerm ast);

	/**
	 * Initializes the {@link IToken#getAstNode()} method of each
	 * token in this token stream.
	 */
	void initAstNodeBinding();
}