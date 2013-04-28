package CompareAST_str;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.imp.debug.stratego.runtime.trans.*;
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
    Fail11:
    { 
      IStrategoTerm term9 = term;
      Success9:
      { 
        Fail12:
        { 
          IStrategoTerm q_198 = null;
          IStrategoTerm r_198 = null;
          if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
            break Fail12;
          IStrategoTerm arg12 = term.getSubterm(0);
          if(arg12.getTermType() != IStrategoTerm.APPL || CompareAST_str._consProgram_1 != ((IStrategoAppl)arg12).getConstructor())
            break Fail12;
          q_198 = arg12.getSubterm(0);
          IStrategoTerm arg13 = term.getSubterm(1);
          if(arg13.getTermType() != IStrategoTerm.APPL || CompareAST_str._consModule_3 != ((IStrategoAppl)arg13).getConstructor())
            break Fail12;
          IStrategoTerm arg14 = arg13.getSubterm(0);
          if(arg14.getTermType() != IStrategoTerm.STRING || !"Main".equals(((IStrategoString)arg14).stringValue()))
            break Fail12;
          IStrategoTerm arg15 = arg13.getSubterm(1);
          if(arg15.getTermType() != IStrategoTerm.APPL || CompareAST_str._consSome_1 != ((IStrategoAppl)arg15).getConstructor())
            break Fail12;
          IStrategoTerm arg16 = arg15.getSubterm(0);
          if(arg16.getTermType() != IStrategoTerm.APPL || CompareAST_str._consExportlist_1 != ((IStrategoAppl)arg16).getConstructor())
            break Fail12;
          IStrategoTerm arg17 = arg16.getSubterm(0);
          if(arg17.getTermType() != IStrategoTerm.LIST || ((IStrategoList)arg17).isEmpty())
            break Fail12;
          IStrategoTerm arg18 = ((IStrategoList)arg17).head();
          if(arg18.getTermType() != IStrategoTerm.STRING || !"main".equals(((IStrategoString)arg18).stringValue()))
            break Fail12;
          IStrategoTerm arg19 = ((IStrategoList)arg17).tail();
          if(arg19.getTermType() != IStrategoTerm.LIST || !((IStrategoList)arg19).isEmpty())
            break Fail12;
          r_198 = arg13.getSubterm(2);
          term = termFactory.makeTuple(q_198, r_198);
          term = compare_0_0.instance.invoke(context, term);
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
            IStrategoTerm k_198 = null;
            IStrategoTerm l_198 = null;
            IStrategoTerm m_198 = null;
            IStrategoTerm n_198 = null;
            IStrategoTerm o_198 = null;
            IStrategoTerm p_198 = null;
            if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
              break Fail13;
            IStrategoTerm arg20 = term.getSubterm(0);
            if(arg20.getTermType() != IStrategoTerm.APPL || CompareAST_str._consOpFunLHS_3 != ((IStrategoAppl)arg20).getConstructor())
              break Fail13;
            m_198 = arg20.getSubterm(0);
            IStrategoTerm arg21 = arg20.getSubterm(1);
            if(arg21.getTermType() != IStrategoTerm.APPL || CompareAST_str._consPrefOp_1 != ((IStrategoAppl)arg21).getConstructor())
              break Fail13;
            k_198 = arg21.getSubterm(0);
            o_198 = arg20.getSubterm(2);
            IStrategoTerm arg22 = term.getSubterm(1);
            if(arg22.getTermType() != IStrategoTerm.APPL || CompareAST_str._consVarFunLHS_2 != ((IStrategoAppl)arg22).getConstructor())
              break Fail13;
            IStrategoTerm arg23 = arg22.getSubterm(0);
            if(arg23.getTermType() != IStrategoTerm.APPL || CompareAST_str._consVar_1 != ((IStrategoAppl)arg23).getConstructor())
              break Fail13;
            l_198 = arg23.getSubterm(0);
            IStrategoTerm arg24 = arg22.getSubterm(1);
            if(arg24.getTermType() != IStrategoTerm.LIST || ((IStrategoList)arg24).isEmpty())
              break Fail13;
            n_198 = ((IStrategoList)arg24).head();
            IStrategoTerm arg25 = ((IStrategoList)arg24).tail();
            if(arg25.getTermType() != IStrategoTerm.LIST || ((IStrategoList)arg25).isEmpty())
              break Fail13;
            p_198 = ((IStrategoList)arg25).head();
            IStrategoTerm arg26 = ((IStrategoList)arg25).tail();
            if(arg26.getTermType() != IStrategoTerm.LIST || !((IStrategoList)arg26).isEmpty())
              break Fail13;
            term = (IStrategoTerm)termFactory.makeListCons(termFactory.makeTuple(k_198, l_198), termFactory.makeListCons(termFactory.makeTuple(m_198, n_198), termFactory.makeListCons(termFactory.makeTuple(o_198, p_198), (IStrategoList)CompareAST_str.constNil0)));
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
              IStrategoTerm d_198 = null;
              IStrategoTerm e_198 = null;
              IStrategoTerm g_198 = null;
              IStrategoTerm h_198 = null;
              IStrategoTerm i_198 = null;
              IStrategoTerm j_198 = null;
              if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                break Fail14;
              IStrategoTerm arg27 = term.getSubterm(0);
              if(arg27.getTermType() != IStrategoTerm.APPL || CompareAST_str._consVarFunLHS_2 != ((IStrategoAppl)arg27).getConstructor())
                break Fail14;
              IStrategoTerm arg28 = arg27.getSubterm(0);
              if(arg28.getTermType() != IStrategoTerm.APPL || CompareAST_str._consBinOp_1 != ((IStrategoAppl)arg28).getConstructor())
                break Fail14;
              d_198 = arg28.getSubterm(0);
              IStrategoTerm arg29 = arg27.getSubterm(1);
              if(arg29.getTermType() != IStrategoTerm.LIST || ((IStrategoList)arg29).isEmpty())
                break Fail14;
              g_198 = ((IStrategoList)arg29).head();
              IStrategoTerm arg30 = ((IStrategoList)arg29).tail();
              if(arg30.getTermType() != IStrategoTerm.LIST || ((IStrategoList)arg30).isEmpty())
                break Fail14;
              i_198 = ((IStrategoList)arg30).head();
              IStrategoTerm arg31 = ((IStrategoList)arg30).tail();
              if(arg31.getTermType() != IStrategoTerm.LIST || !((IStrategoList)arg31).isEmpty())
                break Fail14;
              IStrategoTerm arg32 = term.getSubterm(1);
              if(arg32.getTermType() != IStrategoTerm.APPL || CompareAST_str._consOpFunLHS_3 != ((IStrategoAppl)arg32).getConstructor())
                break Fail14;
              h_198 = arg32.getSubterm(0);
              e_198 = arg32.getSubterm(1);
              j_198 = arg32.getSubterm(2);
              term = (IStrategoTerm)termFactory.makeListCons(termFactory.makeTuple(d_198, e_198), termFactory.makeListCons(termFactory.makeTuple(g_198, h_198), termFactory.makeListCons(termFactory.makeTuple(i_198, j_198), (IStrategoList)CompareAST_str.constNil0)));
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
            IStrategoTerm term12 = term;
            Success12:
            { 
              Fail15:
              { 
                IStrategoTerm u_197 = null;
                IStrategoTerm v_197 = null;
                IStrategoTerm w_197 = null;
                IStrategoTerm x_197 = null;
                IStrategoTerm y_197 = null;
                IStrategoTerm z_197 = null;
                IStrategoTerm a_198 = null;
                IStrategoTerm b_198 = null;
                if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                  break Fail15;
                IStrategoTerm arg33 = term.getSubterm(0);
                if(arg33.getTermType() != IStrategoTerm.APPL || CompareAST_str._consVarFunLHS_2 != ((IStrategoAppl)arg33).getConstructor())
                  break Fail15;
                IStrategoTerm arg34 = arg33.getSubterm(0);
                if(arg34.getTermType() != IStrategoTerm.APPL || CompareAST_str._consBinOp_1 != ((IStrategoAppl)arg34).getConstructor())
                  break Fail15;
                u_197 = arg34.getSubterm(0);
                IStrategoTerm arg35 = arg33.getSubterm(1);
                if(arg35.getTermType() != IStrategoTerm.LIST || ((IStrategoList)arg35).isEmpty())
                  break Fail15;
                w_197 = ((IStrategoList)arg35).head();
                IStrategoTerm arg36 = ((IStrategoList)arg35).tail();
                if(arg36.getTermType() != IStrategoTerm.LIST || ((IStrategoList)arg36).isEmpty())
                  break Fail15;
                y_197 = ((IStrategoList)arg36).head();
                a_198 = ((IStrategoList)arg36).tail();
                IStrategoTerm arg37 = term.getSubterm(1);
                if(arg37.getTermType() != IStrategoTerm.APPL || CompareAST_str._consNestedFunLHS_2 != ((IStrategoAppl)arg37).getConstructor())
                  break Fail15;
                IStrategoTerm arg38 = arg37.getSubterm(0);
                if(arg38.getTermType() != IStrategoTerm.APPL || CompareAST_str._consOpFunLHS_3 != ((IStrategoAppl)arg38).getConstructor())
                  break Fail15;
                x_197 = arg38.getSubterm(0);
                v_197 = arg38.getSubterm(1);
                z_197 = arg38.getSubterm(2);
                b_198 = arg37.getSubterm(1);
                term = a_198;
                IStrategoTerm term13 = term;
                Success13:
                { 
                  Fail16:
                  { 
                    if(term.getTermType() != IStrategoTerm.LIST || !((IStrategoList)term).isEmpty())
                      break Fail16;
                    { 
                      if(true)
                        break Fail15;
                      if(true)
                        break Success13;
                    }
                  }
                  term = term13;
                }
                term = b_198;
                IStrategoTerm term14 = term;
                Success14:
                { 
                  Fail17:
                  { 
                    if(term.getTermType() != IStrategoTerm.LIST || !((IStrategoList)term).isEmpty())
                      break Fail17;
                    { 
                      if(true)
                        break Fail15;
                      if(true)
                        break Success14;
                    }
                  }
                  term = term14;
                }
                term = (IStrategoTerm)termFactory.makeListCons(termFactory.makeTuple(u_197, v_197), termFactory.makeListCons(termFactory.makeTuple(w_197, x_197), termFactory.makeListCons(termFactory.makeTuple(y_197, z_197), termFactory.makeListCons(termFactory.makeTuple(a_198, b_198), (IStrategoList)CompareAST_str.constNil0))));
                term = map_1_0.instance.invoke(context, term, compare_0_0.instance);
                if(term == null)
                  break Fail15;
                term = concat_0_0.instance.invoke(context, term);
                if(term == null)
                  break Fail15;
                if(true)
                  break Success12;
              }
              term = term12;
              IStrategoTerm term15 = term;
              Success15:
              { 
                Fail18:
                { 
                  IStrategoTerm s_197 = null;
                  IStrategoTerm t_197 = null;
                  if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                    break Fail18;
                  IStrategoTerm arg39 = term.getSubterm(0);
                  if(arg39.getTermType() != IStrategoTerm.APPL || CompareAST_str._consContext_1 != ((IStrategoAppl)arg39).getConstructor())
                    break Fail18;
                  s_197 = arg39.getSubterm(0);
                  IStrategoTerm arg40 = term.getSubterm(1);
                  if(arg40.getTermType() != IStrategoTerm.APPL || CompareAST_str._consContext_1 != ((IStrategoAppl)arg40).getConstructor())
                    break Fail18;
                  IStrategoTerm arg41 = arg40.getSubterm(0);
                  if(arg41.getTermType() != IStrategoTerm.LIST || ((IStrategoList)arg41).isEmpty())
                    break Fail18;
                  t_197 = ((IStrategoList)arg41).head();
                  IStrategoTerm arg42 = ((IStrategoList)arg41).tail();
                  if(arg42.getTermType() != IStrategoTerm.LIST || !((IStrategoList)arg42).isEmpty())
                    break Fail18;
                  term = termFactory.makeTuple(s_197, t_197);
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
                    IStrategoTerm q_197 = null;
                    IStrategoTerm r_197 = null;
                    if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                      break Fail19;
                    IStrategoTerm arg43 = term.getSubterm(0);
                    if(arg43.getTermType() != IStrategoTerm.APPL || CompareAST_str._consSContext_1 != ((IStrategoAppl)arg43).getConstructor())
                      break Fail19;
                    q_197 = arg43.getSubterm(0);
                    IStrategoTerm arg44 = term.getSubterm(1);
                    if(arg44.getTermType() != IStrategoTerm.APPL || CompareAST_str._consSContext_1 != ((IStrategoAppl)arg44).getConstructor())
                      break Fail19;
                    IStrategoTerm arg45 = arg44.getSubterm(0);
                    if(arg45.getTermType() != IStrategoTerm.LIST || ((IStrategoList)arg45).isEmpty())
                      break Fail19;
                    r_197 = ((IStrategoList)arg45).head();
                    IStrategoTerm arg46 = ((IStrategoList)arg45).tail();
                    if(arg46.getTermType() != IStrategoTerm.LIST || !((IStrategoList)arg46).isEmpty())
                      break Fail19;
                    term = termFactory.makeTuple(q_197, r_197);
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
                      IStrategoTerm n_197 = null;
                      IStrategoTerm o_197 = null;
                      if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                        break Fail20;
                      IStrategoTerm arg47 = term.getSubterm(0);
                      if(arg47.getTermType() != IStrategoTerm.APPL || CompareAST_str._consEscape_1 != ((IStrategoAppl)arg47).getConstructor())
                        break Fail20;
                      n_197 = arg47.getSubterm(0);
                      o_197 = term.getSubterm(1);
                      term = is_string_0_0.instance.invoke(context, o_197);
                      if(term == null)
                        break Fail20;
                      term = termFactory.makeTuple(n_197, o_197);
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
                        IStrategoTerm e_197 = null;
                        IStrategoTerm i_197 = null;
                        if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                          break Fail21;
                        IStrategoTerm arg48 = term.getSubterm(0);
                        if(arg48.getTermType() != IStrategoTerm.APPL || CompareAST_str._consHexadecimalEsc_1 != ((IStrategoAppl)arg48).getConstructor())
                          break Fail21;
                        i_197 = arg48.getSubterm(0);
                        e_197 = term.getSubterm(1);
                        term = hex_string_to_int_0_0.instance.invoke(context, i_197);
                        if(term == null)
                          break Fail21;
                        term = int_to_string_0_0.instance.invoke(context, term);
                        if(term == null)
                          break Fail21;
                        term = termFactory.makeAppl(CompareAST_str._consDecimalEsc_1, new IStrategoTerm[]{term});
                        term = termFactory.makeTuple(term, e_197);
                        term = compare_0_0.instance.invoke(context, term);
                        if(term == null)
                          break Fail21;
                        if(true)
                          break Success18;
                      }
                      term = term18;
                      IStrategoTerm term19 = term;
                      Success19:
                      { 
                        Fail22:
                        { 
                          IStrategoTerm y_196 = null;
                          IStrategoTerm z_196 = null;
                          if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                            break Fail22;
                          IStrategoTerm arg49 = term.getSubterm(0);
                          if(arg49.getTermType() != IStrategoTerm.APPL || CompareAST_str._consOctalEsc_1 != ((IStrategoAppl)arg49).getConstructor())
                            break Fail22;
                          z_196 = arg49.getSubterm(0);
                          y_196 = term.getSubterm(1);
                          term = oct_string_to_int_0_0.instance.invoke(context, z_196);
                          if(term == null)
                            break Fail22;
                          term = int_to_string_0_0.instance.invoke(context, term);
                          if(term == null)
                            break Fail22;
                          term = termFactory.makeAppl(CompareAST_str._consDecimalEsc_1, new IStrategoTerm[]{term});
                          term = termFactory.makeTuple(term, y_196);
                          term = compare_0_0.instance.invoke(context, term);
                          if(term == null)
                            break Fail22;
                          if(true)
                            break Success19;
                        }
                        term = term19;
                        IStrategoTerm term20 = term;
                        Success20:
                        { 
                          Fail23:
                          { 
                            IStrategoTerm r_196 = null;
                            IStrategoTerm s_196 = null;
                            if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                              break Fail23;
                            IStrategoTerm arg50 = term.getSubterm(0);
                            if(arg50.getTermType() != IStrategoTerm.APPL || CompareAST_str._consDecimalEsc_1 != ((IStrategoAppl)arg50).getConstructor())
                              break Fail23;
                            s_196 = arg50.getSubterm(0);
                            r_196 = term.getSubterm(1);
                            term = string_to_int_0_0.instance.invoke(context, s_196);
                            if(term == null)
                              break Fail23;
                            term = is_ascii_0_0.instance.invoke(context, term);
                            if(term == null)
                              break Fail23;
                            term = (IStrategoTerm)termFactory.makeListCons(term, (IStrategoList)CompareAST_str.constNil0);
                            term = implode_string_0_0.instance.invoke(context, term);
                            if(term == null)
                              break Fail23;
                            term = termFactory.makeTuple(term, r_196);
                            term = compare_0_0.instance.invoke(context, term);
                            if(term == null)
                              break Fail23;
                            if(true)
                              break Success20;
                          }
                          term = term20;
                          IStrategoTerm p_196 = null;
                          IStrategoTerm q_196 = null;
                          if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                            break Fail11;
                          IStrategoTerm arg51 = term.getSubterm(0);
                          if(arg51.getTermType() != IStrategoTerm.APPL || CompareAST_str._consDerive_1 != ((IStrategoAppl)arg51).getConstructor())
                            break Fail11;
                          IStrategoTerm arg52 = arg51.getSubterm(0);
                          if(arg52.getTermType() != IStrategoTerm.LIST || ((IStrategoList)arg52).isEmpty())
                            break Fail11;
                          p_196 = ((IStrategoList)arg52).head();
                          IStrategoTerm arg53 = ((IStrategoList)arg52).tail();
                          if(arg53.getTermType() != IStrategoTerm.LIST || !((IStrategoList)arg53).isEmpty())
                            break Fail11;
                          IStrategoTerm arg54 = term.getSubterm(1);
                          if(arg54.getTermType() != IStrategoTerm.APPL || CompareAST_str._consDerive_1 != ((IStrategoAppl)arg54).getConstructor())
                            break Fail11;
                          q_196 = arg54.getSubterm(0);
                          term = termFactory.makeTuple(p_196, q_196);
                          term = compare_0_0.instance.invoke(context, term);
                          if(term == null)
                            break Fail11;
                        }
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