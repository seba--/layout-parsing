package org.spoofax.jsglr.client.indentation;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoConstructor;
import org.spoofax.interpreter.terms.IStrategoString;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.AbstractParseNode;
import org.spoofax.jsglr.client.ParseNode;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.client.imploder.ProductionAttributeReader;
import org.spoofax.terms.Term;

/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 *
 */
public class LayoutFilter {

  private final Object NO_VALUE = null;
  

  private final boolean atParseTime;
  private ParseTable parseTable;
  private ProductionAttributeReader attrReader;
  private int disambiguationCount;
  private int filterCallCount = 0;
  
  /**
   * 
   * @param parseTable
   * @param atParseTime if true, the filter is conservative: it only discards
   *   trees whose parser state can be discarded in general. This is needed
   *   when discarding trees at parse time, when a later join may represent
   *   alternative trees by the same parser state.
   */
  public LayoutFilter(ParseTable parseTable, boolean atParseTime) {
    this.parseTable = parseTable;
    this.attrReader = new ProductionAttributeReader(parseTable.getFactory());
    this.atParseTime = atParseTime;
  }
  
  int error = 0;
  
  public int getFilterCallCount() {
    return filterCallCount;
  }
  
  public int getDisambiguationCount() {
    return disambiguationCount;
  }
  
  public boolean hasValidLayout(ParseNode t) {
    return hasValidLayout(t.getLabel(), t.getChildren());
  }
  
  public boolean hasValidLayout(int label, AbstractParseNode[] kids) {
    IStrategoTerm layoutConstraint = parseTable.getLabel(label).getAttributes().getLayoutConstraint();
    
    if (layoutConstraint == null)
      return true;
    
    disambiguationCount = 0;
    filterCallCount++;
    Boolean b = evalConstraint(layoutConstraint, kids, new HashMap<String, Object>(), Boolean.class);
    if (b == NO_VALUE)
      return true;
    return b;
  }
  
  @SuppressWarnings("unchecked")
  private <T> T evalConstraint(IStrategoTerm constraint, AbstractParseNode[] kids, Map<String, Object> env, Class<T> cl) {
    Object o = evalConstraint(constraint, kids, env);
    ensureType(o, cl, constraint);
    return (T) o;
  }
  
  private Object evalConstraint(IStrategoTerm constraint, AbstractParseNode[] kids, Map<String, Object> env) {
    switch (constraint.getTermType()) {
    case IStrategoTerm.INT: {
      int i = Term.asJavaInt(constraint);
      return getSubtree(i, kids);
    }
      
    case IStrategoTerm.STRING:
      String v = Term.asJavaString(constraint);
      Object o = env.get(v);
      if (o == null)
        throw new IllegalStateException("undefined variable " + v);
      return o;
      
    case IStrategoTerm.APPL:
      IStrategoConstructor cons = Term.tryGetConstructor(constraint);
      String consName = cons.getName();
      
      if (consName.equals("num")) {
        String num = Term.asJavaString(constraint.getSubterm(0));
        int i = Integer.parseInt(num);
        return i;
      }
      if (consName.equals("tree")) {
        String num = Term.asJavaString(constraint.getSubterm(0));
        int i = Integer.parseInt(num);
        return getSubtree(i, kids);
      }
      if (consName.equals("eq") ||
          consName.equals("gt") ||
          consName.equals("ge") ||
          consName.equals("lt") ||
          consName.equals("le")) {
        ensureChildCount(constraint, 2, consName);
        Integer i1 = evalConstraint(constraint.getSubterm(0), kids, env, Integer.class);
        Integer i2 = evalConstraint(constraint.getSubterm(1), kids, env, Integer.class);
        return binArithComp(consName, i1, i2);
      }
      if (consName.equals("add") ||
          consName.equals("sub") ||
          consName.equals("mul") ||
          consName.equals("div")) {
        ensureChildCount(constraint, 2, consName);
        Integer i1 = evalConstraint(constraint.getSubterm(0), kids, env, Integer.class);
        Integer i2 = evalConstraint(constraint.getSubterm(1), kids, env, Integer.class);
        return binArithOp(consName, i1, i2);
      }
      if (consName.equals("first") ||
          consName.equals("left") ||
          consName.equals("right") ||
          consName.equals("last")) {
        ensureChildCount(constraint, 1, consName);
        AbstractParseNode n = evalConstraint(constraint.getSubterm(0), kids, env, AbstractParseNode.class);
        return nodeSelector(consName, n);
      }
      if (consName.equals("or")) {
        ensureChildCount(constraint, 2, consName);
        Boolean b1 = evalConstraint(constraint.getSubterm(0), kids, env, Boolean.class);
        if (b1 != NO_VALUE && b1)
          return true;
        Boolean b2 = evalConstraint(constraint.getSubterm(1), kids, env, Boolean.class);
        return b2;
      }
      if (consName.equals("and")) {
        ensureChildCount(constraint, 2, consName);
        Boolean b1 = evalConstraint(constraint.getSubterm(0), kids, env, Boolean.class);
        if (b1 != NO_VALUE && !b1)
          return false;
        Boolean b2 = evalConstraint(constraint.getSubterm(1), kids, env, Boolean.class);
        return b2;
      }
      if (consName.equals("not")) {
        ensureChildCount(constraint, 1, consName);
        Boolean b1 = evalConstraint(constraint.getSubterm(0), kids, env, Boolean.class);
        if (b1 == NO_VALUE)
          return NO_VALUE;
        return !b1;
      }
      if (consName.equals("all")) {
        ensureChildCount(constraint, 3, consName);
        
        ensureType(constraint.getSubterm(0), IStrategoString.class, constraint.getSubterm(0));
        v = Term.asJavaString(constraint.getSubterm(0));
        
        AbstractParseNode n = evalConstraint(constraint.getSubterm(1), kids, env, AbstractParseNode.class);
        
        return checkAll(n, v, constraint, kids, env);
      }
      if (consName.equals("col")) {
        ensureChildCount(constraint, 1, consName);
        AbstractParseNode n = evalConstraint(constraint.getSubterm(0), kids, env, AbstractParseNode.class);
        if (n == NO_VALUE)
          return NO_VALUE;
        return n.getColumn();
      }
      if (consName.equals("line")) {
        ensureChildCount(constraint, 1, consName);
        AbstractParseNode n = evalConstraint(constraint.getSubterm(0), kids, env, AbstractParseNode.class);
        if (n == NO_VALUE)
          return NO_VALUE;
        return n.getLine();
      }
      
      throw new IllegalStateException("unhandeled constructor " + consName);
      
    default:
      throw new IllegalStateException("unhandeled constraint " + constraint);
    }
  }
  
  private Boolean checkAll(AbstractParseNode n, String v, IStrategoTerm constraint, AbstractParseNode[] kids, Map<String, Object> env) {
    if (atParseTime)
      return noValue();
    
    Stack<AbstractParseNode> all = new Stack<AbstractParseNode>();
    all.push(n);
    
    String sort = null;
    
    while (!all.isEmpty()) {
      AbstractParseNode next = all.pop();
    
      if (sort == null && !next.isAmbNode() && !next.isParseProductionNode())
        sort = sortOfNode(next);
      
      if (next.isAmbNode()) {
        boolean left = checkAll(next.getChildren()[0], v, constraint, kids, env);
        boolean right = checkAll(next.getChildren()[1], v, constraint, kids, env);
        
        if (!left && !right)
          return false;
        if (left && !right) {
          ((ParseNode) next).disambiguate(next.getChildren()[0]);
          disambiguationCount++;
        }
        if (!left && right) {
          ((ParseNode) next).disambiguate(next.getChildren()[1]);
          disambiguationCount++;
        }
      }
      else if (next.isParseProductionNode() || sort != null && !sort.equals(sortOfNode(next)) || !isListNode(next)){
        Object old = env.get(v);
        env.put(v, next);
        try {
          Boolean b = evalConstraint(constraint.getSubterm(2), kids, env, Boolean.class);
          if (b != NO_VALUE && !b)
            return false;
        } finally {
          if (old == null)
            env.remove(v);
          else
            env.put(v, old);
        }
      }
      else
        for (int j = next.getChildren().length - 1; j >= 0; j--) {
          AbstractParseNode kid = next.getChildren()[j];
          if (kid.isAmbNode() || sort.equals(sortOfNode(kid)))
            all.push(kid);
        }


    }
    
    return true;
  }
  
  private String sortOfNode(AbstractParseNode node) {
    
    return attrReader.getSort((IStrategoAppl) parseTable.getLabel(node.getLabel()).getProduction().getSubterm(1));
  }
  
  private boolean isListNode(AbstractParseNode node) {
    IStrategoTerm prod = parseTable.getLabel(node.getLabel()).getProduction();
    return attrReader.isList((IStrategoAppl) prod.getSubterm(1), (IStrategoAppl) prod.getSubterm(2)) &&
           !attrReader.isFlatten((IStrategoAppl) prod.getSubterm(1), (IStrategoAppl) prod.getSubterm(2));
  }
  
  private AbstractParseNode getSubtree(int i, AbstractParseNode[] kids) {
    i = i - 1;
    int elems = (kids.length + 1) / 2;
    if (i < 0 || i >= elems)
      throw new IllegalStateException("index out of bounds: " + "index is " + i + " but only " + elems + " children available");
    return kids[2 * i];
  }
  
  private Integer binArithOp(String op, Integer i1, Integer i2) {
    if (i1 == NO_VALUE || i2 == NO_VALUE)
      return noValue();
    
    if (op.equals("add"))
      return i1 + i2;
    if (op.equals("sub"))
      return i1 - i2;
    if (op.equals("mult"))
      return i1 * i2;
    if (op.equals("div"))
      return i1 / i2;
    
    throw new IllegalStateException("unknown operator " + op);
  }
  
  private Boolean binArithComp(String comp, Integer i1, Integer i2) {
    if (i1 == NO_VALUE || i2 == NO_VALUE)
      return noValue();
    
    if (comp.equals("eq"))
      return i1.equals(i2);
    if (comp.equals("gt"))
      return i1 > i2;
    if (comp.equals("ge"))
      return i1 >= i2;
    if (comp.equals("lt"))
      return i1 < i2;
    if (comp.equals("le"))
      return i1 <= i2;
      
    throw new IllegalStateException("unknown comparator " + comp);
  }

  private AbstractParseNode nodeSelector(String sel, AbstractParseNode t) {
    if (isNothing(t))
      return noValue();
    
    if (sel.equals("first"))
      return t;
    if (sel.equals("last"))
      return t.getLast();
    
    if (atParseTime)
      return noValue();
    else
      if (sel.equals("left"))
        return t.getLeft();
      else if (sel.equals("right"))
        return t.getRight();
    
    
    throw new IllegalStateException("unknown selector " + sel);
  }
  
  private void ensureType(Object o, Class<?> cl, IStrategoTerm term) {
    if (o != null && !cl.isInstance(o))
      throw new IllegalStateException("ill-typed term " + term + ". Expected type " + cl.getName() + ", was " + o.getClass().getName());
  }
  
  private void ensureChildCount(IStrategoTerm t, int count, String what) {
    if (t.getAllSubterms().length != count)
      throw new IllegalStateException("not enough arguments to " + what);
  }
  
  @SuppressWarnings("unchecked")
  private <T> T noValue() {
    return (T) NO_VALUE;
  }
  
  private boolean isNothing(AbstractParseNode n) {
    if (n.isAmbNode())
      return isNothing(n.getChildren()[0]) && isNothing(n.getChildren()[1]);
    
    return n.isLayout() || n.isEmpty() || n.isIgnoreLayout();
  }
}
