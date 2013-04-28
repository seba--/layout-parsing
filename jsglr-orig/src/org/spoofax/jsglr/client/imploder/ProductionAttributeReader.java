package org.spoofax.jsglr.client.imploder;

import static org.spoofax.terms.StrategoListIterator.iterable;
import static org.spoofax.terms.Term.applAt;
import static org.spoofax.terms.Term.asJavaString;
import static org.spoofax.terms.Term.isTermAppl;
import static org.spoofax.terms.Term.isTermNamed;
import static org.spoofax.terms.Term.javaString;
import static org.spoofax.terms.Term.termAt;
import static org.spoofax.terms.Term.tryGetConstructor;

import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoConstructor;
import org.spoofax.interpreter.terms.IStrategoInt;
import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoNamed;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;


/**
 * Extracts attributes from parse table productions.
 * 
 * @author Lennart Kats <L.C.L.Kats add tudelft.nl>
 */
public class ProductionAttributeReader {
	
	/**
	 * The constructor used for "water" recovery rules.
	 */
	private static final String WATER = "WATER";
	
	/**
	 * The constructor used for "insertion" recovery rules.
	 */
	private static final String INSERT = "INSERTION";
	
	/**
	 * The constructor used for "end insertion" recovery rules.
	 */
	private static final String INSERT_END = "INSERTEND";
	
	private static final String INSERT_OPEN_QUOTE = "INSERTOPENQUOTE";
	
	private final int PARAMETRIZED_SORT_NAME = 0;
	
	private final int PARAMETRIZED_SORT_ARGS = 1;
	
	private final int ALT_SORT_LEFT = 0;
	
	private final int ALT_SORT_RIGHT = 1;
	
	protected final IStrategoConstructor sortFun;
	
	protected final IStrategoConstructor parameterizedSortFun;
	
	private final IStrategoConstructor noAttrsFun;
	
	private final IStrategoConstructor varSymFun;
	
	private final IStrategoConstructor altFun;
	
	private final IStrategoConstructor charClassFun;
	
	private final IStrategoConstructor litFun;
	
	private final IStrategoConstructor cilitFun;
	
	private final IStrategoConstructor lexFun;
	
	private final IStrategoConstructor optFun;
	
	private final IStrategoConstructor layoutFun;
	
	private final IStrategoConstructor cfFun;
	
	private final IStrategoConstructor varsymFun;
	
	private final IStrategoConstructor seqFun;
	
	private final IStrategoConstructor iterFun;
	
	private final IStrategoConstructor iterStarFun;
	
	private final IStrategoConstructor iterPlusFun;
	
	private final IStrategoConstructor iterSepFun;
	
	private final IStrategoConstructor iterStarSepFun;
	
	private final IStrategoConstructor iterPlusSepFun;
	
	public ProductionAttributeReader(ITermFactory factory) {
		sortFun = factory.makeConstructor("sort", 1);
		parameterizedSortFun =
			factory.makeConstructor("parameterized-sort", 2);
		noAttrsFun = factory.makeConstructor("no-attrs", 0);
		varSymFun = factory.makeConstructor("varsym", 1);
		altFun = factory.makeConstructor("alt", 2);
		charClassFun = factory.makeConstructor("char-class", 1);
		litFun = factory.makeConstructor("lit", 1);
		cilitFun = factory.makeConstructor("cilit", 1);
		lexFun = factory.makeConstructor("lex", 1);
		optFun = factory.makeConstructor("opt", 1);
		layoutFun = factory.makeConstructor("layout", 0);
		cfFun = factory.makeConstructor("cf", 1);
		varsymFun = factory.makeConstructor("varsym", 1);
		seqFun = factory.makeConstructor("seq", 2);
		iterFun = factory.makeConstructor("iter", 1);
		iterStarFun = factory.makeConstructor("iter-star", 1);
		iterPlusFun = factory.makeConstructor("iter-plus", 1);
		iterSepFun = factory.makeConstructor("iter-sep", 2);
		iterStarSepFun = factory.makeConstructor("iter-star-sep", 2);
		iterPlusSepFun = factory.makeConstructor("iter-plus-sep", 2);
	}

	public String getConsAttribute(IStrategoAppl attrs) {
		IStrategoTerm consAttr = getAttribute(attrs, "cons");
		return consAttr == null ? null : ((IStrategoNamed) consAttr).getName();
	}
	
	public boolean isWaterConstructor(String constructor) {
		return WATER.equals(constructor);
	}
	
	/**
	 * Tests if a production sort is one of the special
	 * sorts that should be ignored if no constructor is specified.
	 * (These recoveries are reported higher up in the tree.)
	 */
	public boolean isIgnoredUnspecifiedRecoverySort(String sort) {
		// TODO: be more specific: WATERSTART, ...?
		return sort != null && sort.startsWith("WATER");
	}
	
	public boolean isInsertEndConstructor(String constructor) {
		return INSERT_END.equals(constructor);
	}
	
	public boolean isInsertOpenQuoteSort(String sort) {
		return sort != null && sort.startsWith(INSERT_OPEN_QUOTE);
	}
	
	public boolean isInsertConstructor(String constructor) {
		return INSERT.equals(constructor);
	}
	
	public boolean isRecoverProduction(IStrategoAppl attrs, String constructor) {
		return getAttribute(attrs, "recover") != null || isWaterConstructor(constructor);
	}
	
	public boolean isRejectProduction(IStrategoAppl attrs) {
		return getAttribute(attrs, "reject") != null;
	}
	
	public String getDeprecationMessage(IStrategoAppl attrs) {
		IStrategoTerm deprecated = getAttribute(attrs, "deprecated");
		if (deprecated == null) {
			return null;
		} else if (deprecated.getSubtermCount() == 1) {
			return asJavaString(termAt(deprecated, 0));
		} else {
			return "Deprecated construct";
		}
	}
	
	public String getSyntaxErrorExpectedInsertion(LabelInfo label) {
		String inserted;
		IStrategoList lhs = label.getLHS();
		IStrategoAppl rhs = label.getRHS();
		if (rhs.getName().equals("lit")) {
			inserted = "'" + ((IStrategoNamed) termAt(rhs, 0)).getName() + "'";
		} else if (lhs.getSubtermCount() == 1 && tryGetConstructor(termAt(lhs, 0)) == litFun) {
			inserted = "'" + asJavaString(termAt(termAt(lhs, 0), 0)) + "'";
		} else if (rhs.getName().equals("char-class")) {
			inserted = "'" + toString((IStrategoList) termAt(rhs, 0)) + "'";
		} else {
			inserted = getSort(rhs);
			if (inserted == null)
				inserted = "token";
		}
		return inserted;
	}
	
	private static String toString(IStrategoList chars) {
        StringBuilder result = new StringBuilder(chars.getSubtermCount());

        while (chars.head() != null) {
        	IStrategoInt v = (IStrategoInt) chars.head();
            result.append((char) v.intValue());
            chars = chars.tail();
        }
        
        return result.toString();
    }
	
	// FIXME: support meta-var constructors
	public String getMetaVarConstructor(IStrategoAppl rhs) {
		if (varSymFun == rhs.getConstructor()) {
			rhs = termAt(rhs, 0);
			return isList(rhs)
					? "meta-listvar"
					: "meta-var";
		}
		return null;
	}
	
	public IStrategoTerm getAstAttribute(IStrategoAppl attrs) {
		return getAttribute(attrs, "ast");
	}
	
	public boolean isIndentPaddingLexical(IStrategoAppl attrs) {
		return getAttribute(attrs, "indentpadding") != null;
	}

	/** Return the contents of a term attribute (e.g., "cons"), or null if not found. */
	public IStrategoTerm getAttribute(IStrategoAppl attrs, String attrName) {
		if (attrs.getConstructor() == noAttrsFun)
			return null;
		
		IStrategoList list = termAt(attrs, 0);
		
		for (IStrategoTerm attr : iterable(list)) {			
			if (isTermNamed(attr)) {
				IStrategoNamed namedAttr = (IStrategoNamed) attr;
				if (namedAttr.getName().equals("term")) {
					namedAttr = termAt(namedAttr, 0);
					
					if (namedAttr.getName().equals(attrName))
						return namedAttr.getSubtermCount() == 1 ? termAt(namedAttr, 0) : namedAttr;
				}				
			}
		}
		
		return null; // no cons found
	}
	
	/** 
	 * Get the RTG sort name of a production RHS, or for lists, the RTG element sort name.
	 */
    public String getSort(IStrategoAppl rhs) {
    	for (IStrategoTerm current = rhs; current.getSubtermCount() > 0 && isTermAppl(current); current = termAt(current, 0)) {
    		IStrategoAppl currentAppl = (IStrategoAppl) current;
			String sort = tryGetSort(currentAppl);
			if (sort != null) return sort;
    	}
    	
    	return null;
    }
	
	/** 
	 * Get the first occurring RTG sort name of a production LHS.
	 */
	public String tryGetFirstSort(IStrategoList lhs) {
		for (IStrategoTerm lhsPart : lhs.getAllSubterms()) {
			String sort = tryGetSort((IStrategoAppl) lhsPart);
			if (sort != null) return sort;
		}
		return null;
	}

	/** 
	 * Get the RTG sort name of a pattern.
	 */
	public String tryGetSort(IStrategoAppl currentAppl) {
		IStrategoConstructor cons = currentAppl.getConstructor();
		if (cons == cfFun)
			return tryGetSort(applAt(currentAppl, 0));
		if (cons == lexFun)
			return tryGetSort(applAt(currentAppl, 0));
		if (cons == sortFun)
			return javaString(termAt(currentAppl, 0));
		if (cons == parameterizedSortFun)
			return getParameterizedSortName(currentAppl);
		if (cons == charClassFun)
			return null;
		if (cons == altFun)
			return getAltSortName(currentAppl);
		return null;
	}
    
    private String getParameterizedSortName(IStrategoAppl parameterizedSort) {
    	StringBuilder result = new StringBuilder();
    	
    	result.append(((IStrategoNamed)termAt(parameterizedSort, PARAMETRIZED_SORT_NAME)).getName());
    	result.append('_');
    	
		IStrategoList args = termAt(parameterizedSort, PARAMETRIZED_SORT_ARGS);
		
        for (IStrategoTerm arg : iterable(args)) {
			result.append(((IStrategoNamed) arg).getName());
		}
		
		return result.toString();
    }
    
    private String getAltSortName(IStrategoAppl node) {
		String left = getSort(applAt(node, ALT_SORT_LEFT));
		String right = getSort(applAt(node, ALT_SORT_RIGHT));
		
		// HACK: In the RTG, alt sorts appear with a number at the end
		return left + "_" + right + "0";
    }

	/**
	 * Identifies lexical parse tree nodes.
	 * 
	 * @see AsfixAnalyzer#isVariableNode(ATermAppl)
	 *      Identifies variables, which are usually treated similarly to
	 *      lexical nodes.
	 * 
	 * @return true if the current node is lexical.
	 */
	public boolean isNonContextFree(IStrategoAppl rhs) {
		return (lexFun == rhs.getConstructor() || isLiteral(rhs)
		    || isLayout(rhs)) || isVariableNode(rhs);
	}
	
	public boolean isLexical(IStrategoAppl rhs) {
		return lexFun == rhs.getConstructor();
	}

	public boolean isLayout(IStrategoAppl rhs) {
		IStrategoTerm details = termAt(rhs, 0);
		if (!isTermAppl(details))
			return false;
		
		if (optFun == ((IStrategoAppl) details).getConstructor())
			details = termAt(details, 0);
		
		return layoutFun == ((IStrategoAppl) details).getConstructor();
	}

	public boolean isLiteral(IStrategoAppl rhs) {
		IStrategoConstructor fun = rhs.getConstructor();
		return litFun == fun || cilitFun == fun;
	}
	
	public boolean isList(IStrategoAppl rhs) {
		IStrategoAppl details = rhs;
		
		if (details.getConstructor() == varsymFun)
			details = termAt(details, 0);
		
		if (details.getConstructor() == cfFun)
			details = termAt(details, 0);
		              	
	  	if (details.getConstructor() == optFun)
	  		details = termAt(details, 0);
	  	
		IStrategoConstructor fun = details.getConstructor();
		
		 // FIXME: Spoofax/159: AsfixImploder creates tuples instead of lists for seqs
		return isIterFun(fun) || seqFun == fun;
	}

	public boolean isIterFun(IStrategoConstructor fun) {
		return iterFun == fun || iterStarFun == fun || iterPlusFun == fun
				|| iterSepFun == fun || iterStarSepFun == fun || iterPlusSepFun == fun;
	}

	/**
	 * Identifies parse tree nodes that begin variables.
	 * 
	 * @see #isVariableNode(ATermAppl) 
	 * @return true if the current node is lexical.
	 */
	public boolean isVariableNode(IStrategoAppl rhs) {
		return varsymFun == rhs.getConstructor();
	}

	public boolean isLexLayout(IStrategoAppl rhs) {
		if (rhs.getSubtermCount() != 1) return false;
		IStrategoTerm child = rhs.getSubterm(0);
		return isTermAppl(child) && layoutFun == ((IStrategoAppl) child).getConstructor()
			&& lexFun == rhs.getConstructor();
	}

	public boolean isOptional(IStrategoAppl rhs) {
		if (rhs.getConstructor() == optFun)
			return true;
		IStrategoTerm contents = termAt(rhs, 0);
		return contents.getSubtermCount() == 1
			&& isTermAppl(contents)
			&& ((IStrategoAppl) contents).getConstructor() == optFun;
	}

}
