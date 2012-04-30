package org.spoofax.jsglr.tests.haskell;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
import org.strategoxt.stratego_sglr.parsetree_2_0;

import com.sun.corba.se.impl.orb.ParserTable;

/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 *
 */
public class HaskellParser {

  /**
   * Time out for parser (in seconds).
   */
  private static final int TIMEOUT = 30;
  
  private static String tableLocation = HaskellParser.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "/org/spoofax/jsglr/tests/haskell/Haskell.tbl";
  
  private ParseTable table;
  
  public long timeAll;
  public long timeParse;
  
  public int ambiguities = 0;
  
  public AbstractParseNode parseTree;
  
  private ExecutorService executor = Executors.newSingleThreadExecutor();
  
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
      TokenExpectedException, ParseException, SGLRException, InterruptedException, ExecutionException {
    return parse(input, filename, "Module");
  }
  
  public Object parse(final String input, final String filename, final String startSymbol) throws BadTokenException, TokenExpectedException, ParseException, SGLRException, InterruptedException, ExecutionException {
    reset();
    
    long startAll = System.nanoTime();
    final SGLR parser = new SGLR(new TreeBuilder(new TermTreeFactory(new ParentTermFactory(table.getFactory())), true), table);
    
    long startParse = -1;
    long endParse = -1;

    FutureTask<Object> parseTask = new FutureTask<Object>(new Callable<Object>() {
      public Object call() throws BadTokenException, TokenExpectedException, ParseException, SGLRException {
        return parser.parse(input, filename, startSymbol);
      }
    });
    Thread thread = new Thread(parseTask);

    Object o = null;
    try {
      startParse = System.nanoTime();
      thread.start();
      o = parseTask.get(TIMEOUT, TimeUnit.SECONDS);
      endParse = System.nanoTime();
    } catch (TimeoutException e) {
      endParse = startParse - 1;
      thread.stop();
    } finally {
      
      if (endParse == -1)
        endParse = System.nanoTime();
      
      ambiguities = parser.getDisambiguator().getAmbiguityCount();
      parseTree = parser.getParseTree();
      
      long endAll = System.nanoTime();
      
      timeAll = endAll - startAll < 0 ? -1 : (endAll - startAll) / 1000 / 1000;
      timeParse = endParse - startParse < 0 ? -1 : (endParse - startParse) / 1000 / 1000;
    }
    
    return o;
  }
}
