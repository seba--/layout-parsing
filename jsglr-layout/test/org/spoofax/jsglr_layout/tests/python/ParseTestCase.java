/*
 * Created on 13.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 *
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr_layout.tests.python;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.FilterException;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.Label;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.client.ParserException;
import org.spoofax.jsglr.client.Priority;
import org.spoofax.jsglr.client.SGLR;
import org.spoofax.jsglr.client.imploder.TermTreeFactory;
import org.spoofax.jsglr.client.imploder.TreeBuilder;
import org.spoofax.jsglr.client.incremental.IncrementalSGLR;
import org.spoofax.jsglr.io.FileTools;
import org.spoofax.jsglr.io.ParseTableManager;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.terms.Term;
import org.spoofax.terms.TermFactory;
import org.spoofax.terms.attachments.ParentTermFactory;
import org.spoofax.terms.io.binary.TermReader;

public abstract class ParseTestCase extends TestCase {

  private static final ParseTableManager parseTables = new ParseTableManager();

  protected SGLR sglr;

  protected String directory;
  protected String suffix;

  protected ParseTable table;

  protected IncrementalSGLR<IStrategoTerm> incrementalSGLR;

  // shared by all tests
  static final TermFactory pf = new TermFactory();
  
//  static final Context normalizeContext = normalize.init();

  // RemoteParseTableServiceAsync parseTableService =
  // GWT.create(RemoteParseTableService.class);

  @Override
  protected final void setUp() throws Exception {
    gwtSetUp();
  }

  protected void gwtSetUp() throws Exception {
    // no default setup
  }

  public void gwtSetUp(String grammar, String directory, String suffix, String... incrementalSorts) throws ParserException,
      InvalidParseTableException {
    this.directory = directory;
    this.suffix = suffix;
    final String fn = "test-offside/grammars/" + grammar + ".tbl";

    try {
      table = parseTables.loadFromFile(fn);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    sglr = new SGLR(new TreeBuilder(new TermTreeFactory(new ParentTermFactory(
        table.getFactory())), true), table);

    BufferedWriter write = null;
    BufferedWriter writePrios = null;
    try {
      write = new BufferedWriter(new FileWriter("test-offside/grammars/"
          + grammar + ".labs"));
      writePrios = new BufferedWriter(new FileWriter("test-offside/grammars/"
          + grammar + ".prios"));

      List<Label> labels = sglr.getParseTable().getLabels();
      for (int i = 0; i < labels.size(); i++)
        if (labels.get(i) != null) {
          Label lab = labels.get(i);
          write.write(i + ":\t" + lab.getProduction() + "\n");
          
          for (Priority p : sglr.getParseTable().getPriorities(lab)) {
            String kind;
            switch (p.type) {
            case Priority.LEFT:
              kind = "left";
              break;
            case Priority.RIGHT:
              kind = "right";
              break;
            case Priority.GTR:
              kind = "gtr";
              break;
            case Priority.NONASSOC:
              kind = "nonassoc";
              break;
            default:
              kind = "unknown";
            }
            
            writePrios.write(p.left + "  " + p.right + "  " + kind + (p.arg == -1 ? "" : "  <" + p.arg + ">") + "\n");
          }
            
        }
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      if (write != null)
        try {
          write.close();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      if (writePrios != null)
        try {
          writePrios.close();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
    }

  }

  @Override
  protected void tearDown() throws Exception {
    // super.gwtTearDown();

    // sglr.clear();
  }

  public IStrategoTerm doParseTest(final String filePattern) {

    System.out.println("Testing " + filePattern + " ****************");
    
    IStrategoTerm parsed = null;

    final Map<String, String> results = loadAsStrings(filePattern);
    assertFalse("Data file is missing: " + filePattern, results.isEmpty());

    AssertionFailedError err = null;
    
    for (Entry<String, String> entry : results.entrySet()) {

      String file = entry.getKey();
      String content = entry.getValue();
      
      parsed = null;

      long parseTime = System.nanoTime();
      try {
        parsed = (IStrategoTerm) sglr.parse(content, null, null);
      } catch (FilterException e) {
        e.printStackTrace();
      } catch (SGLRException e) {
        System.err.println(e.getMessage());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      parseTime = System.nanoTime() - parseTime;

      System.out.println("Parsing " + file + " took " + parseTime / 1000 / 1000
          + " millis.");

      if (sglr.getDisambiguator().getAmbiguityCount() > 0)
        System.err.println("ambiguous parse: " + sglr.getDisambiguator().getAmbiguityCount() + " ambiguities");
      
//      IStrategoTerm normalized = parsed == null ? null : normalize_0_0.instance.invoke(normalizeContext, parsed);
      IStrategoTerm normalized  = parsed;
      
      try {
        doCompare(filePattern, normalized);
      } catch (AssertionFailedError err2) {
        if (err == null)
          err = err2;
      }
    }
    
    if (err != null)
      throw err;

    return parsed;
  }

  protected Map<String, String> loadAsStrings(String filePattern) {
    Map<String, String> map = new TreeMap<String, String>();

    for (File f : new File(directory).listFiles())
      if (f.getName().equals(filePattern + "." + suffix)
          || f.getName().startsWith(filePattern + "_")
          && f.getName().endsWith(suffix)) {
        String content = FileTools.tryLoadFileAsString(f.getPath());
        map.put(f.getName(), content);
      }

    return map;
  }

  private void doCompare(String s, final IStrategoTerm parsed) {
    System.err.flush();
    System.out.flush();

    String extension = ".aterm";
    String file = directory + "/" + s + extension;
    
    if (!new File(file).exists()) {
      System.err.println("Wanted file does not exist: " + file);
      if (parsed == null) {
        System.err.println("parsing failed");
        fail("parsing failed");
      }
      System.out.println(toCompactString(parsed));
      return;
    }
    
    IStrategoTerm wanted;
    try {
      long parseTime = System.nanoTime();
      wanted = new TermReader(pf).parseFromFile(file);
      parseTime = System.nanoTime() - parseTime;
      System.out.println("reading term " + s + extension + " took " + parseTime
          / 1000 / 1000 + " millis");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }


    assertNotNull(wanted);

    boolean expectFail = Term.isTermAppl(wanted) && Term.hasConstructor((IStrategoAppl) wanted, "Fail") && wanted.getSubtermCount() == 0;
    
    System.out.println(parsed == null ? "parsing failed" : toCompactString(parsed));
    System.out.println(expectFail ? "expecting fail" : toCompactString(wanted));
    
    boolean match = parsed == null && expectFail || parsed != null && parsed.match(wanted);
    
    System.out.println(match ? "ok" : "fail");
    
    if (!match)
      fail();
  }

  private static String toCompactString(IStrategoTerm term) {
    return term.toString();
  }

  public String getModuleName() {
    return "org.spoofax.JsglrGWT";
  }
}
