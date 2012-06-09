package NormalizeAST_str;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.imp.debug.stratego.runtime.trans.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") public class norm_string_char_0_0 extends Strategy 
{ 
  public static norm_string_char_0_0 instance = new norm_string_char_0_0();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term)
  { 
    context.push("norm_string_char_0_0");
    Fail129:
    { 
      IStrategoTerm term116 = term;
      Success116:
      { 
        Fail130:
        { 
          if(term.getTermType() != IStrategoTerm.APPL || NormalizeAST_str._consEscape_1 != ((IStrategoAppl)term).getConstructor())
            break Fail130;
          IStrategoTerm arg141 = term.getSubterm(0);
          if(arg141.getTermType() != IStrategoTerm.APPL || NormalizeAST_str._consCharEsc_1 != ((IStrategoAppl)arg141).getConstructor())
            break Fail130;
          IStrategoTerm arg142 = arg141.getSubterm(0);
          if(arg142.getTermType() != IStrategoTerm.STRING || !"'".equals(((IStrategoString)arg142).stringValue()))
            break Fail130;
          term = NormalizeAST_str.const35;
          if(true)
            break Success116;
        }
        term = norm_char_char_0_0.instance.invoke(context, term116);
        if(term == null)
          break Fail129;
      }
      context.popOnSuccess();
      if(true)
        return term;
    }
    context.popOnFailure();
    return null;
  }
}