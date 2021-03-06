package org.spoofax.jsglr_layout.tests.disamb;

import junit.framework.TestCase;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr_layout.client.AbstractParseNode;
import org.spoofax.jsglr_layout.client.Disambiguator;
import org.spoofax.jsglr_layout.client.FilterException;
import org.spoofax.jsglr_layout.client.InvalidParseTableException;
import org.spoofax.jsglr_layout.client.ParseNode;
import org.spoofax.jsglr_layout.client.ParseProductionNode;
import org.spoofax.jsglr_layout.client.ParseTable;
import org.spoofax.jsglr_layout.client.SGLR;
import org.spoofax.jsglr_layout.client.imploder.TermTreeFactory;
import org.spoofax.jsglr_layout.client.imploder.TreeBuilder;
import org.spoofax.jsglr_layout.shared.SGLRException;
import org.spoofax.terms.TermFactory;
import org.spoofax.terms.attachments.ParentTermFactory;
import org.spoofax.terms.io.TAFTermReader;


/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 *
 */
public class TestDisambiguation extends TestCase {
  private AbstractParseNode tree1;
  private SGLR parser;
  
  @Override
  public void setUp() {
    TermFactory factory = new TermFactory();
    IStrategoTerm term = new TAFTermReader(factory).parseFromString("parse-table(6,0,[label(prod([sort(\"<START>\"),char-class([256])],sort(\"<Start>\"),no-attrs),258),label(prod([cf(layout),cf(layout)],cf(layout),attrs([assoc(left)])),257)],states([state-rec(0,[goto([258],1)],[]),state-rec(1,[],[])]),priorities([arg-gtr-prio(257,1,257)]))");
    ParseTable table;
    try {
      table = new ParseTable(term, factory);
    } catch (InvalidParseTableException e) {
      throw new RuntimeException(e);
    }
    parser = new SGLR(new TreeBuilder(new TermTreeFactory(new ParentTermFactory(table.getFactory())), true), table);
    
    AbstractParseNode c =
      new ParseNode(3, 
                    new AbstractParseNode[] {new ParseProductionNode(3, 0, 0)}, 
                    AbstractParseNode.PARSENODE, 
                    0, 0, false, false);
    AbstractParseNode d =
      new ParseNode(4, 
          new AbstractParseNode[] {new ParseProductionNode(4, 0, 0)}, 
          AbstractParseNode.PARSENODE, 
          0, 1, false, false);
    AbstractParseNode b =
      new ParseNode(2, 
          new AbstractParseNode[] {c, d}, 
          AbstractParseNode.PARSENODE, 
          0, 2, false, false);
    AbstractParseNode e =
      new ParseNode(5, 
          new AbstractParseNode[] {new ParseProductionNode(5, 0, 0)}, 
          AbstractParseNode.PARSENODE, 
          0, 3, false, false);
    AbstractParseNode a =
      new ParseNode(1, 
          new AbstractParseNode[] {b, e}, 
          AbstractParseNode.PARSENODE, 
          0, 4, false, false);

    tree1 = a;
    
  }
  
  public void testFilterTree() throws FilterException, SGLRException, InterruptedException {
    Disambiguator disamb = new Disambiguator();
    disamb.setFilterAssociativity(false);
    disamb.initializeFromParser(parser);
    AbstractParseNode t = disamb.filterTree(tree1);
    assertEquals(tree1, t);
  }
}
