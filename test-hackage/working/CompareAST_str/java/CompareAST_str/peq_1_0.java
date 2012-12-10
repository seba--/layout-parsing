package CompareAST_str;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.imp.debug.stratego.runtime.trans.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") public class peq_1_0 extends Strategy 
{ 
  public static peq_1_0 instance = new peq_1_0();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term, Strategy v_198)
  { 
    ITermFactory termFactory = context.getFactory();
    context.push("peq_1_0");
    Fail24:
    { 
      IStrategoTerm t_198 = null;
      IStrategoTerm u_198 = null;
      IStrategoTerm w_198 = null;
      if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
        break Fail24;
      t_198 = term.getSubterm(0);
      u_198 = term.getSubterm(1);
      term = v_198.invoke(context, t_198);
      if(term == null)
        break Fail24;
      w_198 = term;
      term = v_198.invoke(context, u_198);
      if(term == null)
        break Fail24;
      term = termFactory.makeTuple(w_198, term);
      term = equal_0_0.instance.invoke(context, term);
      if(term == null)
        break Fail24;
      context.popOnSuccess();
      if(true)
        return term;
    }
    context.popOnFailure();
    return null;
  }
}