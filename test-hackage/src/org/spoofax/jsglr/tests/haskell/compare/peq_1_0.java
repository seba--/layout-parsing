package org.spoofax.jsglr.tests.haskell.compare;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") public class peq_1_0 extends Strategy 
{ 
  public static peq_1_0 instance = new peq_1_0();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term, Strategy t_9)
  { 
    ITermFactory termFactory = context.getFactory();
    context.push("peq_1_0");
    Fail22:
    { 
      IStrategoTerm r_9 = null;
      IStrategoTerm s_9 = null;
      IStrategoTerm u_9 = null;
      if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
        break Fail22;
      r_9 = term.getSubterm(0);
      s_9 = term.getSubterm(1);
      term = t_9.invoke(context, r_9);
      if(term == null)
        break Fail22;
      u_9 = term;
      term = t_9.invoke(context, s_9);
      if(term == null)
        break Fail22;
      term = termFactory.makeTuple(u_9, term);
      term = equal_0_0.instance.invoke(context, term);
      if(term == null)
        break Fail22;
      context.popOnSuccess();
      if(true)
        return term;
    }
    context.popOnFailure();
    return null;
  }
}