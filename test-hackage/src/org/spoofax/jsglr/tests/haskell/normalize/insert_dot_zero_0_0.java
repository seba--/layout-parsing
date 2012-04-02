package org.spoofax.jsglr.tests.haskell.normalize;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") public class insert_dot_zero_0_0 extends Strategy 
{ 
  public static insert_dot_zero_0_0 instance = new insert_dot_zero_0_0();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term)
  { 
    ITermFactory termFactory = context.getFactory();
    context.push("insert_dot_zero_0_0");
    Fail36:
    { 
      IStrategoTerm term33 = term;
      Success32:
      { 
        Fail37:
        { 
          IStrategoTerm c_10 = null;
          IStrategoTerm d_10 = null;
          IStrategoTerm e_10 = null;
          c_10 = term;
          term = split_fetch_1_0.instance.invoke(context, c_10, lifted7.instance);
          if(term == null)
            break Fail37;
          if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
            break Fail37;
          d_10 = term.getSubterm(0);
          e_10 = term.getSubterm(1);
          term = explode_string_0_0.instance.invoke(context, normalize.const12);
          if(term == null)
            break Fail37;
          term = termFactory.makeTuple(d_10, term, e_10);
          term = conc_0_0.instance.invoke(context, term);
          if(term == null)
            break Fail37;
          if(true)
            break Success32;
        }
        term = term33;
        IStrategoTerm term34 = term;
        Success33:
        { 
          Fail38:
          { 
            IStrategoTerm v_9 = null;
            IStrategoTerm w_9 = null;
            IStrategoTerm x_9 = null;
            v_9 = term;
            term = split_fetch_1_0.instance.invoke(context, v_9, lifted8.instance);
            if(term == null)
              break Fail38;
            if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
              break Fail38;
            w_9 = term.getSubterm(0);
            x_9 = term.getSubterm(1);
            term = explode_string_0_0.instance.invoke(context, normalize.const13);
            if(term == null)
              break Fail38;
            term = termFactory.makeTuple(w_9, term, x_9);
            term = conc_0_0.instance.invoke(context, term);
            if(term == null)
              break Fail38;
            if(true)
              break Success33;
          }
          term = term34;
          IStrategoTerm q_9 = null;
          q_9 = term;
          term = explode_string_0_0.instance.invoke(context, normalize.const14);
          if(term == null)
            break Fail36;
          term = termFactory.makeTuple(q_9, term);
          term = conc_0_0.instance.invoke(context, term);
          if(term == null)
            break Fail36;
        }
      }
      context.popOnSuccess();
      if(true)
        return term;
    }
    context.popOnFailure();
    return null;
  }
}