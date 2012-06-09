package NormalizeAST_str;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.imp.debug.stratego.runtime.trans.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") public class norm_char_char_0_0 extends Strategy 
{ 
  public static norm_char_char_0_0 instance = new norm_char_char_0_0();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term)
  { 
    ITermFactory termFactory = context.getFactory();
    context.push("norm_char_char_0_0");
    Fail47:
    { 
      IStrategoTerm term35 = term;
      IStrategoConstructor cons4 = term.getTermType() == IStrategoTerm.APPL ? ((IStrategoAppl)term).getConstructor() : null;
      Success35:
      { 
        if(cons4 == NormalizeAST_str._consEscape_1)
        { 
          Fail48:
          { 
            IStrategoTerm arg64 = term.getSubterm(0);
            if(arg64.getTermType() != IStrategoTerm.APPL || NormalizeAST_str._consCharEsc_1 != ((IStrategoAppl)arg64).getConstructor())
              break Fail48;
            IStrategoTerm arg65 = arg64.getSubterm(0);
            if(arg65.getTermType() != IStrategoTerm.STRING || !"\"".equals(((IStrategoString)arg65).stringValue()))
              break Fail48;
            term = NormalizeAST_str.const0;
            if(true)
              break Success35;
          }
          term = term35;
        }
        Success36:
        { 
          if(cons4 == NormalizeAST_str._consEscape_1)
          { 
            Fail49:
            { 
              IStrategoTerm arg66 = term.getSubterm(0);
              if(arg66.getTermType() != IStrategoTerm.APPL || NormalizeAST_str._consASCIIEsc_1 != ((IStrategoAppl)arg66).getConstructor())
                break Fail49;
              IStrategoTerm arg67 = arg66.getSubterm(0);
              if(arg67.getTermType() != IStrategoTerm.STRING || !"SP".equals(((IStrategoString)arg67).stringValue()))
                break Fail49;
              term = NormalizeAST_str.const1;
              if(true)
                break Success36;
            }
            term = term35;
          }
          Success37:
          { 
            if(cons4 == NormalizeAST_str._consEscape_1)
            { 
              Fail50:
              { 
                IStrategoTerm q_3170 = null;
                q_3170 = term.getSubterm(0);
                term = this.invoke(context, q_3170);
                if(term == null)
                  break Fail50;
                term = termFactory.makeAppl(NormalizeAST_str._consEscape_1, new IStrategoTerm[]{term});
                if(true)
                  break Success37;
              }
              term = term35;
            }
            Success38:
            { 
              if(cons4 == NormalizeAST_str._consHexadecimalEsc_1)
              { 
                Fail51:
                { 
                  IStrategoTerm n_3170 = null;
                  n_3170 = term.getSubterm(0);
                  term = hex_string_to_int_0_0.instance.invoke(context, n_3170);
                  if(term == null)
                    break Fail51;
                  term = int_to_string_0_0.instance.invoke(context, term);
                  if(term == null)
                    break Fail51;
                  term = termFactory.makeAppl(NormalizeAST_str._consDecimalEsc_1, new IStrategoTerm[]{term});
                  if(true)
                    break Success38;
                }
                term = term35;
              }
              Success39:
              { 
                if(cons4 == NormalizeAST_str._consOctalEsc_1)
                { 
                  Fail52:
                  { 
                    IStrategoTerm k_3170 = null;
                    k_3170 = term.getSubterm(0);
                    term = oct_string_to_int_0_0.instance.invoke(context, k_3170);
                    if(term == null)
                      break Fail52;
                    term = int_to_string_0_0.instance.invoke(context, term);
                    if(term == null)
                      break Fail52;
                    term = termFactory.makeAppl(NormalizeAST_str._consDecimalEsc_1, new IStrategoTerm[]{term});
                    if(true)
                      break Success39;
                  }
                  term = term35;
                }
                Success40:
                { 
                  if(cons4 == NormalizeAST_str._consDecimalEsc_1)
                  { 
                    Fail53:
                    { 
                      IStrategoTerm g_3170 = null;
                      g_3170 = term.getSubterm(0);
                      term = string_to_int_0_0.instance.invoke(context, g_3170);
                      if(term == null)
                        break Fail53;
                      term = is_ascii_0_0.instance.invoke(context, term);
                      if(term == null)
                        break Fail53;
                      IStrategoTerm term41 = term;
                      Success41:
                      { 
                        Fail54:
                        { 
                          if(term.getTermType() != IStrategoTerm.INT || 127 != ((IStrategoInt)term).intValue())
                            break Fail54;
                          { 
                            if(true)
                              break Fail53;
                            if(true)
                              break Success41;
                          }
                        }
                        term = term41;
                      }
                      term = (IStrategoTerm)termFactory.makeListCons(term, (IStrategoList)NormalizeAST_str.constNil1);
                      term = implode_string_0_0.instance.invoke(context, term);
                      if(term == null)
                        break Fail53;
                      if(true)
                        break Success40;
                    }
                    term = term35;
                  }
                  Success42:
                  { 
                    if(cons4 == NormalizeAST_str._consDecimalEsc_1)
                    { 
                      Fail55:
                      { 
                        IStrategoTerm d_3170 = null;
                        IStrategoTerm e_3170 = null;
                        d_3170 = term.getSubterm(0);
                        term = string_to_int_0_0.instance.invoke(context, d_3170);
                        if(term == null)
                          break Fail55;
                        term = int_to_string_0_0.instance.invoke(context, term);
                        if(term == null)
                          break Fail55;
                        e_3170 = term;
                        term = termFactory.makeTuple(d_3170, term);
                        IStrategoTerm term43 = term;
                        Success43:
                        { 
                          Fail56:
                          { 
                            term = equal_0_0.instance.invoke(context, term);
                            if(term == null)
                              break Fail56;
                            { 
                              if(true)
                                break Fail55;
                              if(true)
                                break Success43;
                            }
                          }
                          term = term43;
                        }
                        term = termFactory.makeAppl(NormalizeAST_str._consDecimalEsc_1, new IStrategoTerm[]{e_3170});
                        if(true)
                          break Success42;
                      }
                      term = term35;
                    }
                    Success44:
                    { 
                      if(cons4 == NormalizeAST_str._consDecimalEsc_1)
                      { 
                        Fail57:
                        { 
                          IStrategoTerm arg68 = term.getSubterm(0);
                          if(arg68.getTermType() != IStrategoTerm.STRING || !"0".equals(((IStrategoString)arg68).stringValue()))
                            break Fail57;
                          term = NormalizeAST_str.constASCIIEsc0;
                          if(true)
                            break Success44;
                        }
                        term = term35;
                      }
                      Success45:
                      { 
                        if(cons4 == NormalizeAST_str._consDecimalEsc_1)
                        { 
                          Fail58:
                          { 
                            IStrategoTerm arg69 = term.getSubterm(0);
                            if(arg69.getTermType() != IStrategoTerm.STRING || !"1".equals(((IStrategoString)arg69).stringValue()))
                              break Fail58;
                            term = NormalizeAST_str.constASCIIEsc1;
                            if(true)
                              break Success45;
                          }
                          term = term35;
                        }
                        Success46:
                        { 
                          if(cons4 == NormalizeAST_str._consDecimalEsc_1)
                          { 
                            Fail59:
                            { 
                              IStrategoTerm arg70 = term.getSubterm(0);
                              if(arg70.getTermType() != IStrategoTerm.STRING || !"2".equals(((IStrategoString)arg70).stringValue()))
                                break Fail59;
                              term = NormalizeAST_str.constASCIIEsc2;
                              if(true)
                                break Success46;
                            }
                            term = term35;
                          }
                          Success47:
                          { 
                            if(cons4 == NormalizeAST_str._consDecimalEsc_1)
                            { 
                              Fail60:
                              { 
                                IStrategoTerm arg71 = term.getSubterm(0);
                                if(arg71.getTermType() != IStrategoTerm.STRING || !"3".equals(((IStrategoString)arg71).stringValue()))
                                  break Fail60;
                                term = NormalizeAST_str.constASCIIEsc3;
                                if(true)
                                  break Success47;
                              }
                              term = term35;
                            }
                            Success48:
                            { 
                              if(cons4 == NormalizeAST_str._consDecimalEsc_1)
                              { 
                                Fail61:
                                { 
                                  IStrategoTerm arg72 = term.getSubterm(0);
                                  if(arg72.getTermType() != IStrategoTerm.STRING || !"4".equals(((IStrategoString)arg72).stringValue()))
                                    break Fail61;
                                  term = NormalizeAST_str.constASCIIEsc4;
                                  if(true)
                                    break Success48;
                                }
                                term = term35;
                              }
                              Success49:
                              { 
                                if(cons4 == NormalizeAST_str._consDecimalEsc_1)
                                { 
                                  Fail62:
                                  { 
                                    IStrategoTerm arg73 = term.getSubterm(0);
                                    if(arg73.getTermType() != IStrategoTerm.STRING || !"5".equals(((IStrategoString)arg73).stringValue()))
                                      break Fail62;
                                    term = NormalizeAST_str.constASCIIEsc5;
                                    if(true)
                                      break Success49;
                                  }
                                  term = term35;
                                }
                                Success50:
                                { 
                                  if(cons4 == NormalizeAST_str._consDecimalEsc_1)
                                  { 
                                    Fail63:
                                    { 
                                      IStrategoTerm arg74 = term.getSubterm(0);
                                      if(arg74.getTermType() != IStrategoTerm.STRING || !"6".equals(((IStrategoString)arg74).stringValue()))
                                        break Fail63;
                                      term = NormalizeAST_str.constASCIIEsc6;
                                      if(true)
                                        break Success50;
                                    }
                                    term = term35;
                                  }
                                  Success51:
                                  { 
                                    if(cons4 == NormalizeAST_str._consDecimalEsc_1)
                                    { 
                                      Fail64:
                                      { 
                                        IStrategoTerm arg75 = term.getSubterm(0);
                                        if(arg75.getTermType() != IStrategoTerm.STRING || !"7".equals(((IStrategoString)arg75).stringValue()))
                                          break Fail64;
                                        term = NormalizeAST_str.constCharEsc0;
                                        if(true)
                                          break Success51;
                                      }
                                      term = term35;
                                    }
                                    Success52:
                                    { 
                                      if(cons4 == NormalizeAST_str._consDecimalEsc_1)
                                      { 
                                        Fail65:
                                        { 
                                          IStrategoTerm arg76 = term.getSubterm(0);
                                          if(arg76.getTermType() != IStrategoTerm.STRING || !"8".equals(((IStrategoString)arg76).stringValue()))
                                            break Fail65;
                                          term = NormalizeAST_str.constCharEsc1;
                                          if(true)
                                            break Success52;
                                        }
                                        term = term35;
                                      }
                                      Success53:
                                      { 
                                        if(cons4 == NormalizeAST_str._consDecimalEsc_1)
                                        { 
                                          Fail66:
                                          { 
                                            IStrategoTerm arg77 = term.getSubterm(0);
                                            if(arg77.getTermType() != IStrategoTerm.STRING || !"9".equals(((IStrategoString)arg77).stringValue()))
                                              break Fail66;
                                            term = NormalizeAST_str.constCharEsc2;
                                            if(true)
                                              break Success53;
                                          }
                                          term = term35;
                                        }
                                        Success54:
                                        { 
                                          if(cons4 == NormalizeAST_str._consDecimalEsc_1)
                                          { 
                                            Fail67:
                                            { 
                                              IStrategoTerm arg78 = term.getSubterm(0);
                                              if(arg78.getTermType() != IStrategoTerm.STRING || !"10".equals(((IStrategoString)arg78).stringValue()))
                                                break Fail67;
                                              term = NormalizeAST_str.constCharEsc3;
                                              if(true)
                                                break Success54;
                                            }
                                            term = term35;
                                          }
                                          Success55:
                                          { 
                                            if(cons4 == NormalizeAST_str._consDecimalEsc_1)
                                            { 
                                              Fail68:
                                              { 
                                                IStrategoTerm arg79 = term.getSubterm(0);
                                                if(arg79.getTermType() != IStrategoTerm.STRING || !"11".equals(((IStrategoString)arg79).stringValue()))
                                                  break Fail68;
                                                term = NormalizeAST_str.constCharEsc4;
                                                if(true)
                                                  break Success55;
                                              }
                                              term = term35;
                                            }
                                            Success56:
                                            { 
                                              if(cons4 == NormalizeAST_str._consDecimalEsc_1)
                                              { 
                                                Fail69:
                                                { 
                                                  IStrategoTerm arg80 = term.getSubterm(0);
                                                  if(arg80.getTermType() != IStrategoTerm.STRING || !"12".equals(((IStrategoString)arg80).stringValue()))
                                                    break Fail69;
                                                  term = NormalizeAST_str.constCharEsc5;
                                                  if(true)
                                                    break Success56;
                                                }
                                                term = term35;
                                              }
                                              Success57:
                                              { 
                                                if(cons4 == NormalizeAST_str._consDecimalEsc_1)
                                                { 
                                                  Fail70:
                                                  { 
                                                    IStrategoTerm arg81 = term.getSubterm(0);
                                                    if(arg81.getTermType() != IStrategoTerm.STRING || !"13".equals(((IStrategoString)arg81).stringValue()))
                                                      break Fail70;
                                                    term = NormalizeAST_str.constCharEsc6;
                                                    if(true)
                                                      break Success57;
                                                  }
                                                  term = term35;
                                                }
                                                Success58:
                                                { 
                                                  if(cons4 == NormalizeAST_str._consDecimalEsc_1)
                                                  { 
                                                    Fail71:
                                                    { 
                                                      IStrategoTerm arg82 = term.getSubterm(0);
                                                      if(arg82.getTermType() != IStrategoTerm.STRING || !"14".equals(((IStrategoString)arg82).stringValue()))
                                                        break Fail71;
                                                      term = NormalizeAST_str.constASCIIEsc7;
                                                      if(true)
                                                        break Success58;
                                                    }
                                                    term = term35;
                                                  }
                                                  Success59:
                                                  { 
                                                    if(cons4 == NormalizeAST_str._consDecimalEsc_1)
                                                    { 
                                                      Fail72:
                                                      { 
                                                        IStrategoTerm arg83 = term.getSubterm(0);
                                                        if(arg83.getTermType() != IStrategoTerm.STRING || !"15".equals(((IStrategoString)arg83).stringValue()))
                                                          break Fail72;
                                                        term = NormalizeAST_str.constASCIIEsc8;
                                                        if(true)
                                                          break Success59;
                                                      }
                                                      term = term35;
                                                    }
                                                    Success60:
                                                    { 
                                                      if(cons4 == NormalizeAST_str._consDecimalEsc_1)
                                                      { 
                                                        Fail73:
                                                        { 
                                                          IStrategoTerm arg84 = term.getSubterm(0);
                                                          if(arg84.getTermType() != IStrategoTerm.STRING || !"16".equals(((IStrategoString)arg84).stringValue()))
                                                            break Fail73;
                                                          term = NormalizeAST_str.constASCIIEsc9;
                                                          if(true)
                                                            break Success60;
                                                        }
                                                        term = term35;
                                                      }
                                                      Success61:
                                                      { 
                                                        if(cons4 == NormalizeAST_str._consDecimalEsc_1)
                                                        { 
                                                          Fail74:
                                                          { 
                                                            IStrategoTerm arg85 = term.getSubterm(0);
                                                            if(arg85.getTermType() != IStrategoTerm.STRING || !"17".equals(((IStrategoString)arg85).stringValue()))
                                                              break Fail74;
                                                            term = NormalizeAST_str.constASCIIEsc10;
                                                            if(true)
                                                              break Success61;
                                                          }
                                                          term = term35;
                                                        }
                                                        Success62:
                                                        { 
                                                          if(cons4 == NormalizeAST_str._consDecimalEsc_1)
                                                          { 
                                                            Fail75:
                                                            { 
                                                              IStrategoTerm arg86 = term.getSubterm(0);
                                                              if(arg86.getTermType() != IStrategoTerm.STRING || !"18".equals(((IStrategoString)arg86).stringValue()))
                                                                break Fail75;
                                                              term = NormalizeAST_str.constASCIIEsc11;
                                                              if(true)
                                                                break Success62;
                                                            }
                                                            term = term35;
                                                          }
                                                          Success63:
                                                          { 
                                                            if(cons4 == NormalizeAST_str._consDecimalEsc_1)
                                                            { 
                                                              Fail76:
                                                              { 
                                                                IStrategoTerm arg87 = term.getSubterm(0);
                                                                if(arg87.getTermType() != IStrategoTerm.STRING || !"19".equals(((IStrategoString)arg87).stringValue()))
                                                                  break Fail76;
                                                                term = NormalizeAST_str.constASCIIEsc12;
                                                                if(true)
                                                                  break Success63;
                                                              }
                                                              term = term35;
                                                            }
                                                            Success64:
                                                            { 
                                                              if(cons4 == NormalizeAST_str._consDecimalEsc_1)
                                                              { 
                                                                Fail77:
                                                                { 
                                                                  IStrategoTerm arg88 = term.getSubterm(0);
                                                                  if(arg88.getTermType() != IStrategoTerm.STRING || !"20".equals(((IStrategoString)arg88).stringValue()))
                                                                    break Fail77;
                                                                  term = NormalizeAST_str.constASCIIEsc13;
                                                                  if(true)
                                                                    break Success64;
                                                                }
                                                                term = term35;
                                                              }
                                                              Success65:
                                                              { 
                                                                if(cons4 == NormalizeAST_str._consDecimalEsc_1)
                                                                { 
                                                                  Fail78:
                                                                  { 
                                                                    IStrategoTerm arg89 = term.getSubterm(0);
                                                                    if(arg89.getTermType() != IStrategoTerm.STRING || !"21".equals(((IStrategoString)arg89).stringValue()))
                                                                      break Fail78;
                                                                    term = NormalizeAST_str.constASCIIEsc14;
                                                                    if(true)
                                                                      break Success65;
                                                                  }
                                                                  term = term35;
                                                                }
                                                                Success66:
                                                                { 
                                                                  if(cons4 == NormalizeAST_str._consDecimalEsc_1)
                                                                  { 
                                                                    Fail79:
                                                                    { 
                                                                      IStrategoTerm arg90 = term.getSubterm(0);
                                                                      if(arg90.getTermType() != IStrategoTerm.STRING || !"22".equals(((IStrategoString)arg90).stringValue()))
                                                                        break Fail79;
                                                                      term = NormalizeAST_str.constASCIIEsc15;
                                                                      if(true)
                                                                        break Success66;
                                                                    }
                                                                    term = term35;
                                                                  }
                                                                  Success67:
                                                                  { 
                                                                    if(cons4 == NormalizeAST_str._consDecimalEsc_1)
                                                                    { 
                                                                      Fail80:
                                                                      { 
                                                                        IStrategoTerm arg91 = term.getSubterm(0);
                                                                        if(arg91.getTermType() != IStrategoTerm.STRING || !"23".equals(((IStrategoString)arg91).stringValue()))
                                                                          break Fail80;
                                                                        term = NormalizeAST_str.constASCIIEsc16;
                                                                        if(true)
                                                                          break Success67;
                                                                      }
                                                                      term = term35;
                                                                    }
                                                                    Success68:
                                                                    { 
                                                                      if(cons4 == NormalizeAST_str._consDecimalEsc_1)
                                                                      { 
                                                                        Fail81:
                                                                        { 
                                                                          IStrategoTerm arg92 = term.getSubterm(0);
                                                                          if(arg92.getTermType() != IStrategoTerm.STRING || !"24".equals(((IStrategoString)arg92).stringValue()))
                                                                            break Fail81;
                                                                          term = NormalizeAST_str.constASCIIEsc17;
                                                                          if(true)
                                                                            break Success68;
                                                                        }
                                                                        term = term35;
                                                                      }
                                                                      Success69:
                                                                      { 
                                                                        if(cons4 == NormalizeAST_str._consDecimalEsc_1)
                                                                        { 
                                                                          Fail82:
                                                                          { 
                                                                            IStrategoTerm arg93 = term.getSubterm(0);
                                                                            if(arg93.getTermType() != IStrategoTerm.STRING || !"25".equals(((IStrategoString)arg93).stringValue()))
                                                                              break Fail82;
                                                                            term = NormalizeAST_str.constASCIIEsc18;
                                                                            if(true)
                                                                              break Success69;
                                                                          }
                                                                          term = term35;
                                                                        }
                                                                        Success70:
                                                                        { 
                                                                          if(cons4 == NormalizeAST_str._consDecimalEsc_1)
                                                                          { 
                                                                            Fail83:
                                                                            { 
                                                                              IStrategoTerm arg94 = term.getSubterm(0);
                                                                              if(arg94.getTermType() != IStrategoTerm.STRING || !"26".equals(((IStrategoString)arg94).stringValue()))
                                                                                break Fail83;
                                                                              term = NormalizeAST_str.constASCIIEsc19;
                                                                              if(true)
                                                                                break Success70;
                                                                            }
                                                                            term = term35;
                                                                          }
                                                                          Success71:
                                                                          { 
                                                                            if(cons4 == NormalizeAST_str._consDecimalEsc_1)
                                                                            { 
                                                                              Fail84:
                                                                              { 
                                                                                IStrategoTerm arg95 = term.getSubterm(0);
                                                                                if(arg95.getTermType() != IStrategoTerm.STRING || !"27".equals(((IStrategoString)arg95).stringValue()))
                                                                                  break Fail84;
                                                                                term = NormalizeAST_str.constASCIIEsc20;
                                                                                if(true)
                                                                                  break Success71;
                                                                              }
                                                                              term = term35;
                                                                            }
                                                                            Success72:
                                                                            { 
                                                                              if(cons4 == NormalizeAST_str._consDecimalEsc_1)
                                                                              { 
                                                                                Fail85:
                                                                                { 
                                                                                  IStrategoTerm arg96 = term.getSubterm(0);
                                                                                  if(arg96.getTermType() != IStrategoTerm.STRING || !"28".equals(((IStrategoString)arg96).stringValue()))
                                                                                    break Fail85;
                                                                                  term = NormalizeAST_str.constASCIIEsc21;
                                                                                  if(true)
                                                                                    break Success72;
                                                                                }
                                                                                term = term35;
                                                                              }
                                                                              Success73:
                                                                              { 
                                                                                if(cons4 == NormalizeAST_str._consDecimalEsc_1)
                                                                                { 
                                                                                  Fail86:
                                                                                  { 
                                                                                    IStrategoTerm arg97 = term.getSubterm(0);
                                                                                    if(arg97.getTermType() != IStrategoTerm.STRING || !"29".equals(((IStrategoString)arg97).stringValue()))
                                                                                      break Fail86;
                                                                                    term = NormalizeAST_str.constASCIIEsc22;
                                                                                    if(true)
                                                                                      break Success73;
                                                                                  }
                                                                                  term = term35;
                                                                                }
                                                                                Success74:
                                                                                { 
                                                                                  if(cons4 == NormalizeAST_str._consDecimalEsc_1)
                                                                                  { 
                                                                                    Fail87:
                                                                                    { 
                                                                                      IStrategoTerm arg98 = term.getSubterm(0);
                                                                                      if(arg98.getTermType() != IStrategoTerm.STRING || !"30".equals(((IStrategoString)arg98).stringValue()))
                                                                                        break Fail87;
                                                                                      term = NormalizeAST_str.constASCIIEsc23;
                                                                                      if(true)
                                                                                        break Success74;
                                                                                    }
                                                                                    term = term35;
                                                                                  }
                                                                                  Success75:
                                                                                  { 
                                                                                    if(cons4 == NormalizeAST_str._consDecimalEsc_1)
                                                                                    { 
                                                                                      Fail88:
                                                                                      { 
                                                                                        IStrategoTerm arg99 = term.getSubterm(0);
                                                                                        if(arg99.getTermType() != IStrategoTerm.STRING || !"31".equals(((IStrategoString)arg99).stringValue()))
                                                                                          break Fail88;
                                                                                        term = NormalizeAST_str.constASCIIEsc24;
                                                                                        if(true)
                                                                                          break Success75;
                                                                                      }
                                                                                      term = term35;
                                                                                    }
                                                                                    Success76:
                                                                                    { 
                                                                                      if(cons4 == NormalizeAST_str._consDecimalEsc_1)
                                                                                      { 
                                                                                        Fail89:
                                                                                        { 
                                                                                          IStrategoTerm arg100 = term.getSubterm(0);
                                                                                          if(arg100.getTermType() != IStrategoTerm.STRING || !"127".equals(((IStrategoString)arg100).stringValue()))
                                                                                            break Fail89;
                                                                                          term = NormalizeAST_str.constASCIIEsc25;
                                                                                          if(true)
                                                                                            break Success76;
                                                                                        }
                                                                                        term = term35;
                                                                                      }
                                                                                      Success77:
                                                                                      { 
                                                                                        if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                        { 
                                                                                          Fail90:
                                                                                          { 
                                                                                            IStrategoTerm arg101 = term.getSubterm(0);
                                                                                            if(arg101.getTermType() != IStrategoTerm.STRING || !"^@".equals(((IStrategoString)arg101).stringValue()))
                                                                                              break Fail90;
                                                                                            term = NormalizeAST_str.constASCIIEsc0;
                                                                                            if(true)
                                                                                              break Success77;
                                                                                          }
                                                                                          term = term35;
                                                                                        }
                                                                                        Success78:
                                                                                        { 
                                                                                          if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                          { 
                                                                                            Fail91:
                                                                                            { 
                                                                                              IStrategoTerm arg102 = term.getSubterm(0);
                                                                                              if(arg102.getTermType() != IStrategoTerm.STRING || !"^A".equals(((IStrategoString)arg102).stringValue()))
                                                                                                break Fail91;
                                                                                              term = NormalizeAST_str.constASCIIEsc1;
                                                                                              if(true)
                                                                                                break Success78;
                                                                                            }
                                                                                            term = term35;
                                                                                          }
                                                                                          Success79:
                                                                                          { 
                                                                                            if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                            { 
                                                                                              Fail92:
                                                                                              { 
                                                                                                IStrategoTerm arg103 = term.getSubterm(0);
                                                                                                if(arg103.getTermType() != IStrategoTerm.STRING || !"^B".equals(((IStrategoString)arg103).stringValue()))
                                                                                                  break Fail92;
                                                                                                term = NormalizeAST_str.constASCIIEsc2;
                                                                                                if(true)
                                                                                                  break Success79;
                                                                                              }
                                                                                              term = term35;
                                                                                            }
                                                                                            Success80:
                                                                                            { 
                                                                                              if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                              { 
                                                                                                Fail93:
                                                                                                { 
                                                                                                  IStrategoTerm arg104 = term.getSubterm(0);
                                                                                                  if(arg104.getTermType() != IStrategoTerm.STRING || !"^C".equals(((IStrategoString)arg104).stringValue()))
                                                                                                    break Fail93;
                                                                                                  term = NormalizeAST_str.constASCIIEsc3;
                                                                                                  if(true)
                                                                                                    break Success80;
                                                                                                }
                                                                                                term = term35;
                                                                                              }
                                                                                              Success81:
                                                                                              { 
                                                                                                if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                                { 
                                                                                                  Fail94:
                                                                                                  { 
                                                                                                    IStrategoTerm arg105 = term.getSubterm(0);
                                                                                                    if(arg105.getTermType() != IStrategoTerm.STRING || !"^D".equals(((IStrategoString)arg105).stringValue()))
                                                                                                      break Fail94;
                                                                                                    term = NormalizeAST_str.constASCIIEsc4;
                                                                                                    if(true)
                                                                                                      break Success81;
                                                                                                  }
                                                                                                  term = term35;
                                                                                                }
                                                                                                Success82:
                                                                                                { 
                                                                                                  if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                                  { 
                                                                                                    Fail95:
                                                                                                    { 
                                                                                                      IStrategoTerm arg106 = term.getSubterm(0);
                                                                                                      if(arg106.getTermType() != IStrategoTerm.STRING || !"^E".equals(((IStrategoString)arg106).stringValue()))
                                                                                                        break Fail95;
                                                                                                      term = NormalizeAST_str.constASCIIEsc5;
                                                                                                      if(true)
                                                                                                        break Success82;
                                                                                                    }
                                                                                                    term = term35;
                                                                                                  }
                                                                                                  Success83:
                                                                                                  { 
                                                                                                    if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                                    { 
                                                                                                      Fail96:
                                                                                                      { 
                                                                                                        IStrategoTerm arg107 = term.getSubterm(0);
                                                                                                        if(arg107.getTermType() != IStrategoTerm.STRING || !"^F".equals(((IStrategoString)arg107).stringValue()))
                                                                                                          break Fail96;
                                                                                                        term = NormalizeAST_str.constASCIIEsc6;
                                                                                                        if(true)
                                                                                                          break Success83;
                                                                                                      }
                                                                                                      term = term35;
                                                                                                    }
                                                                                                    Success84:
                                                                                                    { 
                                                                                                      if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                                      { 
                                                                                                        Fail97:
                                                                                                        { 
                                                                                                          IStrategoTerm arg108 = term.getSubterm(0);
                                                                                                          if(arg108.getTermType() != IStrategoTerm.STRING || !"^G".equals(((IStrategoString)arg108).stringValue()))
                                                                                                            break Fail97;
                                                                                                          term = NormalizeAST_str.constCharEsc0;
                                                                                                          if(true)
                                                                                                            break Success84;
                                                                                                        }
                                                                                                        term = term35;
                                                                                                      }
                                                                                                      Success85:
                                                                                                      { 
                                                                                                        if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                                        { 
                                                                                                          Fail98:
                                                                                                          { 
                                                                                                            IStrategoTerm arg109 = term.getSubterm(0);
                                                                                                            if(arg109.getTermType() != IStrategoTerm.STRING || !"^H".equals(((IStrategoString)arg109).stringValue()))
                                                                                                              break Fail98;
                                                                                                            term = NormalizeAST_str.constCharEsc1;
                                                                                                            if(true)
                                                                                                              break Success85;
                                                                                                          }
                                                                                                          term = term35;
                                                                                                        }
                                                                                                        Success86:
                                                                                                        { 
                                                                                                          if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                                          { 
                                                                                                            Fail99:
                                                                                                            { 
                                                                                                              IStrategoTerm arg110 = term.getSubterm(0);
                                                                                                              if(arg110.getTermType() != IStrategoTerm.STRING || !"^I".equals(((IStrategoString)arg110).stringValue()))
                                                                                                                break Fail99;
                                                                                                              term = NormalizeAST_str.constCharEsc2;
                                                                                                              if(true)
                                                                                                                break Success86;
                                                                                                            }
                                                                                                            term = term35;
                                                                                                          }
                                                                                                          Success87:
                                                                                                          { 
                                                                                                            if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                                            { 
                                                                                                              Fail100:
                                                                                                              { 
                                                                                                                IStrategoTerm arg111 = term.getSubterm(0);
                                                                                                                if(arg111.getTermType() != IStrategoTerm.STRING || !"^J".equals(((IStrategoString)arg111).stringValue()))
                                                                                                                  break Fail100;
                                                                                                                term = NormalizeAST_str.constCharEsc3;
                                                                                                                if(true)
                                                                                                                  break Success87;
                                                                                                              }
                                                                                                              term = term35;
                                                                                                            }
                                                                                                            Success88:
                                                                                                            { 
                                                                                                              if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                                              { 
                                                                                                                Fail101:
                                                                                                                { 
                                                                                                                  IStrategoTerm arg112 = term.getSubterm(0);
                                                                                                                  if(arg112.getTermType() != IStrategoTerm.STRING || !"^K".equals(((IStrategoString)arg112).stringValue()))
                                                                                                                    break Fail101;
                                                                                                                  term = NormalizeAST_str.constCharEsc4;
                                                                                                                  if(true)
                                                                                                                    break Success88;
                                                                                                                }
                                                                                                                term = term35;
                                                                                                              }
                                                                                                              Success89:
                                                                                                              { 
                                                                                                                if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                                                { 
                                                                                                                  Fail102:
                                                                                                                  { 
                                                                                                                    IStrategoTerm arg113 = term.getSubterm(0);
                                                                                                                    if(arg113.getTermType() != IStrategoTerm.STRING || !"^L".equals(((IStrategoString)arg113).stringValue()))
                                                                                                                      break Fail102;
                                                                                                                    term = NormalizeAST_str.constCharEsc5;
                                                                                                                    if(true)
                                                                                                                      break Success89;
                                                                                                                  }
                                                                                                                  term = term35;
                                                                                                                }
                                                                                                                Success90:
                                                                                                                { 
                                                                                                                  if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                                                  { 
                                                                                                                    Fail103:
                                                                                                                    { 
                                                                                                                      IStrategoTerm arg114 = term.getSubterm(0);
                                                                                                                      if(arg114.getTermType() != IStrategoTerm.STRING || !"^M".equals(((IStrategoString)arg114).stringValue()))
                                                                                                                        break Fail103;
                                                                                                                      term = NormalizeAST_str.constCharEsc6;
                                                                                                                      if(true)
                                                                                                                        break Success90;
                                                                                                                    }
                                                                                                                    term = term35;
                                                                                                                  }
                                                                                                                  Success91:
                                                                                                                  { 
                                                                                                                    if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                                                    { 
                                                                                                                      Fail104:
                                                                                                                      { 
                                                                                                                        IStrategoTerm arg115 = term.getSubterm(0);
                                                                                                                        if(arg115.getTermType() != IStrategoTerm.STRING || !"^N".equals(((IStrategoString)arg115).stringValue()))
                                                                                                                          break Fail104;
                                                                                                                        term = NormalizeAST_str.constASCIIEsc7;
                                                                                                                        if(true)
                                                                                                                          break Success91;
                                                                                                                      }
                                                                                                                      term = term35;
                                                                                                                    }
                                                                                                                    Success92:
                                                                                                                    { 
                                                                                                                      if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                                                      { 
                                                                                                                        Fail105:
                                                                                                                        { 
                                                                                                                          IStrategoTerm arg116 = term.getSubterm(0);
                                                                                                                          if(arg116.getTermType() != IStrategoTerm.STRING || !"^O".equals(((IStrategoString)arg116).stringValue()))
                                                                                                                            break Fail105;
                                                                                                                          term = NormalizeAST_str.constASCIIEsc8;
                                                                                                                          if(true)
                                                                                                                            break Success92;
                                                                                                                        }
                                                                                                                        term = term35;
                                                                                                                      }
                                                                                                                      Success93:
                                                                                                                      { 
                                                                                                                        if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                                                        { 
                                                                                                                          Fail106:
                                                                                                                          { 
                                                                                                                            IStrategoTerm arg117 = term.getSubterm(0);
                                                                                                                            if(arg117.getTermType() != IStrategoTerm.STRING || !"^P".equals(((IStrategoString)arg117).stringValue()))
                                                                                                                              break Fail106;
                                                                                                                            term = NormalizeAST_str.constASCIIEsc9;
                                                                                                                            if(true)
                                                                                                                              break Success93;
                                                                                                                          }
                                                                                                                          term = term35;
                                                                                                                        }
                                                                                                                        Success94:
                                                                                                                        { 
                                                                                                                          if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                                                          { 
                                                                                                                            Fail107:
                                                                                                                            { 
                                                                                                                              IStrategoTerm arg118 = term.getSubterm(0);
                                                                                                                              if(arg118.getTermType() != IStrategoTerm.STRING || !"^Q".equals(((IStrategoString)arg118).stringValue()))
                                                                                                                                break Fail107;
                                                                                                                              term = NormalizeAST_str.constASCIIEsc10;
                                                                                                                              if(true)
                                                                                                                                break Success94;
                                                                                                                            }
                                                                                                                            term = term35;
                                                                                                                          }
                                                                                                                          Success95:
                                                                                                                          { 
                                                                                                                            if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                                                            { 
                                                                                                                              Fail108:
                                                                                                                              { 
                                                                                                                                IStrategoTerm arg119 = term.getSubterm(0);
                                                                                                                                if(arg119.getTermType() != IStrategoTerm.STRING || !"^R".equals(((IStrategoString)arg119).stringValue()))
                                                                                                                                  break Fail108;
                                                                                                                                term = NormalizeAST_str.constASCIIEsc11;
                                                                                                                                if(true)
                                                                                                                                  break Success95;
                                                                                                                              }
                                                                                                                              term = term35;
                                                                                                                            }
                                                                                                                            Success96:
                                                                                                                            { 
                                                                                                                              if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                                                              { 
                                                                                                                                Fail109:
                                                                                                                                { 
                                                                                                                                  IStrategoTerm arg120 = term.getSubterm(0);
                                                                                                                                  if(arg120.getTermType() != IStrategoTerm.STRING || !"^S".equals(((IStrategoString)arg120).stringValue()))
                                                                                                                                    break Fail109;
                                                                                                                                  term = NormalizeAST_str.constASCIIEsc12;
                                                                                                                                  if(true)
                                                                                                                                    break Success96;
                                                                                                                                }
                                                                                                                                term = term35;
                                                                                                                              }
                                                                                                                              Success97:
                                                                                                                              { 
                                                                                                                                if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                                                                { 
                                                                                                                                  Fail110:
                                                                                                                                  { 
                                                                                                                                    IStrategoTerm arg121 = term.getSubterm(0);
                                                                                                                                    if(arg121.getTermType() != IStrategoTerm.STRING || !"^T".equals(((IStrategoString)arg121).stringValue()))
                                                                                                                                      break Fail110;
                                                                                                                                    term = NormalizeAST_str.constASCIIEsc13;
                                                                                                                                    if(true)
                                                                                                                                      break Success97;
                                                                                                                                  }
                                                                                                                                  term = term35;
                                                                                                                                }
                                                                                                                                Success98:
                                                                                                                                { 
                                                                                                                                  if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                                                                  { 
                                                                                                                                    Fail111:
                                                                                                                                    { 
                                                                                                                                      IStrategoTerm arg122 = term.getSubterm(0);
                                                                                                                                      if(arg122.getTermType() != IStrategoTerm.STRING || !"^U".equals(((IStrategoString)arg122).stringValue()))
                                                                                                                                        break Fail111;
                                                                                                                                      term = NormalizeAST_str.constASCIIEsc14;
                                                                                                                                      if(true)
                                                                                                                                        break Success98;
                                                                                                                                    }
                                                                                                                                    term = term35;
                                                                                                                                  }
                                                                                                                                  Success99:
                                                                                                                                  { 
                                                                                                                                    if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                                                                    { 
                                                                                                                                      Fail112:
                                                                                                                                      { 
                                                                                                                                        IStrategoTerm arg123 = term.getSubterm(0);
                                                                                                                                        if(arg123.getTermType() != IStrategoTerm.STRING || !"^V".equals(((IStrategoString)arg123).stringValue()))
                                                                                                                                          break Fail112;
                                                                                                                                        term = NormalizeAST_str.constASCIIEsc15;
                                                                                                                                        if(true)
                                                                                                                                          break Success99;
                                                                                                                                      }
                                                                                                                                      term = term35;
                                                                                                                                    }
                                                                                                                                    Success100:
                                                                                                                                    { 
                                                                                                                                      if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                                                                      { 
                                                                                                                                        Fail113:
                                                                                                                                        { 
                                                                                                                                          IStrategoTerm arg124 = term.getSubterm(0);
                                                                                                                                          if(arg124.getTermType() != IStrategoTerm.STRING || !"^W".equals(((IStrategoString)arg124).stringValue()))
                                                                                                                                            break Fail113;
                                                                                                                                          term = NormalizeAST_str.constASCIIEsc16;
                                                                                                                                          if(true)
                                                                                                                                            break Success100;
                                                                                                                                        }
                                                                                                                                        term = term35;
                                                                                                                                      }
                                                                                                                                      Success101:
                                                                                                                                      { 
                                                                                                                                        if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                                                                        { 
                                                                                                                                          Fail114:
                                                                                                                                          { 
                                                                                                                                            IStrategoTerm arg125 = term.getSubterm(0);
                                                                                                                                            if(arg125.getTermType() != IStrategoTerm.STRING || !"^X".equals(((IStrategoString)arg125).stringValue()))
                                                                                                                                              break Fail114;
                                                                                                                                            term = NormalizeAST_str.constASCIIEsc17;
                                                                                                                                            if(true)
                                                                                                                                              break Success101;
                                                                                                                                          }
                                                                                                                                          term = term35;
                                                                                                                                        }
                                                                                                                                        Success102:
                                                                                                                                        { 
                                                                                                                                          if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                                                                          { 
                                                                                                                                            Fail115:
                                                                                                                                            { 
                                                                                                                                              IStrategoTerm arg126 = term.getSubterm(0);
                                                                                                                                              if(arg126.getTermType() != IStrategoTerm.STRING || !"^Y".equals(((IStrategoString)arg126).stringValue()))
                                                                                                                                                break Fail115;
                                                                                                                                              term = NormalizeAST_str.constASCIIEsc18;
                                                                                                                                              if(true)
                                                                                                                                                break Success102;
                                                                                                                                            }
                                                                                                                                            term = term35;
                                                                                                                                          }
                                                                                                                                          Success103:
                                                                                                                                          { 
                                                                                                                                            if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                                                                            { 
                                                                                                                                              Fail116:
                                                                                                                                              { 
                                                                                                                                                IStrategoTerm arg127 = term.getSubterm(0);
                                                                                                                                                if(arg127.getTermType() != IStrategoTerm.STRING || !"^Z".equals(((IStrategoString)arg127).stringValue()))
                                                                                                                                                  break Fail116;
                                                                                                                                                term = NormalizeAST_str.constASCIIEsc19;
                                                                                                                                                if(true)
                                                                                                                                                  break Success103;
                                                                                                                                              }
                                                                                                                                              term = term35;
                                                                                                                                            }
                                                                                                                                            Success104:
                                                                                                                                            { 
                                                                                                                                              if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                                                                              { 
                                                                                                                                                Fail117:
                                                                                                                                                { 
                                                                                                                                                  IStrategoTerm arg128 = term.getSubterm(0);
                                                                                                                                                  if(arg128.getTermType() != IStrategoTerm.STRING || !"^[".equals(((IStrategoString)arg128).stringValue()))
                                                                                                                                                    break Fail117;
                                                                                                                                                  term = NormalizeAST_str.constASCIIEsc20;
                                                                                                                                                  if(true)
                                                                                                                                                    break Success104;
                                                                                                                                                }
                                                                                                                                                term = term35;
                                                                                                                                              }
                                                                                                                                              Success105:
                                                                                                                                              { 
                                                                                                                                                if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                                                                                { 
                                                                                                                                                  Fail118:
                                                                                                                                                  { 
                                                                                                                                                    IStrategoTerm arg129 = term.getSubterm(0);
                                                                                                                                                    if(arg129.getTermType() != IStrategoTerm.STRING || !"^\\".equals(((IStrategoString)arg129).stringValue()))
                                                                                                                                                      break Fail118;
                                                                                                                                                    term = NormalizeAST_str.constASCIIEsc21;
                                                                                                                                                    if(true)
                                                                                                                                                      break Success105;
                                                                                                                                                  }
                                                                                                                                                  term = term35;
                                                                                                                                                }
                                                                                                                                                Success106:
                                                                                                                                                { 
                                                                                                                                                  if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                                                                                  { 
                                                                                                                                                    Fail119:
                                                                                                                                                    { 
                                                                                                                                                      IStrategoTerm arg130 = term.getSubterm(0);
                                                                                                                                                      if(arg130.getTermType() != IStrategoTerm.STRING || !"^]".equals(((IStrategoString)arg130).stringValue()))
                                                                                                                                                        break Fail119;
                                                                                                                                                      term = NormalizeAST_str.constASCIIEsc22;
                                                                                                                                                      if(true)
                                                                                                                                                        break Success106;
                                                                                                                                                    }
                                                                                                                                                    term = term35;
                                                                                                                                                  }
                                                                                                                                                  Success107:
                                                                                                                                                  { 
                                                                                                                                                    if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                                                                                    { 
                                                                                                                                                      Fail120:
                                                                                                                                                      { 
                                                                                                                                                        IStrategoTerm arg131 = term.getSubterm(0);
                                                                                                                                                        if(arg131.getTermType() != IStrategoTerm.STRING || !"^^".equals(((IStrategoString)arg131).stringValue()))
                                                                                                                                                          break Fail120;
                                                                                                                                                        term = NormalizeAST_str.constASCIIEsc23;
                                                                                                                                                        if(true)
                                                                                                                                                          break Success107;
                                                                                                                                                      }
                                                                                                                                                      term = term35;
                                                                                                                                                    }
                                                                                                                                                    Success108:
                                                                                                                                                    { 
                                                                                                                                                      if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                                                                                      { 
                                                                                                                                                        Fail121:
                                                                                                                                                        { 
                                                                                                                                                          IStrategoTerm arg132 = term.getSubterm(0);
                                                                                                                                                          if(arg132.getTermType() != IStrategoTerm.STRING || !"^_".equals(((IStrategoString)arg132).stringValue()))
                                                                                                                                                            break Fail121;
                                                                                                                                                          term = NormalizeAST_str.constASCIIEsc24;
                                                                                                                                                          if(true)
                                                                                                                                                            break Success108;
                                                                                                                                                        }
                                                                                                                                                        term = term35;
                                                                                                                                                      }
                                                                                                                                                      Success109:
                                                                                                                                                      { 
                                                                                                                                                        if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                                                                                        { 
                                                                                                                                                          Fail122:
                                                                                                                                                          { 
                                                                                                                                                            IStrategoTerm arg133 = term.getSubterm(0);
                                                                                                                                                            if(arg133.getTermType() != IStrategoTerm.STRING || !"^?".equals(((IStrategoString)arg133).stringValue()))
                                                                                                                                                              break Fail122;
                                                                                                                                                            term = NormalizeAST_str.constASCIIEsc25;
                                                                                                                                                            if(true)
                                                                                                                                                              break Success109;
                                                                                                                                                          }
                                                                                                                                                          term = term35;
                                                                                                                                                        }
                                                                                                                                                        Success110:
                                                                                                                                                        { 
                                                                                                                                                          if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                                                                                          { 
                                                                                                                                                            Fail123:
                                                                                                                                                            { 
                                                                                                                                                              IStrategoTerm arg134 = term.getSubterm(0);
                                                                                                                                                              if(arg134.getTermType() != IStrategoTerm.STRING || !"BEL".equals(((IStrategoString)arg134).stringValue()))
                                                                                                                                                                break Fail123;
                                                                                                                                                              term = NormalizeAST_str.constCharEsc0;
                                                                                                                                                              if(true)
                                                                                                                                                                break Success110;
                                                                                                                                                            }
                                                                                                                                                            term = term35;
                                                                                                                                                          }
                                                                                                                                                          Success111:
                                                                                                                                                          { 
                                                                                                                                                            if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                                                                                            { 
                                                                                                                                                              Fail124:
                                                                                                                                                              { 
                                                                                                                                                                IStrategoTerm arg135 = term.getSubterm(0);
                                                                                                                                                                if(arg135.getTermType() != IStrategoTerm.STRING || !"BS".equals(((IStrategoString)arg135).stringValue()))
                                                                                                                                                                  break Fail124;
                                                                                                                                                                term = NormalizeAST_str.constCharEsc1;
                                                                                                                                                                if(true)
                                                                                                                                                                  break Success111;
                                                                                                                                                              }
                                                                                                                                                              term = term35;
                                                                                                                                                            }
                                                                                                                                                            Success112:
                                                                                                                                                            { 
                                                                                                                                                              if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                                                                                              { 
                                                                                                                                                                Fail125:
                                                                                                                                                                { 
                                                                                                                                                                  IStrategoTerm arg136 = term.getSubterm(0);
                                                                                                                                                                  if(arg136.getTermType() != IStrategoTerm.STRING || !"HT".equals(((IStrategoString)arg136).stringValue()))
                                                                                                                                                                    break Fail125;
                                                                                                                                                                  term = NormalizeAST_str.constCharEsc2;
                                                                                                                                                                  if(true)
                                                                                                                                                                    break Success112;
                                                                                                                                                                }
                                                                                                                                                                term = term35;
                                                                                                                                                              }
                                                                                                                                                              Success113:
                                                                                                                                                              { 
                                                                                                                                                                if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                                                                                                { 
                                                                                                                                                                  Fail126:
                                                                                                                                                                  { 
                                                                                                                                                                    IStrategoTerm arg137 = term.getSubterm(0);
                                                                                                                                                                    if(arg137.getTermType() != IStrategoTerm.STRING || !"LF".equals(((IStrategoString)arg137).stringValue()))
                                                                                                                                                                      break Fail126;
                                                                                                                                                                    term = NormalizeAST_str.constCharEsc3;
                                                                                                                                                                    if(true)
                                                                                                                                                                      break Success113;
                                                                                                                                                                  }
                                                                                                                                                                  term = term35;
                                                                                                                                                                }
                                                                                                                                                                Success114:
                                                                                                                                                                { 
                                                                                                                                                                  if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                                                                                                  { 
                                                                                                                                                                    Fail127:
                                                                                                                                                                    { 
                                                                                                                                                                      IStrategoTerm arg138 = term.getSubterm(0);
                                                                                                                                                                      if(arg138.getTermType() != IStrategoTerm.STRING || !"VT".equals(((IStrategoString)arg138).stringValue()))
                                                                                                                                                                        break Fail127;
                                                                                                                                                                      term = NormalizeAST_str.constCharEsc4;
                                                                                                                                                                      if(true)
                                                                                                                                                                        break Success114;
                                                                                                                                                                    }
                                                                                                                                                                    term = term35;
                                                                                                                                                                  }
                                                                                                                                                                  Success115:
                                                                                                                                                                  { 
                                                                                                                                                                    if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                                                                                                    { 
                                                                                                                                                                      Fail128:
                                                                                                                                                                      { 
                                                                                                                                                                        IStrategoTerm arg139 = term.getSubterm(0);
                                                                                                                                                                        if(arg139.getTermType() != IStrategoTerm.STRING || !"FF".equals(((IStrategoString)arg139).stringValue()))
                                                                                                                                                                          break Fail128;
                                                                                                                                                                        term = NormalizeAST_str.constCharEsc5;
                                                                                                                                                                        if(true)
                                                                                                                                                                          break Success115;
                                                                                                                                                                      }
                                                                                                                                                                      term = term35;
                                                                                                                                                                    }
                                                                                                                                                                    if(cons4 == NormalizeAST_str._consASCIIEsc_1)
                                                                                                                                                                    { 
                                                                                                                                                                      IStrategoTerm arg140 = term.getSubterm(0);
                                                                                                                                                                      if(arg140.getTermType() != IStrategoTerm.STRING || !"CR".equals(((IStrategoString)arg140).stringValue()))
                                                                                                                                                                        break Fail47;
                                                                                                                                                                      term = NormalizeAST_str.constCharEsc6;
                                                                                                                                                                    }
                                                                                                                                                                    else
                                                                                                                                                                    { 
                                                                                                                                                                      break Fail47;
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