package org.spoofax.jsglr_orig.client.imploder;

import java.util.HashMap;
import java.util.Map;

import org.spoofax.interpreter.terms.ISimpleTerm;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 * @author Karl Trygve Kalleberg <karltk near strategoxt dot org>
 */
public class Token implements IToken, Cloneable {
	
	private static final long serialVersionUID = -6972938219235720902L;

	private static Map<String, Integer> asyncAllTokenKinds;

	private ITokenizer tokenizer;
	
	private final int line;
	
	private final int column;
	
	private final int startOffset;
	
	private int endOffset;
	
	private int index;
	
	private int kind;
	
	private String errorMessage;
	
	private ISimpleTerm astNode;

	public Token(ITokenizer tokenizer, int index, int line, int column, int startOffset, int endOffset, int kind) {
		this(tokenizer, index, line, column, startOffset, endOffset, kind, null, null);
	}

	public Token(ITokenizer tokenizer, int index, int line, int column, int startOffset, int endOffset, int kind, String errorMessage, ISimpleTerm astNode) {
		this.tokenizer = tokenizer;
		this.index = index;
		this.line = line;
		this.column = column;
		this.startOffset = startOffset;
		this.endOffset = endOffset;
		this.kind = kind;
	}
	
	public ITokenizer getTokenizer() {
		return tokenizer;
	}
	
	protected void setTokenizer(ITokenizer tokenizer) {
		this.tokenizer = tokenizer;
	}
	
	public int getKind() {
		return kind;
	}
	
	public void setKind(int kind) {
		this.kind = kind;
	}

	public int getIndex() {
		return index;
	}
	
	protected void setIndex(int index) {
		this.index = index;
	}

	public final int getStartOffset() {
		return startOffset;
	}

	public final int getEndOffset() {
		return endOffset;
	}
	
	public void setEndOffset(int endOffset) {
		this.endOffset = endOffset;
	}

	public int getLine() {
		return line;
	}
	
	public int getEndLine() {
		return line; // our tokens span only one line
	}
	
	public int getColumn() {
		return column;
	}
	
	public int getEndColumn() {
		return column + getEndOffset() - getStartOffset();
	}
	
	public int getLength() {
		return getEndOffset() - getStartOffset() + 1;
	}
	
	/**
	 * Gets the error message associated with this token,
	 * if any.
	 * 
	 * Note that this message is independent from the token kind,
	 * which may also indicate an error.
	 */
	public String getError() {
		return errorMessage;
	}
	
	/**
	 * Sets a syntax error for this token.
	 * (Setting any other kind of error would break cacheability.)
	 */
	public void setError(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public void setAstNode(ISimpleTerm astNode) {
		this.astNode = astNode;
	}
	
	public ISimpleTerm getAstNode() {
		if (astNode == null) getTokenizer().initAstNodeBinding();
		return astNode;
	}
	
	@Override
	public String toString() {
		return tokenizer.toString(this, this);
	}
	
	public char charAt(int index) {
		return tokenizer.getInput().charAt(index + startOffset);
	}

	public int compareTo(IToken other) {
		if (endOffset <= other.getEndOffset()) {
			return -1;
		} else if (startOffset > other.getStartOffset()) {
			return 1;
		} else {
			return 0;
		}
	}
	
	public static int indexOf(IToken token, char c) {
		String stream = token.getTokenizer().getInput();
		for (int i = token.getStartOffset(), last = token.getEndOffset(); i <= last; i++) { 
			if (stream.charAt(i) == c)
				return i;
		}
		return -1;
	}


	
	public static boolean isWhiteSpace(IToken token) {
		String input = token.getTokenizer().getInput();
		for (int i = token.getStartOffset(), last = token.getEndOffset(); i <= last; i++) { 
			switch(input.charAt(i)) {
				case ' ':
				case '\n':
				case '\t':
				case '\f':
				case '\r':
					continue;
				default:
					return false;
			}
		}
		return true;
	}

	public static String tokenKindToString(int kind) {
		return "tokenKind#" + kind;
	}

	public static int valueOf(String tokenKind) {
		Integer result = getTokenKindMap().get(tokenKind);
		return result == null ? TK_NO_TOKEN_KIND : result;
	}
	
	private static Map<String, Integer> getTokenKindMap() {
		synchronized (Token.class) {
			if (asyncAllTokenKinds != null)
				return asyncAllTokenKinds;
			asyncAllTokenKinds = new HashMap<String, Integer>();
			asyncAllTokenKinds.put("TK_UNKNOWN", TK_UNKNOWN);
			asyncAllTokenKinds.put("TK_IDENTIFIER", TK_IDENTIFIER);
			asyncAllTokenKinds.put("TK_NUMBER", TK_NUMBER);
			asyncAllTokenKinds.put("TK_STRING", TK_STRING);
			asyncAllTokenKinds.put("TK_KEYWORD", TK_KEYWORD);
			asyncAllTokenKinds.put("TK_OPERATOR", TK_OPERATOR);
			asyncAllTokenKinds.put("TK_VAR", TK_VAR);
			asyncAllTokenKinds.put("TK_LAYOUT", TK_LAYOUT);
			asyncAllTokenKinds.put("TK_EOF", TK_EOF);
			asyncAllTokenKinds.put("TK_ERROR", TK_ERROR);
			asyncAllTokenKinds.put("TK_ERROR_KEYWORD", TK_ERROR_KEYWORD);
			asyncAllTokenKinds.put("TK_ERROR_EOF_UNEXPECTED", TK_ERROR_EOF_UNEXPECTED);
			asyncAllTokenKinds.put("TK_ERROR_LAYOUT", TK_ERROR_LAYOUT);
			asyncAllTokenKinds.put("TK_RESERVED", TK_RESERVED);
			asyncAllTokenKinds.put("TK_NO_TOKEN_KIND", TK_NO_TOKEN_KIND);
			return asyncAllTokenKinds;
		}
	}
	
	@Override
	public Token clone() {
		try {
			return (Token) super.clone();
		} catch (CloneNotSupportedException e) {
			// Must be supported for IToken
			throw new RuntimeException(e);
		}
	}

}
