package CompareAST_str;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.imp.debug.stratego.runtime.trans.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") public class CompareAST_str  
{ 
  protected static final boolean TRACES_ENABLED = true;

  protected static ITermFactory constantFactory;

  private static WeakReference<Context> initedContext;

  private static boolean isIniting;

  protected static IStrategoTerm constNil0;

  public static IStrategoConstructor _consConc_2;

  public static IStrategoConstructor _consNone_0;

  public static IStrategoConstructor _consIntHash_1;

  public static IStrategoConstructor _consFloatHash_1;

  public static IStrategoConstructor _consBinOp_1;

  public static IStrategoConstructor _consPrefOp_1;

  public static IStrategoConstructor _consEscape_1;

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
    synchronized(CompareAST_str.class)
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
          org.strategoxt.imp.debug.stratego.runtime.trans.Main.init(context);
          context.registerComponent("CompareAST_str");
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
        result = context.invokeStrategyCLI(main_0_0.instance, "CompareAST_str", args);
      }
      finally
      { 
        context.getIOAgent().closeAllFiles();
      }
      if(result == null)
      { 
        System.err.println("CompareAST_str" + (TRACES_ENABLED ? ": rewriting failed, trace:" : ": rewriting failed"));
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
      return context.invokeStrategyCLI(main_0_0.instance, "CompareAST_str", args);
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
    _consIntHash_1 = termFactory.makeConstructor("IntHash", 1);
    _consFloatHash_1 = termFactory.makeConstructor("FloatHash", 1);
    _consBinOp_1 = termFactory.makeConstructor("BinOp", 1);
    _consPrefOp_1 = termFactory.makeConstructor("PrefOp", 1);
    _consEscape_1 = termFactory.makeConstructor("Escape", 1);
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
    constNil0 = (IStrategoTerm)termFactory.makeList();
  }

  public static void registerInterop(org.spoofax.interpreter.core.IContext context, Context compiledContext)
  { 
    new InteropRegisterer().registerLazy(context, compiledContext, InteropRegisterer.class.getClassLoader());
  }
}