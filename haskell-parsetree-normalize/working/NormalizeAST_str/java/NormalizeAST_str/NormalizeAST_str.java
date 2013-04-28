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

  protected static IStrategoTerm const35;

  protected static IStrategoTerm constASCIIEsc25;

  protected static IStrategoTerm const34;

  protected static IStrategoTerm constASCIIEsc24;

  protected static IStrategoTerm const33;

  protected static IStrategoTerm constASCIIEsc23;

  protected static IStrategoTerm const32;

  protected static IStrategoTerm constASCIIEsc22;

  protected static IStrategoTerm const31;

  protected static IStrategoTerm constASCIIEsc21;

  protected static IStrategoTerm const30;

  protected static IStrategoTerm constASCIIEsc20;

  protected static IStrategoTerm const29;

  protected static IStrategoTerm constASCIIEsc19;

  protected static IStrategoTerm const28;

  protected static IStrategoTerm constASCIIEsc18;

  protected static IStrategoTerm const27;

  protected static IStrategoTerm constASCIIEsc17;

  protected static IStrategoTerm const26;

  protected static IStrategoTerm constASCIIEsc16;

  protected static IStrategoTerm const25;

  protected static IStrategoTerm constASCIIEsc15;

  protected static IStrategoTerm const24;

  protected static IStrategoTerm constASCIIEsc14;

  protected static IStrategoTerm const23;

  protected static IStrategoTerm constASCIIEsc13;

  protected static IStrategoTerm const22;

  protected static IStrategoTerm constASCIIEsc12;

  protected static IStrategoTerm const21;

  protected static IStrategoTerm constASCIIEsc11;

  protected static IStrategoTerm const20;

  protected static IStrategoTerm constASCIIEsc10;

  protected static IStrategoTerm const19;

  protected static IStrategoTerm constASCIIEsc9;

  protected static IStrategoTerm const18;

  protected static IStrategoTerm constASCIIEsc8;

  protected static IStrategoTerm const17;

  protected static IStrategoTerm constASCIIEsc7;

  protected static IStrategoTerm const16;

  protected static IStrategoTerm constCharEsc6;

  protected static IStrategoTerm const15;

  protected static IStrategoTerm constCharEsc5;

  protected static IStrategoTerm const14;

  protected static IStrategoTerm constCharEsc4;

  protected static IStrategoTerm const13;

  protected static IStrategoTerm constCharEsc3;

  protected static IStrategoTerm const12;

  protected static IStrategoTerm constCharEsc2;

  protected static IStrategoTerm const11;

  protected static IStrategoTerm constCharEsc1;

  protected static IStrategoTerm const10;

  protected static IStrategoTerm constCharEsc0;

  protected static IStrategoTerm const9;

  protected static IStrategoTerm constASCIIEsc6;

  protected static IStrategoTerm const8;

  protected static IStrategoTerm constASCIIEsc5;

  protected static IStrategoTerm const7;

  protected static IStrategoTerm constASCIIEsc4;

  protected static IStrategoTerm const6;

  protected static IStrategoTerm constASCIIEsc3;

  protected static IStrategoTerm const5;

  protected static IStrategoTerm constASCIIEsc2;

  protected static IStrategoTerm const4;

  protected static IStrategoTerm constASCIIEsc1;

  protected static IStrategoTerm const3;

  protected static IStrategoTerm constASCIIEsc0;

  protected static IStrategoTerm const2;

  protected static IStrategoTerm const1;

  protected static IStrategoTerm const0;

  protected static IStrategoTerm constNil1;

  public static IStrategoConstructor _consConc_2;

  public static IStrategoConstructor _consNone_0;

  public static IStrategoConstructor _consSome_1;

  public static IStrategoConstructor _consCharHash_1;

  public static IStrategoConstructor _consStringHash_1;

  public static IStrategoConstructor _consChar_1;

  public static IStrategoConstructor _consString_1;

  public static IStrategoConstructor _consEscape_1;

  public static IStrategoConstructor _consCharEsc_1;

  public static IStrategoConstructor _consASCIIEsc_1;

  public static IStrategoConstructor _consDecimalEsc_1;

  public static IStrategoConstructor _consOctalEsc_1;

  public static IStrategoConstructor _consHexadecimalEsc_1;

  public static IStrategoConstructor _consCons_2;

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
    _consCharHash_1 = termFactory.makeConstructor("CharHash", 1);
    _consStringHash_1 = termFactory.makeConstructor("StringHash", 1);
    _consChar_1 = termFactory.makeConstructor("Char", 1);
    _consString_1 = termFactory.makeConstructor("String", 1);
    _consEscape_1 = termFactory.makeConstructor("Escape", 1);
    _consCharEsc_1 = termFactory.makeConstructor("CharEsc", 1);
    _consASCIIEsc_1 = termFactory.makeConstructor("ASCIIEsc", 1);
    _consDecimalEsc_1 = termFactory.makeConstructor("DecimalEsc", 1);
    _consOctalEsc_1 = termFactory.makeConstructor("OctalEsc", 1);
    _consHexadecimalEsc_1 = termFactory.makeConstructor("HexadecimalEsc", 1);
    _consCons_2 = termFactory.makeConstructor("Cons", 2);
  }

  public static void initConstants(ITermFactory termFactory)
  { 
    constNil1 = (IStrategoTerm)termFactory.makeList();
    const0 = termFactory.makeString("\"");
    const1 = termFactory.makeString(" ");
    const2 = termFactory.makeString("NUL");
    constASCIIEsc0 = termFactory.makeAppl(NormalizeAST_str._consASCIIEsc_1, new IStrategoTerm[]{NormalizeAST_str.const2});
    const3 = termFactory.makeString("SOH");
    constASCIIEsc1 = termFactory.makeAppl(NormalizeAST_str._consASCIIEsc_1, new IStrategoTerm[]{NormalizeAST_str.const3});
    const4 = termFactory.makeString("STX");
    constASCIIEsc2 = termFactory.makeAppl(NormalizeAST_str._consASCIIEsc_1, new IStrategoTerm[]{NormalizeAST_str.const4});
    const5 = termFactory.makeString("ETX");
    constASCIIEsc3 = termFactory.makeAppl(NormalizeAST_str._consASCIIEsc_1, new IStrategoTerm[]{NormalizeAST_str.const5});
    const6 = termFactory.makeString("EOT");
    constASCIIEsc4 = termFactory.makeAppl(NormalizeAST_str._consASCIIEsc_1, new IStrategoTerm[]{NormalizeAST_str.const6});
    const7 = termFactory.makeString("ENQ");
    constASCIIEsc5 = termFactory.makeAppl(NormalizeAST_str._consASCIIEsc_1, new IStrategoTerm[]{NormalizeAST_str.const7});
    const8 = termFactory.makeString("ACK");
    constASCIIEsc6 = termFactory.makeAppl(NormalizeAST_str._consASCIIEsc_1, new IStrategoTerm[]{NormalizeAST_str.const8});
    const9 = termFactory.makeString("a");
    constCharEsc0 = termFactory.makeAppl(NormalizeAST_str._consCharEsc_1, new IStrategoTerm[]{NormalizeAST_str.const9});
    const10 = termFactory.makeString("b");
    constCharEsc1 = termFactory.makeAppl(NormalizeAST_str._consCharEsc_1, new IStrategoTerm[]{NormalizeAST_str.const10});
    const11 = termFactory.makeString("t");
    constCharEsc2 = termFactory.makeAppl(NormalizeAST_str._consCharEsc_1, new IStrategoTerm[]{NormalizeAST_str.const11});
    const12 = termFactory.makeString("n");
    constCharEsc3 = termFactory.makeAppl(NormalizeAST_str._consCharEsc_1, new IStrategoTerm[]{NormalizeAST_str.const12});
    const13 = termFactory.makeString("v");
    constCharEsc4 = termFactory.makeAppl(NormalizeAST_str._consCharEsc_1, new IStrategoTerm[]{NormalizeAST_str.const13});
    const14 = termFactory.makeString("f");
    constCharEsc5 = termFactory.makeAppl(NormalizeAST_str._consCharEsc_1, new IStrategoTerm[]{NormalizeAST_str.const14});
    const15 = termFactory.makeString("r");
    constCharEsc6 = termFactory.makeAppl(NormalizeAST_str._consCharEsc_1, new IStrategoTerm[]{NormalizeAST_str.const15});
    const16 = termFactory.makeString("SO");
    constASCIIEsc7 = termFactory.makeAppl(NormalizeAST_str._consASCIIEsc_1, new IStrategoTerm[]{NormalizeAST_str.const16});
    const17 = termFactory.makeString("SI");
    constASCIIEsc8 = termFactory.makeAppl(NormalizeAST_str._consASCIIEsc_1, new IStrategoTerm[]{NormalizeAST_str.const17});
    const18 = termFactory.makeString("DLE");
    constASCIIEsc9 = termFactory.makeAppl(NormalizeAST_str._consASCIIEsc_1, new IStrategoTerm[]{NormalizeAST_str.const18});
    const19 = termFactory.makeString("DC1");
    constASCIIEsc10 = termFactory.makeAppl(NormalizeAST_str._consASCIIEsc_1, new IStrategoTerm[]{NormalizeAST_str.const19});
    const20 = termFactory.makeString("DC2");
    constASCIIEsc11 = termFactory.makeAppl(NormalizeAST_str._consASCIIEsc_1, new IStrategoTerm[]{NormalizeAST_str.const20});
    const21 = termFactory.makeString("DC3");
    constASCIIEsc12 = termFactory.makeAppl(NormalizeAST_str._consASCIIEsc_1, new IStrategoTerm[]{NormalizeAST_str.const21});
    const22 = termFactory.makeString("DC4");
    constASCIIEsc13 = termFactory.makeAppl(NormalizeAST_str._consASCIIEsc_1, new IStrategoTerm[]{NormalizeAST_str.const22});
    const23 = termFactory.makeString("NAK");
    constASCIIEsc14 = termFactory.makeAppl(NormalizeAST_str._consASCIIEsc_1, new IStrategoTerm[]{NormalizeAST_str.const23});
    const24 = termFactory.makeString("SYM");
    constASCIIEsc15 = termFactory.makeAppl(NormalizeAST_str._consASCIIEsc_1, new IStrategoTerm[]{NormalizeAST_str.const24});
    const25 = termFactory.makeString("ETB");
    constASCIIEsc16 = termFactory.makeAppl(NormalizeAST_str._consASCIIEsc_1, new IStrategoTerm[]{NormalizeAST_str.const25});
    const26 = termFactory.makeString("CAN");
    constASCIIEsc17 = termFactory.makeAppl(NormalizeAST_str._consASCIIEsc_1, new IStrategoTerm[]{NormalizeAST_str.const26});
    const27 = termFactory.makeString("EM");
    constASCIIEsc18 = termFactory.makeAppl(NormalizeAST_str._consASCIIEsc_1, new IStrategoTerm[]{NormalizeAST_str.const27});
    const28 = termFactory.makeString("SUB");
    constASCIIEsc19 = termFactory.makeAppl(NormalizeAST_str._consASCIIEsc_1, new IStrategoTerm[]{NormalizeAST_str.const28});
    const29 = termFactory.makeString("ESC");
    constASCIIEsc20 = termFactory.makeAppl(NormalizeAST_str._consASCIIEsc_1, new IStrategoTerm[]{NormalizeAST_str.const29});
    const30 = termFactory.makeString("FS");
    constASCIIEsc21 = termFactory.makeAppl(NormalizeAST_str._consASCIIEsc_1, new IStrategoTerm[]{NormalizeAST_str.const30});
    const31 = termFactory.makeString("GS");
    constASCIIEsc22 = termFactory.makeAppl(NormalizeAST_str._consASCIIEsc_1, new IStrategoTerm[]{NormalizeAST_str.const31});
    const32 = termFactory.makeString("RS");
    constASCIIEsc23 = termFactory.makeAppl(NormalizeAST_str._consASCIIEsc_1, new IStrategoTerm[]{NormalizeAST_str.const32});
    const33 = termFactory.makeString("US");
    constASCIIEsc24 = termFactory.makeAppl(NormalizeAST_str._consASCIIEsc_1, new IStrategoTerm[]{NormalizeAST_str.const33});
    const34 = termFactory.makeString("DEL");
    constASCIIEsc25 = termFactory.makeAppl(NormalizeAST_str._consASCIIEsc_1, new IStrategoTerm[]{NormalizeAST_str.const34});
    const35 = termFactory.makeString("'");
  }

  public static void registerInterop(org.spoofax.interpreter.core.IContext context, Context compiledContext)
  { 
    new InteropRegisterer().registerLazy(context, compiledContext, InteropRegisterer.class.getClassLoader());
  }
}