package org.spoofax.jsglr.tests.haskell.compare;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") final class lifted14 extends Strategy 
{ 
  public static final lifted14 instance = new lifted14();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term)
  { 
    Fail112:
    { 
      IStrategoTerm term102 = term;
      Success101:
      { 
        Fail113:
        { 
          if(term.getTermType() != IStrategoTerm.APPL || CompareAST._consEscape_1 != ((IStrategoAppl)term).getConstructor())
            break Fail113;
          IStrategoTerm arg133 = term.getSubterm(0);
          if(arg133.getTermType() != IStrategoTerm.APPL || CompareAST._consCharEsc_1 != ((IStrategoAppl)arg133).getConstructor())
            break Fail113;
          IStrategoTerm arg134 = arg133.getSubterm(0);
          if(arg134.getTermType() != IStrategoTerm.STRING || !"'".equals(((IStrategoString)arg134).stringValue()))
            break Fail113;
          term = compare.const34;
          if(true)
            break Success101;
        }
        term = norm_char_char_0_0.instance.invoke(context, term102);
        if(term == null)
          break Fail112;
      }
      if(true)
        return term;
    }
    return null;
  }
}