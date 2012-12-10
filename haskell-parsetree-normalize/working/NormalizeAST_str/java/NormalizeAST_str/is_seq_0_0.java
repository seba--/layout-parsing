package NormalizeAST_str;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.imp.debug.stratego.runtime.trans.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") public class is_seq_0_0 extends Strategy 
{ 
  public static is_seq_0_0 instance = new is_seq_0_0();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term)
  { 
    Fail42:
    { 
      IStrategoTerm term31 = term;
      Success31:
      { 
        Fail43:
        { 
          if(term.getTermType() != IStrategoTerm.STRING || !"ImportdeclSeq".equals(((IStrategoString)term).stringValue()))
            break Fail43;
          if(true)
            break Success31;
        }
        term = term31;
        IStrategoTerm term32 = term;
        Success32:
        { 
          Fail44:
          { 
            if(term.getTermType() != IStrategoTerm.STRING || !"TopdeclSeq".equals(((IStrategoString)term).stringValue()))
              break Fail44;
            if(true)
              break Success32;
          }
          term = term32;
          IStrategoTerm term33 = term;
          Success33:
          { 
            Fail45:
            { 
              if(term.getTermType() != IStrategoTerm.STRING || !"StmtSeq".equals(((IStrategoString)term).stringValue()))
                break Fail45;
              if(true)
                break Success33;
            }
            term = term33;
            IStrategoTerm term34 = term;
            Success34:
            { 
              Fail46:
              { 
                if(term.getTermType() != IStrategoTerm.STRING || !"DeclSeq".equals(((IStrategoString)term).stringValue()))
                  break Fail46;
                if(true)
                  break Success34;
              }
              term = term34;
              if(term.getTermType() != IStrategoTerm.STRING || !"AltSeq".equals(((IStrategoString)term).stringValue()))
                break Fail42;
            }
          }
        }
      }
      if(true)
        return term;
    }
    context.push("is_seq_0_0");
    context.popOnFailure();
    return null;
  }
}