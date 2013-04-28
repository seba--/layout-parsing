package org.spoofax.jsglr.client;

import static org.spoofax.terms.Term.isTermAppl;
import static org.spoofax.terms.Term.termAt;

import java.util.HashSet;
import java.util.Set;

import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoConstructor;
import org.spoofax.interpreter.terms.IStrategoNamed;
import org.spoofax.interpreter.terms.IStrategoTerm;

/**
 * Recognizes keywords in a language without considering their context.
 * 
 * @see ParseTable#getKeywordRecognizer()
 * 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class KeywordRecognizer {
	
	private final Set<String> keywords = new HashSet<String>();
	
	protected KeywordRecognizer(ParseTable table) {
		if (table != null) {
			IStrategoConstructor litFun = table.getFactory().makeConstructor("lit", 1);
			for (Label l : table.getLabels()) {
				if (l != null) {
					IStrategoTerm rhs = termAt(l.getProduction(), 1);
					if (isTermAppl(rhs) && ((IStrategoAppl) rhs).getConstructor() == litFun) {
						IStrategoNamed lit = termAt(rhs, 0);
						String litString = lit.getName();
						if (isPotentialKeyword(litString))
							keywords.add(litString);
					}
				}
			}
		}
	}
	
	public boolean isKeyword(String literal) {
		return keywords.contains(literal.trim());
	}
	
	/**
	 * Determines whether the given string could possibly 
	 * be a keyword (as opposed to an operator).
	 * 
	 * @see #isKeyword(String)
	 */
	public static boolean isPotentialKeyword(String literal) {
		for (int i = 0, end = literal.length(); i < end; i++) {
			char c = literal.charAt(i);
			if (!isPotentialKeywordChar(c))
				return false;
		}
		return true;
	}
	
	/**
	 * Determines whether the given character could possibly 
	 * be part of a keyword (as opposed to an operator).
	 */
	public static boolean isPotentialKeywordChar(char c) {
		return Character.isLetterOrDigit(c) || c == '_';
	}
}
