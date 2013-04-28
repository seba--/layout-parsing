package CompareAST_str;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.imp.debug.stratego.runtime.trans.*;
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
    Fail25:
    { 
      IStrategoTerm term21 = term;
      Success21:
      { 
        Fail26:
        { 
          IStrategoTerm k_199 = null;
          IStrategoTerm l_199 = null;
          IStrategoTerm m_199 = null;
          k_199 = term;
          term = is_string_0_0.instance.invoke(context, k_199);
          if(term == null)
            break Fail26;
          term = explode_string_0_0.instance.invoke(context, k_199);
          if(term == null)
            break Fail26;
          if(term.getTermType() != IStrategoTerm.LIST || ((IStrategoList)term).isEmpty())
            break Fail26;
          IStrategoTerm arg55 = ((IStrategoList)term).head();
          if(arg55.getTermType() != IStrategoTerm.INT || 48 != ((IStrategoInt)arg55).intValue())
            break Fail26;
          IStrategoTerm arg56 = ((IStrategoList)term).tail();
          if(arg56.getTermType() != IStrategoTerm.LIST || ((IStrategoList)arg56).isEmpty())
            break Fail26;
          l_199 = ((IStrategoList)arg56).head();
          m_199 = ((IStrategoList)arg56).tail();
          term = l_199;
          IStrategoTerm term22 = term;
          Success22:
          { 
            Fail27:
            { 
              if(term.getTermType() != IStrategoTerm.INT || 120 != ((IStrategoInt)term).intValue())
                break Fail27;
              if(true)
                break Success22;
            }
            term = term22;
            if(term.getTermType() != IStrategoTerm.INT || 88 != ((IStrategoInt)term).intValue())
              break Fail26;
          }
          term = hex_chars_to_int_0_0.instance.invoke(context, m_199);
          if(term == null)
            break Fail26;
          if(true)
            break Success21;
        }
        term = term21;
        IStrategoTerm term23 = term;
        Success23:
        { 
          Fail28:
          { 
            IStrategoTerm d_199 = null;
            IStrategoTerm e_199 = null;
            IStrategoTerm h_199 = null;
            d_199 = term;
            term = is_string_0_0.instance.invoke(context, d_199);
            if(term == null)
              break Fail28;
            term = explode_string_0_0.instance.invoke(context, d_199);
            if(term == null)
              break Fail28;
            if(term.getTermType() != IStrategoTerm.LIST || ((IStrategoList)term).isEmpty())
              break Fail28;
            IStrategoTerm arg57 = ((IStrategoList)term).head();
            if(arg57.getTermType() != IStrategoTerm.INT || 48 != ((IStrategoInt)arg57).intValue())
              break Fail28;
            IStrategoTerm arg58 = ((IStrategoList)term).tail();
            if(arg58.getTermType() != IStrategoTerm.LIST || ((IStrategoList)arg58).isEmpty())
              break Fail28;
            e_199 = ((IStrategoList)arg58).head();
            h_199 = ((IStrategoList)arg58).tail();
            term = e_199;
            IStrategoTerm term24 = term;
            Success24:
            { 
              Fail29:
              { 
                if(term.getTermType() != IStrategoTerm.INT || 111 != ((IStrategoInt)term).intValue())
                  break Fail29;
                if(true)
                  break Success24;
              }
              term = term24;
              if(term.getTermType() != IStrategoTerm.INT || 79 != ((IStrategoInt)term).intValue())
                break Fail28;
            }
            term = oct_chars_to_int_0_0.instance.invoke(context, h_199);
            if(term == null)
              break Fail28;
            if(true)
              break Success23;
          }
          term = term23;
          IStrategoTerm a_199 = null;
          a_199 = term;
          term = is_string_0_0.instance.invoke(context, a_199);
          if(term == null)
            break Fail25;
          term = string_to_int_0_0.instance.invoke(context, a_199);
          if(term == null)
            break Fail25;
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