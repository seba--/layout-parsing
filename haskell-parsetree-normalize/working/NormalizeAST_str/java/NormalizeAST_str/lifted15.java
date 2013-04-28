package NormalizeAST_str;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.imp.debug.stratego.runtime.trans.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") final class lifted15 extends Strategy 
{ 
  public static final lifted15 instance = new lifted15();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term)
  { 
    Fail137:
    { 
      term = map_1_0.instance.invoke(context, term, lifted16.instance);
      if(term == null)
        break Fail137;
      if(true)
        return term;
    }
    return null;
  }
}