package org.spoofax.jsglr.tests.haskell.normalize;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") public class NormalizeAST  
{ 
  protected static final boolean TRACES_ENABLED = true;

  protected static ITermFactory constantFactory;

  private static WeakReference<Context> initedContext;

  private static boolean isIniting;

  protected static IStrategoTerm const14;

  protected static IStrategoTerm const13;

  protected static IStrategoTerm const12;

  protected static IStrategoTerm const11;

  protected static IStrategoTerm constCharEsc6;

  protected static IStrategoTerm const10;

  protected static IStrategoTerm constCharEsc5;

  protected static IStrategoTerm const9;

  protected static IStrategoTerm constCharEsc4;

  protected static IStrategoTerm const8;

  protected static IStrategoTerm constCharEsc3;

  protected static IStrategoTerm const7;

  protected static IStrategoTerm constCharEsc2;

  protected static IStrategoTerm const6;

  protected static IStrategoTerm constCharEsc1;

  protected static IStrategoTerm const5;

  protected static IStrategoTerm constCharEsc0;

  protected static IStrategoTerm const4;

  protected static IStrategoTerm const3;

  protected static IStrategoTerm constCons1;

  protected static IStrategoTerm const2;

  protected static IStrategoTerm constSome0;

  protected static IStrategoTerm constExportlist0;

  protected static IStrategoTerm constCons0;

  protected static IStrategoTerm const1;

  protected static IStrategoTerm constNil0;

  protected static IStrategoTerm const0;

  public static IStrategoConstructor _consConc_2;

  public static IStrategoConstructor _consNone_0;

  public static IStrategoConstructor _consBinOp_1;

  public static IStrategoConstructor _consPrefOp_1;

  public static IStrategoConstructor _consString_1;

  public static IStrategoConstructor _consEscape_1;

  public static IStrategoConstructor _consCharEsc_1;

  public static IStrategoConstructor _consDecimalEsc_1;

  public static IStrategoConstructor _consOctalEsc_1;

  public static IStrategoConstructor _consHexadecimalEsc_1;

  public static IStrategoConstructor _consInt_1;

  public static IStrategoConstructor _consFloat_1;

  public static IStrategoConstructor _consModule_3;

  public static IStrategoConstructor _consExportlist_1;

  public static IStrategoConstructor _consDerive_1;

  public static IStrategoConstructor _consContext_1;

  public static IStrategoConstructor _consSContext_1;

  public static IStrategoConstructor _consVar_1;

  public static IStrategoConstructor _consVarFunLHS_2;

  public static IStrategoConstructor _consOpFunLHS_3;

  public static IStrategoConstructor _consNestedFunLHS_2;

  public static IStrategoConstructor _consCons_2;

  public static IStrategoConstructor _consNil_0;

  public static IStrategoConstructor _consSome_1;

  public static IStrategoConstructor _consProgram_1;

  public static Context init(Context context)
  { 
    synchronized(NormalizeAST.class)
    { 
      if(isIniting)
        return null;
      try
      { 
        isIniting = true;
        ITermFactory termFactory = context.getFactory();
        if(constantFactory == null)
        { 
          initConstructors(termFactory);
          initConstants(termFactory);
        }
        if(initedContext == null || initedContext.get() != context)
        { 
          org.strategoxt.stratego_lib.Main.init(context);
          context.registerComponent("normalize");
        }
        initedContext = new WeakReference<Context>(context);
        constantFactory = termFactory;
      }
      finally
      { 
        isIniting = false;
      }
      return context;
    }
  }

  public static Context init()
  { 
    return init(new Context());
  }

  public static void main(String args[])
  { 
    Context context = init();
    context.setStandAlone(true);
    try
    { 
      IStrategoTerm result;
      try
      { 
        result = context.invokeStrategyCLI(main_0_0.instance, "NormalizeAST", args);
      }
      finally
      { 
        context.getIOAgent().closeAllFiles();
      }
      if(result == null)
      { 
        System.err.println("NormalizeAST" + (TRACES_ENABLED ? ": rewriting failed, trace:" : ": rewriting failed"));
        context.printStackTrace();
        context.setStandAlone(false);
        System.exit(1);
      }
      else
      { 
        System.out.println(result);
        context.setStandAlone(false);
        System.exit(0);
      }
    }
    catch(StrategoExit exit)
    { 
      context.setStandAlone(false);
      System.exit(exit.getValue());
    }
  }

  public static IStrategoTerm mainNoExit(String ... args) throws StrategoExit
  { 
    return mainNoExit(new Context(), args);
  }

  public static IStrategoTerm mainNoExit(Context context, String ... args) throws StrategoExit
  { 
    try
    { 
      init(context);
      return context.invokeStrategyCLI(main_0_0.instance, "NormalizeAST", args);
    }
    finally
    { 
      context.getIOAgent().closeAllFiles();
    }
  }

  public static Strategy getMainStrategy()
  { 
    return main_0_0.instance;
  }

  public static void initConstructors(ITermFactory termFactory)
  { 
    _consConc_2 = termFactory.makeConstructor("Conc", 2);
    _consNone_0 = termFactory.makeConstructor("None", 0);
    _consBinOp_1 = termFactory.makeConstructor("BinOp", 1);
    _consPrefOp_1 = termFactory.makeConstructor("PrefOp", 1);
    _consString_1 = termFactory.makeConstructor("String", 1);
    _consEscape_1 = termFactory.makeConstructor("Escape", 1);
    _consCharEsc_1 = termFactory.makeConstructor("CharEsc", 1);
    _consDecimalEsc_1 = termFactory.makeConstructor("DecimalEsc", 1);
    _consOctalEsc_1 = termFactory.makeConstructor("OctalEsc", 1);
    _consHexadecimalEsc_1 = termFactory.makeConstructor("HexadecimalEsc", 1);
    _consInt_1 = termFactory.makeConstructor("Int", 1);
    _consFloat_1 = termFactory.makeConstructor("Float", 1);
    _consModule_3 = termFactory.makeConstructor("Module", 3);
    _consExportlist_1 = termFactory.makeConstructor("Exportlist", 1);
    _consDerive_1 = termFactory.makeConstructor("Derive", 1);
    _consContext_1 = termFactory.makeConstructor("Context", 1);
    _consSContext_1 = termFactory.makeConstructor("SContext", 1);
    _consVar_1 = termFactory.makeConstructor("Var", 1);
    _consVarFunLHS_2 = termFactory.makeConstructor("VarFunLHS", 2);
    _consOpFunLHS_3 = termFactory.makeConstructor("OpFunLHS", 3);
    _consNestedFunLHS_2 = termFactory.makeConstructor("NestedFunLHS", 2);
    _consCons_2 = termFactory.makeConstructor("Cons", 2);
    _consNil_0 = termFactory.makeConstructor("Nil", 0);
    _consSome_1 = termFactory.makeConstructor("Some", 1);
    _consProgram_1 = termFactory.makeConstructor("Program", 1);
  }

  public static void initConstants(ITermFactory termFactory)
  { 
    const0 = termFactory.makeString("Main");
    constNil0 = (IStrategoTerm)termFactory.makeList();
    const1 = termFactory.makeString("main");
    constCons0 = (IStrategoTerm)termFactory.makeListCons(normalize.const1, (IStrategoList)normalize.constNil0);
    constExportlist0 = termFactory.makeAppl(NormalizeAST._consExportlist_1, new IStrategoTerm[]{normalize.constCons0});
    constSome0 = termFactory.makeAppl(NormalizeAST._consSome_1, new IStrategoTerm[]{normalize.constExportlist0});
    const2 = termFactory.makeInt(48);
    constCons1 = (IStrategoTerm)termFactory.makeListCons(normalize.const2, (IStrategoList)normalize.constNil0);
    const3 = termFactory.makeString(".");
    const4 = termFactory.makeString("a");
    constCharEsc0 = termFactory.makeAppl(NormalizeAST._consCharEsc_1, new IStrategoTerm[]{normalize.const4});
    const5 = termFactory.makeString("b");
    constCharEsc1 = termFactory.makeAppl(NormalizeAST._consCharEsc_1, new IStrategoTerm[]{normalize.const5});
    const6 = termFactory.makeString("f");
    constCharEsc2 = termFactory.makeAppl(NormalizeAST._consCharEsc_1, new IStrategoTerm[]{normalize.const6});
    const7 = termFactory.makeString("n");
    constCharEsc3 = termFactory.makeAppl(NormalizeAST._consCharEsc_1, new IStrategoTerm[]{normalize.const7});
    const8 = termFactory.makeString("r");
    constCharEsc4 = termFactory.makeAppl(NormalizeAST._consCharEsc_1, new IStrategoTerm[]{normalize.const8});
    const9 = termFactory.makeString("t");
    constCharEsc5 = termFactory.makeAppl(NormalizeAST._consCharEsc_1, new IStrategoTerm[]{normalize.const9});
    const10 = termFactory.makeString("v");
    constCharEsc6 = termFactory.makeAppl(NormalizeAST._consCharEsc_1, new IStrategoTerm[]{normalize.const10});
    const11 = termFactory.makeString("'");
    const12 = termFactory.makeString(".0e");
    const13 = termFactory.makeString(".0E");
    const14 = termFactory.makeString(".0");
  }

  public static void registerInterop(org.spoofax.interpreter.core.IContext context, Context compiledContext)
  { 
    new InteropRegisterer().registerLazy(context, compiledContext, InteropRegisterer.class.getClassLoader());
  }
}