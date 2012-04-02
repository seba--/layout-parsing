package CompareAST_str;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.imp.debug.stratego.runtime.trans.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") final class lifted10 extends Strategy 
{ 
  TermReference b_4064;

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term)
  { 
    Fail116:
    { 
      if(b_4064.value == null)
        break Fail116;
      term = oct_string_to_int_0_0.instance.invoke(context, b_4064.value);
      if(term == null)
        break Fail116;
      term = int_to_string_0_0.instance.invoke(context, term);
      if(term == null)
        break Fail116;
      if(true)
        return term;
    }
    return null;
  }
}