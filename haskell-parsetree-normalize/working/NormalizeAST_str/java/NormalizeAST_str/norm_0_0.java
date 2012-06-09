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
    context.push("norm_0_0");
    Fail34:
    { 
      IStrategoTerm term26 = term;
      Success26:
      { 
        Fail35:
        { 
          term = flatten_seqs_0_0.instance.invoke(context, term);
          if(term == null)
            break Fail35;
          if(true)
            break Success26;
        }
        term = term26;
        IStrategoTerm term27 = term;
        Success27:
        { 
          Fail36:
          { 
            term = $Char_1_0.instance.invoke(context, term, lifted11.instance);
            if(term == null)
              break Fail36;
            if(true)
              break Success27;
          }
          term = term27;
          IStrategoTerm term28 = term;
          Success28:
          { 
            Fail37:
            { 
              term = $Char$Hash_1_0.instance.invoke(context, term, lifted13.instance);
              if(term == null)
                break Fail37;
              if(true)
                break Success28;
            }
            term = term28;
            IStrategoTerm term29 = term;
            Success29:
            { 
              Fail38:
              { 
                term = $String_1_0.instance.invoke(context, term, lifted15.instance);
                if(term == null)
                  break Fail38;
                if(true)
                  break Success29;
              }
              term = $String$Hash_1_0.instance.invoke(context, term29, lifted18.instance);
              if(term == null)
                break Fail34;
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