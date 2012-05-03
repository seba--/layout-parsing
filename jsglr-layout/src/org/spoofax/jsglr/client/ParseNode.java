/*

 * Created on 30.mar.2006
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 *
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.spoofax.NotImplementedException;
import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.terms.TermFactory;

public class ParseNode extends AbstractParseNode {

  private static final int AMB_LABEL = -1;

  private int label;

  AbstractParseNode[] kids;

  private boolean isParseProductionChain; // should be set only after parsing

  private boolean isSetPPC;

  private int nodeType;

  private int cachedHashCode; // should be set only after parsing
  
  private AbstractParseNode left;
  
  private boolean isLayout;

  @Override
  public int getLabel() {
    if (isAmbNode() || label == AMB_LABEL)
      throw new UnsupportedOperationException();
    return label;
  }

  public ParseNode(int label, AbstractParseNode[] kids, int type, int line, int column, boolean isLayout) {
    super(line, column);
    this.isLayout = isLayout;
    setFields(label, kids, type);
    if (type == AbstractParseNode.AMBIGUITY) {
      this.isParseProductionChain = false;
      this.isSetPPC = true;
      assert this.label == AMB_LABEL;
    }
  }

  public static ParseNode createAmbNode(AbstractParseNode... kids) {
    assert kids.length > 0;

    int line = kids[0].getLine();
    int column = kids[0].getColumn();

    for (int i = 1; i < kids.length; i++) {
      assert kids[i].getLine() == line;
      assert kids[i].getColumn() == column;
    }

    ParseNode amb = new ParseNode(AMB_LABEL, kids, AbstractParseNode.AMBIGUITY, line, column, kids[0].isLayout());
    return amb;
  }

  private void setFields(int label, AbstractParseNode[] kids, int type) {
    assert (type != AbstractParseNode.AMBIGUITY || (label == AMB_LABEL));
    this.nodeType = type;
    this.label = label;
    this.kids = kids;
    this.isParseProductionChain = false;
    this.isSetPPC = false;
  }

  @Override
  public void reject() {
    // FIXME: this might not work if the current node is an AMB
    nodeType = REJECT;
  }

  public void makeAmbiguity(AbstractParseNode pn) {
    // if (isAmbNode()) {
    // if (isInAmbiguityCluster(pn)) return;
    // AbstractParseNode[] newKids = new AbstractParseNode[kids.length + 1];
    // System.arraycopy(kids, 0, newKids, 0, kids.length);
    // newKids[newKids.length - 1] = pn;
    // kids = newKids;
    //
    // if(pn instanceof ParseNode)
    // ((ParseNode) pn).replaceCycle(this, null);
    // } else {
    if (this == pn || (isAmbNode() && isInAmbiguityCluster(pn)))
      return;

    assert getLine() == pn.getLine();
    assert getColumn() == pn.getColumn();

    ParseNode left = new ParseNode(this.label, this.kids, this.nodeType, getLine(), getColumn(), isLayout);
    setFields(AMB_LABEL, new AbstractParseNode[] { left, pn }, AbstractParseNode.AMBIGUITY);

    if (pn instanceof ParseNode)
      ((ParseNode) pn).replaceCycle(this, left);
    // }

    assert (this.cachedHashCode == NO_HASH_CODE) : "Hashcode should not be cached during parsing because descendant nodes may change";
    assert (!this.isParseProductionChain) : "PPC is not set to true during parsing because descendents may change";
  }

  /**
   * Transforms this ambuguity node into a non-ambiguous node by selecting either the left or right child.
   * 
   * @param n the left or right child.
   */
  public void disambiguate(AbstractParseNode n) {
    assert isAmbNode();
    assert n.isParseNode();
    assert getChildren().length == 2 && (n == getChildren()[0] || n == getChildren()[1]);
    
    setFields(n.isAmbNode() ? AMB_LABEL : n.getLabel(), n.getChildren(), n.getNodeType()) ;
    cachedHashCode = NO_HASH_CODE;
  }
  
  private boolean isInAmbiguityCluster(AbstractParseNode pn) {
    for (AbstractParseNode existing : kids) {
      if (pn == existing) {
        return true;
      } else if (existing.isAmbNode()) {
        if (((ParseNode) existing).isInAmbiguityCluster(pn))
          return true;
      }
    }
    return false;
  }

  private void replaceCycle(ParseNode before, ParseNode after) {
    // only reductions for current char (right chain) are inspected
    // XXX: is that assumption correct? what about epsylon productions that
    // consume no chars?
    if (isAmbNode()) { // all kids relate to current char
      for (int i = 0; i < kids.length; i++)
        replaceDescendantAt(before, after, i);
    } else if (kids.length > 0) {
      replaceDescendantAt(before, after, kids.length - 1);
    }
  }

  private void replaceDescendantAt(ParseNode before, ParseNode after, int index) {
    AbstractParseNode kid = kids[index];
    if (kid == before) {
      kids[index] = after == null ? new CycleParseNode(before) : after;
      return; // no further inspection needed since cycles should not occur
    } else if (kid instanceof ParseNode) {
      ((ParseNode) kid).replaceCycle(before, after);
    }
  }

  @Override
  public boolean isParseProductionChain() {
    // REMARK: works because PPC property is not set during parsing, so
    // descendants will not change
    // assert isParseProductionChain == calculateIsParseProdChain(kids);
    if (!isSetPPC)
      initParseProductionChain();
    return isParseProductionChain;
  }

  /**
   * Initialize the {@link #isParseProductionChain} method for this node and any
   * candidate chain nodes below it, without using recursion (which would
   * potentially lead to a stack overflow).
   */
  private void initParseProductionChain() {
    AbstractParseNode deepest = getDeepestNonChainNode();
    if (deepest == this) { // fast path
      isSetPPC = true;
      isParseProductionChain = false;
    } else {
      setParseProductionChainUpTo(deepest == null, deepest);
    }
  }

  /**
   * Find the deepest parse node that is clearly not a parse production chain
   * node.
   * 
   * @see #isParseProductionChain
   */
  private AbstractParseNode getDeepestNonChainNode() {
    AbstractParseNode current = this;
    for (;;) {
      AbstractParseNode[] kids = current.getChildren();
      switch (kids.length) {
      case 2:
        if (current instanceof ParseNode) {
          if (((ParseNode) current).isSetPPC)
            return current.isParseProductionChain() ? null : current;
        }
        if (!kids[0].isParseProductionNode())
          return kids[0];
        current = kids[1];
        break;
      case 1:
        current = kids[0];
        break;
      case 0:
        return current.isParseProductionNode() ? null : current;
      default:
        return current;
      }
    }
  }

  private void setParseProductionChainUpTo(boolean value, AbstractParseNode end) {
    AbstractParseNode current = this;
    AbstractParseNode next = this;
    do {
      current = next;
      ParseNode parseCurrent = null;
      if (current instanceof ParseNode)
        parseCurrent = (ParseNode) current;
      if (parseCurrent == null) {
        assert current.isCycle() || current.isParseProductionNode();
        return;
      }
      parseCurrent.isParseProductionChain = value;
      parseCurrent.isSetPPC = true;
      AbstractParseNode[] kids = parseCurrent.kids;
      switch (kids.length) {
      case 2:
        next = kids[1];
        break;
      case 1:
        next = kids[0];
        break;
      default:
        return;
      }
    } while (current != end);
  }

  // TODO: refactor
  @Override
  public Object toTreeBottomup(BottomupTreeBuilder builder) {
    if (isAmbNode()) {
      return toTreeBottomupAmb(builder);
    }
    builder.visitLabel(label);
    ArrayList<Object> subtrees = new ArrayList<Object>(kids.length);
    for (int i = 0; i < kids.length; i++) {
      subtrees.add(kids[i].toTreeBottomup(builder));
    }

    Object result = builder.buildNode(label, subtrees);
    builder.endVisitLabel(label);
    return result;
  }

  public Object toTreeBottomupAmb(BottomupTreeBuilder builder) {
    ArrayList<Object> collect = new ArrayList<Object>();
    addToTreeAmb(builder, collect);
    return builder.buildAmb(collect);
  }

  private void addToTreeAmb(BottomupTreeBuilder builder, List<Object> collect) {
    for (int i = kids.length - 1; i >= 0; i--) {
      AbstractParseNode alt = kids[i];
      if (alt.isAmbNode()) {
        ((ParseNode) alt).addToTreeAmb(builder, collect);
      } else {
        collect.add(alt.toTreeBottomup(builder));
      }
    }
  }

  /**
   * todo: stolen from TAFReader; move elsewhere
   */
  public static IStrategoList makeList(TermFactory factory,
      List<IStrategoTerm> terms) {
    IStrategoList result = factory.makeList();
    for (int i = terms.size() - 1; i >= 0; i--) {
      result = factory.makeListCons(terms.get(i), result);
    }
    return result;
  }

  @Override
  public String toString() {
    switch (nodeType) {
    case AbstractParseNode.AMBIGUITY:
      return "amb(" + Arrays.toString(kids) + ")";
    case AbstractParseNode.PARSENODE:
      return "regular(aprod(" + label + ")," + Arrays.toString(kids) + ")";
    case AbstractParseNode.AVOID:
      return "avoid(" + getLabel() + "," + Arrays.toString(kids) + ")";
    case AbstractParseNode.PREFER:
      return "prefer(" + getLabel() + "," + Arrays.toString(kids) + ")";
    case AbstractParseNode.REJECT:
      return "reject(" + getLabel() + "," + Arrays.toString(kids) + ")";
    default:
      throw new NotImplementedException();
    }
  }

  @Override
  public int getNodeType() {
    return nodeType;
  }

  @Override
  public AbstractParseNode[] getChildren() {
    return kids;
  }

  public void setChildren(AbstractParseNode[] kids) {
    this.kids = kids;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof ParseNode) || !super.equals(obj))
      return false;
    if (obj == this)
      return true;
    final ParseNode o = (ParseNode) obj;
    if (getNodeType() != o.getNodeType() || label != o.label
        || kids.length != o.kids.length || hashCode() != o.hashCode())
      return false;
    for (int i = 0; i < kids.length; i++) {
      if (!kids[i].equals(o.kids[i]))
        return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    if (cachedHashCode != NO_HASH_CODE)
      return cachedHashCode;
    final int prime = 31;
    int result = prime * label;
    for (AbstractParseNode n : kids)
      result += (prime * n.hashCode());
    cachedHashCode = result; // Assumption is that hashcode is not set during
                             // parsing
    return result;
  }

  @Override
  public String toStringShallow() {
    if (isAmbNode())
      return "Amb";
    return "regular*(" + label + ", {" + kids.length + "})";
  }

  private Boolean isEmpty;

  @Override
  public boolean isEmpty() {
    if (isEmpty != null)
      return isEmpty;

    isEmpty = true;

    for (AbstractParseNode kid : kids)
      if (!kid.isEmpty()) {
        isEmpty = false;
        break;
      }

    return isEmpty;
  }

  private void computeLeft(ParseTable parseTable) {
    for (AbstractParseNode kid : kids)
      if (!kid.isLayout() && !kid.isEmpty() && !kid.ignoreIndent(parseTable)) {
        if (kid.getLine() > getLine() && (left == null || kid.getColumn() < left.getColumn()))
          left = kid;
        AbstractParseNode kidLeft = kid.getLeft(parseTable);
        if (kidLeft != null && 
            kidLeft.getLine() > getLine() &&
            (left == null || kidLeft.getColumn() < left.getColumn()))
          left = kidLeft;
      }
  }

  @Override
  public AbstractParseNode getLeft(ParseTable parseTable) {
    if (left == null)
      computeLeft(parseTable);
    
    return left;
  }
  
  @Override
  public boolean isLayout() {
    return isLayout;
  }
  
  /*
   * private void log(){ System.out.println(this.toStringShallow()); for (int i
   * = 0; i < kids.length; i++) { if(kids[i].isParseNode() ||
   * kids[i].isAmbNode()) ((ParseNode)kids[i]).log(); } }
   */
}
