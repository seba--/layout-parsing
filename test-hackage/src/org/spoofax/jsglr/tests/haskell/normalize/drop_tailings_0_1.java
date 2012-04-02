package org.spoofax.jsglr.tests.haskell.normalize;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") public class drop_tailings_0_1 extends Strategy 
{ 
  public static drop_tailings_0_1 instance = new drop_tailings_0_1();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term, IStrategoTerm o_17)
  { 
    context.push("drop_tailings_0_1");
    Fail34:
    { 
      IStrategoTerm term32 = term;
      Success31:
      { 
        Fail35:
        { 
          IStrategoTerm n_9 = null;
          IStrategoTerm o_9 = null;
          n_9 = term;
          term = split_init_last_0_0.instance.invoke(context, n_9);
          if(term == null)
            break Fail35;
          if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
            break Fail35;
          o_9 = term.getSubterm(0);
          if(term.getSubterm(1) != o_17 && !o_17.match(term.getSubterm(1)))
            break Fail35;
          term = this.invoke(context, o_9, o_17);
          if(term == null)
            break Fail35;
          if(true)
            break Success31;
        }
        term = term32;
      }
      context.popOnSuccess();
      if(true)
        return term;
    }
    context.popOnFailure();
    return null;
  }
}