package org.spoofax.jsglr.tests.haskell.compare;

import java.math.BigInteger;

import org.spoofax.interpreter.core.IContext;
import org.spoofax.interpreter.core.InterpreterException;
import org.spoofax.interpreter.library.AbstractPrimitive;
import org.spoofax.interpreter.library.AbstractStrategoOperatorRegistry;
import org.spoofax.interpreter.stratego.Strategy;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.terms.StrategoInt;
import org.spoofax.terms.StrategoString;

/**
 * @author seba
 *
 */
public class CompareLibrary extends AbstractStrategoOperatorRegistry {

  public CompareLibrary() {
    add(new AbstractPrimitive("COMPARE_convert_int", 0, 2) {
      @Override
      public boolean call(IContext ctx, Strategy[] arg1, IStrategoTerm[] terms) throws InterpreterException {
        IStrategoTerm t = ctx.current();
        IStrategoTerm from = terms[0];
        IStrategoTerm to = terms[1];
        if (t.getTermType() != IStrategoTerm.STRING ||
            from.getTermType() != IStrategoTerm.INT ||
            to.getTermType() != IStrategoTerm.INT)
          return false;
        
        String s = ((StrategoString) t).stringValue();
        if (s.length() == 0) {
          s= "0";
        }
        BigInteger i = new BigInteger(s, ((StrategoInt) from).intValue());
        s = i.toString(((StrategoInt) to).intValue());
        ctx.setCurrent(ctx.getFactory().makeString(s));
        return true;
      }
    });
  }

  @Override
  public String getOperatorRegistryName() {
    return "COMPARE";
  }

}
