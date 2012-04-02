package CompareAST_str;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.imp.debug.stratego.runtime.trans.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") final class lifted13 extends Strategy 
{ 
  public static final lifted13 instance = new lifted13();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term)
  { 
    Fail111:
    { 
      term = repeat_1_0.instance.invoke(context, term, lifted14.instance);
      if(term == null)
        break Fail111;
      if(true)
        return term;
    }
    return null;
  }
}