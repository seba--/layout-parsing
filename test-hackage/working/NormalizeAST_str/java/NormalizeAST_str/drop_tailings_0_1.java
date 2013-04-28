package NormalizeAST_str;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.imp.debug.stratego.runtime.trans.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") public class drop_tailings_0_1 extends Strategy 
{ 
  public static drop_tailings_0_1 instance = new drop_tailings_0_1();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term, IStrategoTerm o_56396)
  { 
    context.push("drop_tailings_0_1");
    Fail5:
    { 
      IStrategoTerm term2 = term;
      Success2:
      { 
        Fail6:
        { 
          IStrategoTerm l_56390 = null;
          IStrategoTerm m_56390 = null;
          l_56390 = term;
          term = split_init_last_0_0.instance.invoke(context, l_56390);
          if(term == null)
            break Fail6;
          if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
            break Fail6;
          m_56390 = term.getSubterm(0);
          if(term.getSubterm(1) != o_56396 && !o_56396.match(term.getSubterm(1)))
            break Fail6;
          term = this.invoke(context, m_56390, o_56396);
          if(term == null)
            break Fail6;
          if(true)
            break Success2;
        }
        term = term2;
      }
      context.popOnSuccess();
      if(true)
        return term;
    }
    context.popOnFailure();
    return null;
  }
}