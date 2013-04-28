package CompareAST_str;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.imp.debug.stratego.runtime.trans.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") public class hs_string_to_float_0_0 extends Strategy 
{ 
  public static hs_string_to_float_0_0 instance = new hs_string_to_float_0_0();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term)
  { 
    context.push("hs_string_to_float_0_0");
    Fail30:
    { 
      IStrategoTerm term25 = term;
      Success25:
      { 
        Fail31:
        { 
          if(term.getTermType() != IStrategoTerm.STRING || !"".equals(((IStrategoString)term).stringValue()))
            break Fail31;
          { 
            if(true)
              break Fail30;
            if(true)
              break Success25;
          }
        }
        term = term25;
      }
      term = string_to_real_0_0.instance.invoke(context, term);
      if(term == null)
        break Fail30;
      context.popOnSuccess();
      if(true)
        return term;
    }
    context.popOnFailure();
    return null;
  }
}