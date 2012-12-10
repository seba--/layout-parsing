package NormalizeAST_str;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.imp.debug.stratego.runtime.trans.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") public class $Char$Hash_1_0 extends Strategy 
{ 
  public static $Char$Hash_1_0 instance = new $Char$Hash_1_0();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term, Strategy a_3178)
  { 
    ITermFactory termFactory = context.getFactory();
    context.push("CharHash_1_0");
    Fail134:
    { 
      IStrategoTerm p_3258 = null;
      IStrategoTerm o_3258 = null;
      if(term.getTermType() != IStrategoTerm.APPL || NormalizeAST_str._consCharHash_1 != ((IStrategoAppl)term).getConstructor())
        break Fail134;
      o_3258 = term.getSubterm(0);
      IStrategoList annos3 = term.getAnnotations();
      p_3258 = annos3;
      term = a_3178.invoke(context, o_3258);
      if(term == null)
        break Fail134;
      term = termFactory.annotateTerm(termFactory.makeAppl(NormalizeAST_str._consCharHash_1, new IStrategoTerm[]{term}), checkListAnnos(termFactory, p_3258));
      context.popOnSuccess();
      if(true)
        return term;
    }
    context.popOnFailure();
    return null;
  }
}