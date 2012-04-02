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

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term, Strategy l_4065)
  { 
    ITermFactory termFactory = context.getFactory();
    context.push("peq_1_0");
    Fail22:
    { 
      IStrategoTerm j_4065 = null;
      IStrategoTerm k_4065 = null;
      IStrategoTerm m_4065 = null;
      if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
        break Fail22;
      j_4065 = term.getSubterm(0);
      k_4065 = term.getSubterm(1);
      term = l_4065.invoke(context, j_4065);
      if(term == null)
        break Fail22;
      m_4065 = term;
      term = l_4065.invoke(context, k_4065);
      if(term == null)
        break Fail22;
      term = termFactory.makeTuple(m_4065, term);
      term = equal_0_0.instance.invoke(context, term);
      if(term == null)
        break Fail22;
      context.popOnSuccess();
      if(true)
        return term;
    }
    context.popOnFailure();
    return null;
  }
}