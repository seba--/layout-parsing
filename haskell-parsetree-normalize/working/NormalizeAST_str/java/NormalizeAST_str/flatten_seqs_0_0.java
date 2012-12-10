package NormalizeAST_str;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.imp.debug.stratego.runtime.trans.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") public class flatten_seqs_0_0 extends Strategy 
{ 
  public static flatten_seqs_0_0 instance = new flatten_seqs_0_0();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term)
  { 
    ITermFactory termFactory = context.getFactory();
    context.push("flatten_seqs_0_0");
    Fail39:
    { 
      IStrategoTerm q_3169 = null;
      IStrategoTerm r_3169 = null;
      IStrategoTerm s_3169 = null;
      IStrategoTerm cons2 = context.invokePrimitive("SSL_get_constructor", term, NO_STRATEGIES, new IStrategoTerm[]{term});
      s_3169 = cons2;
      IStrategoTerm args2 = context.invokePrimitive("SSL_get_arguments", term, NO_STRATEGIES, new IStrategoTerm[]{term});
      if(args2.getTermType() != IStrategoTerm.LIST || ((IStrategoList)args2).isEmpty())
        break Fail39;
      q_3169 = ((IStrategoList)args2).head();
      IStrategoTerm arg59 = ((IStrategoList)args2).tail();
      if(arg59.getTermType() != IStrategoTerm.LIST || ((IStrategoList)arg59).isEmpty())
        break Fail39;
      r_3169 = ((IStrategoList)arg59).head();
      IStrategoTerm arg60 = ((IStrategoList)arg59).tail();
      if(arg60.getTermType() != IStrategoTerm.LIST || !((IStrategoList)arg60).isEmpty())
        break Fail39;
      term = is_seq_0_0.instance.invoke(context, s_3169);
      if(term == null)
        break Fail39;
      term = termFactory.makeTuple(q_3169, r_3169);
      term = append_seqs_0_1.instance.invoke(context, term, s_3169);
      if(term == null)
        break Fail39;
      context.popOnSuccess();
      if(true)
        return term;
    }
    context.popOnFailure();
    return null;
  }
}