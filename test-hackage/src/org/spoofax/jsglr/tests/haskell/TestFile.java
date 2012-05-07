package org.spoofax.jsglr.tests.haskell;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr.tests.haskell.CommandExecution.ExecutionError;
import org.spoofax.jsglr.tests.haskell.compare.CompareAST;
import org.spoofax.jsglr.tests.haskell.compare.compare_0_0;
import org.spoofax.jsglr.tests.haskell_orig.HaskellOrigParser;
import org.spoofax.jsglr_orig.io.FileTools;
import org.spoofax.terms.Term;
import org.strategoxt.lang.Context;
import org.sugarj.haskell.normalize.normalize;
import org.sugarj.haskell.normalize.normalize_0_0;

/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 */
public class TestFile extends ChainedTestCase {
  
  private final static boolean LOGGING = true;
  
//  private Context normalizeContext = NormalizeAST.init();
  private Context compareContext = CompareAST.init();
  public HaskellParser newParserCorrectness = new HaskellParser();
  public HaskellParser newParserSpeed = new HaskellParser();
  public HaskellOrigParser oldParser = new HaskellOrigParser();
  
  private IStrategoTerm newResultCorrectness;
  private Throwable newExceptionCorrectness;
  private IStrategoTerm newResultSpeed;
  private Throwable newExceptionSpeed;
  private IStrategoTerm oldResult;
  private Throwable oldException;
  private String[][] mkExplicitMessages;
  private String[][] mkImplicitMessages;
  
  static final Context normalizeContext = normalize.init();
  
  
  public void testFile_main() throws IOException {
    
    // src/org/spoofax/jsglr/tests/haskell/main.hs
    testFile(new File("c:/Users/seba.INFORMATIK.000/AppData/Local/Temp/4Blocks7755594627937130244/4Blocks-0.2/Core/Brick.hs"), "main");
    printShortLog();
    raiseFailures();
  }
  
  public void testFile(File f, String pkg) throws IOException {
    if (TestConfiguration.skipFile(f)) {
      if (LOGGING)
        System.out.println("[" + pkg + "] " + "skipping " + f.getAbsolutePath());
      return;
    }
    
    if (LOGGING)
      System.out.println("[" + pkg + "] " + "parsing " + f.getAbsolutePath());
    
    File preparedInput = prepareFile(pkg, f);
    if (preparedInput == null)
      return;
    
    newParseCorrectness(preparedInput, pkg);
    File implicitLayoutInput = makeImplicitLayout(preparedInput, pkg);
    newParseSpeed(implicitLayoutInput, pkg);

    File explicitLayoutInput = makeExplicitLayout(preparedInput, pkg);
    oldParse(explicitLayoutInput, pkg);

    Utilities.writeToFile(newResultCorrectness, f.getAbsolutePath() + ".new");
    Utilities.writeToFile(oldResult, f.getAbsolutePath() + ".old");
    
    checkAmbiguities(pkg);
    checkMakeLayoutMessages();
    checkExceptions(pkg);
    
    boolean failed = 
      checkTimeout(f) || 
      checkAmbInfix(pkg) ||
      checkDiff(pkg, f) ||
      checkReferenceParserFailed(pkg, explicitLayoutInput) || 
      checkAllParsersFailed(pkg, explicitLayoutInput);
    
    if (!failed) {
      assert newResultCorrectness != null && newResultSpeed != null && oldResult != null;
      logOk();
      if (LOGGING)
        System.out.println("[" + pkg + "] " + "ok");
    }
  }
  
  private File prepareFile(String pkg, File f) throws IOException {
    File fnorm = new File(f.getAbsolutePath().substring(0, f.getAbsolutePath().length() - 3) + "-norm.hs");
    DeleteUnicode.deleteUnicode(f.getAbsolutePath(), fnorm.getAbsolutePath());
    File fpp = preprocess(fnorm, pkg);
    
    if (fpp == null && LOGGING)
      System.out.println("[" + pkg + "] preprocessing failed");
    
    return fpp;
  }
  
  private IStrategoTerm newParseCorrectness(File f, String pkg) {
    newResultCorrectness = null;
    newExceptionCorrectness = null;

    if (f == null)
      return null;
    String input = FileTools.tryLoadFileAsString(f.getAbsolutePath());
    try {
      newResultCorrectness = (IStrategoTerm) newParserCorrectness.parse(input, f.getAbsolutePath());
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } catch (ExecutionException e) {
      if (e.getCause() instanceof SGLRException)
        newExceptionCorrectness = (SGLRException) e.getCause();
      else if (e.getCause() instanceof StackOverflowError) {
        newExceptionCorrectness = e.getCause();
        if (LOGGING)
          System.out.println("[" + pkg + ", new, corre] " + "STACK OVERFLOW");
      }
      else
        throw new RuntimeException(e);
    } 
    
    if (LOGGING) {
      String time;
      if (newParserCorrectness.timeParse >= 0)
        time = newParserCorrectness.timeParse + "ms";
      else
        time = "TIMEOUT";
      System.out.println("[" + pkg + ", new, corre] " + "parsing took " + time + ", " + (newResultCorrectness != null ? "success" : "failure"));
    }
    
    return newResultCorrectness;
  }
  
  private IStrategoTerm newParseSpeed(File f, String pkg) {
    newResultSpeed = null;
    newExceptionSpeed = null;

    if (f == null)
      return null;
    String input = FileTools.tryLoadFileAsString(f.getAbsolutePath());
    try {
      newResultSpeed = (IStrategoTerm) newParserSpeed .parse(input, f.getAbsolutePath());
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } catch (ExecutionException e) {
      if (e.getCause() instanceof SGLRException)
        newExceptionSpeed = (SGLRException) e.getCause();
      else if (e.getCause() instanceof StackOverflowError) {
        newExceptionSpeed = e.getCause();
        if (LOGGING)
          System.out.println("[" + pkg + ", new, speed] " + "STACK OVERFLOW");
      }
      else
        throw new RuntimeException(e);
    }
    
    if (LOGGING) {
      String time;
      if (newParserSpeed .timeParse >= 0)
        time = newParserSpeed .timeParse + "ms";
      else
        time = "TIMEOUT";
      System.out.println("[" + pkg + ", new, speed] " + "parsing took " + time + ", " + (newResultSpeed != null ? "success" : "failure"));
    }
    
    return newResultCorrectness;
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

  private File makeImplicitLayout(File f, String pkg) throws IOException {
    return makeLayout(f, pkg, false);
  }
  
  private File makeExplicitLayout(File f, String pkg) throws IOException {
    return makeLayout(f, pkg, true);
  }
  
  private File makeLayout(File f, String pkg, boolean explicit) throws IOException {
    if (f == null)
      return null;
    
    File res = new File(f.getAbsolutePath() + (explicit ? ".expl" : ".impl"));

    String[] cmds = new String[] {
        TestConfiguration.PP_HASKELL_COMMAND, 
        "-i", f.getAbsolutePath(), 
        "-o", res.getAbsolutePath(), 
        explicit ? "--explicit-layout" : "--implicit-layout",
        "--ignore-language-pragmas"};
    
    List<String> cmdList = new ArrayList<String>(Arrays.asList(cmds));
    for (String ext : TestConfiguration.HASKELL_EXTENSIONS)
      cmdList.add("-X" + ext);
    
    cmds = cmdList.toArray(new String[cmdList.size()]);
    
    CommandExecution.SILENT_EXECUTION = true;
    CommandExecution.SUB_SILENT_EXECUTION = true;
    
    String[][] messages = new String[][] {new String[] {}, new String[] {}};
    
    try {
      messages = CommandExecution.execute(System.out, System.out, "[" + pkg + ", old]", cmds);
    } catch (ExecutionError e) {
      messages = e.getMessages();
      if (e.getExitValue() == -1)
        throw e;
      
      return null;
    } finally {
      if (explicit)
        mkExplicitMessages = messages;
      else
        mkImplicitMessages = messages;
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
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } catch (ExecutionException e) {
      if (e.getCause() instanceof org.spoofax.jsglr_orig.shared.SGLRException)
        oldException = (org.spoofax.jsglr_orig.shared.SGLRException) e.getCause();
      else if (e.getCause() instanceof StackOverflowError) {
        oldException = e.getCause();
        if (LOGGING)
          System.out.println("[" + pkg + ", old] " + "STACK OVERFLOW");
      }
      else
        throw new RuntimeException(e);
    }
    
    if (LOGGING) {
      System.out.println("[" + pkg + ", old] " + "parsing took " + oldParser.timeParse + "ms, " + (oldResult != null ? "success" : "failure"));
    }
    
    return oldResult;
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
  
  private IStrategoTerm normalize(IStrategoTerm term) {
    return term == null ? null : normalize_0_0.instance.invoke(normalizeContext, term);
  }
  
  private void checkAmbiguities(String pkg) {
    if (newParserCorrectness.parseTree != null && newParserCorrectness.ambiguities > 0) {
      //writeToFile(newParser.parseTree.toString(), f.getAbsolutePath() + ".new.pt");
      System.out.println("*error*" + "[" + pkg + ", new, corre] " + "tree contains " + newParserCorrectness.ambiguities + " ambiguities");
    }

    if (newParserSpeed.parseTree != null && newParserSpeed.ambiguities > 0) {
      //writeToFile(newParser.parseTree.toString(), f.getAbsolutePath() + ".new.pt");
      System.out.println("*error*" + "[" + pkg + ", new, speed] " + "tree contains " + newParserSpeed.ambiguities + " ambiguities");
    }

    if (oldParser.parseTree != null && oldParser.ambiguities > 0) {
      //writeToFile(oldParser.parseTree.toString(), f.getAbsolutePath() + ".old.pt");
      System.out.println("*error*" + "[" + pkg + ", old] " + "tree contains " + oldParser.ambiguities + " ambiguities");
    }
  }
  
  private void checkMakeLayoutMessages() {
    if (mkExplicitMessages[1].length > 0 && LOGGING) {
      System.out.println(mkExplicitMessages[1][0]);
    }
    
    if (mkImplicitMessages[1].length > 0 && LOGGING) {
      System.out.println(mkImplicitMessages[1][0]);
    }
  }
  
  private void checkExceptions(String pkg) {
//    if (newExceptionCorrectness != null) {
//      System.out.println("[" + pkg + ", new] failed");
//      newExceptionCorrectness.printStackTrace(System.out);
//    }
//
//    if (newExceptionSpeed != null) {
//      System.out.println("[" + pkg + ", new] failed");
//      newExceptionSpeed.printStackTrace(System.out);
//    }
//
//    if (oldException != null) {
//      System.out.println("[" + pkg + ", old] failed");
//      oldException.printStackTrace(System.out);
//    }
  }
  
  private boolean checkTimeout(File f) {
    if (newParserCorrectness.timeParse < 0) {
      logTimeout(f.getAbsolutePath() + ".new");
      return true;
    }
    if (newParserSpeed.timeParse < 0) {
      logTimeout(f.getAbsolutePath() + ".new");
      return true;
    }
    if (oldParser.timeParse < 0) {
      logTimeout(f.getAbsolutePath() + ".old");
      return true;
    }
    
    return false;
  }
  
  private boolean checkAmbInfix(String pkg) {
    if (newResultCorrectness != null && newResultSpeed != null && mkExplicitMessages[1].length > 0 && mkExplicitMessages[1][0].endsWith("pp-haskell: Ambiguous infix expression")) {
      logAmbInfix();
      
      if (LOGGING)
        System.out.println("[" + pkg + "] " + "ambInfix");
      
      return true;
    }
    
    return false;
  }
  
  private boolean checkDiff(String pkg, File f) {
    IStrategoTerm newResultCorrectnessNorm = normalize(newResultCorrectness);
    IStrategoTerm newResultSpeedNorm = normalize(newResultSpeed);
    IStrategoTerm oldResultNorm = normalize(oldResult);
    Utilities.writeToFile(newResultCorrectnessNorm, f.getAbsolutePath() + ".new.corre.norm");
    Utilities.writeToFile(newResultSpeedNorm, f.getAbsolutePath() + ".new.speed.norm");
    Utilities.writeToFile(oldResultNorm, f.getAbsolutePath() + ".old.norm");
    
    return 
      !(newExceptionCorrectness instanceof StackOverflowError) && checkDiff(pkg, f, newResultCorrectnessNorm, oldResultNorm, "corre") ||
      checkDiff(pkg, f, newResultSpeedNorm, oldResultNorm, "speed");
  }
  
  private boolean checkDiff(String pkg, File f, IStrategoTerm actual, IStrategoTerm expected, String actualDescriptor) {
    IStrategoList diff = compare(actual, expected);
    
    if (diff == null || !diff.isEmpty()) {
      ParseComparisonFailure failure = logComparisonFailure(f.getAbsolutePath(), expected, actual);
    
      if (LOGGING) {
        System.out.println("*error*" + "[" + pkg + ", new, " + actualDescriptor + "] " + failure.getMessage());
        if (diff != null) {
          String diffString = diff.toString();
          Utilities.writeToFile(diffString, f.getAbsolutePath() + ".new." + actualDescriptor + ".diff");
          System.out.println("*error*" + "[" + pkg + "] " + "diff: " + diffString);
        }
      }
      
      return true;
    }
    
    return false;
  }

  private boolean checkAllParsersFailed(String pkg, File explicitLayoutInput) {
    if (newResultCorrectness == null && newResultSpeed == null && explicitLayoutInput != null && oldResult == null) {
      // reference parser succeeded, but we failed independent of layout
      logNoParse();
      
      if (LOGGING)
        System.out.println("[" + pkg + "] " + "no parse");
      
      return true;
    }
    
    return false;
  }

  private boolean checkReferenceParserFailed(String pkg, File explicitLayoutInput) {
    if (newResultCorrectness == null && explicitLayoutInput == null) {
      // original parser failed as well
      logOkFail();
      if (LOGGING)
        System.out.println("[" + pkg + "] " + "okFail");
      return true;
    }
    return false;
  }
  
}
