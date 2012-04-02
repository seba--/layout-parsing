package org.spoofax.jsglr.tests.haskell.compare;

import org.strategoxt.stratego_lib.*;
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
          IStrategoTerm a_8 = null;
          if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
            break Fail2;
          a_8 = term.getSubterm(0);
          if(term.getSubterm(1) != a_8 && !a_8.match(term.getSubterm(1)))
            break Fail2;
          term = compare.constNil0;
          if(true)
            break Success0;
        }
        term = term0;
        IStrategoTerm term1 = term;
        Success1:
        { 
          Fail3:
          { 
            IStrategoTerm y_7 = null;
            IStrategoTerm z_7 = null;
            if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
              break Fail3;
            y_7 = term.getSubterm(0);
            z_7 = term.getSubterm(1);
            term = termFactory.makeTuple(y_7, z_7);
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
              IStrategoTerm w_7 = null;
              IStrategoTerm x_7 = null;
              if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                break Fail4;
              x_7 = term.getSubterm(0);
              w_7 = term.getSubterm(1);
              term = termFactory.makeTuple(w_7, x_7);
              term = compare_term_0_0.instance.invoke(context, term);
              if(term == null)
                break Fail4;
              if(true)
                break Success2;
            }
            term = term2;
            IStrategoTerm term3 = term;
            Success3:
            { 
              Fail5:
              { 
                IStrategoTerm t_7 = null;
                IStrategoTerm u_7 = null;
                if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                  break Fail5;
                IStrategoTerm arg0 = term.getSubterm(0);
                if(arg0.getTermType() != IStrategoTerm.APPL || CompareAST._consInt_1 != ((IStrategoAppl)arg0).getConstructor())
                  break Fail5;
                t_7 = arg0.getSubterm(0);
                IStrategoTerm arg1 = term.getSubterm(1);
                if(arg1.getTermType() != IStrategoTerm.APPL || CompareAST._consInt_1 != ((IStrategoAppl)arg1).getConstructor())
                  break Fail5;
                u_7 = arg1.getSubterm(0);
                term = termFactory.makeTuple(t_7, u_7);
                term = peq_1_0.instance.invoke(context, term, hs_string_to_int_0_0.instance);
                if(term == null)
                  break Fail5;
                term = compare.constNil0;
                if(true)
                  break Success3;
              }
              term = term3;
              IStrategoTerm term4 = term;
              Success4:
              { 
                Fail6:
                { 
                  IStrategoTerm q_7 = null;
                  IStrategoTerm r_7 = null;
                  if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                    break Fail6;
                  IStrategoTerm arg2 = term.getSubterm(0);
                  if(arg2.getTermType() != IStrategoTerm.APPL || CompareAST._consFloat_1 != ((IStrategoAppl)arg2).getConstructor())
                    break Fail6;
                  q_7 = arg2.getSubterm(0);
                  IStrategoTerm arg3 = term.getSubterm(1);
                  if(arg3.getTermType() != IStrategoTerm.APPL || CompareAST._consFloat_1 != ((IStrategoAppl)arg3).getConstructor())
                    break Fail6;
                  r_7 = arg3.getSubterm(0);
                  term = termFactory.makeTuple(q_7, r_7);
                  term = peq_1_0.instance.invoke(context, term, hs_string_to_float_0_0.instance);
                  if(term == null)
                    break Fail6;
                  term = compare.constNil0;
                  if(true)
                    break Success4;
                }
                term = term4;
                IStrategoTerm term5 = term;
                Success5:
                { 
                  Fail7:
                  { 
                    IStrategoTerm n_7 = null;
                    IStrategoTerm o_7 = null;
                    if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                      break Fail7;
                    IStrategoTerm arg4 = term.getSubterm(0);
                    if(arg4.getTermType() != IStrategoTerm.APPL || CompareAST._consString_1 != ((IStrategoAppl)arg4).getConstructor())
                      break Fail7;
                    n_7 = arg4.getSubterm(0);
                    IStrategoTerm arg5 = term.getSubterm(1);
                    if(arg5.getTermType() != IStrategoTerm.APPL || CompareAST._consString_1 != ((IStrategoAppl)arg5).getConstructor())
                      break Fail7;
                    o_7 = arg5.getSubterm(0);
                    term = termFactory.makeTuple(n_7, o_7);
                    term = peq_1_0.instance.invoke(context, term, norm_string_0_0.instance);
                    if(term == null)
                      break Fail7;
                    term = compare.constNil0;
                    if(true)
                      break Success5;
                  }
                  term = term5;
                  IStrategoTerm term6 = term;
                  Success6:
                  { 
                    Fail8:
                    { 
                      IStrategoTerm k_7 = null;
                      IStrategoTerm l_7 = null;
                      if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                        break Fail8;
                      IStrategoTerm arg6 = term.getSubterm(0);
                      if(arg6.getTermType() != IStrategoTerm.APPL || CompareAST._consChar_1 != ((IStrategoAppl)arg6).getConstructor())
                        break Fail8;
                      k_7 = arg6.getSubterm(0);
                      IStrategoTerm arg7 = term.getSubterm(1);
                      if(arg7.getTermType() != IStrategoTerm.APPL || CompareAST._consChar_1 != ((IStrategoAppl)arg7).getConstructor())
                        break Fail8;
                      l_7 = arg7.getSubterm(0);
                      term = termFactory.makeTuple(k_7, l_7);
                      term = peq_1_0.instance.invoke(context, term, norm_char_0_0.instance);
                      if(term == null)
                        break Fail8;
                      term = compare.constNil0;
                      if(true)
                        break Success6;
                    }
                    term = term6;
                    IStrategoTerm term7 = term;
                    Success7:
                    { 
                      Fail9:
                      { 
                        IStrategoTerm h_7 = null;
                        IStrategoTerm i_7 = null;
                        if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                          break Fail9;
                        IStrategoTerm arg8 = term.getSubterm(0);
                        IStrategoTerm cons0 = context.invokePrimitive("SSL_get_constructor", term, NO_STRATEGIES, new IStrategoTerm[]{arg8});
                        h_7 = cons0;
                        IStrategoTerm args0 = context.invokePrimitive("SSL_get_arguments", term, NO_STRATEGIES, new IStrategoTerm[]{arg8});
                        i_7 = args0;
                        IStrategoTerm arg9 = term.getSubterm(1);
                        IStrategoTerm cons1 = context.invokePrimitive("SSL_get_constructor", term, NO_STRATEGIES, new IStrategoTerm[]{arg9});
                        if(cons1 != h_7 && !h_7.match(cons1))
                          break Fail9;
                        IStrategoTerm args1 = context.invokePrimitive("SSL_get_arguments", term, NO_STRATEGIES, new IStrategoTerm[]{arg9});
                        term = termFactory.makeTuple(i_7, args1);
                        term = zip_1_0.instance.invoke(context, term, compare_0_0.instance);
                        if(term == null)
                          break Fail9;
                        term = concat_0_0.instance.invoke(context, term);
                        if(term == null)
                          break Fail9;
                        if(true)
                          break Success7;
                      }
                      term = term7;
                      IStrategoTerm f_7 = null;
                      IStrategoTerm g_7 = null;
                      if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                        break Fail1;
                      f_7 = term.getSubterm(0);
                      g_7 = term.getSubterm(1);
                      term = (IStrategoTerm)termFactory.makeListCons(termFactory.makeTuple(f_7, g_7), (IStrategoList)compare.constNil0);
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