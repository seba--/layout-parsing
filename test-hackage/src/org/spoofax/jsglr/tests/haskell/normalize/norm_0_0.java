package org.spoofax.jsglr.tests.haskell.normalize;

import org.strategoxt.stratego_lib.*;
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
        if(cons0 == NormalizeAST._consProgram_1)
        { 
          Fail3:
          { 
            IStrategoTerm j_9 = null;
            j_9 = term.getSubterm(0);
            term = termFactory.makeAppl(NormalizeAST._consModule_3, new IStrategoTerm[]{normalize.const0, normalize.constSome0, j_9});
            if(true)
              break Success0;
          }
          term = term0;
        }
        Success1:
        { 
          if(cons0 == NormalizeAST._consInt_1)
          { 
            Fail4:
            { 
              IStrategoTerm c_9 = null;
              IStrategoTerm d_9 = null;
              IStrategoTerm e_9 = null;
              IStrategoTerm f_9 = null;
              c_9 = term.getSubterm(0);
              term = is_string_0_0.instance.invoke(context, c_9);
              if(term == null)
                break Fail4;
              term = explode_string_0_0.instance.invoke(context, c_9);
              if(term == null)
                break Fail4;
              if(term.getTermType() != IStrategoTerm.LIST || ((IStrategoList)term).isEmpty())
                break Fail4;
              IStrategoTerm arg0 = ((IStrategoList)term).head();
              if(arg0.getTermType() != IStrategoTerm.INT || 48 != ((IStrategoInt)arg0).intValue())
                break Fail4;
              IStrategoTerm arg1 = ((IStrategoList)term).tail();
              if(arg1.getTermType() != IStrategoTerm.LIST || ((IStrategoList)arg1).isEmpty())
                break Fail4;
              d_9 = ((IStrategoList)arg1).head();
              e_9 = ((IStrategoList)arg1).tail();
              term = d_9;
              IStrategoTerm term2 = term;
              Success2:
              { 
                Fail5:
                { 
                  if(term.getTermType() != IStrategoTerm.INT || 120 != ((IStrategoInt)term).intValue())
                    break Fail5;
                  if(true)
                    break Success2;
                }
                term = term2;
                if(term.getTermType() != IStrategoTerm.INT || 88 != ((IStrategoInt)term).intValue())
                  break Fail4;
              }
              term = hex_chars_to_int_0_0.instance.invoke(context, e_9);
              if(term == null)
                break Fail4;
              f_9 = term;
              term = int_to_string_0_0.instance.invoke(context, f_9);
              if(term == null)
                break Fail4;
              term = termFactory.makeAppl(NormalizeAST._consInt_1, new IStrategoTerm[]{term});
              if(true)
                break Success1;
            }
            term = term0;
          }
          Success3:
          { 
            if(cons0 == NormalizeAST._consInt_1)
            { 
              Fail6:
              { 
                IStrategoTerm v_8 = null;
                IStrategoTerm w_8 = null;
                IStrategoTerm x_8 = null;
                IStrategoTerm y_8 = null;
                v_8 = term.getSubterm(0);
                term = is_string_0_0.instance.invoke(context, v_8);
                if(term == null)
                  break Fail6;
                term = explode_string_0_0.instance.invoke(context, v_8);
                if(term == null)
                  break Fail6;
                if(term.getTermType() != IStrategoTerm.LIST || ((IStrategoList)term).isEmpty())
                  break Fail6;
                IStrategoTerm arg2 = ((IStrategoList)term).head();
                if(arg2.getTermType() != IStrategoTerm.INT || 48 != ((IStrategoInt)arg2).intValue())
                  break Fail6;
                IStrategoTerm arg3 = ((IStrategoList)term).tail();
                if(arg3.getTermType() != IStrategoTerm.LIST || ((IStrategoList)arg3).isEmpty())
                  break Fail6;
                w_8 = ((IStrategoList)arg3).head();
                x_8 = ((IStrategoList)arg3).tail();
                term = w_8;
                IStrategoTerm term4 = term;
                Success4:
                { 
                  Fail7:
                  { 
                    if(term.getTermType() != IStrategoTerm.INT || 111 != ((IStrategoInt)term).intValue())
                      break Fail7;
                    if(true)
                      break Success4;
                  }
                  term = term4;
                  if(term.getTermType() != IStrategoTerm.INT || 79 != ((IStrategoInt)term).intValue())
                    break Fail6;
                }
                term = oct_chars_to_int_0_0.instance.invoke(context, x_8);
                if(term == null)
                  break Fail6;
                y_8 = term;
                term = int_to_string_0_0.instance.invoke(context, y_8);
                if(term == null)
                  break Fail6;
                term = termFactory.makeAppl(NormalizeAST._consInt_1, new IStrategoTerm[]{term});
                if(true)
                  break Success3;
              }
              term = term0;
            }
            Success5:
            { 
              if(cons0 == NormalizeAST._consFloat_1)
              { 
                Fail8:
                { 
                  IStrategoTerm l_8 = null;
                  IStrategoTerm m_8 = null;
                  IStrategoTerm n_8 = null;
                  IStrategoTerm p_8 = null;
                  IStrategoTerm u_8 = null;
                  l_8 = term.getSubterm(0);
                  term = is_string_0_0.instance.invoke(context, l_8);
                  if(term == null)
                    break Fail8;
                  term = split_at_dot_0_0.instance.invoke(context, l_8);
                  if(term == null)
                    break Fail8;
                  if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                    break Fail8;
                  p_8 = term.getSubterm(0);
                  m_8 = term.getSubterm(1);
                  term = explode_string_0_0.instance.invoke(context, m_8);
                  if(term == null)
                    break Fail8;
                  n_8 = term;
                  IStrategoTerm term6 = term;
                  Success6:
                  { 
                    Fail9:
                    { 
                      if(term.getTermType() != IStrategoTerm.LIST || !((IStrategoList)term).isEmpty())
                        break Fail9;
                      { 
                        if(true)
                          break Fail8;
                        if(true)
                          break Success6;
                      }
                    }
                    term = term6;
                  }
                  u_8 = n_8;
                  term = drop_tailings_0_1.instance.invoke(context, u_8, normalize.const2);
                  if(term == null)
                    break Fail8;
                  term = try_1_0.instance.invoke(context, term, lifted3.instance);
                  if(term == null)
                    break Fail8;
                  term = implode_string_0_0.instance.invoke(context, term);
                  if(term == null)
                    break Fail8;
                  term = termFactory.makeTuple(p_8, normalize.const3, term);
                  term = conc_strings_0_0.instance.invoke(context, term);
                  if(term == null)
                    break Fail8;
                  term = termFactory.makeAppl(NormalizeAST._consFloat_1, new IStrategoTerm[]{term});
                  if(true)
                    break Success5;
                }
                term = term0;
              }
              Success7:
              { 
                if(cons0 == NormalizeAST._consFloat_1)
                { 
                  Fail10:
                  { 
                    IStrategoTerm i_8 = null;
                    i_8 = term.getSubterm(0);
                    term = is_string_0_0.instance.invoke(context, i_8);
                    if(term == null)
                      break Fail10;
                    term = split_at_dot_0_0.instance.invoke(context, i_8);
                    if(term == null)
                      break Fail10;
                    if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
                      break Fail10;
                    if(term.getSubterm(0) != i_8 && !i_8.match(term.getSubterm(0)))
                      break Fail10;
                    term = explode_string_0_0.instance.invoke(context, i_8);
                    if(term == null)
                      break Fail10;
                    term = insert_dot_zero_0_0.instance.invoke(context, term);
                    if(term == null)
                      break Fail10;
                    term = implode_string_0_0.instance.invoke(context, term);
                    if(term == null)
                      break Fail10;
                    term = termFactory.makeAppl(NormalizeAST._consFloat_1, new IStrategoTerm[]{term});
                    if(true)
                      break Success7;
                  }
                  term = term0;
                }
                Success8:
                { 
                  if(cons0 == NormalizeAST._consOpFunLHS_3)
                  { 
                    Fail11:
                    { 
                      IStrategoTerm f_8 = null;
                      IStrategoTerm g_8 = null;
                      IStrategoTerm h_8 = null;
                      g_8 = term.getSubterm(0);
                      IStrategoTerm arg5 = term.getSubterm(1);
                      if(arg5.getTermType() != IStrategoTerm.APPL || NormalizeAST._consPrefOp_1 != ((IStrategoAppl)arg5).getConstructor())
                        break Fail11;
                      f_8 = arg5.getSubterm(0);
                      h_8 = term.getSubterm(2);
                      term = termFactory.makeAppl(NormalizeAST._consVarFunLHS_2, new IStrategoTerm[]{termFactory.makeAppl(NormalizeAST._consVar_1, new IStrategoTerm[]{f_8}), (IStrategoTerm)termFactory.makeListCons(g_8, termFactory.makeListCons(h_8, (IStrategoList)normalize.constNil0))});
                      if(true)
                        break Success8;
                    }
                    term = term0;
                  }
                  Success9:
                  { 
                    if(cons0 == NormalizeAST._consVarFunLHS_2)
                    { 
                      Fail12:
                      { 
                        IStrategoTerm c_8 = null;
                        IStrategoTerm d_8 = null;
                        IStrategoTerm e_8 = null;
                        IStrategoTerm arg6 = term.getSubterm(0);
                        if(arg6.getTermType() != IStrategoTerm.APPL || NormalizeAST._consBinOp_1 != ((IStrategoAppl)arg6).getConstructor())
                          break Fail12;
                        d_8 = arg6.getSubterm(0);
                        IStrategoTerm arg7 = term.getSubterm(1);
                        if(arg7.getTermType() != IStrategoTerm.LIST || ((IStrategoList)arg7).isEmpty())
                          break Fail12;
                        c_8 = ((IStrategoList)arg7).head();
                        IStrategoTerm arg8 = ((IStrategoList)arg7).tail();
                        if(arg8.getTermType() != IStrategoTerm.LIST || ((IStrategoList)arg8).isEmpty())
                          break Fail12;
                        e_8 = ((IStrategoList)arg8).head();
                        IStrategoTerm arg9 = ((IStrategoList)arg8).tail();
                        if(arg9.getTermType() != IStrategoTerm.LIST || !((IStrategoList)arg9).isEmpty())
                          break Fail12;
                        term = termFactory.makeAppl(NormalizeAST._consOpFunLHS_3, new IStrategoTerm[]{c_8, d_8, e_8});
                        if(true)
                          break Success9;
                      }
                      term = term0;
                    }
                    Success10:
                    { 
                      if(cons0 == NormalizeAST._consVarFunLHS_2)
                      { 
                        Fail13:
                        { 
                          IStrategoTerm x_7 = null;
                          IStrategoTerm y_7 = null;
                          IStrategoTerm z_7 = null;
                          IStrategoTerm a_8 = null;
                          IStrategoTerm arg10 = term.getSubterm(0);
                          if(arg10.getTermType() != IStrategoTerm.APPL || NormalizeAST._consBinOp_1 != ((IStrategoAppl)arg10).getConstructor())
                            break Fail13;
                          y_7 = arg10.getSubterm(0);
                          IStrategoTerm arg11 = term.getSubterm(1);
                          if(arg11.getTermType() != IStrategoTerm.LIST || ((IStrategoList)arg11).isEmpty())
                            break Fail13;
                          x_7 = ((IStrategoList)arg11).head();
                          IStrategoTerm arg12 = ((IStrategoList)arg11).tail();
                          if(arg12.getTermType() != IStrategoTerm.LIST || ((IStrategoList)arg12).isEmpty())
                            break Fail13;
                          z_7 = ((IStrategoList)arg12).head();
                          a_8 = ((IStrategoList)arg12).tail();
                          term = a_8;
                          IStrategoTerm term11 = term;
                          Success11:
                          { 
                            Fail14:
                            { 
                              if(term.getTermType() != IStrategoTerm.LIST || !((IStrategoList)term).isEmpty())
                                break Fail14;
                              { 
                                if(true)
                                  break Fail13;
                                if(true)
                                  break Success11;
                              }
                            }
                            term = term11;
                          }
                          term = termFactory.makeAppl(NormalizeAST._consNestedFunLHS_2, new IStrategoTerm[]{termFactory.makeAppl(NormalizeAST._consOpFunLHS_3, new IStrategoTerm[]{x_7, y_7, z_7}), a_8});
                          if(true)
                            break Success10;
                        }
                        term = term0;
                      }
                      Success12:
                      { 
                        if(cons0 == NormalizeAST._consContext_1)
                        { 
                          Fail15:
                          { 
                            IStrategoTerm v_7 = null;
                            v_7 = term.getSubterm(0);
                            term = v_7;
                            IStrategoTerm term13 = term;
                            Success13:
                            { 
                              Fail16:
                              { 
                                term = is_list_0_0.instance.invoke(context, term);
                                if(term == null)
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
                            term = termFactory.makeAppl(NormalizeAST._consContext_1, new IStrategoTerm[]{(IStrategoTerm)termFactory.makeListCons(v_7, (IStrategoList)normalize.constNil0)});
                            if(true)
                              break Success12;
                          }
                          term = term0;
                        }
                        Success15:
                        { 
                          if(cons0 == NormalizeAST._consSContext_1)
                          { 
                            Fail18:
                            { 
                              IStrategoTerm t_7 = null;
                              t_7 = term.getSubterm(0);
                              term = t_7;
                              IStrategoTerm term16 = term;
                              Success16:
                              { 
                                Fail19:
                                { 
                                  term = is_list_0_0.instance.invoke(context, term);
                                  if(term == null)
                                    break Fail19;
                                  { 
                                    if(true)
                                      break Fail18;
                                    if(true)
                                      break Success16;
                                  }
                                }
                                term = term16;
                              }
                              IStrategoTerm term17 = term;
                              Success17:
                              { 
                                Fail20:
                                { 
                                  if(term.getTermType() != IStrategoTerm.LIST || !((IStrategoList)term).isEmpty())
                                    break Fail20;
                                  { 
                                    if(true)
                                      break Fail18;
                                    if(true)
                                      break Success17;
                                  }
                                }
                                term = term17;
                              }
                              term = termFactory.makeAppl(NormalizeAST._consSContext_1, new IStrategoTerm[]{(IStrategoTerm)termFactory.makeListCons(t_7, (IStrategoList)normalize.constNil0)});
                              if(true)
                                break Success15;
                            }
                            term = term0;
                          }
                          Success18:
                          { 
                            if(cons0 == NormalizeAST._consHexadecimalEsc_1)
                            { 
                              Fail21:
                              { 
                                IStrategoTerm q_7 = null;
                                q_7 = term.getSubterm(0);
                                term = hex_string_to_int_0_0.instance.invoke(context, q_7);
                                if(term == null)
                                  break Fail21;
                                term = int_to_string_0_0.instance.invoke(context, term);
                                if(term == null)
                                  break Fail21;
                                term = termFactory.makeAppl(NormalizeAST._consDecimalEsc_1, new IStrategoTerm[]{term});
                                term = try_1_0.instance.invoke(context, term, norm_0_0.instance);
                                if(term == null)
                                  break Fail21;
                                if(true)
                                  break Success18;
                              }
                              term = term0;
                            }
                            Success19:
                            { 
                              if(cons0 == NormalizeAST._consOctalEsc_1)
                              { 
                                Fail22:
                                { 
                                  IStrategoTerm n_7 = null;
                                  n_7 = term.getSubterm(0);
                                  term = oct_string_to_int_0_0.instance.invoke(context, n_7);
                                  if(term == null)
                                    break Fail22;
                                  term = int_to_string_0_0.instance.invoke(context, term);
                                  if(term == null)
                                    break Fail22;
                                  term = termFactory.makeAppl(NormalizeAST._consDecimalEsc_1, new IStrategoTerm[]{term});
                                  term = try_1_0.instance.invoke(context, term, norm_0_0.instance);
                                  if(term == null)
                                    break Fail22;
                                  if(true)
                                    break Success19;
                                }
                                term = term0;
                              }
                              Success20:
                              { 
                                if(cons0 == NormalizeAST._consDecimalEsc_1)
                                { 
                                  Fail23:
                                  { 
                                    IStrategoTerm j_7 = null;
                                    j_7 = term.getSubterm(0);
                                    term = string_to_int_0_0.instance.invoke(context, j_7);
                                    if(term == null)
                                      break Fail23;
                                    term = is_ascii_0_0.instance.invoke(context, term);
                                    if(term == null)
                                      break Fail23;
                                    term = (IStrategoTerm)termFactory.makeListCons(term, (IStrategoList)normalize.constNil0);
                                    term = implode_string_0_0.instance.invoke(context, term);
                                    if(term == null)
                                      break Fail23;
                                    if(true)
                                      break Success20;
                                  }
                                  term = term0;
                                }
                                Success21:
                                { 
                                  if(cons0 == NormalizeAST._consDecimalEsc_1)
                                  { 
                                    Fail24:
                                    { 
                                      IStrategoTerm arg13 = term.getSubterm(0);
                                      if(arg13.getTermType() != IStrategoTerm.STRING || !"7".equals(((IStrategoString)arg13).stringValue()))
                                        break Fail24;
                                      term = normalize.constCharEsc0;
                                      if(true)
                                        break Success21;
                                    }
                                    term = term0;
                                  }
                                  Success22:
                                  { 
                                    if(cons0 == NormalizeAST._consDecimalEsc_1)
                                    { 
                                      Fail25:
                                      { 
                                        IStrategoTerm arg14 = term.getSubterm(0);
                                        if(arg14.getTermType() != IStrategoTerm.STRING || !"8".equals(((IStrategoString)arg14).stringValue()))
                                          break Fail25;
                                        term = normalize.constCharEsc1;
                                        if(true)
                                          break Success22;
                                      }
                                      term = term0;
                                    }
                                    Success23:
                                    { 
                                      if(cons0 == NormalizeAST._consDecimalEsc_1)
                                      { 
                                        Fail26:
                                        { 
                                          IStrategoTerm arg15 = term.getSubterm(0);
                                          if(arg15.getTermType() != IStrategoTerm.STRING || !"12".equals(((IStrategoString)arg15).stringValue()))
                                            break Fail26;
                                          term = normalize.constCharEsc2;
                                          if(true)
                                            break Success23;
                                        }
                                        term = term0;
                                      }
                                      Success24:
                                      { 
                                        if(cons0 == NormalizeAST._consDecimalEsc_1)
                                        { 
                                          Fail27:
                                          { 
                                            IStrategoTerm arg16 = term.getSubterm(0);
                                            if(arg16.getTermType() != IStrategoTerm.STRING || !"10".equals(((IStrategoString)arg16).stringValue()))
                                              break Fail27;
                                            term = normalize.constCharEsc3;
                                            if(true)
                                              break Success24;
                                          }
                                          term = term0;
                                        }
                                        Success25:
                                        { 
                                          if(cons0 == NormalizeAST._consDecimalEsc_1)
                                          { 
                                            Fail28:
                                            { 
                                              IStrategoTerm arg17 = term.getSubterm(0);
                                              if(arg17.getTermType() != IStrategoTerm.STRING || !"13".equals(((IStrategoString)arg17).stringValue()))
                                                break Fail28;
                                              term = normalize.constCharEsc4;
                                              if(true)
                                                break Success25;
                                            }
                                            term = term0;
                                          }
                                          Success26:
                                          { 
                                            if(cons0 == NormalizeAST._consDecimalEsc_1)
                                            { 
                                              Fail29:
                                              { 
                                                IStrategoTerm arg18 = term.getSubterm(0);
                                                if(arg18.getTermType() != IStrategoTerm.STRING || !"9".equals(((IStrategoString)arg18).stringValue()))
                                                  break Fail29;
                                                term = normalize.constCharEsc5;
                                                if(true)
                                                  break Success26;
                                              }
                                              term = term0;
                                            }
                                            Success27:
                                            { 
                                              if(cons0 == NormalizeAST._consDecimalEsc_1)
                                              { 
                                                Fail30:
                                                { 
                                                  IStrategoTerm arg19 = term.getSubterm(0);
                                                  if(arg19.getTermType() != IStrategoTerm.STRING || !"11".equals(((IStrategoString)arg19).stringValue()))
                                                    break Fail30;
                                                  term = normalize.constCharEsc6;
                                                  if(true)
                                                    break Success27;
                                                }
                                                term = term0;
                                              }
                                              Success28:
                                              { 
                                                if(cons0 == NormalizeAST._consEscape_1)
                                                { 
                                                  Fail31:
                                                  { 
                                                    IStrategoTerm arg20 = term.getSubterm(0);
                                                    if(arg20.getTermType() != IStrategoTerm.APPL || NormalizeAST._consCharEsc_1 != ((IStrategoAppl)arg20).getConstructor())
                                                      break Fail31;
                                                    IStrategoTerm arg21 = arg20.getSubterm(0);
                                                    if(arg21.getTermType() != IStrategoTerm.STRING || !"'".equals(((IStrategoString)arg21).stringValue()))
                                                      break Fail31;
                                                    term = normalize.const11;
                                                    if(true)
                                                      break Success28;
                                                  }
                                                  term = term0;
                                                }
                                                Success29:
                                                { 
                                                  if(cons0 == NormalizeAST._consString_1)
                                                  { 
                                                    Fail32:
                                                    { 
                                                      IStrategoTerm g_7 = null;
                                                      IStrategoTerm h_7 = null;
                                                      g_7 = term.getSubterm(0);
                                                      term = filter_1_0.instance.invoke(context, g_7, lifted6.instance);
                                                      if(term == null)
                                                        break Fail32;
                                                      h_7 = term;
                                                      term = termFactory.makeTuple(g_7, term);
                                                      IStrategoTerm term31 = term;
                                                      Success30:
                                                      { 
                                                        Fail33:
                                                        { 
                                                          term = equal_0_0.instance.invoke(context, term);
                                                          if(term == null)
                                                            break Fail33;
                                                          { 
                                                            if(true)
                                                              break Fail32;
                                                            if(true)
                                                              break Success30;
                                                          }
                                                        }
                                                        term = term31;
                                                      }
                                                      term = termFactory.makeAppl(NormalizeAST._consString_1, new IStrategoTerm[]{h_7});
                                                      if(true)
                                                        break Success29;
                                                    }
                                                    term = term0;
                                                  }
                                                  if(cons0 == NormalizeAST._consDerive_1)
                                                  { 
                                                    IStrategoTerm f_7 = null;
                                                    IStrategoTerm arg24 = term.getSubterm(0);
                                                    if(arg24.getTermType() != IStrategoTerm.LIST || ((IStrategoList)arg24).isEmpty())
                                                      break Fail2;
                                                    f_7 = ((IStrategoList)arg24).head();
                                                    IStrategoTerm arg25 = ((IStrategoList)arg24).tail();
                                                    if(arg25.getTermType() != IStrategoTerm.LIST || !((IStrategoList)arg25).isEmpty())
                                                      break Fail2;
                                                    term = termFactory.makeAppl(NormalizeAST._consDerive_1, new IStrategoTerm[]{f_7});
                                                  }
                                                  else
                                                  { 
                                                    break Fail2;
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