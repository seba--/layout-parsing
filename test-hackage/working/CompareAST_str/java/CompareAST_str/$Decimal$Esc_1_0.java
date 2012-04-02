package CompareAST_str;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.imp.debug.stratego.runtime.trans.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") public class $Decimal$Esc_1_0 extends Strategy 
{ 
  public static $Decimal$Esc_1_0 instance = new $Decimal$Esc_1_0();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term, Strategy b_4073)
  { 
    ITermFactory termFactory = context.getFactory();
    context.push("DecimalEsc_1_0");
    Fail110:
    { 
      IStrategoTerm q_4151 = null;
      IStrategoTerm p_4151 = null;
      if(term.getTermType() != IStrategoTerm.APPL || CompareAST_str._consDecimalEsc_1 != ((IStrategoAppl)term).getConstructor())
        break Fail110;
      p_4151 = term.getSubterm(0);
      IStrategoList annos0 = term.getAnnotations();
      q_4151 = annos0;
      term = b_4073.invoke(context, p_4151);
      if(term == null)
        break Fail110;
      term = termFactory.annotateTerm(termFactory.makeAppl(CompareAST_str._consDecimalEsc_1, new IStrategoTerm[]{term}), checkListAnnos(termFactory, q_4151));
      context.popOnSuccess();
      if(true)
        return term;
    }
    context.popOnFailure();
    return null;
  }
}