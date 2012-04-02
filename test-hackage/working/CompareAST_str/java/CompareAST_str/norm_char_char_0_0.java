package CompareAST_str;

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
    Fail29:
    { 
      IStrategoTerm term23 = term;
      IStrategoConstructor cons2 = term.getTermType() == IStrategoTerm.APPL ? ((IStrategoAppl)term).getConstructor() : null;
      Success23:
      { 
        if(cons2 == CompareAST_str._consEscape_1)
        { 
          Fail30:
          { 
            IStrategoTerm arg56 = term.getSubterm(0);
            if(arg56.getTermType() != IStrategoTerm.APPL || CompareAST_str._consCharEsc_1 != ((IStrategoAppl)arg56).getConstructor())
              break Fail30;
            IStrategoTerm arg57 = arg56.getSubterm(0);
            if(arg57.getTermType() != IStrategoTerm.STRING || !"\"".equals(((IStrategoString)arg57).stringValue()))
              break Fail30;
            term = CompareAST_str.const0;
            if(true)
              break Success23;
          }
          term = term23;
        }
        Success24:
        { 
          if(cons2 == CompareAST_str._consEscape_1)
          { 
            Fail31:
            { 
              IStrategoTerm arg58 = term.getSubterm(0);
              if(arg58.getTermType() != IStrategoTerm.APPL || CompareAST_str._consASCIIEsc_1 != ((IStrategoAppl)arg58).getConstructor())
                break Fail31;
              IStrategoTerm arg59 = arg58.getSubterm(0);
              if(arg59.getTermType() != IStrategoTerm.STRING || !"SP".equals(((IStrategoString)arg59).stringValue()))
                break Fail31;
              term = CompareAST_str.const1;
              if(true)
                break Success24;
            }
            term = term23;
          }
          Success25:
          { 
            if(cons2 == CompareAST_str._consEscape_1)
            { 
              Fail32:
              { 
                IStrategoTerm n_4066 = null;
                n_4066 = term.getSubterm(0);
                term = this.invoke(context, n_4066);
                if(term == null)
                  break Fail32;
                term = termFactory.makeAppl(CompareAST_str._consEscape_1, new IStrategoTerm[]{term});
                if(true)
                  break Success25;
              }
              term = term23;
            }
            Success26:
            { 
              if(cons2 == CompareAST_str._consHexadecimalEsc_1)
              { 
                Fail33:
                { 
                  IStrategoTerm k_4066 = null;
                  k_4066 = term.getSubterm(0);
                  term = hex_string_to_int_0_0.instance.invoke(context, k_4066);
                  if(term == null)
                    break Fail33;
                  term = int_to_string_0_0.instance.invoke(context, term);
                  if(term == null)
                    break Fail33;
                  term = termFactory.makeAppl(CompareAST_str._consDecimalEsc_1, new IStrategoTerm[]{term});
                  if(true)
                    break Success26;
                }
                term = term23;
              }
              Success27:
              { 
                if(cons2 == CompareAST_str._consOctalEsc_1)
                { 
                  Fail34:
                  { 
                    IStrategoTerm h_4066 = null;
                    h_4066 = term.getSubterm(0);
                    term = oct_string_to_int_0_0.instance.invoke(context, h_4066);
                    if(term == null)
                      break Fail34;
                    term = int_to_string_0_0.instance.invoke(context, term);
                    if(term == null)
                      break Fail34;
                    term = termFactory.makeAppl(CompareAST_str._consDecimalEsc_1, new IStrategoTerm[]{term});
                    if(true)
                      break Success27;
                  }
                  term = term23;
                }
                Success28:
                { 
                  if(cons2 == CompareAST_str._consDecimalEsc_1)
                  { 
                    Fail35:
                    { 
                      IStrategoTerm d_4066 = null;
                      d_4066 = term.getSubterm(0);
                      term = string_to_int_0_0.instance.invoke(context, d_4066);
                      if(term == null)
                        break Fail35;
                      term = is_ascii_0_0.instance.invoke(context, term);
                      if(term == null)
                        break Fail35;
                      term = (IStrategoTerm)termFactory.makeListCons(term, (IStrategoList)CompareAST_str.constNil0);
                      term = implode_string_0_0.instance.invoke(context, term);
                      if(term == null)
                        break Fail35;
                      if(true)
                        break Success28;
                    }
                    term = term23;
                  }
                  Success29:
                  { 
                    if(cons2 == CompareAST_str._consDecimalEsc_1)
                    { 
                      Fail36:
                      { 
                        IStrategoTerm arg60 = term.getSubterm(0);
                        if(arg60.getTermType() != IStrategoTerm.STRING || !"0".equals(((IStrategoString)arg60).stringValue()))
                          break Fail36;
                        term = CompareAST_str.constASCIIEsc0;
                        if(true)
                          break Success29;
                      }
                      term = term23;
                    }
                    Success30:
                    { 
                      if(cons2 == CompareAST_str._consDecimalEsc_1)
                      { 
                        Fail37:
                        { 
                          IStrategoTerm arg61 = term.getSubterm(0);
                          if(arg61.getTermType() != IStrategoTerm.STRING || !"1".equals(((IStrategoString)arg61).stringValue()))
                            break Fail37;
                          term = CompareAST_str.constASCIIEsc1;
                          if(true)
                            break Success30;
                        }
                        term = term23;
                      }
                      Success31:
                      { 
                        if(cons2 == CompareAST_str._consDecimalEsc_1)
                        { 
                          Fail38:
                          { 
                            IStrategoTerm arg62 = term.getSubterm(0);
                            if(arg62.getTermType() != IStrategoTerm.STRING || !"2".equals(((IStrategoString)arg62).stringValue()))
                              break Fail38;
                            term = CompareAST_str.constASCIIEsc2;
                            if(true)
                              break Success31;
                          }
                          term = term23;
                        }
                        Success32:
                        { 
                          if(cons2 == CompareAST_str._consDecimalEsc_1)
                          { 
                            Fail39:
                            { 
                              IStrategoTerm arg63 = term.getSubterm(0);
                              if(arg63.getTermType() != IStrategoTerm.STRING || !"3".equals(((IStrategoString)arg63).stringValue()))
                                break Fail39;
                              term = CompareAST_str.constASCIIEsc3;
                              if(true)
                                break Success32;
                            }
                            term = term23;
                          }
                          Success33:
                          { 
                            if(cons2 == CompareAST_str._consDecimalEsc_1)
                            { 
                              Fail40:
                              { 
                                IStrategoTerm arg64 = term.getSubterm(0);
                                if(arg64.getTermType() != IStrategoTerm.STRING || !"4".equals(((IStrategoString)arg64).stringValue()))
                                  break Fail40;
                                term = CompareAST_str.constASCIIEsc4;
                                if(true)
                                  break Success33;
                              }
                              term = term23;
                            }
                            Success34:
                            { 
                              if(cons2 == CompareAST_str._consDecimalEsc_1)
                              { 
                                Fail41:
                                { 
                                  IStrategoTerm arg65 = term.getSubterm(0);
                                  if(arg65.getTermType() != IStrategoTerm.STRING || !"5".equals(((IStrategoString)arg65).stringValue()))
                                    break Fail41;
                                  term = CompareAST_str.constASCIIEsc5;
                                  if(true)
                                    break Success34;
                                }
                                term = term23;
                              }
                              Success35:
                              { 
                                if(cons2 == CompareAST_str._consDecimalEsc_1)
                                { 
                                  Fail42:
                                  { 
                                    IStrategoTerm arg66 = term.getSubterm(0);
                                    if(arg66.getTermType() != IStrategoTerm.STRING || !"6".equals(((IStrategoString)arg66).stringValue()))
                                      break Fail42;
                                    term = CompareAST_str.constASCIIEsc6;
                                    if(true)
                                      break Success35;
                                  }
                                  term = term23;
                                }
                                Success36:
                                { 
                                  if(cons2 == CompareAST_str._consDecimalEsc_1)
                                  { 
                                    Fail43:
                                    { 
                                      IStrategoTerm arg67 = term.getSubterm(0);
                                      if(arg67.getTermType() != IStrategoTerm.STRING || !"7".equals(((IStrategoString)arg67).stringValue()))
                                        break Fail43;
                                      term = CompareAST_str.constCharEsc0;
                                      if(true)
                                        break Success36;
                                    }
                                    term = term23;
                                  }
                                  Success37:
                                  { 
                                    if(cons2 == CompareAST_str._consDecimalEsc_1)
                                    { 
                                      Fail44:
                                      { 
                                        IStrategoTerm arg68 = term.getSubterm(0);
                                        if(arg68.getTermType() != IStrategoTerm.STRING || !"8".equals(((IStrategoString)arg68).stringValue()))
                                          break Fail44;
                                        term = CompareAST_str.constCharEsc1;
                                        if(true)
                                          break Success37;
                                      }
                                      term = term23;
                                    }
                                    Success38:
                                    { 
                                      if(cons2 == CompareAST_str._consDecimalEsc_1)
                                      { 
                                        Fail45:
                                        { 
                                          IStrategoTerm arg69 = term.getSubterm(0);
                                          if(arg69.getTermType() != IStrategoTerm.STRING || !"9".equals(((IStrategoString)arg69).stringValue()))
                                            break Fail45;
                                          term = CompareAST_str.constCharEsc2;
                                          if(true)
                                            break Success38;
                                        }
                                        term = term23;
                                      }
                                      Success39:
                                      { 
                                        if(cons2 == CompareAST_str._consDecimalEsc_1)
                                        { 
                                          Fail46:
                                          { 
                                            IStrategoTerm arg70 = term.getSubterm(0);
                                            if(arg70.getTermType() != IStrategoTerm.STRING || !"10".equals(((IStrategoString)arg70).stringValue()))
                                              break Fail46;
                                            term = CompareAST_str.constCharEsc3;
                                            if(true)
                                              break Success39;
                                          }
                                          term = term23;
                                        }
                                        Success40:
                                        { 
                                          if(cons2 == CompareAST_str._consDecimalEsc_1)
                                          { 
                                            Fail47:
                                            { 
                                              IStrategoTerm arg71 = term.getSubterm(0);
                                              if(arg71.getTermType() != IStrategoTerm.STRING || !"11".equals(((IStrategoString)arg71).stringValue()))
                                                break Fail47;
                                              term = CompareAST_str.constCharEsc4;
                                              if(true)
                                                break Success40;
                                            }
                                            term = term23;
                                          }
                                          Success41:
                                          { 
                                            if(cons2 == CompareAST_str._consDecimalEsc_1)
                                            { 
                                              Fail48:
                                              { 
                                                IStrategoTerm arg72 = term.getSubterm(0);
                                                if(arg72.getTermType() != IStrategoTerm.STRING || !"12".equals(((IStrategoString)arg72).stringValue()))
                                                  break Fail48;
                                                term = CompareAST_str.constCharEsc5;
                                                if(true)
                                                  break Success41;
                                              }
                                              term = term23;
                                            }
                                            Success42:
                                            { 
                                              if(cons2 == CompareAST_str._consDecimalEsc_1)
                                              { 
                                                Fail49:
                                                { 
                                                  IStrategoTerm arg73 = term.getSubterm(0);
                                                  if(arg73.getTermType() != IStrategoTerm.STRING || !"13".equals(((IStrategoString)arg73).stringValue()))
                                                    break Fail49;
                                                  term = CompareAST_str.constCharEsc6;
                                                  if(true)
                                                    break Success42;
                                                }
                                                term = term23;
                                              }
                                              Success43:
                                              { 
                                                if(cons2 == CompareAST_str._consDecimalEsc_1)
                                                { 
                                                  Fail50:
                                                  { 
                                                    IStrategoTerm arg74 = term.getSubterm(0);
                                                    if(arg74.getTermType() != IStrategoTerm.STRING || !"14".equals(((IStrategoString)arg74).stringValue()))
                                                      break Fail50;
                                                    term = CompareAST_str.constASCIIEsc7;
                                                    if(true)
                                                      break Success43;
                                                  }
                                                  term = term23;
                                                }
                                                Success44:
                                                { 
                                                  if(cons2 == CompareAST_str._consDecimalEsc_1)
                                                  { 
                                                    Fail51:
                                                    { 
                                                      IStrategoTerm arg75 = term.getSubterm(0);
                                                      if(arg75.getTermType() != IStrategoTerm.STRING || !"15".equals(((IStrategoString)arg75).stringValue()))
                                                        break Fail51;
                                                      term = CompareAST_str.constASCIIEsc8;
                                                      if(true)
                                                        break Success44;
                                                    }
                                                    term = term23;
                                                  }
                                                  Success45:
                                                  { 
                                                    if(cons2 == CompareAST_str._consDecimalEsc_1)
                                                    { 
                                                      Fail52:
                                                      { 
                                                        IStrategoTerm arg76 = term.getSubterm(0);
                                                        if(arg76.getTermType() != IStrategoTerm.STRING || !"16".equals(((IStrategoString)arg76).stringValue()))
                                                          break Fail52;
                                                        term = CompareAST_str.constASCIIEsc9;
                                                        if(true)
                                                          break Success45;
                                                      }
                                                      term = term23;
                                                    }
                                                    Success46:
                                                    { 
                                                      if(cons2 == CompareAST_str._consDecimalEsc_1)
                                                      { 
                                                        Fail53:
                                                        { 
                                                          IStrategoTerm arg77 = term.getSubterm(0);
                                                          if(arg77.getTermType() != IStrategoTerm.STRING || !"17".equals(((IStrategoString)arg77).stringValue()))
                                                            break Fail53;
                                                          term = CompareAST_str.constASCIIEsc10;
                                                          if(true)
                                                            break Success46;
                                                        }
                                                        term = term23;
                                                      }
                                                      Success47:
                                                      { 
                                                        if(cons2 == CompareAST_str._consDecimalEsc_1)
                                                        { 
                                                          Fail54:
                                                          { 
                                                            IStrategoTerm arg78 = term.getSubterm(0);
                                                            if(arg78.getTermType() != IStrategoTerm.STRING || !"18".equals(((IStrategoString)arg78).stringValue()))
                                                              break Fail54;
                                                            term = CompareAST_str.constASCIIEsc11;
                                                            if(true)
                                                              break Success47;
                                                          }
                                                          term = term23;
                                                        }
                                                        Success48:
                                                        { 
                                                          if(cons2 == CompareAST_str._consDecimalEsc_1)
                                                          { 
                                                            Fail55:
                                                            { 
                                                              IStrategoTerm arg79 = term.getSubterm(0);
                                                              if(arg79.getTermType() != IStrategoTerm.STRING || !"19".equals(((IStrategoString)arg79).stringValue()))
                                                                break Fail55;
                                                              term = CompareAST_str.constASCIIEsc12;
                                                              if(true)
                                                                break Success48;
                                                            }
                                                            term = term23;
                                                          }
                                                          Success49:
                                                          { 
                                                            if(cons2 == CompareAST_str._consDecimalEsc_1)
                                                            { 
                                                              Fail56:
                                                              { 
                                                                IStrategoTerm arg80 = term.getSubterm(0);
                                                                if(arg80.getTermType() != IStrategoTerm.STRING || !"20".equals(((IStrategoString)arg80).stringValue()))
                                                                  break Fail56;
                                                                term = CompareAST_str.constASCIIEsc13;
                                                                if(true)
                                                                  break Success49;
                                                              }
                                                              term = term23;
                                                            }
                                                            Success50:
                                                            { 
                                                              if(cons2 == CompareAST_str._consDecimalEsc_1)
                                                              { 
                                                                Fail57:
                                                                { 
                                                                  IStrategoTerm arg81 = term.getSubterm(0);
                                                                  if(arg81.getTermType() != IStrategoTerm.STRING || !"21".equals(((IStrategoString)arg81).stringValue()))
                                                                    break Fail57;
                                                                  term = CompareAST_str.constASCIIEsc14;
                                                                  if(true)
                                                                    break Success50;
                                                                }
                                                                term = term23;
                                                              }
                                                              Success51:
                                                              { 
                                                                if(cons2 == CompareAST_str._consDecimalEsc_1)
                                                                { 
                                                                  Fail58:
                                                                  { 
                                                                    IStrategoTerm arg82 = term.getSubterm(0);
                                                                    if(arg82.getTermType() != IStrategoTerm.STRING || !"22".equals(((IStrategoString)arg82).stringValue()))
                                                                      break Fail58;
                                                                    term = CompareAST_str.constASCIIEsc15;
                                                                    if(true)
                                                                      break Success51;
                                                                  }
                                                                  term = term23;
                                                                }
                                                                Success52:
                                                                { 
                                                                  if(cons2 == CompareAST_str._consDecimalEsc_1)
                                                                  { 
                                                                    Fail59:
                                                                    { 
                                                                      IStrategoTerm arg83 = term.getSubterm(0);
                                                                      if(arg83.getTermType() != IStrategoTerm.STRING || !"23".equals(((IStrategoString)arg83).stringValue()))
                                                                        break Fail59;
                                                                      term = CompareAST_str.constASCIIEsc16;
                                                                      if(true)
                                                                        break Success52;
                                                                    }
                                                                    term = term23;
                                                                  }
                                                                  Success53:
                                                                  { 
                                                                    if(cons2 == CompareAST_str._consDecimalEsc_1)
                                                                    { 
                                                                      Fail60:
                                                                      { 
                                                                        IStrategoTerm arg84 = term.getSubterm(0);
                                                                        if(arg84.getTermType() != IStrategoTerm.STRING || !"24".equals(((IStrategoString)arg84).stringValue()))
                                                                          break Fail60;
                                                                        term = CompareAST_str.constASCIIEsc17;
                                                                        if(true)
                                                                          break Success53;
                                                                      }
                                                                      term = term23;
                                                                    }
                                                                    Success54:
                                                                    { 
                                                                      if(cons2 == CompareAST_str._consDecimalEsc_1)
                                                                      { 
                                                                        Fail61:
                                                                        { 
                                                                          IStrategoTerm arg85 = term.getSubterm(0);
                                                                          if(arg85.getTermType() != IStrategoTerm.STRING || !"25".equals(((IStrategoString)arg85).stringValue()))
                                                                            break Fail61;
                                                                          term = CompareAST_str.constASCIIEsc18;
                                                                          if(true)
                                                                            break Success54;
                                                                        }
                                                                        term = term23;
                                                                      }
                                                                      Success55:
                                                                      { 
                                                                        if(cons2 == CompareAST_str._consDecimalEsc_1)
                                                                        { 
                                                                          Fail62:
                                                                          { 
                                                                            IStrategoTerm arg86 = term.getSubterm(0);
                                                                            if(arg86.getTermType() != IStrategoTerm.STRING || !"26".equals(((IStrategoString)arg86).stringValue()))
                                                                              break Fail62;
                                                                            term = CompareAST_str.constASCIIEsc19;
                                                                            if(true)
                                                                              break Success55;
                                                                          }
                                                                          term = term23;
                                                                        }
                                                                        Success56:
                                                                        { 
                                                                          if(cons2 == CompareAST_str._consDecimalEsc_1)
                                                                          { 
                                                                            Fail63:
                                                                            { 
                                                                              IStrategoTerm arg87 = term.getSubterm(0);
                                                                              if(arg87.getTermType() != IStrategoTerm.STRING || !"27".equals(((IStrategoString)arg87).stringValue()))
                                                                                break Fail63;
                                                                              term = CompareAST_str.constASCIIEsc20;
                                                                              if(true)
                                                                                break Success56;
                                                                            }
                                                                            term = term23;
                                                                          }
                                                                          Success57:
                                                                          { 
                                                                            if(cons2 == CompareAST_str._consDecimalEsc_1)
                                                                            { 
                                                                              Fail64:
                                                                              { 
                                                                                IStrategoTerm arg88 = term.getSubterm(0);
                                                                                if(arg88.getTermType() != IStrategoTerm.STRING || !"28".equals(((IStrategoString)arg88).stringValue()))
                                                                                  break Fail64;
                                                                                term = CompareAST_str.constASCIIEsc21;
                                                                                if(true)
                                                                                  break Success57;
                                                                              }
                                                                              term = term23;
                                                                            }
                                                                            Success58:
                                                                            { 
                                                                              if(cons2 == CompareAST_str._consDecimalEsc_1)
                                                                              { 
                                                                                Fail65:
                                                                                { 
                                                                                  IStrategoTerm arg89 = term.getSubterm(0);
                                                                                  if(arg89.getTermType() != IStrategoTerm.STRING || !"29".equals(((IStrategoString)arg89).stringValue()))
                                                                                    break Fail65;
                                                                                  term = CompareAST_str.constASCIIEsc22;
                                                                                  if(true)
                                                                                    break Success58;
                                                                                }
                                                                                term = term23;
                                                                              }
                                                                              Success59:
                                                                              { 
                                                                                if(cons2 == CompareAST_str._consDecimalEsc_1)
                                                                                { 
                                                                                  Fail66:
                                                                                  { 
                                                                                    IStrategoTerm arg90 = term.getSubterm(0);
                                                                                    if(arg90.getTermType() != IStrategoTerm.STRING || !"30".equals(((IStrategoString)arg90).stringValue()))
                                                                                      break Fail66;
                                                                                    term = CompareAST_str.constASCIIEsc23;
                                                                                    if(true)
                                                                                      break Success59;
                                                                                  }
                                                                                  term = term23;
                                                                                }
                                                                                Success60:
                                                                                { 
                                                                                  if(cons2 == CompareAST_str._consDecimalEsc_1)
                                                                                  { 
                                                                                    Fail67:
                                                                                    { 
                                                                                      IStrategoTerm arg91 = term.getSubterm(0);
                                                                                      if(arg91.getTermType() != IStrategoTerm.STRING || !"31".equals(((IStrategoString)arg91).stringValue()))
                                                                                        break Fail67;
                                                                                      term = CompareAST_str.constASCIIEsc24;
                                                                                      if(true)
                                                                                        break Success60;
                                                                                    }
                                                                                    term = term23;
                                                                                  }
                                                                                  Success61:
                                                                                  { 
                                                                                    if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                    { 
                                                                                      Fail68:
                                                                                      { 
                                                                                        IStrategoTerm arg92 = term.getSubterm(0);
                                                                                        if(arg92.getTermType() != IStrategoTerm.STRING || !"^@".equals(((IStrategoString)arg92).stringValue()))
                                                                                          break Fail68;
                                                                                        term = CompareAST_str.constASCIIEsc0;
                                                                                        if(true)
                                                                                          break Success61;
                                                                                      }
                                                                                      term = term23;
                                                                                    }
                                                                                    Success62:
                                                                                    { 
                                                                                      if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                      { 
                                                                                        Fail69:
                                                                                        { 
                                                                                          IStrategoTerm arg93 = term.getSubterm(0);
                                                                                          if(arg93.getTermType() != IStrategoTerm.STRING || !"^A".equals(((IStrategoString)arg93).stringValue()))
                                                                                            break Fail69;
                                                                                          term = CompareAST_str.constASCIIEsc1;
                                                                                          if(true)
                                                                                            break Success62;
                                                                                        }
                                                                                        term = term23;
                                                                                      }
                                                                                      Success63:
                                                                                      { 
                                                                                        if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                        { 
                                                                                          Fail70:
                                                                                          { 
                                                                                            IStrategoTerm arg94 = term.getSubterm(0);
                                                                                            if(arg94.getTermType() != IStrategoTerm.STRING || !"^B".equals(((IStrategoString)arg94).stringValue()))
                                                                                              break Fail70;
                                                                                            term = CompareAST_str.constASCIIEsc2;
                                                                                            if(true)
                                                                                              break Success63;
                                                                                          }
                                                                                          term = term23;
                                                                                        }
                                                                                        Success64:
                                                                                        { 
                                                                                          if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                          { 
                                                                                            Fail71:
                                                                                            { 
                                                                                              IStrategoTerm arg95 = term.getSubterm(0);
                                                                                              if(arg95.getTermType() != IStrategoTerm.STRING || !"^C".equals(((IStrategoString)arg95).stringValue()))
                                                                                                break Fail71;
                                                                                              term = CompareAST_str.constASCIIEsc3;
                                                                                              if(true)
                                                                                                break Success64;
                                                                                            }
                                                                                            term = term23;
                                                                                          }
                                                                                          Success65:
                                                                                          { 
                                                                                            if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                            { 
                                                                                              Fail72:
                                                                                              { 
                                                                                                IStrategoTerm arg96 = term.getSubterm(0);
                                                                                                if(arg96.getTermType() != IStrategoTerm.STRING || !"^D".equals(((IStrategoString)arg96).stringValue()))
                                                                                                  break Fail72;
                                                                                                term = CompareAST_str.constASCIIEsc4;
                                                                                                if(true)
                                                                                                  break Success65;
                                                                                              }
                                                                                              term = term23;
                                                                                            }
                                                                                            Success66:
                                                                                            { 
                                                                                              if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                              { 
                                                                                                Fail73:
                                                                                                { 
                                                                                                  IStrategoTerm arg97 = term.getSubterm(0);
                                                                                                  if(arg97.getTermType() != IStrategoTerm.STRING || !"^E".equals(((IStrategoString)arg97).stringValue()))
                                                                                                    break Fail73;
                                                                                                  term = CompareAST_str.constASCIIEsc5;
                                                                                                  if(true)
                                                                                                    break Success66;
                                                                                                }
                                                                                                term = term23;
                                                                                              }
                                                                                              Success67:
                                                                                              { 
                                                                                                if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                                { 
                                                                                                  Fail74:
                                                                                                  { 
                                                                                                    IStrategoTerm arg98 = term.getSubterm(0);
                                                                                                    if(arg98.getTermType() != IStrategoTerm.STRING || !"^F".equals(((IStrategoString)arg98).stringValue()))
                                                                                                      break Fail74;
                                                                                                    term = CompareAST_str.constASCIIEsc6;
                                                                                                    if(true)
                                                                                                      break Success67;
                                                                                                  }
                                                                                                  term = term23;
                                                                                                }
                                                                                                Success68:
                                                                                                { 
                                                                                                  if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                                  { 
                                                                                                    Fail75:
                                                                                                    { 
                                                                                                      IStrategoTerm arg99 = term.getSubterm(0);
                                                                                                      if(arg99.getTermType() != IStrategoTerm.STRING || !"^G".equals(((IStrategoString)arg99).stringValue()))
                                                                                                        break Fail75;
                                                                                                      term = CompareAST_str.constCharEsc0;
                                                                                                      if(true)
                                                                                                        break Success68;
                                                                                                    }
                                                                                                    term = term23;
                                                                                                  }
                                                                                                  Success69:
                                                                                                  { 
                                                                                                    if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                                    { 
                                                                                                      Fail76:
                                                                                                      { 
                                                                                                        IStrategoTerm arg100 = term.getSubterm(0);
                                                                                                        if(arg100.getTermType() != IStrategoTerm.STRING || !"^H".equals(((IStrategoString)arg100).stringValue()))
                                                                                                          break Fail76;
                                                                                                        term = CompareAST_str.constCharEsc1;
                                                                                                        if(true)
                                                                                                          break Success69;
                                                                                                      }
                                                                                                      term = term23;
                                                                                                    }
                                                                                                    Success70:
                                                                                                    { 
                                                                                                      if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                                      { 
                                                                                                        Fail77:
                                                                                                        { 
                                                                                                          IStrategoTerm arg101 = term.getSubterm(0);
                                                                                                          if(arg101.getTermType() != IStrategoTerm.STRING || !"^I".equals(((IStrategoString)arg101).stringValue()))
                                                                                                            break Fail77;
                                                                                                          term = CompareAST_str.constCharEsc2;
                                                                                                          if(true)
                                                                                                            break Success70;
                                                                                                        }
                                                                                                        term = term23;
                                                                                                      }
                                                                                                      Success71:
                                                                                                      { 
                                                                                                        if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                                        { 
                                                                                                          Fail78:
                                                                                                          { 
                                                                                                            IStrategoTerm arg102 = term.getSubterm(0);
                                                                                                            if(arg102.getTermType() != IStrategoTerm.STRING || !"^J".equals(((IStrategoString)arg102).stringValue()))
                                                                                                              break Fail78;
                                                                                                            term = CompareAST_str.constCharEsc3;
                                                                                                            if(true)
                                                                                                              break Success71;
                                                                                                          }
                                                                                                          term = term23;
                                                                                                        }
                                                                                                        Success72:
                                                                                                        { 
                                                                                                          if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                                          { 
                                                                                                            Fail79:
                                                                                                            { 
                                                                                                              IStrategoTerm arg103 = term.getSubterm(0);
                                                                                                              if(arg103.getTermType() != IStrategoTerm.STRING || !"^K".equals(((IStrategoString)arg103).stringValue()))
                                                                                                                break Fail79;
                                                                                                              term = CompareAST_str.constCharEsc4;
                                                                                                              if(true)
                                                                                                                break Success72;
                                                                                                            }
                                                                                                            term = term23;
                                                                                                          }
                                                                                                          Success73:
                                                                                                          { 
                                                                                                            if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                                            { 
                                                                                                              Fail80:
                                                                                                              { 
                                                                                                                IStrategoTerm arg104 = term.getSubterm(0);
                                                                                                                if(arg104.getTermType() != IStrategoTerm.STRING || !"^L".equals(((IStrategoString)arg104).stringValue()))
                                                                                                                  break Fail80;
                                                                                                                term = CompareAST_str.constCharEsc5;
                                                                                                                if(true)
                                                                                                                  break Success73;
                                                                                                              }
                                                                                                              term = term23;
                                                                                                            }
                                                                                                            Success74:
                                                                                                            { 
                                                                                                              if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                                              { 
                                                                                                                Fail81:
                                                                                                                { 
                                                                                                                  IStrategoTerm arg105 = term.getSubterm(0);
                                                                                                                  if(arg105.getTermType() != IStrategoTerm.STRING || !"^M".equals(((IStrategoString)arg105).stringValue()))
                                                                                                                    break Fail81;
                                                                                                                  term = CompareAST_str.constCharEsc6;
                                                                                                                  if(true)
                                                                                                                    break Success74;
                                                                                                                }
                                                                                                                term = term23;
                                                                                                              }
                                                                                                              Success75:
                                                                                                              { 
                                                                                                                if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                                                { 
                                                                                                                  Fail82:
                                                                                                                  { 
                                                                                                                    IStrategoTerm arg106 = term.getSubterm(0);
                                                                                                                    if(arg106.getTermType() != IStrategoTerm.STRING || !"^N".equals(((IStrategoString)arg106).stringValue()))
                                                                                                                      break Fail82;
                                                                                                                    term = CompareAST_str.constASCIIEsc7;
                                                                                                                    if(true)
                                                                                                                      break Success75;
                                                                                                                  }
                                                                                                                  term = term23;
                                                                                                                }
                                                                                                                Success76:
                                                                                                                { 
                                                                                                                  if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                                                  { 
                                                                                                                    Fail83:
                                                                                                                    { 
                                                                                                                      IStrategoTerm arg107 = term.getSubterm(0);
                                                                                                                      if(arg107.getTermType() != IStrategoTerm.STRING || !"^O".equals(((IStrategoString)arg107).stringValue()))
                                                                                                                        break Fail83;
                                                                                                                      term = CompareAST_str.constASCIIEsc8;
                                                                                                                      if(true)
                                                                                                                        break Success76;
                                                                                                                    }
                                                                                                                    term = term23;
                                                                                                                  }
                                                                                                                  Success77:
                                                                                                                  { 
                                                                                                                    if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                                                    { 
                                                                                                                      Fail84:
                                                                                                                      { 
                                                                                                                        IStrategoTerm arg108 = term.getSubterm(0);
                                                                                                                        if(arg108.getTermType() != IStrategoTerm.STRING || !"^P".equals(((IStrategoString)arg108).stringValue()))
                                                                                                                          break Fail84;
                                                                                                                        term = CompareAST_str.constASCIIEsc9;
                                                                                                                        if(true)
                                                                                                                          break Success77;
                                                                                                                      }
                                                                                                                      term = term23;
                                                                                                                    }
                                                                                                                    Success78:
                                                                                                                    { 
                                                                                                                      if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                                                      { 
                                                                                                                        Fail85:
                                                                                                                        { 
                                                                                                                          IStrategoTerm arg109 = term.getSubterm(0);
                                                                                                                          if(arg109.getTermType() != IStrategoTerm.STRING || !"^Q".equals(((IStrategoString)arg109).stringValue()))
                                                                                                                            break Fail85;
                                                                                                                          term = CompareAST_str.constASCIIEsc10;
                                                                                                                          if(true)
                                                                                                                            break Success78;
                                                                                                                        }
                                                                                                                        term = term23;
                                                                                                                      }
                                                                                                                      Success79:
                                                                                                                      { 
                                                                                                                        if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                                                        { 
                                                                                                                          Fail86:
                                                                                                                          { 
                                                                                                                            IStrategoTerm arg110 = term.getSubterm(0);
                                                                                                                            if(arg110.getTermType() != IStrategoTerm.STRING || !"^R".equals(((IStrategoString)arg110).stringValue()))
                                                                                                                              break Fail86;
                                                                                                                            term = CompareAST_str.constASCIIEsc11;
                                                                                                                            if(true)
                                                                                                                              break Success79;
                                                                                                                          }
                                                                                                                          term = term23;
                                                                                                                        }
                                                                                                                        Success80:
                                                                                                                        { 
                                                                                                                          if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                                                          { 
                                                                                                                            Fail87:
                                                                                                                            { 
                                                                                                                              IStrategoTerm arg111 = term.getSubterm(0);
                                                                                                                              if(arg111.getTermType() != IStrategoTerm.STRING || !"^S".equals(((IStrategoString)arg111).stringValue()))
                                                                                                                                break Fail87;
                                                                                                                              term = CompareAST_str.constASCIIEsc12;
                                                                                                                              if(true)
                                                                                                                                break Success80;
                                                                                                                            }
                                                                                                                            term = term23;
                                                                                                                          }
                                                                                                                          Success81:
                                                                                                                          { 
                                                                                                                            if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                                                            { 
                                                                                                                              Fail88:
                                                                                                                              { 
                                                                                                                                IStrategoTerm arg112 = term.getSubterm(0);
                                                                                                                                if(arg112.getTermType() != IStrategoTerm.STRING || !"^T".equals(((IStrategoString)arg112).stringValue()))
                                                                                                                                  break Fail88;
                                                                                                                                term = CompareAST_str.constASCIIEsc13;
                                                                                                                                if(true)
                                                                                                                                  break Success81;
                                                                                                                              }
                                                                                                                              term = term23;
                                                                                                                            }
                                                                                                                            Success82:
                                                                                                                            { 
                                                                                                                              if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                                                              { 
                                                                                                                                Fail89:
                                                                                                                                { 
                                                                                                                                  IStrategoTerm arg113 = term.getSubterm(0);
                                                                                                                                  if(arg113.getTermType() != IStrategoTerm.STRING || !"^U".equals(((IStrategoString)arg113).stringValue()))
                                                                                                                                    break Fail89;
                                                                                                                                  term = CompareAST_str.constASCIIEsc14;
                                                                                                                                  if(true)
                                                                                                                                    break Success82;
                                                                                                                                }
                                                                                                                                term = term23;
                                                                                                                              }
                                                                                                                              Success83:
                                                                                                                              { 
                                                                                                                                if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                                                                { 
                                                                                                                                  Fail90:
                                                                                                                                  { 
                                                                                                                                    IStrategoTerm arg114 = term.getSubterm(0);
                                                                                                                                    if(arg114.getTermType() != IStrategoTerm.STRING || !"^V".equals(((IStrategoString)arg114).stringValue()))
                                                                                                                                      break Fail90;
                                                                                                                                    term = CompareAST_str.constASCIIEsc15;
                                                                                                                                    if(true)
                                                                                                                                      break Success83;
                                                                                                                                  }
                                                                                                                                  term = term23;
                                                                                                                                }
                                                                                                                                Success84:
                                                                                                                                { 
                                                                                                                                  if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                                                                  { 
                                                                                                                                    Fail91:
                                                                                                                                    { 
                                                                                                                                      IStrategoTerm arg115 = term.getSubterm(0);
                                                                                                                                      if(arg115.getTermType() != IStrategoTerm.STRING || !"^W".equals(((IStrategoString)arg115).stringValue()))
                                                                                                                                        break Fail91;
                                                                                                                                      term = CompareAST_str.constASCIIEsc16;
                                                                                                                                      if(true)
                                                                                                                                        break Success84;
                                                                                                                                    }
                                                                                                                                    term = term23;
                                                                                                                                  }
                                                                                                                                  Success85:
                                                                                                                                  { 
                                                                                                                                    if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                                                                    { 
                                                                                                                                      Fail92:
                                                                                                                                      { 
                                                                                                                                        IStrategoTerm arg116 = term.getSubterm(0);
                                                                                                                                        if(arg116.getTermType() != IStrategoTerm.STRING || !"^X".equals(((IStrategoString)arg116).stringValue()))
                                                                                                                                          break Fail92;
                                                                                                                                        term = CompareAST_str.constASCIIEsc17;
                                                                                                                                        if(true)
                                                                                                                                          break Success85;
                                                                                                                                      }
                                                                                                                                      term = term23;
                                                                                                                                    }
                                                                                                                                    Success86:
                                                                                                                                    { 
                                                                                                                                      if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                                                                      { 
                                                                                                                                        Fail93:
                                                                                                                                        { 
                                                                                                                                          IStrategoTerm arg117 = term.getSubterm(0);
                                                                                                                                          if(arg117.getTermType() != IStrategoTerm.STRING || !"^Y".equals(((IStrategoString)arg117).stringValue()))
                                                                                                                                            break Fail93;
                                                                                                                                          term = CompareAST_str.constASCIIEsc18;
                                                                                                                                          if(true)
                                                                                                                                            break Success86;
                                                                                                                                        }
                                                                                                                                        term = term23;
                                                                                                                                      }
                                                                                                                                      Success87:
                                                                                                                                      { 
                                                                                                                                        if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                                                                        { 
                                                                                                                                          Fail94:
                                                                                                                                          { 
                                                                                                                                            IStrategoTerm arg118 = term.getSubterm(0);
                                                                                                                                            if(arg118.getTermType() != IStrategoTerm.STRING || !"^Z".equals(((IStrategoString)arg118).stringValue()))
                                                                                                                                              break Fail94;
                                                                                                                                            term = CompareAST_str.constASCIIEsc19;
                                                                                                                                            if(true)
                                                                                                                                              break Success87;
                                                                                                                                          }
                                                                                                                                          term = term23;
                                                                                                                                        }
                                                                                                                                        Success88:
                                                                                                                                        { 
                                                                                                                                          if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                                                                          { 
                                                                                                                                            Fail95:
                                                                                                                                            { 
                                                                                                                                              IStrategoTerm arg119 = term.getSubterm(0);
                                                                                                                                              if(arg119.getTermType() != IStrategoTerm.STRING || !"^[".equals(((IStrategoString)arg119).stringValue()))
                                                                                                                                                break Fail95;
                                                                                                                                              term = CompareAST_str.constASCIIEsc20;
                                                                                                                                              if(true)
                                                                                                                                                break Success88;
                                                                                                                                            }
                                                                                                                                            term = term23;
                                                                                                                                          }
                                                                                                                                          Success89:
                                                                                                                                          { 
                                                                                                                                            if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                                                                            { 
                                                                                                                                              Fail96:
                                                                                                                                              { 
                                                                                                                                                IStrategoTerm arg120 = term.getSubterm(0);
                                                                                                                                                if(arg120.getTermType() != IStrategoTerm.STRING || !"^\\".equals(((IStrategoString)arg120).stringValue()))
                                                                                                                                                  break Fail96;
                                                                                                                                                term = CompareAST_str.constASCIIEsc21;
                                                                                                                                                if(true)
                                                                                                                                                  break Success89;
                                                                                                                                              }
                                                                                                                                              term = term23;
                                                                                                                                            }
                                                                                                                                            Success90:
                                                                                                                                            { 
                                                                                                                                              if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                                                                              { 
                                                                                                                                                Fail97:
                                                                                                                                                { 
                                                                                                                                                  IStrategoTerm arg121 = term.getSubterm(0);
                                                                                                                                                  if(arg121.getTermType() != IStrategoTerm.STRING || !"^]".equals(((IStrategoString)arg121).stringValue()))
                                                                                                                                                    break Fail97;
                                                                                                                                                  term = CompareAST_str.constASCIIEsc22;
                                                                                                                                                  if(true)
                                                                                                                                                    break Success90;
                                                                                                                                                }
                                                                                                                                                term = term23;
                                                                                                                                              }
                                                                                                                                              Success91:
                                                                                                                                              { 
                                                                                                                                                if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                                                                                { 
                                                                                                                                                  Fail98:
                                                                                                                                                  { 
                                                                                                                                                    IStrategoTerm arg122 = term.getSubterm(0);
                                                                                                                                                    if(arg122.getTermType() != IStrategoTerm.STRING || !"^^".equals(((IStrategoString)arg122).stringValue()))
                                                                                                                                                      break Fail98;
                                                                                                                                                    term = CompareAST_str.constASCIIEsc23;
                                                                                                                                                    if(true)
                                                                                                                                                      break Success91;
                                                                                                                                                  }
                                                                                                                                                  term = term23;
                                                                                                                                                }
                                                                                                                                                Success92:
                                                                                                                                                { 
                                                                                                                                                  if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                                                                                  { 
                                                                                                                                                    Fail99:
                                                                                                                                                    { 
                                                                                                                                                      IStrategoTerm arg123 = term.getSubterm(0);
                                                                                                                                                      if(arg123.getTermType() != IStrategoTerm.STRING || !"^_".equals(((IStrategoString)arg123).stringValue()))
                                                                                                                                                        break Fail99;
                                                                                                                                                      term = CompareAST_str.constASCIIEsc24;
                                                                                                                                                      if(true)
                                                                                                                                                        break Success92;
                                                                                                                                                    }
                                                                                                                                                    term = term23;
                                                                                                                                                  }
                                                                                                                                                  Success93:
                                                                                                                                                  { 
                                                                                                                                                    if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                                                                                    { 
                                                                                                                                                      Fail100:
                                                                                                                                                      { 
                                                                                                                                                        IStrategoTerm arg124 = term.getSubterm(0);
                                                                                                                                                        if(arg124.getTermType() != IStrategoTerm.STRING || !"BEL".equals(((IStrategoString)arg124).stringValue()))
                                                                                                                                                          break Fail100;
                                                                                                                                                        term = CompareAST_str.constCharEsc0;
                                                                                                                                                        if(true)
                                                                                                                                                          break Success93;
                                                                                                                                                      }
                                                                                                                                                      term = term23;
                                                                                                                                                    }
                                                                                                                                                    Success94:
                                                                                                                                                    { 
                                                                                                                                                      if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                                                                                      { 
                                                                                                                                                        Fail101:
                                                                                                                                                        { 
                                                                                                                                                          IStrategoTerm arg125 = term.getSubterm(0);
                                                                                                                                                          if(arg125.getTermType() != IStrategoTerm.STRING || !"BS".equals(((IStrategoString)arg125).stringValue()))
                                                                                                                                                            break Fail101;
                                                                                                                                                          term = CompareAST_str.constCharEsc1;
                                                                                                                                                          if(true)
                                                                                                                                                            break Success94;
                                                                                                                                                        }
                                                                                                                                                        term = term23;
                                                                                                                                                      }
                                                                                                                                                      Success95:
                                                                                                                                                      { 
                                                                                                                                                        if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                                                                                        { 
                                                                                                                                                          Fail102:
                                                                                                                                                          { 
                                                                                                                                                            IStrategoTerm arg126 = term.getSubterm(0);
                                                                                                                                                            if(arg126.getTermType() != IStrategoTerm.STRING || !"HT".equals(((IStrategoString)arg126).stringValue()))
                                                                                                                                                              break Fail102;
                                                                                                                                                            term = CompareAST_str.constCharEsc2;
                                                                                                                                                            if(true)
                                                                                                                                                              break Success95;
                                                                                                                                                          }
                                                                                                                                                          term = term23;
                                                                                                                                                        }
                                                                                                                                                        Success96:
                                                                                                                                                        { 
                                                                                                                                                          if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                                                                                          { 
                                                                                                                                                            Fail103:
                                                                                                                                                            { 
                                                                                                                                                              IStrategoTerm arg127 = term.getSubterm(0);
                                                                                                                                                              if(arg127.getTermType() != IStrategoTerm.STRING || !"LF".equals(((IStrategoString)arg127).stringValue()))
                                                                                                                                                                break Fail103;
                                                                                                                                                              term = CompareAST_str.constCharEsc3;
                                                                                                                                                              if(true)
                                                                                                                                                                break Success96;
                                                                                                                                                            }
                                                                                                                                                            term = term23;
                                                                                                                                                          }
                                                                                                                                                          Success97:
                                                                                                                                                          { 
                                                                                                                                                            if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                                                                                            { 
                                                                                                                                                              Fail104:
                                                                                                                                                              { 
                                                                                                                                                                IStrategoTerm arg128 = term.getSubterm(0);
                                                                                                                                                                if(arg128.getTermType() != IStrategoTerm.STRING || !"VT".equals(((IStrategoString)arg128).stringValue()))
                                                                                                                                                                  break Fail104;
                                                                                                                                                                term = CompareAST_str.constCharEsc4;
                                                                                                                                                                if(true)
                                                                                                                                                                  break Success97;
                                                                                                                                                              }
                                                                                                                                                              term = term23;
                                                                                                                                                            }
                                                                                                                                                            Success98:
                                                                                                                                                            { 
                                                                                                                                                              if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                                                                                              { 
                                                                                                                                                                Fail105:
                                                                                                                                                                { 
                                                                                                                                                                  IStrategoTerm arg129 = term.getSubterm(0);
                                                                                                                                                                  if(arg129.getTermType() != IStrategoTerm.STRING || !"FF".equals(((IStrategoString)arg129).stringValue()))
                                                                                                                                                                    break Fail105;
                                                                                                                                                                  term = CompareAST_str.constCharEsc5;
                                                                                                                                                                  if(true)
                                                                                                                                                                    break Success98;
                                                                                                                                                                }
                                                                                                                                                                term = term23;
                                                                                                                                                              }
                                                                                                                                                              if(cons2 == CompareAST_str._consASCIIEsc_1)
                                                                                                                                                              { 
                                                                                                                                                                IStrategoTerm arg130 = term.getSubterm(0);
                                                                                                                                                                if(arg130.getTermType() != IStrategoTerm.STRING || !"CR".equals(((IStrategoString)arg130).stringValue()))
                                                                                                                                                                  break Fail29;
                                                                                                                                                                term = CompareAST_str.constCharEsc6;
                                                                                                                                                              }
                                                                                                                                                              else
                                                                                                                                                              { 
                                                                                                                                                                break Fail29;
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