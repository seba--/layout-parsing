package CompareAST_str;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.imp.debug.stratego.runtime.trans.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") final class lifted9 extends Strategy 
{ 
  TermReference f_4064;

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term)
  { 
    Fail117:
    { 
      if(f_4064.value == null)
        break Fail117;
      term = hex_string_to_int_0_0.instance.invoke(context, f_4064.value);
      if(term == null)
        break Fail117;
      term = int_to_string_0_0.instance.invoke(context, term);
      if(term == null)
        break Fail117;
      if(true)
        return term;
    }
    return null;
  }
}