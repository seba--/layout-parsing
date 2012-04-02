package org.spoofax.jsglr.client;

import org.spoofax.jsglr.client.imploder.ProductionAttributeReader;

/**
 * @deprecated Use {@link ProductionAttributeReader} instead.
 */
public class ParseTreeTools {

	/* UNDONE: no longer maintained - use ProductionAttributeReader instead!!
	private final static int PARSE_TREE = 0;
	private final static int APPL_PROD = 0;
	private final static int APPL_ARGS = 1;
	private final static int PROD_ATTRS = 2;
	private final static int ATTRS_LIST = 0;
	private final static int TERM_CONS = 0;
	private final static int CONS_NAME = 0;

	private final IStrategoConstructor parsetreeFun;
	private final IStrategoConstructor applFun;
	private final IStrategoConstructor termFun;
	private final IStrategoConstructor prodFun;
	private final IStrategoConstructor noattrsFun;
	private final IStrategoConstructor consFun;

	public ParseTreeTools(TermFactory factory) {
		super();
		parsetreeFun = factory.makeConstructor("parsetree", 2);
		applFun = factory.makeConstructor("appl", 2);
		prodFun = factory.makeConstructor("prod", 3);
		noattrsFun = factory.makeConstructor("no-attrs", 0);
		termFun = factory.makeConstructor("term", 1);
		consFun = factory.makeConstructor("cons", 1);
	}

	private static IStrategoAppl assertAppl(IStrategoTerm t) {
		if(isTermAppl(t)) {
			return (IStrategoAppl) t;
		}
		else {
			throw new IllegalArgumentException("Expected IStrategoTerm application: " + t);
		}
	}

	private static IStrategoAppl assertAppl(IStrategoTerm t, IStrategoConstructor fun) {
		IStrategoAppl result = assertAppl(t);
		if(result.getConstructor() != fun) {
			throw new IllegalArgumentException("Expected application of function " + fun + ": " + result.getConstructor());
		}

		return result;
	}

	/**
	 * Given a production or application returns the constructor name
	 * attached to the production, or null if there is no constructor.
	 *
	 * @deprecated Use {@link ProductionAttributeReader#getConsAttribute()} instead.
	 *
	 * @author Martin Bravenboer
	 * @author Lennart Kats
	 *
	public String getConstructor(IStrategoTerm arg) {
		IStrategoAppl appl = assertAppl(arg, applFun);

		IStrategoAppl prod;
		if(appl.getConstructor() == prodFun) {
			prod = appl;
		}
		else if(appl.getConstructor() == applFun) {
			prod = assertAppl(appl.getSubterm(APPL_PROD), prodFun);
		}
		else {
			throw new IllegalArgumentException("Expected prod or appl: " + arg);
		}

		IStrategoAppl attrs = assertAppl(prod.getSubterm(PROD_ATTRS));
		if(attrs.getConstructor() == noattrsFun) {
			return null;
		}
		else {
			for(IStrategoTerm attr: (IStrategoList) attrs.getSubterm(ATTRS_LIST)) {
				if (attr instanceof ATermAppl) {
					ATermAppl namedAttr = (ATermAppl) attr;
					if (namedAttr.getConstructor() == termFun) {
						namedAttr = (ATermAppl) namedAttr.getSubterm(TERM_CONS);
						if (namedAttr.getConstructor() == consFun) {
							namedAttr = (ATermAppl) namedAttr.getSubterm(CONS_NAME);
							return namedAttr.getName();
						}
					}
				}
			}
		}

		return null;
	}

	/**
	 * Yields a parse tree (parsetree or appl) to a String.
	 *
	 * @author Martin Bravenboer
	 *
	public String yield(IStrategoTerm parsetree) {
		StringBuilder builder = new StringBuilder();
		yield(parsetree, builder);
		return builder.toString();
	}

	/**
	 * Yields a parse tree (parsetree or appl) to a string builder.
	 *
	 * @author Martin Bravenboer
	 *
	public void yield(IStrategoTerm parsetree, StringBuilder builder) {
		ATermAppl appl = assertAppl(parsetree);
		if(appl.getConstructor() == parsetreeFun) {
			appl = assertAppl((ATerm) appl.getSubterm(PARSE_TREE));
		}

		yieldAppl(appl, builder);
	}

	/**
	 * Private helper for the yield method.
	 *
	private void yieldAppl(ATermAppl appl, StringBuilder builder) {
		for(IStrategoTerm t : (IStrategoList) appl.getSubterm(APPL_ARGS)) {
			if(t instanceof ATermAppl) {
				ATermAppl arg = (ATermAppl) t;
				if(arg.getConstructor() == applFun) {
					yieldAppl(arg, builder);
				}
				else {
					throw new IllegalArgumentException("Don't know how to yield " + arg);
				}
			}
			else if(t instanceof ATermInt) {
				ATermInt arg = (ATermInt) t;
				builder.append((char) arg.getInt());
			}
			else {
				throw new IllegalArgumentException("Don't know how to yield " + t);
			}
		}
	}
    */
}
