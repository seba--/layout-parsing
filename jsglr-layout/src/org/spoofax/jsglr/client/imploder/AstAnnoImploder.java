package org.spoofax.jsglr.client.imploder;

import static org.spoofax.interpreter.terms.IStrategoTerm.APPL;
import static org.spoofax.interpreter.terms.IStrategoTerm.INT;
import static org.spoofax.interpreter.terms.IStrategoTerm.LIST;
import static org.spoofax.interpreter.terms.IStrategoTerm.PLACEHOLDER;
import static org.spoofax.interpreter.terms.IStrategoTerm.REAL;
import static org.spoofax.interpreter.terms.IStrategoTerm.STRING;
import static org.spoofax.terms.Term.isTermInt;
import static org.spoofax.terms.Term.isTermNamed;
import static org.spoofax.terms.Term.javaInt;
import static org.spoofax.terms.Term.termAt;

import java.util.ArrayList;
import java.util.List;

import org.spoofax.NotImplementedException;
import org.spoofax.interpreter.terms.IStrategoInt;
import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoNamed;
import org.spoofax.interpreter.terms.IStrategoPlaceholder;
import org.spoofax.interpreter.terms.IStrategoReal;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.terms.ParseError;

/**
 * Implodes {ast} annotations in asfix trees.
 * 
 * Note that this class assigns a null sort to all children
 * of the constructed TNode.
 * 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class AstAnnoImploder<TNode> {

	private final ITreeFactory<TNode> factory;
	
	private final List<TNode> placeholderValues;
	
	private final IToken leftToken, rightToken;
	
	private final ITermFactory termFactory;
	
	public AstAnnoImploder(ITreeFactory<TNode> factory, ITermFactory termFactory, List<TNode> placeholderValues, IToken leftToken, IToken rightToken) {
		this.factory = factory;
		this.termFactory = termFactory;
		this.placeholderValues = placeholderValues;
		this.leftToken = leftToken;
		this.rightToken = rightToken;
	}
	
	public TNode implode(IStrategoTerm ast, String sort) throws ParseError {
		// Placeholder terms are represented as strings; must parse them and fill in their arguments
		String astString = ast.toString();
		if (astString.startsWith("\"") && astString.endsWith("\"")) {
			astString = astString.substring(1, astString.length() - 1);
			astString = astString.replace("\\\\", "\\").replace("\\\"", "\"");
			ast = termFactory.parseFromString(astString);
		}
		
		return toNode(ast, sort);
	}
	
	private TNode toNode(IStrategoTerm term, String sort) {
		switch (term.getTermType()) {
			case PLACEHOLDER:
				return placeholderToNode(term, sort);
				
			case APPL: case STRING:
				return namedToNode(term, sort);
				
			case LIST:
				return listToNode(term, sort);
				
			case INT:
				IStrategoInt i = (IStrategoInt) term;
				return factory.createIntTerminal(sort, leftToken, i.intValue());
				
			case REAL:
				IStrategoReal r = (IStrategoReal) term;
				return factory.createRealTerminal(sort, leftToken, r.realValue());
				
			default:
				throw new IllegalStateException("Unexpected term type encountered in {ast} attribute");
		}
	}
	
	private TNode placeholderToNode(IStrategoTerm placeholder, String sort) {
		IStrategoTerm term = ((IStrategoPlaceholder) placeholder).getTemplate();
		if (isTermInt(term)) {
			int id = javaInt(term);
			if (1 <= id && id <= placeholderValues.size()) {
				return placeholderValues.get(id - 1);
			}
		} else if (isTermNamed(term)) {
			String type = ((IStrategoNamed) term).getName();
			if ("conc".equals(type) && term.getSubtermCount() == 2) {
				TNode left = toNode(termAt(term, 0), null);
				TNode right = toNode(termAt(term, 1), null);
				List<TNode> children = new ArrayList<TNode>();
				for (TNode node : factory.getChildren(left))
					children.add(node);
				for (TNode node : factory.getChildren(right))
					children.add(node);
				return factory.createList(sort, leftToken, rightToken, children);
			} else if ("yield".equals(type) && term.getSubtermCount() == 1) {
				throw new NotImplementedException("not implemented: yield in {ast} attribute");
			}
		}
			
		throw new IllegalStateException("Error in syntax definition: illegal placeholder in {ast} attribute: " + placeholder);
	}
	
	private TNode namedToNode(IStrategoTerm term, String sort) {
		IStrategoNamed appl = (IStrategoNamed) term;
		ArrayList<TNode> children = new ArrayList<TNode>(appl.getSubtermCount());
		for (int i = 0; i < appl.getSubtermCount(); i++) {
			children.add(toNode(termAt(appl, i), null));
		}
		if (appl.getTermType() == STRING) {
			return factory.createStringTerminal(sort, leftToken, rightToken, appl.getName());
		} else {
			return factory.createNonTerminal(sort, appl.getName(), leftToken, rightToken, children);
		}
	}
	
	private TNode listToNode(IStrategoTerm term, String sort) {
		// TODO: Fishy (Spoofax/49)
		IStrategoList list = (IStrategoList) term;
		ArrayList<TNode> children = new ArrayList<TNode>(list.getSubtermCount());
		for (int i = 0; i < term.getSubtermCount(); i++) {
			children.add(toNode(termAt(term, i), null));
		}
		return factory.createList(sort, leftToken, rightToken, children);
	}
}
