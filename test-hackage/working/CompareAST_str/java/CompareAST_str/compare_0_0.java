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
          IStrategoTerm o_196 = null;
          if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
            break Fail2;
          o_196 = term.getSubterm(0);
          if(term.getSubterm(1) != o_196 && !o_196.match(term.getSubterm(1)))
            break Fail2;
          term = CompareAST_str.constNil0;
          if(true)
            break Success0;
        }
        term = term0;
        IStrategoTerm term1 = term;
        Success1:
        { 
          Fail3:
          { 
            IStrategoTerm m_196 = null;
            IStrategoTerm n_196 = null;
            if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
              break Fail3;
            m_196 = term.getSubterm(0);
            n_196 = term.getSubterm(1);
            term = termFactory.makeTuple(m_196, n_196);
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
              IStrategoTerm k_196 = null;
              IStrategoTerm l_196 = null;
              if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                break Fail4;
              l_196 = term.getSubterm(0);
              k_196 = term.getSubterm(1);
              term = termFactory.makeTuple(k_196, l_196);
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
                IStrategoTerm h_196 = null;
                IStrategoTerm i_196 = null;
                if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                  break Fail5;
                IStrategoTerm arg0 = term.getSubterm(0);
                if(arg0.getTermType() != IStrategoTerm.APPL || CompareAST_str._consInt_1 != ((IStrategoAppl)arg0).getConstructor())
                  break Fail5;
                h_196 = arg0.getSubterm(0);
                IStrategoTerm arg1 = term.getSubterm(1);
                if(arg1.getTermType() != IStrategoTerm.APPL || CompareAST_str._consInt_1 != ((IStrategoAppl)arg1).getConstructor())
                  break Fail5;
                i_196 = arg1.getSubterm(0);
                term = termFactory.makeTuple(h_196, i_196);
                term = peq_1_0.instance.invoke(context, term, hs_string_to_int_0_0.instance);
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
                  IStrategoTerm e_196 = null;
                  IStrategoTerm f_196 = null;
                  if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                    break Fail6;
                  IStrategoTerm arg2 = term.getSubterm(0);
                  if(arg2.getTermType() != IStrategoTerm.APPL || CompareAST_str._consFloat_1 != ((IStrategoAppl)arg2).getConstructor())
                    break Fail6;
                  e_196 = arg2.getSubterm(0);
                  IStrategoTerm arg3 = term.getSubterm(1);
                  if(arg3.getTermType() != IStrategoTerm.APPL || CompareAST_str._consFloat_1 != ((IStrategoAppl)arg3).getConstructor())
                    break Fail6;
                  f_196 = arg3.getSubterm(0);
                  term = termFactory.makeTuple(e_196, f_196);
                  term = peq_1_0.instance.invoke(context, term, hs_string_to_float_0_0.instance);
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
                    IStrategoTerm b_196 = null;
                    IStrategoTerm c_196 = null;
                    if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                      break Fail7;
                    IStrategoTerm arg4 = term.getSubterm(0);
                    if(arg4.getTermType() != IStrategoTerm.APPL || CompareAST_str._consIntHash_1 != ((IStrategoAppl)arg4).getConstructor())
                      break Fail7;
                    b_196 = arg4.getSubterm(0);
                    IStrategoTerm arg5 = term.getSubterm(1);
                    if(arg5.getTermType() != IStrategoTerm.APPL || CompareAST_str._consIntHash_1 != ((IStrategoAppl)arg5).getConstructor())
                      break Fail7;
                    c_196 = arg5.getSubterm(0);
                    term = termFactory.makeTuple(b_196, c_196);
                    term = peq_1_0.instance.invoke(context, term, hs_string_to_int_0_0.instance);
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
                      IStrategoTerm y_195 = null;
                      IStrategoTerm z_195 = null;
                      if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                        break Fail8;
                      IStrategoTerm arg6 = term.getSubterm(0);
                      if(arg6.getTermType() != IStrategoTerm.APPL || CompareAST_str._consFloatHash_1 != ((IStrategoAppl)arg6).getConstructor())
                        break Fail8;
                      y_195 = arg6.getSubterm(0);
                      IStrategoTerm arg7 = term.getSubterm(1);
                      if(arg7.getTermType() != IStrategoTerm.APPL || CompareAST_str._consFloatHash_1 != ((IStrategoAppl)arg7).getConstructor())
                        break Fail8;
                      z_195 = arg7.getSubterm(0);
                      term = termFactory.makeTuple(y_195, z_195);
                      term = peq_1_0.instance.invoke(context, term, hs_string_to_float_0_0.instance);
                      if(term == null)
                        break Fail8;
                      term = CompareAST_str.constNil0;
                      if(true)
                        break Success6;
                    }
                    term = term6;
                    IStrategoTerm term7 = term;
                    Success7:
                    { 
                      Fail9:
                      { 
                        IStrategoTerm q_195 = null;
                        IStrategoTerm r_195 = null;
                        IStrategoTerm s_195 = null;
                        IStrategoTerm t_195 = null;
                        IStrategoTerm u_195 = null;
                        if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                          break Fail9;
                        IStrategoTerm arg8 = term.getSubterm(0);
                        if(arg8.getTermType() != IStrategoTerm.LIST || ((IStrategoList)arg8).isEmpty())
                          break Fail9;
                        s_195 = ((IStrategoList)arg8).head();
                        q_195 = ((IStrategoList)arg8).tail();
                        IStrategoTerm arg9 = term.getSubterm(1);
                        if(arg9.getTermType() != IStrategoTerm.LIST || ((IStrategoList)arg9).isEmpty())
                          break Fail9;
                        t_195 = ((IStrategoList)arg9).head();
                        r_195 = ((IStrategoList)arg9).tail();
                        term = termFactory.makeTuple(s_195, t_195);
                        term = this.invoke(context, term);
                        if(term == null)
                          break Fail9;
                        u_195 = term;
                        term = termFactory.makeTuple(q_195, r_195);
                        term = this.invoke(context, term);
                        if(term == null)
                          break Fail9;
                        term = termFactory.makeTuple(u_195, term);
                        term = conc_0_0.instance.invoke(context, term);
                        if(term == null)
                          break Fail9;
                        if(true)
                          break Success7;
                      }
                      term = term7;
                      IStrategoTerm term8 = term;
                      Success8:
                      { 
                        Fail10:
                        { 
                          IStrategoTerm n_195 = null;
                          IStrategoTerm o_195 = null;
                          if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                            break Fail10;
                          IStrategoTerm arg10 = term.getSubterm(0);
                          IStrategoTerm cons0 = context.invokePrimitive("SSL_get_constructor", term, NO_STRATEGIES, new IStrategoTerm[]{arg10});
                          n_195 = cons0;
                          IStrategoTerm args0 = context.invokePrimitive("SSL_get_arguments", term, NO_STRATEGIES, new IStrategoTerm[]{arg10});
                          o_195 = args0;
                          IStrategoTerm arg11 = term.getSubterm(1);
                          IStrategoTerm cons1 = context.invokePrimitive("SSL_get_constructor", term, NO_STRATEGIES, new IStrategoTerm[]{arg11});
                          if(cons1 != n_195 && !n_195.match(cons1))
                            break Fail10;
                          IStrategoTerm args1 = context.invokePrimitive("SSL_get_arguments", term, NO_STRATEGIES, new IStrategoTerm[]{arg11});
                          term = termFactory.makeTuple(o_195, args1);
                          term = this.invoke(context, term);
                          if(term == null)
                            break Fail10;
                          if(true)
                            break Success8;
                        }
                        term = term8;
                        IStrategoTerm l_195 = null;
                        IStrategoTerm m_195 = null;
                        if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                          break Fail1;
                        l_195 = term.getSubterm(0);
                        m_195 = term.getSubterm(1);
                        term = (IStrategoTerm)termFactory.makeListCons(termFactory.makeTuple(l_195, m_195), (IStrategoList)CompareAST_str.constNil0);
                      }
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