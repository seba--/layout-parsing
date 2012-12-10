package NormalizeAST_str;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.imp.debug.stratego.runtime.trans.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") public class $String_1_0 extends Strategy 
{ 
  public static $String_1_0 instance = new $String_1_0();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term, Strategy f_3177)
  { 
    ITermFactory termFactory = context.getFactory();
    context.push("String_1_0");
    Fail131:
    { 
      IStrategoTerm j_3256 = null;
      IStrategoTerm i_3256 = null;
      if(term.getTermType() != IStrategoTerm.APPL || NormalizeAST_str._consString_1 != ((IStrategoAppl)term).getConstructor())
        break Fail131;
      i_3256 = term.getSubterm(0);
      IStrategoList annos0 = term.getAnnotations();
      j_3256 = annos0;
      term = f_3177.invoke(context, i_3256);
      if(term == null)
        break Fail131;
      term = termFactory.annotateTerm(termFactory.makeAppl(NormalizeAST_str._consString_1, new IStrategoTerm[]{term}), checkListAnnos(termFactory, j_3256));
      context.popOnSuccess();
      if(true)
        return term;
    }
    context.popOnFailure();
    return null;
  }
}