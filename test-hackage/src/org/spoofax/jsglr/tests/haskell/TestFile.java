package org.spoofax.jsglr.tests.haskell;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import junit.framework.TestCase;

import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr.tests.haskell.CommandExecution.ExecutionError;
import org.spoofax.jsglr.tests.haskell.compare.CompareAST;
import org.spoofax.jsglr.tests.haskell.compare.compare_0_0;
import org.spoofax.jsglr.tests.haskell_orig.HaskellOrigParser;
import org.spoofax.jsglr.tests.result.FileResult;
import org.spoofax.jsglr_orig.io.FileTools;
import org.spoofax.terms.Term;
import org.strategoxt.lang.Context;
import org.sugarj.haskell.normalize.normalize;
import org.sugarj.haskell.normalize.normalize_0_0;

/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 */
public class TestFile extends TestCase {
  
  private final static boolean LOGGING = true;
  
  static final Context normalizeContext = normalize.init();
  
  private Context compareContext = CompareAST.init();
  public HaskellParser newParserCorrectness = new HaskellParser();
  public HaskellParser newParserSpeed = new HaskellParser();
  public HaskellOrigParser oldParser = new HaskellOrigParser();
  
  private IStrategoTerm newResultCorrectness;
  private IStrategoTerm newResultSpeed;
  private IStrategoTerm oldResult;
  
  private FileResult result;
  
  public void testFile_main() throws IOException {
    // src/org/spoofax/jsglr/tests/haskell/main.hs
    String path = "c:/Users/SEBAIN~1.000/AppData/Local/Temp/HPDF3011494024294633954/HPDF-1.4.2/Graphics/PDF/Hyphenate/English.hs";
    testFile(new File(path), "main");

    String csv = path + ".csv";
    result.writeCSVHeader(csv);
    result.appendAsCSV(csv);
    System.out.println(csv);
  }
  
  public FileResult testFile(File f, String pkg) throws IOException {
    result = new FileResult();
    
    result.pkg = pkg;
    result.file = f.getAbsolutePath();
    
    if (TestConfiguration.skipFile(f)) {
      result.skipped = true;
      if (LOGGING)
        System.out.println("[" + pkg + "] " + "skipping " + f.getAbsolutePath());
      return result;
    }
    else
      result.skipped = false;
    
    if (LOGGING)
      System.out.println("[" + pkg + "] " + "parsing " + f.getAbsolutePath());
    
    File preparedInput = prepareFile(pkg, f);
    if (preparedInput == null) {
      result.cppPreprocess = false;
      if (LOGGING)
        System.out.println("[" + pkg + "] preprocessing failed");
      return result;
    }
    else
      result.cppPreprocess = true;
    
    newParseCorrectness(preparedInput, pkg);
    File implicitLayoutInput = makeImplicitLayout(preparedInput, pkg);
    newParseSpeed(implicitLayoutInput, pkg);

    File explicitLayoutInput = makeExplicitLayout(preparedInput, pkg);
    oldParse(explicitLayoutInput, pkg);

    Utilities.writeToFile(newResultCorrectness, f.getAbsolutePath() + ".new.corre");
    Utilities.writeToFile(newResultSpeed, f.getAbsolutePath() + ".new.speed");
    Utilities.writeToFile(oldResult, f.getAbsolutePath() + ".old");
    
    checkDiff(pkg, f);
    
    result.allNull = oldResult == null && newResultCorrectness == null && newResultSpeed == null;
    result.allSuccess = oldResult != null && newResultCorrectness != null && newResultSpeed != null &&
                        result.differencesToReferenceParser.t2 == 0 && result.differencesToReferenceParser.t3 == 0;
    
    if (LOGGING)
      System.out.println("[" + pkg + "] " + "done");

    result.writeCSVHeader(f.getAbsolutePath() + ".csv");
    result.appendAsCSV(f.getAbsolutePath() + ".csv");
    return result;
  }
  
  private File prepareFile(String pkg, File f) throws IOException {
    File fnorm = new File(f.getAbsolutePath().substring(0, f.getAbsolutePath().length() - 3) + "-norm.hs");
    DeleteUnicode.deleteUnicode(f.getAbsolutePath(), fnorm.getAbsolutePath());
    File fpp = preprocess(fnorm, pkg);
    return fpp;
  }
  

  private IStrategoTerm oldParse(File f, String pkg) {
    oldResult = null;
    
    if (f == null)
      return null;
    
    String input = FileTools.tryLoadFileAsString(f.getAbsolutePath());
    result.linesOfCode.t1 = input.split("\n").length;
    result.byteSize.t1 = input.getBytes().length;

    System.gc();
    
    try {
      oldResult = (IStrategoTerm) oldParser.parse(input, f.getAbsolutePath());
      result.parseOk.t1 = oldResult != null;
      result.stackOverflow.t1 = false;
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } catch (ExecutionException e) {
      if (e.getCause() instanceof org.spoofax.jsglr_orig.shared.SGLRException) {
        result.parseExceptions.t1 = e.getCause().getMessage();
        if (LOGGING)
          System.out.println(e.getCause().getMessage());
      }
      
      result.stackOverflow.t1 = e.getCause() instanceof StackOverflowError;

      if (!(e.getCause() instanceof org.spoofax.jsglr_orig.shared.SGLRException) && !(e.getCause() instanceof StackOverflowError))
        result.otherExceptions.t1 = e.getCause().getMessage();
    } finally {
      result.ambiguities.t1 = oldParser.ambiguities;
      result.layoutFilterCallsParseTime.t1 = 0;
      result.layoutFilteringParseTime.t1 = 0;
      result.layoutFilterCallsDisambiguationTime.t1 = 0;
      result.layoutFilteringDisambiguationTime.t1 = 0;
      result.enforcedNewlineSkips.t1 = 0;
      result.time.t1 = oldParser.timeParse;
      result.timeout.t1 = oldParser.timeParse < 0;
    }
    
    if (LOGGING) {
      System.out.println("[" + pkg + ", old] " + "parsing took " + (oldParser.timeParse / 1000 / 1000) + "ms, " + (oldResult != null ? "success" : "failure"));
    }
    
    return oldResult;
  }
  
  private IStrategoTerm newParseCorrectness(File f, String pkg) {
    newResultCorrectness = null;

    if (f == null)
      return null;
    String input = FileTools.tryLoadFileAsString(f.getAbsolutePath());
    result.linesOfCode.t2 = input.split("\n").length;
    result.byteSize.t2 = input.getBytes().length;
    
    System.gc();
    
    try {
      newResultCorrectness = (IStrategoTerm) newParserCorrectness.parse(input, f.getAbsolutePath());
      result.parseOk.t2 = newResultCorrectness != null;
      result.stackOverflow.t2 = false;
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } catch (ExecutionException e) {
      if (e.getCause() instanceof SGLRException) {
        result.parseExceptions.t2 = e.getCause().getMessage();
        if (LOGGING)
          System.out.println(e.getCause().getMessage());
      }

      result.stackOverflow.t2 = e.getCause() instanceof StackOverflowError;

      if (!(e.getCause() instanceof SGLRException) && !(e.getCause() instanceof StackOverflowError))
        result.otherExceptions.t2 = e.getCause().getMessage();
    } finally {
      result.ambiguities.t2 = newParserCorrectness.getAmbiguities();
      result.layoutFilterCallsParseTime.t2 = newParserCorrectness.getLayoutFilterCountParseTime();
      result.layoutFilteringParseTime.t2 = newParserCorrectness.getLayoutFilteringCountParseTime();
      result.layoutFilterCallsDisambiguationTime.t2 = newParserCorrectness.getLayoutFilterCountDisambiguationTime();
      result.layoutFilteringDisambiguationTime.t2 = newParserCorrectness.getLayoutFilteringCountDisambiguationTime();
      result.enforcedNewlineSkips.t2 = newParserCorrectness.getEnforcedNewlineSkips();
      result.time.t2 = newParserCorrectness.timeParse;
      result.timeout.t2 = newParserCorrectness.timeParse < 0;
    }
    
    if (LOGGING) {
      String time;
      if (newParserCorrectness.timeParse >= 0)
        time = newParserCorrectness.timeParse / 1000 / 1000 + "ms";
      else
        time = "TIMEOUT";
      System.out.println("[" + pkg + ", new, corre] " + "parsing took " + time + ", " + (newResultCorrectness != null ? "success" : "failure"));
    }
    
    return newResultCorrectness;
  }
  
  private IStrategoTerm newParseSpeed(File f, String pkg) {
    newResultSpeed = null;

    if (f == null)
      return null;
    String input = FileTools.tryLoadFileAsString(f.getAbsolutePath());
    result.linesOfCode.t3 = input.split("\n").length;
    result.byteSize.t3 = input.getBytes().length;

    System.gc();
    
    try {
      newResultSpeed = (IStrategoTerm) newParserSpeed.parse(input, f.getAbsolutePath());
      result.parseOk.t3 = newResultSpeed != null;
      result.stackOverflow.t3 = false;
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } catch (ExecutionException e) {
      if (e.getCause() instanceof SGLRException) {
        result.parseExceptions.t3 = e.getCause().getMessage();
        if (LOGGING)
          System.out.println(e.getCause().getMessage());
      }
      
      result.stackOverflow.t3 = e.getCause() instanceof StackOverflowError;

      if (!(e.getCause() instanceof SGLRException) && !(e.getCause() instanceof StackOverflowError))
        result.otherExceptions.t3 = e.getCause().getMessage();
    } finally {
      result.ambiguities.t3 = newParserSpeed.getAmbiguities();
      result.layoutFilterCallsParseTime.t3 = newParserSpeed.getLayoutFilterCountParseTime();
      result.layoutFilteringParseTime.t3 = newParserSpeed.getLayoutFilteringCountParseTime();
      result.layoutFilterCallsDisambiguationTime.t3 = newParserSpeed.getLayoutFilterCountDisambiguationTime();
      result.layoutFilteringDisambiguationTime.t3 = newParserSpeed.getLayoutFilteringCountDisambiguationTime();
      result.enforcedNewlineSkips.t3 = newParserCorrectness.getEnforcedNewlineSkips();
      result.time.t3 = newParserSpeed.timeParse;
      result.timeout.t3 = newParserSpeed.timeParse < 0;
    }
    
    if (LOGGING) {
      String time;
      if (newParserSpeed.timeParse >= 0)
        time = newParserSpeed.timeParse / 1000 / 1000 + "ms";
      else
        time = "TIMEOUT";
      System.out.println("[" + pkg + ", new, speed] " + "parsing took " + time + ", " + (newResultSpeed != null ? "success" : "failure"));
    }
    
    return newResultSpeed;
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

    List<String> cmds = new LinkedList<String>();
    cmds.add(TestConfiguration.PP_HASKELL_COMMAND);
    cmds.add("-i"); cmds.add(f.getAbsolutePath());
    cmds.add("-o"); cmds.add(res.getAbsolutePath());
    cmds.add("--ignore-language-pragmas");
    cmds.add(explicit ? "--explicit-layout" : "--implicit-layout");
    if (explicit)
      cmds.add("--one-line-per-decl");
    
    for (String ext : TestConfiguration.HASKELL_EXTENSIONS)
      cmds.add("-X" + ext);
    
    cmds.toArray(new String[cmds.size()]);
    
    CommandExecution.SILENT_EXECUTION = true;
    CommandExecution.SUB_SILENT_EXECUTION = true;
    
    String[][] messages = new String[][] {new String[] {}, new String[] {}};
    
    try {
      messages = CommandExecution.execute(System.out, System.out, "[" + pkg + ", old]", cmds.toArray(new String[cmds.size()]));
    } catch (ExecutionError e) {
      messages = e.getMessages();
      if (e.getExitValue() == -1)
        throw e;
      
      return null;
    } finally {
      if (explicit)
        result.makeExplicitLayout = messages[1].length == 0; 
      else
        result.makeImplicitLayout = messages[1].length == 0;
      
      result.ambInfix |= messages[1].length > 0 && messages[1][0].endsWith("pp-haskell: Ambiguous infix expression");
    }
    
    return res;
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
  
  private void checkDiff(String pkg, File f) {
    IStrategoTerm oldResultNorm = null;
    IStrategoTerm newResultCorrectnessNorm = null;
    IStrategoTerm newResultSpeedNorm = null;
    try {
      oldResultNorm = normalize(oldResult);
      result.normalizeOk.t1 = true;
    } catch (StackOverflowError e) {
    }
    try {
      newResultCorrectnessNorm = normalize(newResultCorrectness);
      result.normalizeOk.t2 = true;
    } catch (StackOverflowError e) {
    }
    try {
      newResultSpeedNorm = normalize(newResultSpeed);
      result.normalizeOk.t3 = true;
    } catch (StackOverflowError e) {
    }

    Utilities.writeToFile(newResultCorrectnessNorm, f.getAbsolutePath() + ".new.corre.norm");
    Utilities.writeToFile(newResultSpeedNorm, f.getAbsolutePath() + ".new.speed.norm");
    Utilities.writeToFile(oldResultNorm, f.getAbsolutePath() + ".old.norm");
    
    result.differencesToReferenceParser.t1 = 0;
    try {
      result.differencesToReferenceParser.t2 = checkDiff(pkg, f, newResultCorrectnessNorm, oldResultNorm, "corre");
    } catch (StackOverflowError e) {
      result.normalizeOk.t2 = false;
    }
    try {
      result.differencesToReferenceParser.t3 = checkDiff(pkg, f, newResultSpeedNorm, oldResultNorm, "speed");
    } catch (StackOverflowError e) {
      result.normalizeOk.t3 = false;
    }
  }
  
  private int checkDiff(String pkg, File f, IStrategoTerm actual, IStrategoTerm expected, String actualDescriptor) {
    IStrategoList diff = compare(actual, expected);
    
    if (diff == null || !diff.isEmpty()) {
      if (LOGGING) {
        System.out.println("*error*" + "[" + pkg + ", new, " + actualDescriptor + "] " + "Comparison failed for " + f.getAbsolutePath());
        if (diff != null) {
          String diffString = diff.toString();
          Utilities.writeToFile(diffString, f.getAbsolutePath() + ".new." + actualDescriptor + ".diff");
          System.out.println("*error*" + "[" + pkg + "] " + "diff: " + diffString);
        }
      }
    }
    
    return diff == null ? 0 : diff.size();
  }
  
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

}
