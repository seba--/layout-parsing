package org.spoofax.jsglr.tests.haskell;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
  private static final int TIMEOUT = 30;
  
  private static final boolean DEBUG = false;

  private static final String tableLocation;
  
  private static final ParseTable table;
  static {
    try {
      tableLocation  = new File(HaskellParser.class.getResource("/org/spoofax/jsglr/tests/haskell/Haskell.tbl").toURI()).getPath();
      table = new ParseTableManager().loadFromFile(tableLocation);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public int ambiguities;
  
//  public AbstractParseNode parseTree;
  
  long startParse = -1;
  long endParse = -1;
  public long timeParse;
  public long memoryBefore;
  public long memoryAfter;
  
  public HaskellParser() {
  }

  private void reset() {
    timeParse = -1;
    ambiguities = 0;
//    parseTree = null;
    memoryBefore = -1;
    memoryAfter = -1;
  }
  
  public Object parse(String input, String filename) throws InterruptedException, ExecutionException {
    return parse(input, filename, "Module");
  }
  
  @SuppressWarnings("deprecation")
  public Object parse(final String input, final String filename, final String startSymbol) throws ExecutionException {
    reset();
    final SGLR parser = new SGLR(new TreeBuilder(new TermTreeFactory(new ParentTermFactory(table.getFactory())), true), table);
    
    FutureTask<Object> parseTask = new FutureTask<Object>(new Callable<Object>() {
      public Object call() throws BadTokenException, TokenExpectedException, ParseException, SGLRException, InterruptedException {
        memoryBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        startParse = System.nanoTime();
        try {
          return parser.parse(input, filename, startSymbol);
        } catch (RuntimeException e) {
          e.printStackTrace();
          throw e;
        } finally {
          endParse = System.nanoTime();
          memoryAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        }
      }
    });
    Thread thread = new Thread(parseTask);

    Object o = null;
    try {
      thread.start();
      if (DEBUG)
        o = parseTask.get();
      else
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
      } catch (OutOfMemoryError e1) {
        thread.stop();
        System.err.println("heap overflow: " + filename);
        throw e1;
      }
      endParse = startParse - 1;
    } catch (InterruptedException e) {
      endParse = startParse - 1;
    } catch (OutOfMemoryError e) {
      thread.stop();
      System.err.println("heap overflow: " + filename);
      throw e;
    } finally {
      if (endParse == -1)
        endParse = System.nanoTime();
      
      ambiguities = parser.getDisambiguator().getAmbiguityCount();
//      parseTree = parser.getParseTree();
      
      timeParse = endParse - startParse;
      if (timeParse < 0)
        timeParse = -1;
      
      try {
        thread.join();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
    
    return o;
  }
}
