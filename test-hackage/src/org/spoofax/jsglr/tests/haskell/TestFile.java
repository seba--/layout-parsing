package org.spoofax.jsglr.tests.haskell;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import junit.framework.TestCase;

import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.tests.haskell.CommandExecution.ExecutionError;
import org.spoofax.jsglr.tests.haskell.compare.CompareAST;
import org.spoofax.jsglr.tests.haskell.compare.CompareLibrary;
import org.spoofax.jsglr.tests.haskell.compare.compare_0_0;
import org.spoofax.jsglr.tests.result.FileResult;
import org.spoofax.jsglr_layout.shared.SGLRException;
import org.spoofax.jsglr_layout.tests.haskell.HaskellParser;
import org.spoofax.jsglr_orig.io.FileTools;
import org.spoofax.terms.Term;
import org.strategoxt.lang.Context;
import org.sugarj.haskell.normalize.normalize;
import org.sugarj.haskell.normalize.normalize_0_0;

/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 */
public class TestFile extends TestCase {

  private final static boolean LOGGING = false;

  private static Context normalizeContext = normalize.init();
  private static Context compareContext = CompareAST.init();
  static {
    normalizeContext.addOperatorRegistry(new CompareLibrary());
    compareContext.addOperatorRegistry(new CompareLibrary());
  }

  public HaskellParser newParserOrig = new HaskellParser();
  public HaskellParser newParserImpl = new HaskellParser();
  public org.spoofax.jsglr_orig.tests.haskell.HaskellParser oldParser = new org.spoofax.jsglr_orig.tests.haskell.HaskellParser();

  private IStrategoTerm newResultOrig;
  private IStrategoTerm newResultImpl;
  private IStrategoTerm oldResult;

  private FileResult result;

  public void testFile_main() throws IOException {
    // src/org/spoofax/jsglr/tests/haskell/main.hs
    //String file = "sample-data/template-haskell/Printf.hs";//"d:/tmp/test.hs";
    //String file = "hackage-data/activehs/0.3/activehs-0.3/Parse.hs";
    //String file = "hackage-data/accelerate-examples/0.12.0.0/accelerate-examples-0.12.0.0/examples/crystal/Config.hs";
   // String file = "hackage-data/zoom/0.1.0.1/zoom-0.1.0.1/Zoom/Template/TH.hs";
  //  String file = "hackage-data/AC-MiniTest/1.1.1/AC-MiniTest-1.1.1/Test/AC/Test.hs";
    //String file = "hackage-data/accelerate/0.12.0.0/accelerate-0.12.0.0/Data/Array/Accelerate/Pretty/Traverse.hs";
   // String file = "hackage-data/AbortT-transformers/1.0/AbortT-transformers-1.0/Control/Monad/Trans/Abort.hs";
  //  String file = "hackage-data/AC-VanillaArray/1.1.2/AC-VanillaArray-1.1.2/Data/Array/Vanilla/Unsafe.hs";
   // String file = "hackage-data/accelerate-examples/0.12.0.0/accelerate-examples-0.12.0.0/examples/tests/simple/DotP.hs";
    //String file = "hackage-data/AC-Vector-Fancy/2.4.0/AC-Vector-Fancy-2.4.0/Data/Vector/Fancy.hs";
   // String file ="hackage-data/accelerate-io/0.12.0.0/accelerate-io-0.12.0.0/Data/Array/Accelerate/IO/Repa.hs";
   //  String file = "hackage-data/ZipFold/0.1.4/ZipFold-0.1.4/src/Data/WithCont.hs";
   // String file = "hackage-data/Zwaluw/0.1/Zwaluw-0.1/Web/Zwaluw.hs";
    String file = "hackage-data/";
    //file += "accelerate-examples/0.12.0.0/accelerate-examples-0.12.0.0/examples/quickcheck/Test/Base.hs";
 //   String file = "hackage-data/ZipFold/0.1.4/ZipFold-0.1.4/src/Data/Zip/FoldL.hs";
    file += "zipper/0.4.1/zipper-0.4.1/src/Generics/MultiRec/Zipper.hs";
    testFile(new File(file), file, "main");
    testFile(new File(file), file, "main");
    testFile(new File(file), file, "main");
    
    String csv = file + ".csv";
    result.writeCSVHeader(csv);
    result.appendAsCSV(csv);
    System.out.println(csv);
  }

  public FileResult testFile(File f, String path, String pkg)
      throws IOException {
    clean(f);

    result = new FileResult();

    result.pkg = pkg;
    result.path = path;

    if (TestConfiguration.skipFile(f)) {
      result.skipped = true;
      if (LOGGING)
        System.out
            .println("[" + pkg + "] " + "skipping " + f.getAbsolutePath());
      return result;
    } else
      result.skipped = false;

    if (LOGGING)
      System.out.println("[" + pkg + "] " + "parsing " + f.getAbsolutePath());

    File preparedInput = prepareFile(pkg, f);
    if (preparedInput == null) {
      result.cppPreprocess = false;
      if (LOGGING)
        System.out.println("[" + pkg + "] preprocessing failed");
      return result;
    } else
      result.cppPreprocess = true;

    File implicitLayoutInput = makeImplicitLayout(preparedInput, pkg);
    File explicitLayoutInput = makeExplicitLayout(preparedInput, pkg);

    oldParse(explicitLayoutInput, pkg);
    newParseOrig(preparedInput, pkg);
    newParseImpl(implicitLayoutInput, pkg);

    Utilities.writeToFile(newResultOrig, f.getAbsolutePath() + ".new.orig");
    Utilities.writeToFile(newResultImpl, f.getAbsolutePath() + ".new.impl");
    Utilities.writeToFile(oldResult, f.getAbsolutePath() + ".old");

    checkAmbiguities(pkg, f);
    checkDiff(pkg, f);

    result.allNull = oldResult == null && newResultOrig == null
        && newResultImpl == null;
    result.allSuccess = result.parseOk.t1 && result.parseOk.t2
        && result.parseOk.t3 && result.differencesToReferenceParser.t2 == 0
        && result.differencesToReferenceParser.t3 == 0;

    if (LOGGING)
      System.out.println("[" + pkg + "] " + "done");

    result.writeCSVHeader(f.getAbsolutePath() + ".csv");
    result.appendAsCSV(f.getAbsolutePath() + ".csv");
    return result;
  }

  private File prepareFile(String pkg, File f) throws IOException {
    try {
      File fnorm = new File(f.getAbsolutePath() + ".norm.hs");
      DeleteUnicode.deleteUnicode(f.getAbsolutePath(), fnorm.getAbsolutePath());
      File fpp = preprocess(fnorm, pkg);
      Utilities.deleteFile(fnorm);
      return fpp;
    } catch (OutOfMemoryError e) {
      e = null;
      System.gc();
      return null;
    }
  }

  private IStrategoTerm oldParse(File f, String pkg) {
    oldResult = null;

    if (f == null)
      return null;

    String input;
    try {
      input = FileTools.tryLoadFileAsString(f.getAbsolutePath());
    } catch (OutOfMemoryError e) {
      result.outOfMemory.t1 = true;
      return null;
    }

    if (input == null) {
      result.otherExceptions.t1 = "input is null";
      return null;
    }

    result.linesOfCode.t1 = input.split("\n").length;
    result.byteSize.t1 = input.getBytes().length;

    System.gc();
    result.memoryBefore.t1 = Utilities.usedMemory();

    try {
      oldResult = (IStrategoTerm) oldParser.parse(input, f.getAbsolutePath());
      result.parseOk.t1 = oldResult != null;
      result.stackOverflow.t1 = false;

    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } catch (OutOfMemoryError e) {
      result.outOfMemory.t1 = true;
      ;
    } catch (ExecutionException e) {
      if (e.getCause() instanceof org.spoofax.jsglr_orig.shared.SGLRException) {
        result.parseExceptions.t1 = e.getCause().getMessage();
        if (LOGGING)
          System.out.println(e.getCause().getMessage());
      }

      result.stackOverflow.t1 = e.getCause() instanceof StackOverflowError;
      if (e.getCause() instanceof StackOverflowError)
        e.getCause().printStackTrace();

      if (!(e.getCause() instanceof org.spoofax.jsglr_orig.shared.SGLRException)
          && !(e.getCause() instanceof StackOverflowError)) {
        result.otherExceptions.t1 = e.getCause().getMessage();
        // e.getCause().printStackTrace();
      }
    } finally {
      result.ambiguities.t1 = oldParser.ambiguities;
      result.layoutFilterCallsParseTime.t1 = 0;
      result.layoutFilteringParseTime.t1 = 0;
      result.layoutFilterCallsDisambiguationTime.t1 = 0;
      result.layoutFilteringDisambiguationTime.t1 = 0;
      result.enforcedNewlineSkips.t1 = 0;
      result.time.t1 = oldParser.timeParse;
      result.timeout.t1 = oldParser.timeParse < 0;
      result.memoryBefore.t1 = oldParser.memoryBefore;
      result.memoryAfter.t1 = oldParser.memoryAfter;
    }

    if (LOGGING) {
      String time;
      if (oldParser.timeParse >= 0)
        time = oldParser.timeParse / 1000 / 1000 + "ms";
      else
        time = "TIMEOUT";
      System.out.println("[" + pkg + ", old] " + "parsing took " + time + ", "
          + (oldResult != null ? "success" : "failure"));
    }

    return oldResult;
  }

  private IStrategoTerm newParseOrig(File f, String pkg) {
    newResultOrig = null;

    if (f == null)
      return null;

    String input;
    try {
      input = org.spoofax.jsglr_layout.io.FileTools.tryLoadFileAsString(f
          .getAbsolutePath());
    } catch (OutOfMemoryError e) {
      result.outOfMemory.t2 = true;
      return null;
    }

    if (input == null) {
      result.otherExceptions.t2 = "input is null";
      return null;
    }

    result.linesOfCode.t2 = input.split("\n").length;
    result.byteSize.t2 = input.getBytes().length;

    System.gc();

    try {
      newResultOrig = (IStrategoTerm) newParserOrig.parse(input,
          f.getAbsolutePath());
      result.parseOk.t2 = newResultOrig != null;
      result.stackOverflow.t2 = false;
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } catch (OutOfMemoryError e) {
      result.outOfMemory.t2 = true;
      ;
    } catch (ExecutionException e) {
      if (e.getCause() instanceof SGLRException) {
        result.parseExceptions.t2 = e.getCause().getMessage();
        if (LOGGING)
          System.out.println(e.getCause().getMessage());
      }

      result.stackOverflow.t2 = e.getCause() instanceof StackOverflowError;
      if (e.getCause() instanceof StackOverflowError)
        e.getCause().printStackTrace();

      if (!(e.getCause() instanceof SGLRException)
          && !(e.getCause() instanceof StackOverflowError))
        result.otherExceptions.t2 = e.getCause().getMessage();
    } finally {
      result.ambiguities.t2 = newParserOrig.ambiguities;
      result.layoutFilterCallsParseTime.t2 = newParserOrig.layoutFilterCountParseTime;
      result.layoutFilteringParseTime.t2 = newParserOrig.layoutFilteringCountParseTime;
      result.layoutFilterCallsDisambiguationTime.t2 = newParserOrig.layoutFilterCountDisambiguationTime;
      result.layoutFilteringDisambiguationTime.t2 = newParserOrig.layoutFilteringCountDisambiguationTime;
      result.enforcedNewlineSkips.t2 = newParserOrig.enforcedNewlineSkips;
      result.time.t2 = newParserOrig.timeParse;
      result.timeout.t2 = newParserOrig.timeParse < 0;
      result.memoryBefore.t2 = newParserOrig.memoryBefore;
      result.memoryAfter.t2 = newParserOrig.memoryAfter;
    }

    if (LOGGING) {
      String time;
      if (newParserOrig.timeParse >= 0)
        time = newParserOrig.timeParse / 1000 / 1000 + "ms";
      else
        time = "TIMEOUT";
      System.out.println("[" + pkg + ", new, orig] " + "parsing took " + time
          + ", " + (newResultOrig != null ? "success" : "failure"));
    }

    return newResultOrig;
  }

  private IStrategoTerm newParseImpl(File f, String pkg) {
    newResultImpl = null;

    if (f == null)
      return null;

    String input;
    try {
      input = org.spoofax.jsglr_layout.io.FileTools.tryLoadFileAsString(f
          .getAbsolutePath());
    } catch (OutOfMemoryError e) {
      result.outOfMemory.t3 = true;
      return null;
    }

    if (input == null) {
      result.otherExceptions.t3 = "input is null";
      return null;
    }

    result.linesOfCode.t3 = input.split("\n").length;
    result.byteSize.t3 = input.getBytes().length;

    System.gc();
    result.memoryBefore.t3 = Utilities.usedMemory();

    try {
      newResultImpl = (IStrategoTerm) newParserImpl.parse(input,
          f.getAbsolutePath());
      result.parseOk.t3 = newResultImpl != null;
      result.stackOverflow.t3 = false;
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } catch (OutOfMemoryError e) {
      result.outOfMemory.t3 = true;
      ;
    } catch (ExecutionException e) {
      if (e.getCause() instanceof SGLRException) {
        result.parseExceptions.t3 = e.getCause().getMessage();
        if (LOGGING)
          System.out.println(e.getCause().getMessage());
      }

      result.stackOverflow.t3 = e.getCause() instanceof StackOverflowError;
      if (e.getCause() instanceof StackOverflowError)
        e.getCause().printStackTrace();

      if (!(e.getCause() instanceof SGLRException)
          && !(e.getCause() instanceof StackOverflowError))
        result.otherExceptions.t3 = e.getCause().getMessage();
    } finally {
      result.ambiguities.t3 = newParserImpl.ambiguities;
      result.layoutFilterCallsParseTime.t3 = newParserImpl.layoutFilterCountParseTime;
      result.layoutFilteringParseTime.t3 = newParserImpl.layoutFilteringCountParseTime;
      result.layoutFilterCallsDisambiguationTime.t3 = newParserImpl.layoutFilterCountDisambiguationTime;
      result.layoutFilteringDisambiguationTime.t3 = newParserImpl.layoutFilteringCountDisambiguationTime;
      result.enforcedNewlineSkips.t3 = newParserOrig.enforcedNewlineSkips;
      result.time.t3 = newParserImpl.timeParse;
      result.timeout.t3 = newParserImpl.timeParse < 0;
      result.memoryBefore.t3 = newParserImpl.memoryBefore;
      result.memoryAfter.t3 = newParserImpl.memoryAfter;
    }

    if (LOGGING) {
      String time;
      if (newParserImpl.timeParse >= 0)
        time = newParserImpl.timeParse / 1000 / 1000 + "ms";
      else
        time = "TIMEOUT";
      System.out.println("[" + pkg + ", new, impl] " + "parsing took " + time
          + ", " + (newResultImpl != null ? "success" : "failure"));
    }

    return newResultImpl;
  }

  private File makeImplicitLayout(File f, String pkg) throws IOException {
    return makeLayout(f, pkg, false);
  }

  private File makeExplicitLayout(File f, String pkg) throws IOException {
    return makeLayout(f, pkg, true);
  }

  private File makeLayout(File f, String pkg, boolean explicit)
      throws IOException {
    if (f == null)
      return null;

    File res = new File(f.getAbsolutePath() + (explicit ? ".expl" : ".impl"));

    List<String> cmds = new LinkedList<String>();
    cmds.add(TestConfiguration.PP_HASKELL_COMMAND);
    cmds.add("-i");
    cmds.add(f.getAbsolutePath());
    cmds.add("-o");
    cmds.add(res.getAbsolutePath());
    cmds.add("--ignore-language-pragmas");
    cmds.add("--line-length=500");
    cmds.add("--ribbons-per-line=10");
    cmds.add(explicit ? "--explicit-layout" : "--implicit-layout");

    for (String ext : TestConfiguration.HASKELL_EXTENSIONS)
      cmds.add("-X" + ext);

    cmds.toArray(new String[cmds.size()]);

    CommandExecution.SILENT_EXECUTION = true;
    CommandExecution.SUB_SILENT_EXECUTION = true;

    String[][] messages = new String[][] { new String[] {}, new String[] {} };
    long time = -1;

    try {
      Object[] result = CommandExecution.execute(System.out, System.out, "["
          + pkg + ", old]", cmds.toArray(new String[cmds.size()]));
      time = (Long) result[0];
      messages[0] = (String[]) result[1];
      messages[1] = (String[]) result[2];
    } catch (ExecutionError e) {
      e.printStackTrace();
      messages = e.getMessages();
      return null;
    } finally {
      if (result.referenceTime <= 0)
        result.referenceTime = time;

      if (explicit)
        result.makeExplicitLayout = messages[1].length == 0;
      else
        result.makeImplicitLayout = messages[1].length == 0;

      result.ambInfix |= messages[1].length > 0
          && messages[1][0].endsWith("Ambiguous infix expression");
    }

    return res.exists() ? res : null;
  }

  private File preprocess(File f, String pkg) {
    File res = new File(Utilities.dropExtension(f.getAbsolutePath()) + ".pp");

    CommandExecution.SILENT_EXECUTION = true;
    CommandExecution.SUB_SILENT_EXECUTION = true;

    String[] cmds = new String[] { "ghc", "-E", "-cpp", "-optP-P", "-o",
        res.getAbsolutePath(), f.getAbsolutePath() };

    try {
      CommandExecution.execute(System.out, System.out, "[" + pkg + ", pp]",
          cmds);
    } catch (ExecutionError e) {
      if (e.getExitValue() == -1)
        throw e;

      return null;
    }
    return res;
  }

  private IStrategoTerm normalize(IStrategoTerm term) {
    return term == null ? null : normalize_0_0.instance.invoke(
        normalizeContext, term);
  }

  private void checkDiff(String pkg, File f) {
    IStrategoTerm oldResultNorm = null;
    IStrategoTerm newResultOrigNorm = null;
    IStrategoTerm newResultImplNorm = null;
    try {
      oldResultNorm = normalize(oldResult);
      result.normalizeOk.t1 = true;
    } catch (StackOverflowError e) {
    }
    try {
      newResultOrigNorm = normalize(newResultOrig);
      result.normalizeOk.t2 = true;
    } catch (StackOverflowError e) {
    }
    try {
      newResultImplNorm = normalize(newResultImpl);
      result.normalizeOk.t3 = true;
    } catch (StackOverflowError e) {
    }

    Utilities.writeToFile(newResultOrigNorm, f.getAbsolutePath()
        + ".new.orig.norm");
    Utilities.writeToFile(newResultImplNorm, f.getAbsolutePath()
        + ".new.impl.norm");
    Utilities.writeToFile(oldResultNorm, f.getAbsolutePath() + ".old.norm");

    result.differencesToReferenceParser.t1 = 0;
    try {
      result.differencesToReferenceParser.t2 = checkDiff(pkg, f,
          newResultOrigNorm, oldResultNorm, "orig");
    } catch (StackOverflowError e) {
      result.normalizeOk.t2 = false;
    }
    try {
      result.differencesToReferenceParser.t3 = checkDiff(pkg, f,
          newResultImplNorm, oldResultNorm, "impl");
    } catch (StackOverflowError e) {
      result.normalizeOk.t3 = false;
    }
  }

  private int checkDiff(String pkg, File f, IStrategoTerm actual,
      IStrategoTerm expected, String actualDescriptor) {
    IStrategoList diff = compare(actual, expected);

    if (diff == null || !diff.isEmpty()) {
      if (LOGGING) {
        System.out.println("*error*" + "[" + pkg + ", new, " + actualDescriptor
            + "] " + "Comparison failed for " + f.getAbsolutePath());
        if (diff != null) {
          String diffString = diff.toString();
          Utilities.writeToFile(diffString, f.getAbsolutePath() + ".new."
              + actualDescriptor + ".diff");
          System.out.println("*error*" + "[" + pkg + "] " + "diff: "
              + diffString);
        }
      }
    }

    return diff == null ? 1 : diff.size();
  }

  private IStrategoList compare(IStrategoTerm term1, IStrategoTerm term2) {
    if (term1 == null && term2 == null)
      return compareContext.getFactory().makeList();
    if (term1 == null || term2 == null)
      return null;

    IStrategoTerm result = compare_0_0.instance.invoke(compareContext,
        compareContext.getFactory().makeTuple(term1, term2));
    if (result != null && Term.isTermList(result))
      return (IStrategoList) result;

    return null;
  }

  private void checkAmbiguities(String pkg, File f) {
    if (LOGGING && oldParser.ambiguities > 0)
      System.out.println("[" + pkg + ", old] " + oldParser.ambiguities
          + " ambiguities");
    if (LOGGING && newParserOrig.ambiguities > 0)
      System.out.println("[" + pkg + ", new orig] " + newParserOrig.ambiguities
          + " ambiguities");
    if (LOGGING && newParserImpl.ambiguities > 0)
      System.out.println("[" + pkg + ", new impl] " + newParserImpl.ambiguities
          + " ambiguities");
  }

  private void clean(File f) {
    /*
     * Utilities.deleteFile(f.getAbsoluteFile() + ".csv");
     * Utilities.deleteFile(f.getAbsoluteFile() + ".norm");
     * Utilities.deleteFile(f.getAbsoluteFile() + ".norm.pp");
     * Utilities.deleteFile(f.getAbsoluteFile() + ".norm.pp.expl");
     * Utilities.deleteFile(f.getAbsoluteFile() + ".norm.pp.impl");
     * Utilities.deleteFile(f.getAbsoluteFile() + ".old");
     * Utilities.deleteFile(f.getAbsoluteFile() + ".new.corre");
     * Utilities.deleteFile(f.getAbsoluteFile() + ".new.orig");
     * Utilities.deleteFile(f.getAbsoluteFile() + ".new.speed");
     * Utilities.deleteFile(f.getAbsoluteFile() + ".new.impl");
     * Utilities.deleteFile(f.getAbsoluteFile() + ".old.pt");
     * Utilities.deleteFile(f.getAbsoluteFile() + ".new.pt");
     */
  }
}
