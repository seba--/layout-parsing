package org.spoofax.jsglr_layout.client;

import static org.spoofax.terms.Term.isTermAppl;
import static org.spoofax.terms.Term.termAt;

import java.util.HashSet;
import java.util.Set;

import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoConstructor;
import org.spoofax.interpreter.terms.IStrategoNamed;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.jsglr.client.InvalidParseTableException;

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
	
	public org.spoofax.jsglr.client.KeywordRecognizer makeStdKeywordRecognizer(ITermFactory factory) {
	  org.spoofax.jsglr.client.ParseTable pt;
	  try {
	    ITermFactory f = factory;
	    IStrategoTerm tbl =
  	    f.makeAppl(f.makeConstructor("parse-table", 5), 
  	        f.makeInt(6),
  	        f.makeInt(0),
  	        f.makeList(),
  	        f.makeAppl(f.makeConstructor("states", 1), f.makeList()),
  	        f.makeAppl(f.makeConstructor("priorities", 1), 
  	                   f.makeList(f.makeAppl(f.makeConstructor("arg-gtr-prio", 3), 
  	                                         f.makeInt(257), f.makeInt(1), f.makeInt(257))))); // XXX

      pt = new org.spoofax.jsglr.client.ParseTable(tbl, f);
    } catch (InvalidParseTableException e) {
      throw new RuntimeException(e);
    }
	  
	  return pt.getKeywordRecognizer();
	}
}
