package org.spoofax.jsglr.tests.haskell.normalize;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") final class lifted6 extends Strategy 
{ 
  public static final lifted6 instance = new lifted6();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term)
  { 
    Fail41:
    { 
      IStrategoTerm term30 = term;
      Success34:
      { 
        Fail42:
        { 
          if(term.getTermType() != IStrategoTerm.APPL || NormalizeAST._consEscape_1 != ((IStrategoAppl)term).getConstructor())
            break Fail42;
          IStrategoTerm arg22 = term.getSubterm(0);
          if(arg22.getTermType() != IStrategoTerm.APPL || NormalizeAST._consCharEsc_1 != ((IStrategoAppl)arg22).getConstructor())
            break Fail42;
          IStrategoTerm arg23 = arg22.getSubterm(0);
          if(arg23.getTermType() != IStrategoTerm.STRING || !"&".equals(((IStrategoString)arg23).stringValue()))
            break Fail42;
          { 
            if(true)
              break Fail41;
            if(true)
              break Success34;
          }
        }
        term = term30;
      }
      if(true)
        return term;
    }
    return null;
  }
}