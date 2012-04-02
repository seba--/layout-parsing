package org.spoofax.jsglr.tests.haskell.compare;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") final class lifted10 extends Strategy 
{ 
  TermReference j_8;

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term)
  { 
    Fail116:
    { 
      if(j_8.value == null)
        break Fail116;
      term = oct_string_to_int_0_0.instance.invoke(context, j_8.value);
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