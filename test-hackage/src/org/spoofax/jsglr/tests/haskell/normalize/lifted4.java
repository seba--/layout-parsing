package org.spoofax.jsglr.tests.haskell.normalize;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") final class lifted4 extends Strategy 
{ 
  public static final lifted4 instance = new lifted4();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term)
  { 
    Fail8:
    { 
      if(term.getTermType() != IStrategoTerm.INT || 101 != ((IStrategoInt)term).intValue())
        break Fail8;
      if(true)
        return term;
    }
    return null;
  }
}