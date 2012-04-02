package org.spoofax.jsglr.tests.haskell.compare;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") public class compare_term_0_0 extends Strategy 
{ 
  public static compare_term_0_0 instance = new compare_term_0_0();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term)
  { 
    ITermFactory termFactory = context.getFactory();
    context.push("compare_term_0_0");
    Fail10:
    { 
      IStrategoTerm term8 = term;
      Success8:
      { 
        Fail11:
        { 
          IStrategoTerm p_9 = null;
          IStrategoTerm q_9 = null;
          if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
            break Fail11;
          IStrategoTerm arg10 = term.getSubterm(0);
          if(arg10.getTermType() != IStrategoTerm.APPL || CompareAST._consProgram_1 != ((IStrategoAppl)arg10).getConstructor())
            break Fail11;
          p_9 = arg10.getSubterm(0);
          IStrategoTerm arg11 = term.getSubterm(1);
          if(arg11.getTermType() != IStrategoTerm.APPL || CompareAST._consModule_3 != ((IStrategoAppl)arg11).getConstructor())
            break Fail11;
          IStrategoTerm arg12 = arg11.getSubterm(0);
          if(arg12.getTermType() != IStrategoTerm.STRING || !"Main".equals(((IStrategoString)arg12).stringValue()))
            break Fail11;
          IStrategoTerm arg13 = arg11.getSubterm(1);
          if(arg13.getTermType() != IStrategoTerm.APPL || CompareAST._consSome_1 != ((IStrategoAppl)arg13).getConstructor())
            break Fail11;
          IStrategoTerm arg14 = arg13.getSubterm(0);
          if(arg14.getTermType() != IStrategoTerm.APPL || CompareAST._consExportlist_1 != ((IStrategoAppl)arg14).getConstructor())
            break Fail11;
          IStrategoTerm arg15 = arg14.getSubterm(0);
          if(arg15.getTermType() != IStrategoTerm.LIST || ((IStrategoList)arg15).isEmpty())
            break Fail11;
          IStrategoTerm arg16 = ((IStrategoList)arg15).head();
          if(arg16.getTermType() != IStrategoTerm.STRING || !"main".equals(((IStrategoString)arg16).stringValue()))
            break Fail11;
          IStrategoTerm arg17 = ((IStrategoList)arg15).tail();
          if(arg17.getTermType() != IStrategoTerm.LIST || !((IStrategoList)arg17).isEmpty())
            break Fail11;
          q_9 = arg11.getSubterm(2);
          term = termFactory.makeTuple(p_9, q_9);
          term = compare_0_0.instance.invoke(context, term);
          if(term == null)
            break Fail11;
          if(true)
            break Success8;
        }
        term = term8;
        IStrategoTerm term9 = term;
        Success9:
        { 
          Fail12:
          { 
            IStrategoTerm j_9 = null;
            IStrategoTerm k_9 = null;
            IStrategoTerm l_9 = null;
            IStrategoTerm m_9 = null;
            IStrategoTerm n_9 = null;
            IStrategoTerm o_9 = null;
            if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
              break Fail12;
            IStrategoTerm arg18 = term.getSubterm(0);
            if(arg18.getTermType() != IStrategoTerm.APPL || CompareAST._consOpFunLHS_3 != ((IStrategoAppl)arg18).getConstructor())
              break Fail12;
            l_9 = arg18.getSubterm(0);
            IStrategoTerm arg19 = arg18.getSubterm(1);
            if(arg19.getTermType() != IStrategoTerm.APPL || CompareAST._consPrefOp_1 != ((IStrategoAppl)arg19).getConstructor())
              break Fail12;
            j_9 = arg19.getSubterm(0);
            n_9 = arg18.getSubterm(2);
            IStrategoTerm arg20 = term.getSubterm(1);
            if(arg20.getTermType() != IStrategoTerm.APPL || CompareAST._consVarFunLHS_2 != ((IStrategoAppl)arg20).getConstructor())
              break Fail12;
            IStrategoTerm arg21 = arg20.getSubterm(0);
            if(arg21.getTermType() != IStrategoTerm.APPL || CompareAST._consVar_1 != ((IStrategoAppl)arg21).getConstructor())
              break Fail12;
            k_9 = arg21.getSubterm(0);
            IStrategoTerm arg22 = arg20.getSubterm(1);
            if(arg22.getTermType() != IStrategoTerm.LIST || ((IStrategoList)arg22).isEmpty())
              break Fail12;
            m_9 = ((IStrategoList)arg22).head();
            IStrategoTerm arg23 = ((IStrategoList)arg22).tail();
            if(arg23.getTermType() != IStrategoTerm.LIST || ((IStrategoList)arg23).isEmpty())
              break Fail12;
            o_9 = ((IStrategoList)arg23).head();
            IStrategoTerm arg24 = ((IStrategoList)arg23).tail();
            if(arg24.getTermType() != IStrategoTerm.LIST || !((IStrategoList)arg24).isEmpty())
              break Fail12;
            term = (IStrategoTerm)termFactory.makeListCons(termFactory.makeTuple(j_9, k_9), termFactory.makeListCons(termFactory.makeTuple(l_9, m_9), termFactory.makeListCons(termFactory.makeTuple(n_9, o_9), (IStrategoList)compare.constNil0)));
            term = map_1_0.instance.invoke(context, term, compare_0_0.instance);
            if(term == null)
              break Fail12;
            term = concat_0_0.instance.invoke(context, term);
            if(term == null)
              break Fail12;
            if(true)
              break Success9;
          }
          term = term9;
          IStrategoTerm term10 = term;
          Success10:
          { 
            Fail13:
            { 
              IStrategoTerm d_9 = null;
              IStrategoTerm e_9 = null;
              IStrategoTerm f_9 = null;
              IStrategoTerm g_9 = null;
              IStrategoTerm h_9 = null;
              IStrategoTerm i_9 = null;
              if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                break Fail13;
              IStrategoTerm arg25 = term.getSubterm(0);
              if(arg25.getTermType() != IStrategoTerm.APPL || CompareAST._consVarFunLHS_2 != ((IStrategoAppl)arg25).getConstructor())
                break Fail13;
              IStrategoTerm arg26 = arg25.getSubterm(0);
              if(arg26.getTermType() != IStrategoTerm.APPL || CompareAST._consBinOp_1 != ((IStrategoAppl)arg26).getConstructor())
                break Fail13;
              d_9 = arg26.getSubterm(0);
              IStrategoTerm arg27 = arg25.getSubterm(1);
              if(arg27.getTermType() != IStrategoTerm.LIST || ((IStrategoList)arg27).isEmpty())
                break Fail13;
              f_9 = ((IStrategoList)arg27).head();
              IStrategoTerm arg28 = ((IStrategoList)arg27).tail();
              if(arg28.getTermType() != IStrategoTerm.LIST || ((IStrategoList)arg28).isEmpty())
                break Fail13;
              h_9 = ((IStrategoList)arg28).head();
              IStrategoTerm arg29 = ((IStrategoList)arg28).tail();
              if(arg29.getTermType() != IStrategoTerm.LIST || !((IStrategoList)arg29).isEmpty())
                break Fail13;
              IStrategoTerm arg30 = term.getSubterm(1);
              if(arg30.getTermType() != IStrategoTerm.APPL || CompareAST._consOpFunLHS_3 != ((IStrategoAppl)arg30).getConstructor())
                break Fail13;
              g_9 = arg30.getSubterm(0);
              e_9 = arg30.getSubterm(1);
              i_9 = arg30.getSubterm(2);
              term = (IStrategoTerm)termFactory.makeListCons(termFactory.makeTuple(d_9, e_9), termFactory.makeListCons(termFactory.makeTuple(f_9, g_9), termFactory.makeListCons(termFactory.makeTuple(h_9, i_9), (IStrategoList)compare.constNil0)));
              term = map_1_0.instance.invoke(context, term, compare_0_0.instance);
              if(term == null)
                break Fail13;
              term = concat_0_0.instance.invoke(context, term);
              if(term == null)
                break Fail13;
              if(true)
                break Success10;
            }
            term = term10;
            IStrategoTerm term11 = term;
            Success11:
            { 
              Fail14:
              { 
                IStrategoTerm u_8 = null;
                IStrategoTerm v_8 = null;
                IStrategoTerm w_8 = null;
                IStrategoTerm x_8 = null;
                IStrategoTerm y_8 = null;
                IStrategoTerm z_8 = null;
                IStrategoTerm a_9 = null;
                IStrategoTerm b_9 = null;
                if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                  break Fail14;
                IStrategoTerm arg31 = term.getSubterm(0);
                if(arg31.getTermType() != IStrategoTerm.APPL || CompareAST._consVarFunLHS_2 != ((IStrategoAppl)arg31).getConstructor())
                  break Fail14;
                IStrategoTerm arg32 = arg31.getSubterm(0);
                if(arg32.getTermType() != IStrategoTerm.APPL || CompareAST._consBinOp_1 != ((IStrategoAppl)arg32).getConstructor())
                  break Fail14;
                u_8 = arg32.getSubterm(0);
                IStrategoTerm arg33 = arg31.getSubterm(1);
                if(arg33.getTermType() != IStrategoTerm.LIST || ((IStrategoList)arg33).isEmpty())
                  break Fail14;
                w_8 = ((IStrategoList)arg33).head();
                IStrategoTerm arg34 = ((IStrategoList)arg33).tail();
                if(arg34.getTermType() != IStrategoTerm.LIST || ((IStrategoList)arg34).isEmpty())
                  break Fail14;
                y_8 = ((IStrategoList)arg34).head();
                a_9 = ((IStrategoList)arg34).tail();
                IStrategoTerm arg35 = term.getSubterm(1);
                if(arg35.getTermType() != IStrategoTerm.APPL || CompareAST._consNestedFunLHS_2 != ((IStrategoAppl)arg35).getConstructor())
                  break Fail14;
                IStrategoTerm arg36 = arg35.getSubterm(0);
                if(arg36.getTermType() != IStrategoTerm.APPL || CompareAST._consOpFunLHS_3 != ((IStrategoAppl)arg36).getConstructor())
                  break Fail14;
                x_8 = arg36.getSubterm(0);
                v_8 = arg36.getSubterm(1);
                z_8 = arg36.getSubterm(2);
                b_9 = arg35.getSubterm(1);
                term = a_9;
                IStrategoTerm term12 = term;
                Success12:
                { 
                  Fail15:
                  { 
                    if(term.getTermType() != IStrategoTerm.LIST || !((IStrategoList)term).isEmpty())
                      break Fail15;
                    { 
                      if(true)
                        break Fail14;
                      if(true)
                        break Success12;
                    }
                  }
                  term = term12;
                }
                term = b_9;
                IStrategoTerm term13 = term;
                Success13:
                { 
                  Fail16:
                  { 
                    if(term.getTermType() != IStrategoTerm.LIST || !((IStrategoList)term).isEmpty())
                      break Fail16;
                    { 
                      if(true)
                        break Fail14;
                      if(true)
                        break Success13;
                    }
                  }
                  term = term13;
                }
                term = (IStrategoTerm)termFactory.makeListCons(termFactory.makeTuple(u_8, v_8), termFactory.makeListCons(termFactory.makeTuple(w_8, x_8), termFactory.makeListCons(termFactory.makeTuple(y_8, z_8), termFactory.makeListCons(termFactory.makeTuple(a_9, b_9), (IStrategoList)compare.constNil0))));
                term = map_1_0.instance.invoke(context, term, compare_0_0.instance);
                if(term == null)
                  break Fail14;
                term = concat_0_0.instance.invoke(context, term);
                if(term == null)
                  break Fail14;
                if(true)
                  break Success11;
              }
              term = term11;
              IStrategoTerm term14 = term;
              Success14:
              { 
                Fail17:
                { 
                  IStrategoTerm s_8 = null;
                  IStrategoTerm t_8 = null;
                  if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                    break Fail17;
                  IStrategoTerm arg37 = term.getSubterm(0);
                  if(arg37.getTermType() != IStrategoTerm.APPL || CompareAST._consContext_1 != ((IStrategoAppl)arg37).getConstructor())
                    break Fail17;
                  s_8 = arg37.getSubterm(0);
                  IStrategoTerm arg38 = term.getSubterm(1);
                  if(arg38.getTermType() != IStrategoTerm.APPL || CompareAST._consContext_1 != ((IStrategoAppl)arg38).getConstructor())
                    break Fail17;
                  IStrategoTerm arg39 = arg38.getSubterm(0);
                  if(arg39.getTermType() != IStrategoTerm.LIST || ((IStrategoList)arg39).isEmpty())
                    break Fail17;
                  t_8 = ((IStrategoList)arg39).head();
                  IStrategoTerm arg40 = ((IStrategoList)arg39).tail();
                  if(arg40.getTermType() != IStrategoTerm.LIST || !((IStrategoList)arg40).isEmpty())
                    break Fail17;
                  term = termFactory.makeTuple(s_8, t_8);
                  term = compare_0_0.instance.invoke(context, term);
                  if(term == null)
                    break Fail17;
                  if(true)
                    break Success14;
                }
                term = term14;
                IStrategoTerm term15 = term;
                Success15:
                { 
                  Fail18:
                  { 
                    IStrategoTerm q_8 = null;
                    IStrategoTerm r_8 = null;
                    if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                      break Fail18;
                    IStrategoTerm arg41 = term.getSubterm(0);
                    if(arg41.getTermType() != IStrategoTerm.APPL || CompareAST._consSContext_1 != ((IStrategoAppl)arg41).getConstructor())
                      break Fail18;
                    q_8 = arg41.getSubterm(0);
                    IStrategoTerm arg42 = term.getSubterm(1);
                    if(arg42.getTermType() != IStrategoTerm.APPL || CompareAST._consSContext_1 != ((IStrategoAppl)arg42).getConstructor())
                      break Fail18;
                    IStrategoTerm arg43 = arg42.getSubterm(0);
                    if(arg43.getTermType() != IStrategoTerm.LIST || ((IStrategoList)arg43).isEmpty())
                      break Fail18;
                    r_8 = ((IStrategoList)arg43).head();
                    IStrategoTerm arg44 = ((IStrategoList)arg43).tail();
                    if(arg44.getTermType() != IStrategoTerm.LIST || !((IStrategoList)arg44).isEmpty())
                      break Fail18;
                    term = termFactory.makeTuple(q_8, r_8);
                    term = compare_0_0.instance.invoke(context, term);
                    if(term == null)
                      break Fail18;
                    if(true)
                      break Success15;
                  }
                  term = term15;
                  IStrategoTerm term16 = term;
                  Success16:
                  { 
                    Fail19:
                    { 
                      IStrategoTerm m_8 = null;
                      TermReference n_8 = new TermReference();
                      if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                        break Fail19;
                      IStrategoTerm arg45 = term.getSubterm(0);
                      if(arg45.getTermType() != IStrategoTerm.APPL || CompareAST._consHexadecimalEsc_1 != ((IStrategoAppl)arg45).getConstructor())
                        break Fail19;
                      if(n_8.value == null)
                        n_8.value = arg45.getSubterm(0);
                      else
                        if(n_8.value != arg45.getSubterm(0) && !n_8.value.match(arg45.getSubterm(0)))
                          break Fail19;
                      m_8 = term.getSubterm(1);
                      lifted9 lifted90 = new lifted9();
                      lifted90.n_8 = n_8;
                      term = $Decimal$Esc_1_0.instance.invoke(context, term, lifted90);
                      if(term == null)
                        break Fail19;
                      term = termFactory.makeTuple(term, m_8);
                      term = compare_0_0.instance.invoke(context, term);
                      if(term == null)
                        break Fail19;
                      if(true)
                        break Success16;
                    }
                    term = term16;
                    IStrategoTerm term17 = term;
                    Success17:
                    { 
                      Fail20:
                      { 
                        IStrategoTerm i_8 = null;
                        TermReference j_8 = new TermReference();
                        if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                          break Fail20;
                        IStrategoTerm arg46 = term.getSubterm(0);
                        if(arg46.getTermType() != IStrategoTerm.APPL || CompareAST._consOctalEsc_1 != ((IStrategoAppl)arg46).getConstructor())
                          break Fail20;
                        if(j_8.value == null)
                          j_8.value = arg46.getSubterm(0);
                        else
                          if(j_8.value != arg46.getSubterm(0) && !j_8.value.match(arg46.getSubterm(0)))
                            break Fail20;
                        i_8 = term.getSubterm(1);
                        lifted10 lifted100 = new lifted10();
                        lifted100.j_8 = j_8;
                        term = $Decimal$Esc_1_0.instance.invoke(context, term, lifted100);
                        if(term == null)
                          break Fail20;
                        term = termFactory.makeTuple(term, i_8);
                        term = compare_0_0.instance.invoke(context, term);
                        if(term == null)
                          break Fail20;
                        if(true)
                          break Success17;
                      }
                      term = term17;
                      IStrategoTerm term18 = term;
                      Success18:
                      { 
                        Fail21:
                        { 
                          IStrategoTerm d_8 = null;
                          IStrategoTerm e_8 = null;
                          if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                            break Fail21;
                          IStrategoTerm arg47 = term.getSubterm(0);
                          if(arg47.getTermType() != IStrategoTerm.APPL || CompareAST._consDecimalEsc_1 != ((IStrategoAppl)arg47).getConstructor())
                            break Fail21;
                          e_8 = arg47.getSubterm(0);
                          d_8 = term.getSubterm(1);
                          term = string_to_int_0_0.instance.invoke(context, e_8);
                          if(term == null)
                            break Fail21;
                          term = is_ascii_0_0.instance.invoke(context, term);
                          if(term == null)
                            break Fail21;
                          term = (IStrategoTerm)termFactory.makeListCons(term, (IStrategoList)compare.constNil0);
                          term = implode_string_0_0.instance.invoke(context, term);
                          if(term == null)
                            break Fail21;
                          term = termFactory.makeTuple(term, d_8);
                          term = compare_0_0.instance.invoke(context, term);
                          if(term == null)
                            break Fail21;
                          if(true)
                            break Success18;
                        }
                        term = term18;
                        IStrategoTerm b_8 = null;
                        IStrategoTerm c_8 = null;
                        if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                          break Fail10;
                        IStrategoTerm arg48 = term.getSubterm(0);
                        if(arg48.getTermType() != IStrategoTerm.APPL || CompareAST._consDerive_1 != ((IStrategoAppl)arg48).getConstructor())
                          break Fail10;
                        IStrategoTerm arg49 = arg48.getSubterm(0);
                        if(arg49.getTermType() != IStrategoTerm.LIST || ((IStrategoList)arg49).isEmpty())
                          break Fail10;
                        b_8 = ((IStrategoList)arg49).head();
                        IStrategoTerm arg50 = ((IStrategoList)arg49).tail();
                        if(arg50.getTermType() != IStrategoTerm.LIST || !((IStrategoList)arg50).isEmpty())
                          break Fail10;
                        IStrategoTerm arg51 = term.getSubterm(1);
                        if(arg51.getTermType() != IStrategoTerm.APPL || CompareAST._consDerive_1 != ((IStrategoAppl)arg51).getConstructor())
                          break Fail10;
                        c_8 = arg51.getSubterm(0);
                        term = termFactory.makeTuple(b_8, c_8);
                        term = compare_0_0.instance.invoke(context, term);
                        if(term == null)
                          break Fail10;
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