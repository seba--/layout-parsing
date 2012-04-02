package org.spoofax.jsglr.tests.haskell.compare;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") public class $Decimal$Esc_1_0 extends Strategy 
{ 
  public static $Decimal$Esc_1_0 instance = new $Decimal$Esc_1_0();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term, Strategy j_17)
  { 
    ITermFactory termFactory = context.getFactory();
    context.push("DecimalEsc_1_0");
    Fail110:
    { 
      IStrategoTerm q_106 = null;
      IStrategoTerm p_106 = null;
      if(term.getTermType() != IStrategoTerm.APPL || CompareAST._consDecimalEsc_1 != ((IStrategoAppl)term).getConstructor())
        break Fail110;
      p_106 = term.getSubterm(0);
      IStrategoList annos0 = term.getAnnotations();
      q_106 = annos0;
      term = j_17.invoke(context, p_106);
      if(term == null)
        break Fail110;
      term = termFactory.annotateTerm(termFactory.makeAppl(CompareAST._consDecimalEsc_1, new IStrategoTerm[]{term}), checkListAnnos(termFactory, q_106));
      context.popOnSuccess();
      if(true)
        return term;
    }
    context.popOnFailure();
    return null;
  }
}