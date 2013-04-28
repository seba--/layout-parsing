package org.spoofax.jsglr.client.imploder;

import static org.spoofax.terms.Term.termAt;

import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoTerm;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class LabelInfo {

	private final IStrategoAppl production;
	
	private final String sort;
	
	private final String constructor;
	
	private final String deprecationMessage;
	
	private final IStrategoTerm astAttribute;
	
	private final boolean isNonContextFree;
	
	private final boolean isLexical;

	private final boolean isVar;

	private final boolean isList;

	private final boolean isIndentPaddingLexical;
	
	private final boolean isLexLayout;
	
	private final boolean isSortProduction;

	private final boolean isLayout;
	
	private final boolean isLiteral;
	
	private final boolean isOptional;
	
	private final boolean isRecover;
	
	private final boolean isReject;
	
	private final String metaVarConstructor;
	
	public LabelInfo(ProductionAttributeReader reader, IStrategoAppl production) {
		this.production = production;
		IStrategoAppl rhs = getRHS();
		IStrategoAppl attrs = getAttrs();
		sort = reader.getSort(rhs);
		constructor = reader.getConsAttribute(attrs);
		astAttribute = reader.getAstAttribute(attrs);
		isNonContextFree = reader.isNonContextFree(rhs);
		isList = reader.isList(rhs);
		isVar = reader.isVariableNode(rhs);
		isIndentPaddingLexical = reader.isIndentPaddingLexical(attrs);
		isLexLayout = reader.isLexLayout(rhs);
		isLexical = reader.isLexical(rhs);
		isLayout = reader.isLayout(rhs);
		isLiteral = reader.isLiteral(rhs);
		isOptional = reader.isOptional(rhs);
		isRecover = reader.isRecoverProduction(attrs, constructor);
		isReject = reader.isRejectProduction(attrs);
		deprecationMessage = reader.getDeprecationMessage(attrs);
		isSortProduction = reader.sortFun == rhs.getConstructor() || reader.parameterizedSortFun == rhs.getConstructor();
		metaVarConstructor = reader.getMetaVarConstructor(rhs);
	}
    
	protected IStrategoList getLHS() {
    	return termAt(production, 0);
    }
    
	protected IStrategoAppl getRHS() {
    	return termAt(production, 1);
    }
    
	protected IStrategoAppl getAttrs() {
    	return termAt(production, 2);
    }
	
	public String getSort() {
		return sort;
	}
	
	public String getConstructor() {
		return constructor;
	}
	
	public IStrategoTerm getAstAttribute() {
		return astAttribute;
	}
	
	public boolean isNonContextFree() {
		return isNonContextFree;
	}
	
	public boolean isLexical() {
		return isLexical;
	}
	
	public boolean isList() {
		return isList;
	}
	
	public boolean isVar() {
		return isVar;
	}
	
	public boolean isIndentPaddingLexical() {
		return isIndentPaddingLexical;
	}
	
	public boolean isLexLayout() {
		return isLexLayout;
	}
	
	public boolean isLayout() {
		return isLayout;
	}
	
	public boolean isLiteral() {
		return isLiteral;
	}
	
	public boolean isRecover() {
		return isRecover;
	}
	
	public boolean isReject() {
		return isReject;
	}
	
	public String getDeprecationMessage() {
		return deprecationMessage;
	}
	
	public boolean isSortProduction() {
		return isSortProduction;
	}
	
	public String getMetaVarConstructor() {
		return metaVarConstructor;
	}

	public boolean isOptional() {
		return isOptional;
	}
	
	@Override
	public String toString() {
		return production.toString();
	}
}
