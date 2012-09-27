package org.spoofax.jsglr.client.imploder;

import static org.spoofax.jsglr.client.imploder.IToken.TK_IDENTIFIER;
import static org.spoofax.jsglr.client.imploder.IToken.TK_KEYWORD;
import static org.spoofax.jsglr.client.imploder.IToken.TK_LAYOUT;
import static org.spoofax.jsglr.client.imploder.IToken.TK_NUMBER;
import static org.spoofax.jsglr.client.imploder.IToken.TK_OPERATOR;
import static org.spoofax.jsglr.client.imploder.IToken.TK_STRING;
import static org.spoofax.jsglr.client.imploder.IToken.TK_VAR;
import static org.spoofax.terms.Term.intAt;
import static org.spoofax.terms.Term.isTermAppl;
import static org.spoofax.terms.Term.termAt;

import java.util.Iterator;

import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoString;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.terms.TermVisitor;

/**
 * Class that handles producing and printing token kinds.
 * 
 * @note May be overridden for specific grammars.
 * 
 * @see TokenKind
 * 
 * @author Lennart Kats <L.C.L.Kats add tudelft.nl>
 */
public class TokenKindManager {
	private static final int RANGE_START = 0;
	
	private static final int RANGE_END = 1;
	
	// General token kind information
	
	/**
	 * Get the token kind for a given sort.
	 */
	public int getTokenKind(LabelInfo label) {
		// TODO: Optimization - cache token kind info in LabelInfo
		
		if (label == null) {
			return TK_STRING; // usually indicates a character/int terminal, e.g. 'x'
		} else if (label.isLayout()) {
			return TK_LAYOUT;
		} else if (label.isLexical()) {
			if (isStringLiteral(label)) {
				return TK_STRING;
			} else if (isNumberLiteral(label)) {
				return TK_NUMBER;
			} else {
				return TK_IDENTIFIER;
			}
		} else if (label.isVar()) {
			return TK_VAR;
		} else if (isOperator(label)) {
			return TK_OPERATOR;
		} else {
			return TK_KEYWORD;
		}
	}

	protected static boolean isOperator(LabelInfo label) {
		if (!label.isLiteral()) return false;
		
		IStrategoString lit = termAt(label.getRHS(), 0);
		String contents = lit.stringValue();
		
		for (int i = 0; i < contents.length(); i++) {
			char c = contents.charAt(i);
			if (Character.isLetter(c)) return false;
		}
		
		return true;
	}
	
	protected static boolean isStringLiteral(LabelInfo label) {
		return topdownHasSpaces(label.getLHS());
	}
	
	private static boolean topdownHasSpaces(IStrategoTerm term) {
		// Return true if any character range of this contains spaces
		Iterator<IStrategoTerm> iterator = TermVisitor.tryGetListIterator(term); 
		for (int i = 0, max = term.getSubtermCount(); i < max; i++) {
			IStrategoTerm child = iterator == null ? term.getSubterm(i) : iterator.next();
			if (isRangeAppl(child)) {
				int start = intAt(child, RANGE_START);
				int end = intAt(child, RANGE_END);
				if (start <= ' ' && ' ' <= end) return true;
			} else {
				if (topdownHasSpaces(child)) return true;
			}
		}
		
		return false;
	}

	private static boolean isRangeAppl(IStrategoTerm child) {
		// TODO: Optimize - use getConstructor() instead and make a "range" field
		return isTermAppl(child) && ((IStrategoAppl) child).getName().equals("range");
	}
	
	protected static boolean isNumberLiteral(LabelInfo label) {
		IStrategoTerm range = getFirstRange(label.getLHS());
		
		return range != null && intAt(range, RANGE_START) == '0' && intAt(range, RANGE_END) == '9';
	}
	
	private static IStrategoTerm getFirstRange(IStrategoTerm term) {
		// Get very first character range in this term
		for (int i = 0; i < term.getSubtermCount(); i++) {
			IStrategoTerm child = termAt(term, i);
			if (isRangeAppl(child)) {
				return child;
			} else {
				child = getFirstRange(child);
				if (child != null) return child;
			}
		}
		
		return null;
	}
}
