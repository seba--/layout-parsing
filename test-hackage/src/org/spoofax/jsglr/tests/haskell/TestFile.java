package org.spoofax.jsglr.tests.haskell;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.tests.haskell.CommandExecution.ExecutionError;
import org.spoofax.jsglr.tests.haskell.compare.CompareAST;
import org.spoofax.jsglr.tests.haskell.compare.compare_0_0;
import org.spoofax.jsglr.tests.haskell_orig.HaskellOrigParser;
import org.spoofax.jsglr_orig.io.FileTools;
import org.spoofax.terms.Term;
import org.spoofax.terms.io.InlinePrinter;
import org.strategoxt.lang.Context;

/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 */
public class TestFile extends ChainedTestCase {
  
  private final static boolean LOGGING = true;
  
//  private Context normalizeContext = NormalizeAST.init();
  private Context compareContext = CompareAST.init();
  private HaskellParser newParser = new HaskellParser();
  private HaskellOrigParser oldParser = new HaskellOrigParser();
  
  private IStrategoTerm newResult;
  private org.spoofax.jsglr.shared.SGLRException newException;
  private IStrategoTerm oldResult;
  private org.spoofax.jsglr_orig.shared.SGLRException oldException;
  private String[][] mkExplicitMessages;
  
  
  public void testFile_main() throws IOException {
    // src/org/spoofax/jsglr/tests/haskell/main.hs
    testFile(new File("/var/folders/aw/aw2pcGAuGEyvWvKgy3h3GU+++TM/-Tmp-/4Blocks6218105210166694846/4Blocks-0.2/Interface/CommandKeys.hs"), "main");
    printShortLog();
    raiseFailures();
  }
  
  public void testFile(File f, String pkg) throws IOException {
    if (f.getPath().indexOf(pkg+"-") > 0 && TestConfiguration.SKIP_FILES.contains(f.getPath().substring(f.getPath().indexOf(pkg+"-")))) {
      if (LOGGING)
        System.out.println("[" + pkg + "] " + "skipping " + f.getAbsolutePath());
      return;
    }
      
    
    if (LOGGING)
      System.out.println("[" + pkg + "] " + "parsing " + f.getAbsolutePath());
    
    File fnorm = new File(f.getAbsolutePath().substring(0, f.getAbsolutePath().length() - 3) + "-norm.hs");
    DeleteUnicode.deleteUnicode(f.getAbsolutePath(), fnorm.getAbsolutePath());
    
    File fpp = preprocess(fnorm, pkg);
    
    if (fpp == null && LOGGING)
      System.out.println("[" + pkg + "] preprocessing failed");
    
    newParse(fpp, pkg);
//    IStrategoTerm normalizedNewResult = normalize(newResult, pkg);
//    assert newResult == null || normalizedNewResult != null;

    File explicitLayoutInput = makeExplicitLayout(fpp, pkg);
    oldParse(explicitLayoutInput, pkg);

    writeToFile(newResult, f.getAbsolutePath() + ".new");
//    writeToFile(normalizedNewResult, f.getAbsolutePath() + ".norm");
    writeToFile(oldResult, f.getAbsolutePath() + ".old");

    boolean ambiguities = false;
    
    if (newParser.parseTree != null && newParser.ambiguities > 0) {
      ambiguities = true;
      //writeToFile(newParser.parseTree.toString(), f.getAbsolutePath() + ".new.pt");
      System.out.println("*error*" + "[" + pkg + ", new] " + "tree contains " + newParser.ambiguities + " ambiguities");
    }

    if (oldParser.parseTree != null && oldParser.ambiguities > 0) {
      ambiguities = true;
      //writeToFile(oldParser.parseTree.toString(), f.getAbsolutePath() + ".old.pt");
      System.out.println("*error*" + "[" + pkg + ", old] " + "tree contains " + oldParser.ambiguities + " ambiguities");
    }
    
    if (mkExplicitMessages[1].length > 0 && LOGGING) {
      System.out.println(mkExplicitMessages[1][0]);
    }
    
    IStrategoList diff = compare(newResult, oldResult);
    
    if (diff == null || !diff.isEmpty()) {
      if (newException != null) {
        System.out.println("[" + pkg + ", new] failed");
        newException.printStackTrace(System.out);
      }
      
      if (oldException != null) {
        System.out.println("[" + pkg + ", old] failed");
        oldException.printStackTrace(System.out);
      }
      
      if (newResult != null && mkExplicitMessages[1].length > 0 && mkExplicitMessages[1][0].endsWith("pp-haskell: Ambiguous infix expression")) {
        logAmbInfix();
        
        if (LOGGING)
          System.out.println("[" + pkg + "] " + "ambInfix");
      }
      else {
        ParseComparisonFailure failure = logComparisonFailure(f.getAbsolutePath(), oldResult, newResult);
      
        if (LOGGING) {
          System.out.println("*error*" + "[" + pkg + "] " + failure.getMessage());
          if (diff != null) {
            String diffString = diff.toString();
            writeToFile(diffString, f.getAbsolutePath() + ".diff");
            System.out.println("*error*" + "[" + pkg + "] " + "diff: " + diffString);
          }
        }
      }
    }
    else {
      // new and old result are equal
      
      if (newResult == null && explicitLayoutInput != null && oldResult == null) {
        // reference parser succeeded, but we failed independent of layout
        logNoParse();
        
        if (newException != null) {
          System.out.println("[" + pkg + ", new] failed");
          newException.printStackTrace(System.out);
        }

        if (oldException != null) {
          System.out.println("[" + pkg + ", old] failed");
          oldException.printStackTrace(System.out);
        }
        
        if (LOGGING)
          System.out.println("[" + pkg + "] " + "no parse");
      }
      else if (newResult == null && explicitLayoutInput == null) {
        // original parser failed as well
        logOkFail();
        if (LOGGING)
          System.out.println("[" + pkg + "] " + "okFail");
      } 
      else if(newResult != null && oldResult != null) {
        // new and old parser succeeded
        logOk();
        if (LOGGING)
          System.out.println("[" + pkg + "] " + "ok");
      }
      else
        assert false;
      
    }
  }
  
  private IStrategoTerm newParse(File f, String pkg) {
    newResult = null;
    newException = null;

    if (f == null)
      return null;
    String input = FileTools.tryLoadFileAsString(f.getAbsolutePath());
    try {
      newResult = (IStrategoTerm) newParser.parse(input, f.getAbsolutePath());
    } catch (org.spoofax.jsglr.shared.SGLRException e) {
      newException = e;
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
    
    if (LOGGING) {
      String time;
      if (newParser.timeParse >= 0)
        time = newParser.timeParse + "ms";
      else
        time = "TIMEOUT";
      System.out.println("[" + pkg + ", new] " + "parsing took " + time + ", " + (newResult != null ? "success" : "failure"));
    }
    
    return newResult;
  }
  
//  private IStrategoTerm normalize(IStrategoTerm term, String pkg) {
//    if (term == null)
//      return null;
//    
//    IStrategoTerm result = normalize_0_0.instance.invoke(normalizeContext, term);
//    return result;
//  }

  private IStrategoList compare(IStrategoTerm term1, IStrategoTerm term2) {
    if (term1 == null && term2 == null)
      return compareContext.getFactory().makeList();
    if (term1 == null || term2 == null)
      return null;
    
    IStrategoTerm result = compare_0_0.instance.invoke(compareContext, compareContext.getFactory().makeTuple(term1, term2));
    if (result != null && Term.isTermList(result))
      return (IStrategoList) result;
    
    return null;
  }

  
  private File makeExplicitLayout(File f, String pkg) throws IOException {
    if (f == null)
      return null;
    
    File res = new File(f.getAbsolutePath() + ".pp.hs");

    String[] cmds = new String[] {
        TestConfiguration.PP_HASKELL_COMMAND, 
        "-i", f.getAbsolutePath(), 
        "-o", res.getAbsolutePath(), 
        "--explicit-layout",
        "--ignore-language-pragmas"};
    
    List<String> cmdList = new ArrayList<String>(Arrays.asList(cmds));
    for (String ext : TestConfiguration.HASKELL_EXTENSIONS)
      cmdList.add("-X" + ext);
    
    cmds = cmdList.toArray(new String[cmdList.size()]);
    
    CommandExecution.SILENT_EXECUTION = true;
    CommandExecution.SUB_SILENT_EXECUTION = true;
    
    mkExplicitMessages = new String[][] {new String[] {}, new String[] {}};
    
    try {
      mkExplicitMessages = CommandExecution.execute(System.out, System.out, "[" + pkg + ", old]", cmds);
    } catch (ExecutionError e) {
      mkExplicitMessages = e.getMessages();
      if (e.getExitValue() == -1)
        throw e;
      
      return null;
    }
    
    return res;
  }
  
  private IStrategoTerm oldParse(File f, String pkg) {
    oldResult = null;
    oldException = null;
    
    if (f == null)
      return null;
    
    String input = FileTools.tryLoadFileAsString(f.getAbsolutePath());
    try {
      oldResult = (IStrategoTerm) oldParser.parse(input, f.getAbsolutePath());
    } catch (org.spoofax.jsglr_orig.shared.SGLRException e) {
      oldException = e;
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
    
    if (LOGGING) {
      System.out.println("[" + pkg + ", old] " + "parsing took " + oldParser.timeParse + "ms, " + (oldResult != null ? "success" : "failure"));
    }
    
    return oldResult;
  }
  
  private void writeToFile(IStrategoTerm t, String f) {
    String content;
    if (t == null)
      content = "null";
    else {
      InlinePrinter printer = new InlinePrinter();
      t.prettyPrint(printer);
      content = printer.getString();
    }
    writeToFile(content, f);
  }
  
  private void writeToFile(String s, String f) {
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(f);
      fos.write(s.getBytes());
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      if (fos != null)
        try {
          fos.close();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
    }
  }
  
  private File preprocess(File f, String pkg) {
    File res = new File(f.getAbsolutePath() + "pp");
    
    CommandExecution.SILENT_EXECUTION = true;
    CommandExecution.SUB_SILENT_EXECUTION = true;
    
    String[] cmds = new String[] {
      "ghc",
      "-E",
      "-cpp", "-optP-P",
      f.getAbsolutePath()
    };
    
    try {
      CommandExecution.execute(System.out, System.out, "[" + pkg + ", pp]", cmds);
    } catch (ExecutionError e) {
      if (e.getExitValue() == -1)
        throw e;
      
      return null;
    }
    return res;
  }
}
