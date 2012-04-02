package org.spoofax.jsglr.tests.haskell.compare;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") final class lifted9 extends Strategy 
{ 
  TermReference n_8;

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term)
  { 
    Fail117:
    { 
      if(n_8.value == null)
        break Fail117;
      term = hex_string_to_int_0_0.instance.invoke(context, n_8.value);
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