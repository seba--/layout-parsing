package NormalizeAST_str;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.imp.debug.stratego.runtime.trans.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") public class append_seqs_0_1 extends Strategy 
{ 
  public static append_seqs_0_1 instance = new append_seqs_0_1();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term, IStrategoTerm c_3178)
  { 
    ITermFactory termFactory = context.getFactory();
    context.push("append_seqs_0_1");
    Fail40:
    { 
      IStrategoTerm term30 = term;
      Success30:
      { 
        Fail41:
        { 
          IStrategoTerm y_3169 = null;
          IStrategoTerm z_3169 = null;
          IStrategoTerm a_3170 = null;
          IStrategoTerm b_3170 = null;
          IStrategoTerm c_3170 = null;
          if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
            break Fail41;
          IStrategoTerm arg61 = term.getSubterm(0);
          IStrategoTerm cons3 = context.invokePrimitive("SSL_get_constructor", term, NO_STRATEGIES, new IStrategoTerm[]{arg61});
          if(cons3 != c_3178 && !c_3178.match(cons3))
            break Fail41;
          IStrategoTerm args3 = context.invokePrimitive("SSL_get_arguments", term, NO_STRATEGIES, new IStrategoTerm[]{arg61});
          if(args3.getTermType() != IStrategoTerm.LIST || ((IStrategoList)args3).isEmpty())
            break Fail41;
          y_3169 = ((IStrategoList)args3).head();
          IStrategoTerm arg62 = ((IStrategoList)args3).tail();
          if(arg62.getTermType() != IStrategoTerm.LIST || ((IStrategoList)arg62).isEmpty())
            break Fail41;
          z_3169 = ((IStrategoList)arg62).head();
          IStrategoTerm arg63 = ((IStrategoList)arg62).tail();
          if(arg63.getTermType() != IStrategoTerm.LIST || !((IStrategoList)arg63).isEmpty())
            break Fail41;
          a_3170 = term.getSubterm(1);
          c_3170 = term;
          term = termFactory.makeTuple(z_3169, a_3170);
          term = this.invoke(context, term, c_3178);
          if(term == null)
            break Fail41;
          b_3170 = term;
          term = c_3170;
          IStrategoTerm mkterm0;
          mkterm0 = context.invokePrimitive("SSL_mkterm", term, NO_STRATEGIES, new IStrategoTerm[]{c_3178, (IStrategoTerm)termFactory.makeListCons(y_3169, termFactory.makeListCons(b_3170, (IStrategoList)NormalizeAST_str.constNil1))});
          if(mkterm0 == null)
            break Fail41;
          term = mkterm0;
          if(true)
            break Success30;
        }
        term = term30;
        IStrategoTerm v_3169 = null;
        IStrategoTerm w_3169 = null;
        if(term.getTermType() != IStrategoTerm.TUPLE || term.getSubtermCount() != 2)
          break Fail40;
        v_3169 = term.getSubterm(0);
        w_3169 = term.getSubterm(1);
        IStrategoTerm mkterm1;
        mkterm1 = context.invokePrimitive("SSL_mkterm", term, NO_STRATEGIES, new IStrategoTerm[]{c_3178, (IStrategoTerm)termFactory.makeListCons(v_3169, termFactory.makeListCons(w_3169, (IStrategoList)NormalizeAST_str.constNil1))});
        if(mkterm1 == null)
          break Fail40;
        term = mkterm1;
      }
      context.popOnSuccess();
      if(true)
        return term;
    }
    context.popOnFailure();
    return null;
  }
}