package NormalizeAST_str;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.imp.debug.stratego.runtime.trans.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") final class lifted11 extends Strategy 
{ 
  public static final lifted11 instance = new lifted11();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term)
  { 
    Fail140:
    { 
      term = repeat_1_0.instance.invoke(context, term, norm_char_char_0_0.instance);
      if(term == null)
        break Fail140;
      if(true)
        return term;
    }
    return null;
  }
}