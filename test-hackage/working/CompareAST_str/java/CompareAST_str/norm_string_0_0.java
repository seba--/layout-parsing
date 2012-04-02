package CompareAST_str;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.imp.debug.stratego.runtime.trans.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") public class norm_string_0_0 extends Strategy 
{ 
  public static norm_string_0_0 instance = new norm_string_0_0();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term)
  { 
    ITermFactory termFactory = context.getFactory();
    context.push("norm_string_0_0");
    Fail107:
    { 
      IStrategoTerm term99 = term;
      Success99:
      { 
        Fail108:
        { 
          IStrategoTerm r_4066 = null;
          IStrategoTerm s_4066 = null;
          r_4066 = term;
          term = filter_1_0.instance.invoke(context, r_4066, lifted12.instance);
          if(term == null)
            break Fail108;
          s_4066 = term;
          term = termFactory.makeTuple(r_4066, term);
          IStrategoTerm term101 = term;
          Success100:
          { 
            Fail109:
            { 
              term = equal_0_0.instance.invoke(context, term);
              if(term == null)
                break Fail109;
              { 
                if(true)
                  break Fail108;
                if(true)
                  break Success100;
              }
            }
            term = term101;
          }
          term = this.invoke(context, s_4066);
          if(term == null)
            break Fail108;
          if(true)
            break Success99;
        }
        term = term99;
        term = map_1_0.instance.invoke(context, term, lifted13.instance);
        if(term == null)
          break Fail107;
      }
      context.popOnSuccess();
      if(true)
        return term;
    }
    context.popOnFailure();
    return null;
  }
}