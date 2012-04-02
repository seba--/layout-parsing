package CompareAST_str;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.imp.debug.stratego.runtime.trans.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") public class compare_0_0 extends Strategy 
{ 
  public static compare_0_0 instance = new compare_0_0();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term)
  { 
    ITermFactory termFactory = context.getFactory();
    context.push("compare_0_0");
    Fail1:
    { 
      IStrategoTerm term0 = term;
      Success0:
      { 
        Fail2:
        { 
          IStrategoTerm r_4063 = null;
          IStrategoTerm s_4063 = null;
          if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
            break Fail2;
          r_4063 = term.getSubterm(0);
          s_4063 = term.getSubterm(1);
          term = termFactory.makeTuple(r_4063, s_4063);
          term = compare_term_0_0.instance.invoke(context, term);
          if(term == null)
            break Fail2;
          if(true)
            break Success0;
        }
        term = term0;
        IStrategoTerm term1 = term;
        Success1:
        { 
          Fail3:
          { 
            IStrategoTerm p_4063 = null;
            IStrategoTerm q_4063 = null;
            if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
              break Fail3;
            q_4063 = term.getSubterm(0);
            p_4063 = term.getSubterm(1);
            term = termFactory.makeTuple(p_4063, q_4063);
            term = compare_term_0_0.instance.invoke(context, term);
            if(term == null)
              break Fail3;
            if(true)
              break Success1;
          }
          term = term1;
          IStrategoTerm term2 = term;
          Success2:
          { 
            Fail4:
            { 
              IStrategoTerm m_4063 = null;
              IStrategoTerm n_4063 = null;
              if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                break Fail4;
              IStrategoTerm arg0 = term.getSubterm(0);
              if(arg0.getTermType() != IStrategoTerm.APPL || CompareAST_str._consInt_1 != ((IStrategoAppl)arg0).getConstructor())
                break Fail4;
              m_4063 = arg0.getSubterm(0);
              IStrategoTerm arg1 = term.getSubterm(1);
              if(arg1.getTermType() != IStrategoTerm.APPL || CompareAST_str._consInt_1 != ((IStrategoAppl)arg1).getConstructor())
                break Fail4;
              n_4063 = arg1.getSubterm(0);
              term = termFactory.makeTuple(m_4063, n_4063);
              term = peq_1_0.instance.invoke(context, term, hs_string_to_int_0_0.instance);
              if(term == null)
                break Fail4;
              term = CompareAST_str.constNil0;
              if(true)
                break Success2;
            }
            term = term2;
            IStrategoTerm term3 = term;
            Success3:
            { 
              Fail5:
              { 
                IStrategoTerm j_4063 = null;
                IStrategoTerm k_4063 = null;
                if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                  break Fail5;
                IStrategoTerm arg2 = term.getSubterm(0);
                if(arg2.getTermType() != IStrategoTerm.APPL || CompareAST_str._consFloat_1 != ((IStrategoAppl)arg2).getConstructor())
                  break Fail5;
                j_4063 = arg2.getSubterm(0);
                IStrategoTerm arg3 = term.getSubterm(1);
                if(arg3.getTermType() != IStrategoTerm.APPL || CompareAST_str._consFloat_1 != ((IStrategoAppl)arg3).getConstructor())
                  break Fail5;
                k_4063 = arg3.getSubterm(0);
                term = termFactory.makeTuple(j_4063, k_4063);
                term = peq_1_0.instance.invoke(context, term, hs_string_to_float_0_0.instance);
                if(term == null)
                  break Fail5;
                term = CompareAST_str.constNil0;
                if(true)
                  break Success3;
              }
              term = term3;
              IStrategoTerm term4 = term;
              Success4:
              { 
                Fail6:
                { 
                  IStrategoTerm g_4063 = null;
                  IStrategoTerm h_4063 = null;
                  if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                    break Fail6;
                  IStrategoTerm arg4 = term.getSubterm(0);
                  if(arg4.getTermType() != IStrategoTerm.APPL || CompareAST_str._consString_1 != ((IStrategoAppl)arg4).getConstructor())
                    break Fail6;
                  g_4063 = arg4.getSubterm(0);
                  IStrategoTerm arg5 = term.getSubterm(1);
                  if(arg5.getTermType() != IStrategoTerm.APPL || CompareAST_str._consString_1 != ((IStrategoAppl)arg5).getConstructor())
                    break Fail6;
                  h_4063 = arg5.getSubterm(0);
                  term = termFactory.makeTuple(g_4063, h_4063);
                  term = peq_1_0.instance.invoke(context, term, norm_string_0_0.instance);
                  if(term == null)
                    break Fail6;
                  term = CompareAST_str.constNil0;
                  if(true)
                    break Success4;
                }
                term = term4;
                IStrategoTerm term5 = term;
                Success5:
                { 
                  Fail7:
                  { 
                    IStrategoTerm d_4063 = null;
                    IStrategoTerm e_4063 = null;
                    if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                      break Fail7;
                    IStrategoTerm arg6 = term.getSubterm(0);
                    if(arg6.getTermType() != IStrategoTerm.APPL || CompareAST_str._consChar_1 != ((IStrategoAppl)arg6).getConstructor())
                      break Fail7;
                    d_4063 = arg6.getSubterm(0);
                    IStrategoTerm arg7 = term.getSubterm(1);
                    if(arg7.getTermType() != IStrategoTerm.APPL || CompareAST_str._consChar_1 != ((IStrategoAppl)arg7).getConstructor())
                      break Fail7;
                    e_4063 = arg7.getSubterm(0);
                    term = termFactory.makeTuple(d_4063, e_4063);
                    term = peq_1_0.instance.invoke(context, term, norm_char_0_0.instance);
                    if(term == null)
                      break Fail7;
                    term = CompareAST_str.constNil0;
                    if(true)
                      break Success5;
                  }
                  term = term5;
                  IStrategoTerm term6 = term;
                  Success6:
                  { 
                    Fail8:
                    { 
                      IStrategoTerm a_4063 = null;
                      IStrategoTerm b_4063 = null;
                      if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                        break Fail8;
                      IStrategoTerm arg8 = term.getSubterm(0);
                      IStrategoTerm cons0 = context.invokePrimitive("SSL_get_constructor", term, NO_STRATEGIES, new IStrategoTerm[]{arg8});
                      a_4063 = cons0;
                      IStrategoTerm args0 = context.invokePrimitive("SSL_get_arguments", term, NO_STRATEGIES, new IStrategoTerm[]{arg8});
                      b_4063 = args0;
                      IStrategoTerm arg9 = term.getSubterm(1);
                      IStrategoTerm cons1 = context.invokePrimitive("SSL_get_constructor", term, NO_STRATEGIES, new IStrategoTerm[]{arg9});
                      if(cons1 != a_4063 && !a_4063.match(cons1))
                        break Fail8;
                      IStrategoTerm args1 = context.invokePrimitive("SSL_get_arguments", term, NO_STRATEGIES, new IStrategoTerm[]{arg9});
                      term = termFactory.makeTuple(b_4063, args1);
                      term = zip_1_0.instance.invoke(context, term, compare_0_0.instance);
                      if(term == null)
                        break Fail8;
                      term = concat_0_0.instance.invoke(context, term);
                      if(term == null)
                        break Fail8;
                      if(true)
                        break Success6;
                    }
                    term = term6;
                    IStrategoTerm term7 = term;
                    Success7:
                    { 
                      Fail9:
                      { 
                        IStrategoTerm z_4062 = null;
                        if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                          break Fail9;
                        z_4062 = term.getSubterm(0);
                        if(term.getSubterm(1) != z_4062 && !z_4062.match(term.getSubterm(1)))
                          break Fail9;
                        term = CompareAST_str.constNil0;
                        if(true)
                          break Success7;
                      }
                      term = term7;
                      IStrategoTerm x_4062 = null;
                      IStrategoTerm y_4062 = null;
                      if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                        break Fail1;
                      x_4062 = term.getSubterm(0);
                      y_4062 = term.getSubterm(1);
                      term = (IStrategoTerm)termFactory.makeListCons(termFactory.makeTuple(x_4062, y_4062), (IStrategoList)CompareAST_str.constNil0);
                    }
                  }
                }
              }
            }
          }
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