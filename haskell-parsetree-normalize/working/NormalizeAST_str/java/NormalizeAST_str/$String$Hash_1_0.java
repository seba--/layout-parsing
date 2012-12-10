package NormalizeAST_str;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.imp.debug.stratego.runtime.trans.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") public class $String$Hash_1_0 extends Strategy 
{ 
  public static $String$Hash_1_0 instance = new $String$Hash_1_0();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term, Strategy z_3177)
  { 
    ITermFactory termFactory = context.getFactory();
    context.push("StringHash_1_0");
    Fail133:
    { 
      IStrategoTerm m_3258 = null;
      IStrategoTerm l_3258 = null;
      if(term.getTermType() != IStrategoTerm.APPL || NormalizeAST_str._consStringHash_1 != ((IStrategoAppl)term).getConstructor())
        break Fail133;
      l_3258 = term.getSubterm(0);
      IStrategoList annos2 = term.getAnnotations();
      m_3258 = annos2;
      term = z_3177.invoke(context, l_3258);
      if(term == null)
        break Fail133;
      term = termFactory.annotateTerm(termFactory.makeAppl(NormalizeAST_str._consStringHash_1, new IStrategoTerm[]{term}), checkListAnnos(termFactory, m_3258));
      context.popOnSuccess();
      if(true)
        return term;
    }
    context.popOnFailure();
    return null;
  }
}