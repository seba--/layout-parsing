package org.spoofax.jsglr_orig.client.incremental;

import static org.spoofax.jsglr_orig.client.imploder.ImploderAttachment.getElementSort;
import static org.spoofax.jsglr_orig.client.imploder.ImploderAttachment.getSort;
import static org.spoofax.terms.Term.applAt;
import static org.spoofax.terms.Term.asJavaString;
import static org.spoofax.terms.Term.isTermAppl;
import static org.spoofax.terms.Term.termAt;
import static org.spoofax.terms.Term.tryGetConstructor;
import static org.spoofax.terms.attachments.ParentAttachment.getParent;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.spoofax.interpreter.terms.ISimpleTerm;
import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoConstructor;
import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.jsglr_orig.client.Label;
import org.spoofax.jsglr_orig.client.ParseTable;
import org.spoofax.jsglr_orig.client.imploder.ProductionAttributeReader;

/**
 * A collection of incremental sorts for a particular language
 * and parse table. Uses injections from and to
 * the input set of sorts.
 * 
 * Should be reused, and not reconstructed with every parse.
 * 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class IncrementalSortSet {
	
	private final IStrategoConstructor sortFun;
	
	private final IStrategoConstructor cfFun;
	
	private final IStrategoConstructor lexFun;
	
	private final Set<String> incrementalSorts;
	
	private final Set<String> incrementalContainerSorts;

	/**
	 * @param expand
	 *            Whether to expand the set of sorts with injections to those
	 *            sorts (e.g., add MethodDec if ClassBodyDec was specified.)
	 */
	private IncrementalSortSet(ParseTable table, boolean expand, Set<String> sorts) {
		sortFun = table.getFactory().makeConstructor("sort", 1);
		cfFun = table.getFactory().makeConstructor("cf", 1);
		lexFun = table.getFactory().makeConstructor("lex", 1);
		incrementalSorts = expand ? getInjectionsTo(table, sorts, false) : sorts;
		incrementalContainerSorts = getInjectionsTo(table, incrementalSorts, true);
	}
	
	public static IncrementalSortSet create(ParseTable table, boolean expand, String... sorts) {
		return new IncrementalSortSet(table, expand, asSet(sorts));
	}
	
	public static IncrementalSortSet create(ParseTable table, boolean expand, Set<String> sorts) {
		return new IncrementalSortSet(table, expand, sorts);
	}
	
	public static IncrementalSortSet read(ParseTable table) {
		IStrategoConstructor incrementalFun = table.getFactory().makeConstructor("incremental", 0);
		ProductionAttributeReader reader = new ProductionAttributeReader(table.getFactory());
		Set<String> sorts = new HashSet<String>();
		
		for (int i = ParseTable.LABEL_BASE, max = table.getProductionCount(); i < max; i++) {
			IStrategoTerm prod = table.getProduction(i);
			if (isIncrementalProduction(prod, incrementalFun))
				sorts.add(reader.getSort(applAt(prod, 1)));
		}
		return create(table, true, sorts);
	}
	
	private static boolean isIncrementalProduction(IStrategoTerm prod, IStrategoConstructor incrementalFun) {
		IStrategoTerm attrsContainer = termAt(prod, 2);
		if (attrsContainer.getSubtermCount() > 0) {
			IStrategoList attrs = termAt(attrsContainer, 0);
			while (!attrs.isEmpty()) {
				IStrategoTerm attr = attrs.head();
				if (attr.getSubtermCount() == 1)
					attr = attr.getSubterm(0);
				if (tryGetConstructor(attr) == incrementalFun)
					return true;
				attrs = attrs.tail();
			}
		}
		return false;
	}
	
	public boolean isEmpty() {
		return incrementalSorts.size() == 0;
	}
	
	public boolean isIncrementalNode(ISimpleTerm term) {
		IStrategoTerm parent = getParent(term);
		return parent != null && parent.isList() && incrementalSorts.contains(getSort(term));
	}
	
	public boolean isIncrementalContainerNode(ISimpleTerm list) {
		assert list.isList();
		return incrementalContainerSorts.contains(getElementSort(list));
	}
	
	private static<T> Set<T> asSet(T... values) {
		Set<T> results = new HashSet<T>(values.length);
		for (T value : values)
			results.add(value);
		return results;
	}
	
	private Set<String> getInjectionsTo(ParseTable table, Collection<String> sorts, boolean reverse) {
		// TODO: optimize - store injections first instead of looping over all productions many times
		for (;;) {
			int oldSize = sorts.size();
			Set<String> results = new HashSet<String>();
			for (String sort : sorts) {
				addInjectionsTo(table, sort, reverse, results);
			}
			
			if (oldSize == results.size()) // fixpoint
				return results;
			sorts = results;
		}
	}
	
	private void addInjectionsTo(ParseTable table, String sort, boolean reverse, Set<String> results) {
		ITermFactory factory = table.getFactory();
		IStrategoTerm sortTerm = factory.makeAppl(sortFun, factory.makeString(sort));
		for (Label l : table.getLabels()) {
			if (l != null) {
				IStrategoAppl production = l.getProduction();
				IStrategoTerm sort1 = reverse ? getFromSort(production) : getToSort(production);
				if (sortTerm.equals(sort1)) {
					IStrategoTerm sort2 = reverse ? getToSort(production) : getFromSort(production);
					if (sort2 != null)
						results.add(asJavaString(termAt(sort2, 0)));
				}
			}
		}
		results.add(sort);
	}

	private IStrategoTerm getToSort(IStrategoAppl production) {
		IStrategoTerm toSort = termAt(production, 1);
		toSort = stripFun(toSort, cfFun);
		toSort = stripFun(toSort, lexFun);
		if (((IStrategoAppl) toSort).getConstructor() != sortFun) return null;
		return toSort;
	}
	
	private IStrategoTerm getFromSort(IStrategoAppl production) {
		IStrategoList lhs = termAt(production, 0);
		if (lhs.getSubtermCount() != 1) return null;
		IStrategoTerm lhsFirst = lhs.head();
		lhsFirst = stripFun(lhsFirst, cfFun);
		lhsFirst = stripFun(lhsFirst, lexFun);
		if (((IStrategoAppl) lhsFirst).getConstructor() != sortFun) return null;
		return lhsFirst;
	}
	
	public IStrategoTerm stripFun(IStrategoTerm appl, IStrategoConstructor fun) {
		if (isTermAppl(appl) && ((IStrategoAppl) appl).getConstructor() == fun) {
			return termAt(appl, 0);
		} else {
			return appl;
		}
	}
	
	@Override
	public String toString() {
		return "(" + incrementalSorts + ", " + incrementalContainerSorts + ")";
	}
}
