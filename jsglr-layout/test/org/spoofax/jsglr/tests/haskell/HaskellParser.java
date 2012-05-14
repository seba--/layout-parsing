package org.spoofax.jsglr.tests.haskell;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
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

/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 *
 */
public class HaskellParser {

  /**
   * Time out for parser (in seconds).
   */
  private static final int TIMEOUT = 10;
  
  private static final String tableLocation = HaskellParser.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "/org/spoofax/jsglr/tests/haskell/Haskell.tbl";
  
  private static final ParseTable table;
  static {
    try {
      table = new ParseTableManager().loadFromFile(tableLocation);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  long startParse = -1;
  long endParse = -1;
  public long timeParse;
  public long memoryBefore;
  public long memoryAfter;
  
  SGLR parser;
  public AbstractParseNode parseTree;
  
  public HaskellParser() {
  }
  
  private void reset() {
    timeParse = -1;
    parser = null;
    parseTree = null;
    memoryBefore = -1;
    memoryAfter = -1;
  }
  
  public Object parse(String input, String filename) throws InterruptedException, ExecutionException {
    return parse(input, filename, "Module");
  }
  
  @SuppressWarnings("deprecation")
  public Object parse(final String input, final String filename, final String startSymbol) throws ExecutionException {
    reset();
    
    parser = new SGLR(new TreeBuilder(new TermTreeFactory(new ParentTermFactory(table.getFactory())), true), table);

    FutureTask<Object> parseTask = new FutureTask<Object>(new Callable<Object>() {
      public Object call() throws BadTokenException, TokenExpectedException, ParseException, SGLRException, InterruptedException {
        memoryBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        startParse = System.nanoTime();
        try {
          return parser.parse(input, filename, startSymbol);
        } finally {
          endParse = System.nanoTime();
          memoryAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        }
      }
    });
    Thread thread = new Thread(parseTask);
    
    startParse = -1;
    endParse = -1;
    
    Object o = null;
    try {
      thread.start();
      o = parseTask.get(TIMEOUT, TimeUnit.SECONDS);
    } catch (TimeoutException e) {
      thread.interrupt();
      try {
        parseTask.get(TIMEOUT, TimeUnit.SECONDS);
      } catch (InterruptedException e1) {
        // do nothing
      } catch (ExecutionException e1) {
        // do nothing
      } catch (TimeoutException e1) {
        System.err.println("does not interrupt: " + filename);
        thread.stop();
      }
      endParse = startParse - 1;
    } catch (InterruptedException e) {
      endParse = startParse - 1;
    } finally {
      
      if (endParse == -1)
        endParse = System.nanoTime();
      
      parseTree = parser.getParseTree();
      
      timeParse = endParse - startParse;
      if (timeParse < 0)
        timeParse = -1;
    }
    
    return o;
  }
  
  public int getAmbiguities() {
    return parser == null || parser.getDisambiguator() == null ? 0 : parser.getDisambiguator().getAmbiguityCount();
  }
  
  public int getLayoutFilterCountParseTime() {
    return parser.getLayoutFilterCallCount();
  }
  
  public int getLayoutFilteringCountParseTime() {
    return parser.getLayoutFilteringCount();
  }

  public int getLayoutFilterCountDisambiguationTime() {
    return parser.getDisambiguator().getLayoutFilterCallCount();
  }

  public int getLayoutFilteringCountDisambiguationTime() {
    return parser.getDisambiguator().getLayoutFilteringCount();
  }

  public int getEnforcedNewlineSkips() {
    return parser.getEnforcedNewlineSkips();
  }
}
