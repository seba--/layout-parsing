package NormalizeAST_str;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.imp.debug.stratego.runtime.trans.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") public class norm_0_0 extends Strategy 
{ 
  public static norm_0_0 instance = new norm_0_0();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term)
  { 
    ITermFactory termFactory = context.getFactory();
    context.push("norm_0_0");
    Fail2:
    { 
      IStrategoTerm term0 = term;
      IStrategoConstructor cons0 = term.getTermType() == IStrategoTerm.APPL ? ((IStrategoAppl)term).getConstructor() : null;
      Success0:
      { 
        if(cons0 == NormalizeAST_str._consProgram_1)
        { 
          Fail3:
          { 
            IStrategoTerm h_56390 = null;
            h_56390 = term.getSubterm(0);
            term = termFactory.makeAppl(NormalizeAST_str._consModule_3, new IStrategoTerm[]{NormalizeAST_str.const0, NormalizeAST_str.constSome0, h_56390});
            if(true)
              break Success0;
          }
          term = term0;
        }
        if(cons0 == NormalizeAST_str._consFloat_1)
        { 
          IStrategoTerm x_56389 = null;
          IStrategoTerm y_56389 = null;
          IStrategoTerm z_56389 = null;
          IStrategoTerm b_56390 = null;
          IStrategoTerm g_56390 = null;
          x_56389 = term.getSubterm(0);
          term = is_string_0_0.instance.invoke(context, x_56389);
          if(term == null)
            break Fail2;
          term = split_at_dot_0_0.instance.invoke(context, x_56389);
          if(term == null)
            break Fail2;
          if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
            break Fail2;
          b_56390 = term.getSubterm(0);
          y_56389 = term.getSubterm(1);
          term = explode_string_0_0.instance.invoke(context, y_56389);
          if(term == null)
            break Fail2;
          z_56389 = term;
          IStrategoTerm term1 = term;
          Success1:
          { 
            Fail4:
            { 
              if(term.getTermType() != IStrategoTerm.LIST || !((IStrategoList)term).isEmpty())
                break Fail4;
              { 
                if(true)
                  break Fail2;
                if(true)
                  break Success1;
              }
            }
            term = term1;
          }
          g_56390 = z_56389;
          term = drop_tailings_0_1.instance.invoke(context, g_56390, NormalizeAST_str.const2);
          if(term == null)
            break Fail2;
          term = implode_string_0_0.instance.invoke(context, term);
          if(term == null)
            break Fail2;
          term = termFactory.makeTuple(b_56390, NormalizeAST_str.const3, term);
          term = conc_strings_0_0.instance.invoke(context, term);
          if(term == null)
            break Fail2;
          term = termFactory.makeAppl(NormalizeAST_str._consFloat_1, new IStrategoTerm[]{term});
        }
        else
        { 
          break Fail2;
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