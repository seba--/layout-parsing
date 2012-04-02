package org.spoofax.jsglr.tests.haskell.normalize;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") public class normalize_0_0 extends Strategy 
{ 
  public static normalize_0_0 instance = new normalize_0_0();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term)
  { 
    context.push("normalize_0_0");
    Fail1:
    { 
      term = bottomup_1_0.instance.invoke(context, term, lifted1.instance);
      if(term == null)
        break Fail1;
      context.popOnSuccess();
      if(true)
        return term;
    }
    context.popOnFailure();
    return null;
  }
}