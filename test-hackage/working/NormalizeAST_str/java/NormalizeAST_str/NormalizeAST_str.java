package NormalizeAST_str;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.imp.debug.stratego.runtime.trans.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") public class NormalizeAST_str  
{ 
  protected static final boolean TRACES_ENABLED = true;

  protected static ITermFactory constantFactory;

  private static WeakReference<Context> initedContext;

  private static boolean isIniting;

  protected static IStrategoTerm const3;

  protected static IStrategoTerm const2;

  protected static IStrategoTerm constSome0;

  protected static IStrategoTerm constExportlist0;

  protected static IStrategoTerm constCons0;

  protected static IStrategoTerm const1;

  protected static IStrategoTerm constNil0;

  protected static IStrategoTerm const0;

  public static IStrategoConstructor _consConc_2;

  public static IStrategoConstructor _consNone_0;

  protected static IStrategoConstructor _consSome_1;

  public static IStrategoConstructor _consModule_3;

  public static IStrategoConstructor _consExportlist_1;

  public static IStrategoConstructor _consFloat_1;

  public static IStrategoConstructor _consCons_2;

  public static IStrategoConstructor _consProgram_1;

  public static Context init(Context context)
  { 
    synchronized(NormalizeAST_str.class)
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
          context.registerComponent("NormalizeAST_str");
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
        result = context.invokeStrategyCLI(main_0_0.instance, "NormalizeAST_str", args);
      }
      finally
      { 
        context.getIOAgent().closeAllFiles();
      }
      if(result == null)
      { 
        System.err.println("NormalizeAST_str" + (TRACES_ENABLED ? ": rewriting failed, trace:" : ": rewriting failed"));
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
      return context.invokeStrategyCLI(main_0_0.instance, "NormalizeAST_str", args);
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
    _consSome_1 = termFactory.makeConstructor("Some", 1);
    _consModule_3 = termFactory.makeConstructor("Module", 3);
    _consExportlist_1 = termFactory.makeConstructor("Exportlist", 1);
    _consFloat_1 = termFactory.makeConstructor("Float", 1);
    _consCons_2 = termFactory.makeConstructor("Cons", 2);
    _consProgram_1 = termFactory.makeConstructor("Program", 1);
  }

  public static void initConstants(ITermFactory termFactory)
  { 
    const0 = termFactory.makeString("Main");
    constNil0 = (IStrategoTerm)termFactory.makeList();
    const1 = termFactory.makeString("main");
    constCons0 = (IStrategoTerm)termFactory.makeListCons(NormalizeAST_str.const1, (IStrategoList)NormalizeAST_str.constNil0);
    constExportlist0 = termFactory.makeAppl(NormalizeAST_str._consExportlist_1, new IStrategoTerm[]{NormalizeAST_str.constCons0});
    constSome0 = termFactory.makeAppl(NormalizeAST_str._consSome_1, new IStrategoTerm[]{NormalizeAST_str.constExportlist0});
    const2 = termFactory.makeInt(48);
    const3 = termFactory.makeString(".");
  }

  public static void registerInterop(org.spoofax.interpreter.core.IContext context, Context compiledContext)
  { 
    new InteropRegisterer().registerLazy(context, compiledContext, InteropRegisterer.class.getClassLoader());
  }
}