package NormalizeAST_str;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.imp.debug.stratego.runtime.trans.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") final class lifted18 extends Strategy 
{ 
  public static final lifted18 instance = new lifted18();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term)
  { 
    Fail135:
    { 
      term = map_1_0.instance.invoke(context, term, lifted19.instance);
      if(term == null)
        break Fail135;
      if(true)
        return term;
    }
    return null;
  }
}