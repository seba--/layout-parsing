package org.spoofax.jsglr.tests.haskell.compare;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") final class lifted12 extends Strategy 
{ 
  public static final lifted12 instance = new lifted12();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term)
  { 
    Fail114:
    { 
      IStrategoTerm term100 = term;
      Success102:
      { 
        Fail115:
        { 
          if(term.getTermType() != IStrategoTerm.APPL || CompareAST._consEscape_1 != ((IStrategoAppl)term).getConstructor())
            break Fail115;
          IStrategoTerm arg131 = term.getSubterm(0);
          if(arg131.getTermType() != IStrategoTerm.APPL || CompareAST._consCharEsc_1 != ((IStrategoAppl)arg131).getConstructor())
            break Fail115;
          IStrategoTerm arg132 = arg131.getSubterm(0);
          if(arg132.getTermType() != IStrategoTerm.STRING || !"&".equals(((IStrategoString)arg132).stringValue()))
            break Fail115;
          { 
            if(true)
              break Fail114;
            if(true)
              break Success102;
          }
        }
        term = term100;
      }
      if(true)
        return term;
    }
    return null;
  }
}