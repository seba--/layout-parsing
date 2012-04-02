package org.spoofax.jsglr.tests.haskell.compare;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") public class hs_string_to_int_0_0 extends Strategy 
{ 
  public static hs_string_to_int_0_0 instance = new hs_string_to_int_0_0();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term)
  { 
    context.push("hs_string_to_int_0_0");
    Fail23:
    { 
      IStrategoTerm term19 = term;
      Success19:
      { 
        Fail24:
        { 
          IStrategoTerm g_10 = null;
          IStrategoTerm h_10 = null;
          IStrategoTerm i_10 = null;
          g_10 = term;
          term = is_string_0_0.instance.invoke(context, g_10);
          if(term == null)
            break Fail24;
          term = explode_string_0_0.instance.invoke(context, g_10);
          if(term == null)
            break Fail24;
          if(term.getTermType() != IStrategoTerm.LIST || ((IStrategoList)term).isEmpty())
            break Fail24;
          IStrategoTerm arg52 = ((IStrategoList)term).head();
          if(arg52.getTermType() != IStrategoTerm.INT || 48 != ((IStrategoInt)arg52).intValue())
            break Fail24;
          IStrategoTerm arg53 = ((IStrategoList)term).tail();
          if(arg53.getTermType() != IStrategoTerm.LIST || ((IStrategoList)arg53).isEmpty())
            break Fail24;
          h_10 = ((IStrategoList)arg53).head();
          i_10 = ((IStrategoList)arg53).tail();
          term = h_10;
          IStrategoTerm term20 = term;
          Success20:
          { 
            Fail25:
            { 
              if(term.getTermType() != IStrategoTerm.INT || 120 != ((IStrategoInt)term).intValue())
                break Fail25;
              if(true)
                break Success20;
            }
            term = term20;
            if(term.getTermType() != IStrategoTerm.INT || 88 != ((IStrategoInt)term).intValue())
              break Fail24;
          }
          term = hex_chars_to_int_0_0.instance.invoke(context, i_10);
          if(term == null)
            break Fail24;
          if(true)
            break Success19;
        }
        term = term19;
        IStrategoTerm term21 = term;
        Success21:
        { 
          Fail26:
          { 
            IStrategoTerm b_10 = null;
            IStrategoTerm c_10 = null;
            IStrategoTerm d_10 = null;
            b_10 = term;
            term = is_string_0_0.instance.invoke(context, b_10);
            if(term == null)
              break Fail26;
            term = explode_string_0_0.instance.invoke(context, b_10);
            if(term == null)
              break Fail26;
            if(term.getTermType() != IStrategoTerm.LIST || ((IStrategoList)term).isEmpty())
              break Fail26;
            IStrategoTerm arg54 = ((IStrategoList)term).head();
            if(arg54.getTermType() != IStrategoTerm.INT || 48 != ((IStrategoInt)arg54).intValue())
              break Fail26;
            IStrategoTerm arg55 = ((IStrategoList)term).tail();
            if(arg55.getTermType() != IStrategoTerm.LIST || ((IStrategoList)arg55).isEmpty())
              break Fail26;
            c_10 = ((IStrategoList)arg55).head();
            d_10 = ((IStrategoList)arg55).tail();
            term = c_10;
            IStrategoTerm term22 = term;
            Success22:
            { 
              Fail27:
              { 
                if(term.getTermType() != IStrategoTerm.INT || 111 != ((IStrategoInt)term).intValue())
                  break Fail27;
                if(true)
                  break Success22;
              }
              term = term22;
              if(term.getTermType() != IStrategoTerm.INT || 79 != ((IStrategoInt)term).intValue())
                break Fail26;
            }
            term = oct_chars_to_int_0_0.instance.invoke(context, d_10);
            if(term == null)
              break Fail26;
            if(true)
              break Success21;
          }
          term = term21;
          IStrategoTerm y_9 = null;
          y_9 = term;
          term = is_string_0_0.instance.invoke(context, y_9);
          if(term == null)
            break Fail23;
          term = string_to_int_0_0.instance.invoke(context, y_9);
          if(term == null)
            break Fail23;
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