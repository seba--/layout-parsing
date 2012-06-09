package NormalizeAST_str;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.imp.debug.stratego.runtime.trans.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") final class lifted16 extends Strategy 
{ 
  public static final lifted16 instance = new lifted16();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term)
  { 
    Fail138:
    { 
      term = repeat_1_0.instance.invoke(context, term, norm_string_char_0_0.instance);
      if(term == null)
        break Fail138;
      if(true)
        return term;
    }
    return null;
  }
}