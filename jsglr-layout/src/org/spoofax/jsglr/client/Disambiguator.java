/*
 * Created on 11.apr.2006
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 *
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr.client;

import static org.spoofax.jsglr.client.AbstractParseNode.AMBIGUITY;
import static org.spoofax.jsglr.client.AbstractParseNode.AVOID;
import static org.spoofax.jsglr.client.AbstractParseNode.CYCLE;
import static org.spoofax.jsglr.client.AbstractParseNode.PARSENODE;
import static org.spoofax.jsglr.client.AbstractParseNode.PARSE_PRODUCTION_NODE;
import static org.spoofax.jsglr.client.AbstractParseNode.PREFER;
import static org.spoofax.jsglr.client.AbstractParseNode.REJECT;
import static org.spoofax.terms.Term.termAt;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.spoofax.NotImplementedException;
import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.ProductionAttributeReader;
import org.spoofax.jsglr.client.indentation.LayoutFilter;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr.shared.Tools;

/**
 * @author Karl Trygve Kalleberg <karltk near strategoxt.org>
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class Disambiguator {

  private static final int FILTER_DRAW = 1;

  private static final int FILTER_LEFT_WINS = 2;

  private static final int FILTER_RIGHT_WINS = 3;

  private boolean filterAny;

  private boolean filterCycles;

  private boolean filterDirectPreference;

  private boolean filterPreferenceCount;

  private boolean filterInjectionCount;

  private boolean filterLongestMatch;

  private boolean filterTopSort;

  private boolean filterReject;

  private boolean filterAssociativity;

  private boolean filterPriorities;

  private boolean filterStrict;

  private boolean logStatistics;

  private boolean ambiguityIsError;

  private int ambiguityCount;
  private int newAmbiguityCount;

  // Current parser state

  private AmbiguityManager ambiguityManager;

  private SGLR parser;

  private ParseTable parseTable;

  private ProductionAttributeReader prodReader;

  private LayoutFilter layoutFilter;
  private int layoutFiltering;
  
  // private Map<AmbKey, IParseNode> resolvedTable = new HashMap<AmbKey,
  // IParseNode>();

  /**
   * Sets whether any filter should be applied at all (excluding the top sort
   * filter).
   */
  public final void setFilterAny(boolean filterAny) {
    this.filterAny = filterAny;
  }

  public final void setFilterDirectPreference(boolean filterDirectPreference) {
    this.filterDirectPreference = filterDirectPreference;
  }

  public boolean getFilterDirectPreference() {
    return filterDirectPreference;
  }

  /**
   * For preference count filtering, see
   * {@link #setFilterPreferenceCount(boolean)}.
   */
  @Deprecated
  public final void setFilterIndirectPreference(boolean filterIndirectPreference) {
    throw new UnsupportedOperationException();
  }

  /**
   * For preference count filtering, see {@link #getFilterPreferenceCount()}.
   */
  @Deprecated
  public boolean getFilterIndirectPreference() {
    throw new UnsupportedOperationException();
  }

  public final void setFilterInjectionCount(boolean filterInjectionCount) {
    this.filterInjectionCount = filterInjectionCount;
  }

  public boolean getFilterInjectionCount() {
    return filterInjectionCount;
  }

  public final void setFilterLongestMatch(boolean filterLongestMatch) {
    this.filterLongestMatch = filterLongestMatch;
  }

  public boolean getLongestMatch() {
    return filterLongestMatch;
  }

  public final void setFilterPreferenceCount(boolean filterPreferenceCount) {
    this.filterPreferenceCount = filterPreferenceCount;
  }

  public boolean getFilterPreferenceCount() {
    return filterPreferenceCount;
  }

  public final void setFilterTopSort(boolean filterTopSort) {
    this.filterTopSort = filterTopSort;
  }

  public boolean getFilterTopSort() {
    return filterTopSort;
  }

  public void setFilterCycles(boolean filterCycles) {
    this.filterCycles = filterCycles;
  }

  public boolean isFilterCycles() {
    return filterCycles;
  }

  public void setFilterAssociativity(boolean filterAssociativity) {
    this.filterAssociativity = filterAssociativity;
  }

  public boolean getFilterAssociativity() {
    return filterAssociativity;
  }

  public void setFilterPriorities(boolean filterPriorities) {
    this.filterPriorities = filterPriorities;
  }

  public boolean getFilterPriorities() {
    return filterPriorities;
  }

  /**
   * Sets whether to enable strict filtering, triggering a FilterException when
   * the priorities filter encounters an unfiltered ambiguity.
   */
  public void setFilterStrict(boolean filterStrict) {
    this.filterStrict = filterStrict;
  }

  public boolean getFilterStrict() {
    return filterStrict;
  }

  public final void setHeuristicFilters(boolean heuristicFilters) {
    setFilterPreferenceCount(heuristicFilters);
    setFilterInjectionCount(heuristicFilters);
  }

  public void setFilterReject(boolean filterReject) {
    this.filterReject = filterReject;
  }

  public boolean getFilterReject() {
    return filterReject;
  }

  public void setLogStatistics(boolean logStatistics) {
    this.logStatistics = logStatistics;
  }

  public boolean getLogStatistics() {
    return logStatistics;
  }

  public void setAmbiguityIsError(boolean ambiguityIsError) {
    this.ambiguityIsError = ambiguityIsError;
  }

  public boolean getAmbiguityIsError() {
    return ambiguityIsError;
  }

  public int getAmbiguityCount() {
    return newAmbiguityCount;
  }
  
  public int getLayoutFilteringCount() {
    return layoutFiltering;
  }

  public int getLayoutFilterCallCount() {
    return layoutFilter == null ? 0 : layoutFilter.getFilterCallCount();
  }

  public final void setDefaultFilters() {
    filterAny = true;
    filterCycles = false; // TODO: filterCycles; enable by default
    filterDirectPreference = true;
    filterPreferenceCount = false;
    filterInjectionCount = false;
    filterLongestMatch = true;
    filterTopSort = true;
    filterReject = true;
    filterAssociativity = true;
    filterPriorities = true;
    filterStrict = false; // TODO: disable filterStrict hack
    logStatistics = true;
    ambiguityIsError = false;
  }

  public Disambiguator() {
    setDefaultFilters();
  }

  public Object applyFilters(SGLR parser, AbstractParseNode root, String sort,
      int inputLength) throws SGLRException, FilterException, InterruptedException {
    AbstractParseNode t = root;
    if (Tools.debugging) {
      Tools.debug("applyFilters()");
    }

    try {
      try {
        if (Tools.debugging) {
          Tools.debug("applyFilters()");
        }

        initializeFromParser(parser);
        t = applyTopSortFilter(sort, t);

        if (filterAny) {
          t = applyCycleDetectFilter(t);

          // SG_FilterTree
          ambiguityManager.resetClustersVisitedCount();
          t = filterTree(t, false);
        }

        if (t == null)
          return null;
      } catch (RuntimeException e) {
        throw new FilterException(parser,
            "Runtime exception when applying filters", e);
      }
      
      newAmbiguityCount = t.getAmbiguityCount();

      return yieldTreeTop(t);

    } finally {
//      System.out.println("  layout filter calls at parse time: " + parser.getLayoutFilterCallCount());
//      System.out.println("  illegal layout filtered at parse time: " + parser.getLayoutFilteringCount());
//      System.out.println("  layout filter calls at disambiguation time: " + getLayoutFilterCallCount());
//      System.out.println("  illegal layout filtered at disambiguation time: " + getLayoutFilteringCount());
//      System.out.println("  enforced newline skips: " + parser.getEnforcedNewlineSkips());
//      initializeFromParser(null);
    }
  }

  public void initializeFromParser(SGLR parser) {
    if (parser == null) {
      this.parser = null;
      parseTable = null;
      ambiguityCount += ambiguityManager == null ? 0 : ambiguityManager.getAmbiguitiesCount();
      ambiguityManager = null;
      layoutFilter = null;
    } else {
      this.parser = parser;
      parseTable = parser.getParseTable();
      prodReader = new ProductionAttributeReader(parseTable.getFactory());
      ambiguityManager = parser.getAmbiguityManager();
      ambiguityCount = 0;
      layoutFilter = new LayoutFilter(parseTable, false);
      layoutFiltering = 0;
    }
  }

  private void logStatus() {
    Tools.logger("Number of rejects: ", parser.getRejectCount());
    Tools.logger("Number of reductions: ", parser.getReductionCount());
    Tools.logger("Number of ambiguities: ",
        ambiguityManager.getMaxNumberOfAmbiguities());
    Tools.logger("Number of calls to Amb: ",
        ambiguityManager.getAmbiguityCallsCount());
    Tools.logger("Count Eagerness Comparisons: ",
        ambiguityManager.getEagernessComparisonCount(), " / ",
        ambiguityManager.getEagernessSucceededCount());
    Tools.logger("Number of Injection Counts: ",
        ambiguityManager.getInjectionCount());
  }

  private Object yieldTree(AbstractParseNode t) {
    parser.getTreeBuilder().reset(); // in case yieldTree is used for debugging
    return parser.getTreeBuilder().buildTree(t);
  }

  private Object yieldTreeTop(AbstractParseNode t) throws SGLRException {
    int ambCount = ambiguityManager.getAmbiguitiesCount();

    if (Tools.debugging) {
      Tools.debug("convertToATerm: ", t);
    }

    try {
      ambiguityCount += ambiguityManager.getAmbiguitiesCount();
      ambiguityManager.resetAmbiguityCount();
      final Object r = yieldTree(t);

      if (logStatistics)
        logStatus();

      if (Tools.debugging) {
        Tools.debug("yield: ", r);
      }

      if (ambiguityIsError && ambCount > 0) {
        throw new SGLRException(parser, "Ambiguities found");
      } else {
        return parser.getTreeBuilder().buildTreeTop(r, ambCount);
      }
    } finally {
      parser.getTreeBuilder().reset();
    }
  }

  private AbstractParseNode applyCycleDetectFilter(AbstractParseNode t)
      throws FilterException {

    if (Tools.debugging) {
      Tools.debug("applyCycleDetectFilter() - ", t);
    }

    if (filterCycles) {
      if (ambiguityManager.getMaxNumberOfAmbiguities() > 0) {
        if (isCyclicTerm(t)) {
          throw new FilterException(parser, "Term is cyclic");
        }
      }
    }

    return t;
  }

  private IStrategoTerm getProduction(AbstractParseNode t) {
    if (t.isParseNode()) {
      return parseTable.getProduction(((ParseNode) t).getLabel());
    } else {
      return parseTable.getProduction(((ParseProductionNode) t).getLabel());
    }
  }

  private AbstractParseNode applyTopSortFilter(String sort, AbstractParseNode t)
      throws SGLRException {

    if (Tools.debugging) {
      Tools.debug("applyTopSortFilter() - ", t);
    }

    if (sort != null && filterTopSort) {
      t = selectOnTopSort(t, sort);
      if (t == null) {
        throw new StartSymbolException(parser,
            "Desired start symbol not found: " + sort);
      }
    }

    return t;
  }

  private boolean matchProdOnTopSort(IStrategoTerm prod, String sort)
      throws FilterException {
    assert sort != null;
    /*
     * sort = sort.replaceAll("\"", ""); return
     * prod.match("prod([cf(opt(layout)),cf(sort(\"" + sort +
     * "\")),cf(opt(layout))], sort(\"<START>\"),no-attrs)") != null ||
     * prod.match("prod([cf(sort(\"" + sort +
     * "\"))], sort(\"<START>\"),no-attrs)") != null ||
     * prod.match("prod([lex(sort(\"" + sort +
     * "\"))], sort(\"<START>\"),no-attrs)") != null ||
     * prod.match("prod([sort(\"" + sort + "\")], sort(\"<START>\"),no-attrs)")
     * != null;
     */
    IStrategoList lhs = termAt(prod, 0);
    IStrategoAppl rhs = termAt(prod, 1);
    String foundSort = prodReader.tryGetFirstSort(lhs);
    assert foundSort != null;
    assert "<START>".equals(prodReader.tryGetSort(rhs));
    return sort.equals(foundSort);
  }

  private AbstractParseNode selectOnTopSort(AbstractParseNode t, String sort)
      throws FilterException {
    final List<AbstractParseNode> results = new ArrayList<AbstractParseNode>();

    if (t.isAmbNode()) {
      addTopSortAlternatives(t, sort, results);

      switch (results.size()) {
      case 0:
        return null;
      case 1:
        return results.get(0);
      default:
        ambiguityManager.increaseAmbiguityCount();
        return ParseNode.createAmbNode(results.toArray(new AbstractParseNode[results.size()]));
      }
    } else {
      final IStrategoTerm prod = getProduction(t);
      return matchProdOnTopSort(prod, sort) ? t : null;
    }
  }

  private void addTopSortAlternatives(AbstractParseNode t, String sort,
      List<AbstractParseNode> results) throws FilterException {
    for (final AbstractParseNode amb : t.getChildren()) {
      if (amb.isAmbNode()) {
        addTopSortAlternatives(amb, sort, results);
      } else {
        final IStrategoTerm prod = getProduction(amb);
        if (matchProdOnTopSort(prod, sort)) {
          results.add(amb);
        }
      }
    }
  }

  /**
   * @param inAmbiguityCluster
   *          We're inside an amb and can return null to reject this branch.
   * @throws InterruptedException 
   */
  public AbstractParseNode filterTree(AbstractParseNode node, boolean inAmbiguityCluster) throws FilterException, InterruptedException {
    // SG_FilterTreeRecursive
    if (Tools.debugging) {
      Tools.debug("filterTree(node)    - ", node);
    }
    
    LinkedList<AbstractParseNode> input= new LinkedList<AbstractParseNode>();
    LinkedList<AbstractParseNode> output = new LinkedList<AbstractParseNode>();
    LinkedList<AbstractParseNode> pending = new LinkedList<AbstractParseNode>();

    input.push(node);

    while (!input.isEmpty() || !pending.isEmpty()) {
      if (Thread.currentThread().isInterrupted())
        throw new InterruptedException();
      
      int pendingPeekPos = pending.isEmpty() ? -1 : output.size() - pending.peek().getChildren().length - 1;
      if (!pending.isEmpty() && pendingPeekPos >= 0 && output.get(output.size() - pendingPeekPos - 1) == pending.peek()) {
        AbstractParseNode t = pending.pop();
        
        AbstractParseNode[] args = new AbstractParseNode[t.getChildren().length];
        boolean changed = false;
        
        boolean rejected = false;
        for (int i = t.getChildren().length - 1; i >= 0; i--) {
          args[i] = output.pop();
          rejected = rejected || args[i] == null;
          changed = changed || args[i] != t.getChildren()[i];
        }
        
        output.pop();
        
        
        if (!rejected && changed)
          t = new ParseNode(t.getLabel(), 
                            args, 
                            t.getNodeType(),
                            t.getLine(),
                            t.getColumn(),
                            t.isLayout(),
                            t.isIgnoreLayout());
        
        if (!rejected) {
          if (filterAssociativity)
            t = applyAssociativityPriorityFilter(t);

          if (!layoutFilter.hasValidLayout((ParseNode) t)) {
            layoutFiltering++;
            rejected = true;
          }
          else
            layoutFiltering += layoutFilter.getDisambiguationCount();

          ambiguityManager.decreaseAmbiguityCount(layoutFilter.getDisambiguationCount());
        }
        
        if (rejected)
          return null;
        
        output.push(t);
      }
      else {
        AbstractParseNode t = input.pop();
      
        switch (t.getNodeType()) {
        case AMBIGUITY:
          if (!(inAmbiguityCluster && output.isEmpty())) {
            // (some cycle stuff should be done here)
            final AbstractParseNode[] ambs = t.getChildren();
            t = filterAmbiguities(ambs);
            if (t == null)
              return null;
            output.push(t);
          } else {
            // FIXME: hasRejectProd(Amb) can never succeed?
            if (filterReject && parseTable.hasRejects() && hasRejectProd(t)) {
              output.push(t);
            }
            else {
              final AbstractParseNode[] ambs = t.getChildren();
              output.push(filterAmbiguities(ambs));
            }
  
          }
          break;
        case PARSENODE:
        case AVOID:
        case PREFER:
        case REJECT:

          boolean rejected = false;
          
          if (!layoutFilter.hasValidLayout((ParseNode) t)) {
            layoutFiltering++;
            rejected = true;
          }
          else
            layoutFiltering += layoutFilter.getDisambiguationCount();
          
          if (!rejected && filterReject && parseTable.hasRejects() && hasRejectProd(t)) {
            rejected = true;
          }
          
          if (rejected)
            return null;
          else {
            output.push(t);
            if (t.getChildren().length > 0 && !t.isParseProductionChain()) {
              pending.push(t);
              for (int i = t.getChildren().length - 1; i >= 0; i--)
                input.push(t.getChildren()[i]);
            }
          }
          break;
        case PARSE_PRODUCTION_NODE:
          // leaf node -- do nothing (cannot be any ambiguities here)
          output.push(t);
          break;
        case CYCLE:
          output.push(t);
          break;
        default:
          throw new IllegalStateException("Unknown node type: " + t);
        }
      }
    }
    
    assert output.size() == 1;
    return output.peek();
  }

  private AbstractParseNode applyAssociativityPriorityFilter(AbstractParseNode t)
      throws FilterException {
    // SG_Associativity_Priority_Filter(pt, t)
    // - ok

    if (Tools.debugging) {
      Tools.debug("applyAssociativityPriorityFilter() - ", t);
    }

    AbstractParseNode r = t;

    if (t.isParseNode()) {
      final Label prodLabel = getProductionLabel(t);
      final ParseNode n = (ParseNode) t;

      if (filterAssociativity) {
        if (prodLabel.isLeftAssociative()) {
          r = applyLeftAssociativeFilter(n, prodLabel);
        } else if (prodLabel.isRightAssociative()) {
          r = applyRightAssociativeFilter(n, prodLabel);
        }

      }

      if (filterPriorities && parseTable.hasPriorities()) {
        if (Tools.debugging) {
          Tools.debug(" - about to look up : ", prodLabel.labelNumber);
        }

        if (!lookupGtrPriority(prodLabel).isEmpty()) {
          if (Tools.debugging) {
            Tools.debug(" - found");
          }
          if (r.isAmbNode()) {
            return r;
          }
          return applyPriorityFilter((ParseNode) r, prodLabel);
        }
        if (Tools.debugging) {
          Tools.debug(" - not found");
        }
      }
    }

    return r;
  }

  private AbstractParseNode applyRightAssociativeFilter(ParseNode t,
      Label prodLabel) throws FilterException {
    // SG_Right_Associativity_Filter(t, prodl)
    // - almost ok

    if (Tools.debugging) {
      Tools.debug("applyRightAssociativeFilter() - ", t);
    }

    final List<AbstractParseNode> newAmbiguities = new ArrayList<AbstractParseNode>();
    final AbstractParseNode[] kids = t.getChildren();
    final AbstractParseNode firstKid = kids[0];

    if (firstKid.isAmbNode()) {

      for (final AbstractParseNode amb : firstKid.getChildren()) {
        if (((ParseNode) amb).getLabel() != prodLabel.labelNumber) {
          newAmbiguities.add(amb);
        }
      }

      final int additionalAmbNodes = newAmbiguities.isEmpty() ? 0 : 1;
      final AbstractParseNode[] restKids = new AbstractParseNode[kids.length
          - 1 + additionalAmbNodes];
      System.arraycopy(kids, 1, restKids, 0, kids.length - 1);

      // FIXME is this correct?
      if (!newAmbiguities.isEmpty()) {
        AbstractParseNode extraAmb;
        if (newAmbiguities.size() > 1) {
          extraAmb = ParseNode.createAmbNode(newAmbiguities.toArray(new AbstractParseNode[newAmbiguities.size()]));
          ambiguityManager.increaseAmbiguityCount();
        } else {
          extraAmb = newAmbiguities.get(0);
        }
        restKids[restKids.length - 1] = extraAmb;
      } else {
        throw new FilterException(parser);
      }

      // FIXME is this correct?
      return new ParseNode(t.getLabel(), restKids, AbstractParseNode.PARSENODE, t.getLine(), t.getColumn(), t.isLayout(), t.isIgnoreLayout());

    } else if (firstKid.isParseNode()) {
      if (((ParseNode) firstKid).getLabel() == prodLabel.labelNumber) {
        throw new FilterException(parser);
      }
    }
    return t;
  }

  private AbstractParseNode applyPriorityFilter(ParseNode t, Label prodLabel)
      throws FilterException {
    // SG_Priority_Filter

    if (Tools.debugging) {
      Tools.debug("applyPriorityFilter() - ", t);
    }

    final List<AbstractParseNode> newAmbiguities = new ArrayList<AbstractParseNode>();
    final List<AbstractParseNode> newKids = new ArrayList<AbstractParseNode>();

    final int l0 = prodLabel.labelNumber;
    int kidnumber = 0;

    for (final AbstractParseNode kid : t.getChildren()) {
      AbstractParseNode newKid = kid;
      final AbstractParseNode injection = jumpOverInjections(kid);

      if (injection.isAmbNode()) {
        newAmbiguities.clear();
        for (final AbstractParseNode amb : injection.getChildren()) {
          final AbstractParseNode injAmb = jumpOverInjections(amb);

          if (injAmb.isParseNode()) {
            final Label label = getProductionLabel(t);
            if (hasGreaterPriority(l0, label.labelNumber, kidnumber)) {
              newAmbiguities.add(amb);
            }
          }
        }

        if (!newAmbiguities.isEmpty()) {
          AbstractParseNode n = null;
          if (newAmbiguities.size() > 1) {
            n = ParseNode.createAmbNode(newAmbiguities.toArray(new AbstractParseNode[newAmbiguities.size()]));
            ambiguityManager.increaseAmbiguityCount();
          } else {
            n = newAmbiguities.get(0);
          }
          newKid = replaceUnderInjections(kid, injection, n);
        } else {
          // fishy: another filter might be borked
          if (filterStrict) {
            throw new FilterException(parser);
          } else {
            // TODO: log or whatever?
            return t;
          }
        }
      } else if (injection.isParseNode()) {
        final int l1 = ((ParseNode) injection).getLabel();
        if (hasGreaterPriority(l0, l1, kidnumber)) {
          throw new FilterException(parser);
        }
      }

      newKids.add(newKid);
      kidnumber++;
    }

    // FIXME (KTK) get rid of toArray by precomputing the necessary size of
    // newKids earlier in the method
    return new ParseNode(t.getLabel(),
        newKids.toArray(new AbstractParseNode[newKids.size()]), AbstractParseNode.PARSENODE, t.getLine(), t.getColumn(), t.isLayout(), t.isIgnoreLayout());
  }

  private AbstractParseNode replaceUnderInjections(AbstractParseNode alt,
      AbstractParseNode injection, AbstractParseNode n) throws FilterException {
    // SG_Replace_Under_Injections
    // - not ok

    throw new FilterException(parser,
        "replaceUnderInjections is not implemented",
        new NotImplementedException());
    /*
     * if (ATisEqual(t, injT)) { return newTree; } else { IStrategoList sons =
     * (IStrategoList)ATgetArgument((ATerm) t, 1); tree newSon =
     * SG_Replace_Under_Injections((tree)ATgetFirst(sons), injT, newTree);
     * return ATsetArgument((ATermAppl)t, (ATerm)ATmakeList1((ATerm)newSon), 1);
     * }
     */
  }

  private AbstractParseNode jumpOverInjections(AbstractParseNode t) {

    if (Tools.debugging) {
      Tools.debug("jumpOverInjections() - ", t);
    }

    if (t.isParseNode()) {
      int prod = ((ParseNode) t).getLabel();
      ParseNode n = (ParseNode) t;
      while (isUserDefinedLabel(prod)) {
        final AbstractParseNode x = n.kids[0];
        if (x.isParseNode()) {
          n = (ParseNode) x;
          prod = n.getLabel();
        } else {
          return x;
        }
      }
    }

    return t;
  }

  // TODO: shouldn't this be called isInjection?

  private boolean isUserDefinedLabel(int prod) {
    final Label l = parseTable.lookupInjection(prod);
    if (l == null) {
      return false;
    }
    return l.isInjection();
  }

  private boolean hasGreaterPriority(int l0, int l1, int arg) {
    final List<Priority> prios = lookupGtrPriority(parseTable.getLabel(l0));

    for (int i = 0, size = prios.size(); i < size; i++) {
      final Priority p = prios.get(i);
      if (l1 == p.right) {
        if (p.arg == -1 || p.arg == arg) {
          return true;
        }
      }
    }
    return false;
  }

  private List<Priority> lookupGtrPriority(Label prodLabel) {
    return parseTable.getPriorities(prodLabel);
  }

  private AbstractParseNode applyLeftAssociativeFilter(ParseNode t,
      Label prodLabel) throws FilterException {
    // SG_Right_Associativity_Filter()

    if (Tools.debugging) {
      Tools.debug("applyLeftAssociativeFilter() - ", t);
    }

    final List<AbstractParseNode> newAmbiguities = new ArrayList<AbstractParseNode>();
    final AbstractParseNode[] kids = t.kids;
    AbstractParseNode last = kids[kids.length - 1];

    if (last.isAmbNode()) {

      for (final AbstractParseNode amb : last.getChildren()) {
        if (amb.isAmbNode()
            || !parseTable.getLabel(((ParseNode) amb).getLabel()).equals(
                prodLabel)) {
          newAmbiguities.add(amb);
        }
      }

      if (!newAmbiguities.isEmpty()) {
        final AbstractParseNode[] rest = new AbstractParseNode[kids.length];
        for (int i = 0; i < kids.length - 1; i++) {
          rest[i] = kids[i];
        }

        if (newAmbiguities.size() > 1) {
          last = ParseNode.createAmbNode(newAmbiguities.toArray(new AbstractParseNode[newAmbiguities.size()]));
          ambiguityManager.increaseAmbiguityCount();
        } else {
          last = newAmbiguities.get(0);
        }
        rest[rest.length - 1] = last;
        ambiguityManager.increaseAmbiguityCount();
        return new ParseNode(t.getLabel(), rest, AbstractParseNode.PARSENODE, t.getLine(), t.getColumn(), t.isLayout(), t.isIgnoreLayout());
      } else {
        throw new FilterException(parser);
      }
    } else if (last.isParseNode()) {
      final Label other = parseTable.getLabel(((ParseNode) last).getLabel());
      if (prodLabel.equals(other)) {
        throw new FilterException(parser);
      }
    }

    return t;
  }

  private Label getProductionLabel(AbstractParseNode t) {
    if (t.isParseNode()) {
      return parseTable.getLabel(((ParseNode) t).getLabel());
    } else if (t instanceof ParseProductionNode) {
      return parseTable.getLabel(((ParseProductionNode) t).getLabel());
    }
    return null;
  }

  private boolean hasRejectProd(AbstractParseNode t) {
    return t.isParseRejectNode();
  }

  private AbstractParseNode filterAmbiguities(AbstractParseNode[] ambs)
      throws FilterException, InterruptedException {
    // SG_FilterAmb

    if (Tools.debugging) {
      Tools.debug("filterAmbiguities() - [", ambs.length, "]");
    }

    List<AbstractParseNode> newAmbiguities = new ArrayList<AbstractParseNode>();

    for (final AbstractParseNode amb : ambs) {
      final AbstractParseNode newAmb = filterTree(amb, true);
      if (newAmb != null) {
        newAmbiguities.add(newAmb);
      }
    }

    if (newAmbiguities.size() > 1) {
      /* Handle ambiguities inside this ambiguity cluster */
      final List<AbstractParseNode> oldAmbiguities = new ArrayList<AbstractParseNode>(
          newAmbiguities);
      for (final AbstractParseNode amb : oldAmbiguities) {
        if (newAmbiguities.remove(amb)) {
          newAmbiguities = filterAmbiguityList(newAmbiguities, amb);
        }
      }
    }

    if (newAmbiguities.isEmpty()) {
      // All alternatives were rejected;
      // the outer context should be rejected as well
      return null;
    }

    if (newAmbiguities.size() == 1) {
      return newAmbiguities.get(0);
    }
    
    ambiguityManager.increaseAmbiguityCount();
    return ParseNode.createAmbNode(newAmbiguities.toArray(new AbstractParseNode[newAmbiguities.size()]));
  }

  private List<AbstractParseNode> filterAmbiguityList(
      List<AbstractParseNode> ambs, AbstractParseNode t) {
    // SG_FilterAmbList

    boolean keepT = true;
    final List<AbstractParseNode> r = new ArrayList<AbstractParseNode>();

    if (ambs.isEmpty()) {
      r.add(t);
      return r;
    }

    for (int i = 0, max = ambs.size(); i < max; i++) {
      final AbstractParseNode amb = ambs.get(i);
      switch (filter(t, amb)) {
      case FILTER_DRAW:
        r.add(amb);
        break;
      case FILTER_RIGHT_WINS:
        r.add(amb);
        keepT = false;
      }
    }

    if (keepT) {
      r.add(t);
    }

    return r;
  }

  private int filter(AbstractParseNode left, AbstractParseNode right) {
    // SG_Filter(t0, t1)

    if (Tools.debugging) {
      Tools.debug("filter()");
    }

    if (left.equals(right)) {
      return FILTER_LEFT_WINS;
    }

    /*
     * UNDONE: direct eagerness filter seems to be disabled in reference SGLR if
     * (filterDirectPreference && parseTable.hasPrefersOrAvoids()) { int r =
     * filterOnDirectPrefers(left, right); if (r != FILTER_DRAW) return r; }
     */

    // like C-SGLR, we use indirect preference filtering if the direct one is
    // enabled
    if (filterDirectPreference && parseTable.hasPrefersOrAvoids()) {
      final int r = filterOnIndirectPrefers(left, right);
      if (r != FILTER_DRAW) {
        return r;
      }
    }

    if (filterPreferenceCount && parseTable.hasPrefersOrAvoids()) {
      final int r = filterOnPreferCount(left, right);
      if (r != FILTER_DRAW) {
        return r;
      }
    }

    if (filterInjectionCount) {
      final int r = filterOnInjectionCount(left, right);
      if (r != FILTER_DRAW) {
        return r;
      }
    }
    
    if (filterLongestMatch) {
      final int r = filterLongestMatch(left, right);
      if (r != FILTER_DRAW) {
        return r;
      }
    }
    
    return filterPermissiveLiterals(left, right);
  }

  private int filterPermissiveLiterals(AbstractParseNode left,
      AbstractParseNode right) {
    // Work-around for http://bugs.strategoxt.org/browse/SPI-5 (Permissive
    // grammars introduce ambiguities for literals)

    if (left.isParseNode() && right.isParseNode()) {
      final AbstractParseNode[] leftKids = ((ParseNode) left).kids;
      final AbstractParseNode[] rightKids = ((ParseNode) right).kids;
      if (leftKids.length > 0 && rightKids.length == 1) {
        if (leftKids[0] instanceof ParseProductionNode
            && rightKids[0].equals(left)) {
          return FILTER_LEFT_WINS;
        }
      }
    }
    return FILTER_DRAW;
  }

  private int filterOnInjectionCount(AbstractParseNode left,
      AbstractParseNode right) {

    if (Tools.debugging) {
      Tools.debug("filterOnInjectionCount()");
    }

    ambiguityManager.increaseInjectionCount();

    final int leftInjectionCount = countAllInjections(left);
    final int rightInjectionCount = countAllInjections(right);

    if (leftInjectionCount != rightInjectionCount) {
      ambiguityManager.increaseInjectionFilterSucceededCount();
    }

    if (leftInjectionCount > rightInjectionCount) {
      return FILTER_RIGHT_WINS;
    } else if (rightInjectionCount > leftInjectionCount) {
      return FILTER_LEFT_WINS;
    }

    return FILTER_DRAW;
  }

  private int countAllInjections(AbstractParseNode t) {
    // SG_CountAllInjectionsInTree
    // - ok
    if (t.isAmbNode()) {
      // Trick from forest.c
      return t.getChildren().length == 0 ? 0 : countAllInjections(t
          .getChildren()[0]);
    } else if (t.isParseNode()) {
      final int c = getProductionLabel(t).isInjection() ? 1 : 0;
      return c + countAllInjections(((ParseNode) t).kids);
    }
    return 0;
  }

  private int countAllInjections(AbstractParseNode[] ls) {
    // SG_CountAllInjectionsInTree
    // - ok
    int r = 0;
    for (int i = 0, max = ls.length; i < max; i++) {
      r += countAllInjections(ls[i]);
    }
    return r;
  }

  private int filterOnPreferCount(AbstractParseNode left,
      AbstractParseNode right) {

    if (Tools.debugging) {
      Tools.debug("filterOnPreferCount()");
    }

    ambiguityManager.increaseEagernessFilterCalledCount();

    int r = FILTER_DRAW;
    if (parseTable.hasPrefers() || parseTable.hasAvoids()) {
      final int leftPreferCount = countPrefers(left);
      final int rightPreferCount = countPrefers(right);
      final int leftAvoidCount = countAvoids(left);
      final int rightAvoidCount = countAvoids(right);

      if ((leftPreferCount > rightPreferCount && leftAvoidCount <= rightAvoidCount)
          || (leftPreferCount == rightPreferCount && leftAvoidCount < rightAvoidCount)) {
        Tools.logger("Eagerness priority: ", left, " > ", right);
        r = FILTER_LEFT_WINS;
      }

      if ((rightPreferCount > leftPreferCount && rightAvoidCount <= leftAvoidCount)
          || (rightPreferCount == leftPreferCount && rightAvoidCount < leftAvoidCount)) {
        if (r != FILTER_DRAW) {
          Tools.logger("Symmetric eagerness priority: ", left, " == ", right);
          r = FILTER_DRAW;
        } else {
          Tools.logger("Eagerness priority: ", right, " > ", left);
          r = FILTER_RIGHT_WINS;
        }
      }
    }

    if (r != FILTER_DRAW) {
      ambiguityManager.increaseEagernessFilterSucceededCount();
    }

    return r;
  }

  private int countPrefers(AbstractParseNode t) {
    // SG_CountPrefersInTree
    // - ok
    if (t.isAmbNode()) {
      return countPrefers(t.getChildren());
    } else if (t.isParseNode()) {
      final int type = getProductionType(t);
      if (type == ProductionType.PREFER) {
        return 1;
      } else if (type == ProductionType.AVOID) {
        return 0;
      }
      return countPrefers(((ParseNode) t).kids);
    }
    return 0;
  }

  private int countPrefers(AbstractParseNode[] ls) {
    // SG_CountPrefersInTree
    // - ok
    int r = 0;
    for (final AbstractParseNode n : ls) {
      r += countPrefers(n);
    }
    return r;
  }

  private int countAvoids(AbstractParseNode t) {
    // SG_CountAvoidsInTree
    // - ok
    if (t.isAmbNode()) {
      return countAvoids(t.getChildren());
    } else if (t.isParseNode()) {
      final int type = getProductionType(t);
      if (type == ProductionType.PREFER) {
        return 0;
      } else if (type == ProductionType.AVOID) {
        return 1;
      }
      return countAvoids(((ParseNode) t).kids);
    }
    return 0;
  }

  private int countAvoids(AbstractParseNode[] ls) {
    // SG_CountAvoidsInTree
    // - ok
    int r = 0;
    for (final AbstractParseNode n : ls) {
      r += countAvoids(n);
    }
    return r;
  }

  private int filterOnIndirectPrefers(AbstractParseNode left,
      AbstractParseNode right) {
    // SG_Indirect_Eagerness_Filter

    if (Tools.debugging) {
      Tools.debug("filterOnIndirectPrefers()");
    }

    if (left.isAmbNode() || right.isAmbNode()) {
      return FILTER_DRAW;
    }

    if (!getLabel(left).equals(getLabel(right))) {
      return filterOnDirectPrefers(left, right);
    }

    final ParseNode l = (ParseNode) left;
    final ParseNode r = (ParseNode) right;

    final AbstractParseNode[] leftArgs = l.kids;
    final AbstractParseNode[] rightArgs = r.kids;

    final int diffs = computeDistinctArguments(leftArgs, rightArgs);

    if (diffs == 1) {
      for (int i = 0; i < leftArgs.length; i++) {
        final AbstractParseNode leftArg = leftArgs[i];
        final AbstractParseNode rightArg = rightArgs[i];

        if (!leftArg.equals(rightArg)) {
          return filterOnIndirectPrefers(leftArg, rightArg);
        }
      }

    }
    return FILTER_DRAW;
  }

  private int filterOnDirectPrefers(AbstractParseNode left,
      AbstractParseNode right) {
    // SG_Direct_Eagerness_Filter

    if (Tools.debugging) {
      Tools.debug("filterOnDirectPrefers()");
    }

    // TODO: optimize - move up the jumpOverInjectionsModuloEagerness calls
    if (isLeftMoreEager(left, right)) {
      return FILTER_LEFT_WINS;
    }
    if (isLeftMoreEager(right, left)) {
      return FILTER_RIGHT_WINS;
    }

    return FILTER_DRAW;
  }

  private boolean isLeftMoreEager(AbstractParseNode left,
      AbstractParseNode right) {
    assert !(left.isAmbNode() || right.isAmbNode());
    if (isMoreEager(left, right)) {
      return true;
    }

    final AbstractParseNode newLeft = jumpOverInjectionsModuloEagerness(left);
    final AbstractParseNode newRight = jumpOverInjectionsModuloEagerness(right);

    if (newLeft.isParseNode() && newRight.isParseNode()) {
      return isMoreEager(newLeft, newRight);
    }

    return false;
  }

  private AbstractParseNode jumpOverInjectionsModuloEagerness(
      AbstractParseNode t) {

    if (Tools.debugging) {
      Tools.debug("jumpOverInjectionsModuloEagerness()");
    }

    final int prodType = getProductionType(t);

    if (t.isParseNode() && prodType != ProductionType.PREFER
        && prodType != ProductionType.AVOID) {

      Label prod = getLabel(t);

      while (prod.isInjection()) {
        t = ((ParseNode) t).kids[0];

        if (t.isParseNode()) {
          final int prodTypeX = getProductionType(t);

          if (prodTypeX != ProductionType.PREFER
              && prodTypeX != ProductionType.AVOID) {
            prod = getLabel(t);
            continue;
          }
        }
        return t;
      }
    }
    return t;
  }

  private Label getLabel(AbstractParseNode t) {
    if (t.isParseNode()) {
      final ParseNode n = (ParseNode) t;
      return parseTable.getLabel(n.getLabel());
    } else if (t instanceof ParseProductionNode) {
      final ParseProductionNode n = (ParseProductionNode) t;
      return parseTable.getLabel(n.prod);
    }
    return null;
  }

  private int getProductionType(AbstractParseNode t) {
    return getLabel(t).getAttributes().getType();
  }

  private boolean isMoreEager(AbstractParseNode left, AbstractParseNode right) {
    final int leftLabel = ((ParseNode) left).getLabel();
    final int rightLabel = ((ParseNode) right).getLabel();

    final Label leftProd = parseTable.getLabel(leftLabel);
    final Label rightProd = parseTable.getLabel(rightLabel);

    if (leftProd.isMoreEager(rightProd)) {
      return true;
    }

    return false;
  }

  private int computeDistinctArguments(AbstractParseNode[] leftArgs,
      AbstractParseNode[] rightArgs) {
    // countDistinctArguments
    int r = 0;
    for (int i = 0; i < leftArgs.length; i++) {
      if (!leftArgs[i].equals(rightArgs[i])) {
        r++;
      }
    }
    return r;
  }

  private boolean isCyclicTerm(AbstractParseNode t) {

    ambiguityManager.dumpIndexTable();

    final List<AbstractParseNode> cycles = computeCyclicTerm(t);

    return cycles != null && cycles.size() > 0;
  }

  private List<AbstractParseNode> computeCyclicTerm(AbstractParseNode t) {
    // FIXME rewrite to use HashMap and object id
    final PositionMap visited = new PositionMap(
        ambiguityManager.getMaxNumberOfAmbiguities());

    ambiguityCount += ambiguityManager.getAmbiguitiesCount();
    ambiguityManager.resetAmbiguityCount();

    return computeCyclicTerm(t, false, visited);
  }

  private List<AbstractParseNode> computeCyclicTerm(AbstractParseNode t,
      boolean inAmbiguityCluster, PositionMap visited) {

    if (Tools.debugging) {
      Tools.debug("computeCyclicTerm() - ", t);
    }

    if (t instanceof ParseProductionNode) {
      if (Tools.debugging) {
        Tools.debug(" bumping");
      }
      return null;
    } else if (t.isParseNode()) {
      // Amb ambiguities = null;
      List<AbstractParseNode> cycle = null;
      // int clusterIndex;
      final ParseNode n = (ParseNode) t;

      if (inAmbiguityCluster) {
        cycle = computeCyclicTerm(n.kids, false, visited);
      } else {
        /*
         * if (ambiguityManager.isInputAmbiguousAt(parseTreePosition)) {
         * ambiguityManager.increaseAmbiguityCount(); clusterIndex =
         * ambiguityManager.getClusterIndex(t, parseTreePosition); if
         * (SGLR.isDebugging()) { Tools.debug(" - clusterIndex : ",
         * clusterIndex); } if (markMap.isMarked(clusterIndex)) { return new
         * ArrayList<IParseNode>(); } ambiguities =
         * ambiguityManager.getClusterOnIndex(clusterIndex); } else {
         * clusterIndex = -1; }
         */

        throw new NotImplementedException();
        /*
         * if (ambiguities == null) { cycle = computeCyclicTerm(((ParseNode)
         * t).getKids(), false, visited); } else { int length =
         * visited.getValue(clusterIndex); int savePos = parseTreePosition;
         * 
         * if (length == -1) { //markMap.mark(clusterIndex); cycle =
         * computeCyclicTermInAmbiguityCluster(ambiguities, visited);
         * visited.put(clusterIndex, parseTreePosition - savePos);
         * //markMap.unmark(clusterIndex); } else { parseTreePosition += length;
         * } }
         */
      }
      return cycle;
    } else {
      throw new FatalException();
    }
  }

  /*
   * private List<IParseNode> computeCyclicTermInAmbiguityCluster(Amb
   * ambiguities, PositionMap visited) {
   * 
   * 
   * List<IParseNode> ambs = ambiguities.getAlternatives(); for (int i = 0, max
   * = ambs.size(); i < max; i++) { IParseNode amb = ambs.get(i);
   * List<IParseNode> cycle = computeCyclicTerm(amb, true, visited); if (cycle
   * != null) return cycle; } return null; }
   */

  private List<AbstractParseNode> computeCyclicTerm(AbstractParseNode[] kids,
      boolean b, PositionMap visited) {

    for (int i = 0, max = kids.length; i < max; i++) {
      final List<AbstractParseNode> cycle = computeCyclicTerm(kids[i], false,
          visited);
      if (cycle != null) {
        return cycle;
      }
    }
    return null;
  }

  private List<int[]> getLongestMatchPositions(AbstractParseNode n) {
    if (n.isAmbNode()) {
      List<int[]> pos1 = getLongestMatchPositions(n.getChildren()[0]);
      List<int[]> pos2 = getLongestMatchPositions(n.getChildren()[1]);
      assert pos1.size() == pos2.size();
      return pos1;
    }

    LinkedList<AbstractParseNode> nodes = new LinkedList<AbstractParseNode>();
    nodes.push(n);
    
    ArrayList<int[]> positions = new ArrayList<int[]>();
    
    while (!nodes.isEmpty()) {
      n = nodes.pop();
      
      if (n.isParseProductionNode())
        continue;
            
      if (!n.isAmbNode() && parseTable.getLabel(n.getLabel()).getAttributes().isLongestMatch())
        positions.add(new int[]{n.getLine(), n.getColumn(), n.getLast().getLine(), n.getLast().getColumn()});
      
      for (int i = n.getChildren().length - 1; i >= 0; i--)
        nodes.push(n.getChildren()[i]);
    }

    return positions;
  }
  
  private int filterLongestMatch(AbstractParseNode left, AbstractParseNode right) {
    List<int[]> leftPositions = getLongestMatchPositions(left);
    List<int[]> rightPositions = getLongestMatchPositions(right);
    
    for (int i = 0; i < Math.min(leftPositions.size(), rightPositions.size()); i++) {
      int[] leftPosition = leftPositions.get(i);
      int[] rightPosition = rightPositions.get(i);

      if (leftPosition[0] == rightPosition[0] && leftPosition[1] == rightPosition[1]) {
        if (leftPosition[2] > rightPosition[2] || leftPosition[2] == rightPosition[2] && leftPosition[3] > rightPosition[3])
          return FILTER_LEFT_WINS;
        else if (leftPosition[2] < rightPosition[2] || leftPosition[2] == rightPosition[2] && leftPosition[3] < rightPosition[3])
          return FILTER_RIGHT_WINS;
      }
      else
        System.out.println("mismatching start");
    }
    
    assert leftPositions.size() == rightPositions.size();
    
    return FILTER_DRAW;
  }
  
}
