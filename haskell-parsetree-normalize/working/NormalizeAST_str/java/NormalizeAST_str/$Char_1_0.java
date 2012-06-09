package NormalizeAST_str;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.imp.debug.stratego.runtime.trans.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") public class $Char_1_0 extends Strategy 
{ 
  public static $Char_1_0 instance = new $Char_1_0();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term, Strategy g_3177)
  { 
    ITermFactory termFactory = context.getFactory();
    context.push("Char_1_0");
    Fail132:
    { 
      IStrategoTerm m_3256 = null;
      IStrategoTerm l_3256 = null;
      if(term.getTermType() != IStrategoTerm.APPL || NormalizeAST_str._consChar_1 != ((IStrategoAppl)term).getConstructor())
        break Fail132;
      l_3256 = term.getSubterm(0);
      IStrategoList annos1 = term.getAnnotations();
      m_3256 = annos1;
      term = g_3177.invoke(context, l_3256);
      if(term == null)
        break Fail132;
      term = termFactory.annotateTerm(termFactory.makeAppl(NormalizeAST_str._consChar_1, new IStrategoTerm[]{term}), checkListAnnos(termFactory, m_3256));
      context.popOnSuccess();
      if(true)
        return term;
    }
    context.popOnFailure();
    return null;
  }
}