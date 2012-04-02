package org.spoofax.jsglr.tests.haskell.normalize;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") final class lifted7 extends Strategy 
{ 
  public static final lifted7 instance = new lifted7();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term)
  { 
    Fail40:
    { 
      if(term.getTermType() != IStrategoTerm.INT || 101 != ((IStrategoInt)term).intValue())
        break Fail40;
      if(true)
        return term;
    }
    return null;
  }
}