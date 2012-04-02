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
    Fail23:
    { 
      IStrategoTerm term19 = term;
      Success19:
      { 
        Fail24:
        { 
          IStrategoTerm y_4065 = null;
          IStrategoTerm z_4065 = null;
          IStrategoTerm a_4066 = null;
          y_4065 = term;
          term = is_string_0_0.instance.invoke(context, y_4065);
          if(term == null)
            break Fail24;
          term = explode_string_0_0.instance.invoke(context, y_4065);
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
          z_4065 = ((IStrategoList)arg53).head();
          a_4066 = ((IStrategoList)arg53).tail();
          term = z_4065;
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
          term = hex_chars_to_int_0_0.instance.invoke(context, a_4066);
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
            IStrategoTerm t_4065 = null;
            IStrategoTerm u_4065 = null;
            IStrategoTerm v_4065 = null;
            t_4065 = term;
            term = is_string_0_0.instance.invoke(context, t_4065);
            if(term == null)
              break Fail26;
            term = explode_string_0_0.instance.invoke(context, t_4065);
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
            u_4065 = ((IStrategoList)arg55).head();
            v_4065 = ((IStrategoList)arg55).tail();
            term = u_4065;
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
            term = oct_chars_to_int_0_0.instance.invoke(context, v_4065);
            if(term == null)
              break Fail26;
            if(true)
              break Success21;
          }
          term = term21;
          IStrategoTerm q_4065 = null;
          q_4065 = term;
          term = is_string_0_0.instance.invoke(context, q_4065);
          if(term == null)
            break Fail23;
          term = string_to_int_0_0.instance.invoke(context, q_4065);
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