package org.spoofax.jsglr.tests.haskell;

import org.spoofax.jsglr.client.AbstractParseNode;
import org.spoofax.jsglr.client.ParseException;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.client.SGLR;
import org.spoofax.jsglr.client.imploder.TermTreeFactory;
import org.spoofax.jsglr.client.imploder.TreeBuilder;
import org.spoofax.jsglr.io.ParseTableManager;
import org.spoofax.jsglr.shared.BadTokenException;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr.shared.TokenExpectedException;
import org.spoofax.terms.attachments.ParentTermFactory;

/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 *
 */
public class HaskellParser {

  private static String tableLocation = HaskellParser.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "/org/spoofax/jsglr/tests/haskell/Haskell.tbl";
  
  private ParseTable table;
  
  public long timeAll;
  public long timeParse;
  
  public int ambiguities = 0;
  
  public AbstractParseNode parseTree;
  
  public HaskellParser() {
    try {
      table = new ParseTableManager().loadFromFile(tableLocation);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  private void reset() {
    timeAll = -1;
    timeParse = -1;
    ambiguities = 0;
    parseTree = null;
  }
  
  public Object parse(String input, String filename) throws BadTokenException,
      TokenExpectedException, ParseException, SGLRException {
    return parse(input, filename, "Module");
  }
  
  public Object parse(String input, String filename, String startSymbol) throws BadTokenException, TokenExpectedException, ParseException,
  SGLRException {
    reset();
    
    long startAll = System.nanoTime();
    SGLR parser = new SGLR(new TreeBuilder(new TermTreeFactory(new ParentTermFactory(table.getFactory())), true), table);
    
    long startParse = System.nanoTime();
    Object o = null;
    try {
      o = parser.parse(input, filename, startSymbol);
    } finally {
      long endParse = System.nanoTime();
      
      ambiguities = parser.getDisambiguator().getAmbiguityCount();
      parseTree = parser.getParseTree();
      
      long endAll = System.nanoTime();
      
      timeAll = (endAll - startAll) / 1000 / 1000;
      timeParse = (endParse - startParse) / 1000 / 1000;
    }
    
    return o;
  }
}
