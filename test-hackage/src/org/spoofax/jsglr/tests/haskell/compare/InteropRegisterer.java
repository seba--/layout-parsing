package org.spoofax.jsglr.tests.haskell.compare;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("unused") public class InteropRegisterer extends org.strategoxt.lang.InteropRegisterer 
{ 
  @Override public void register(org.spoofax.interpreter.core.IContext context, Context compiledContext)
  { 
    register(context, compiledContext, context.getVarScope());
  }

  @Override public void registerLazy(org.spoofax.interpreter.core.IContext context, Context compiledContext, ClassLoader classLoader)
  { 
    registerLazy(context, compiledContext, classLoader, context.getVarScope());
  }

  private void register(org.spoofax.interpreter.core.IContext context, Context compiledContext, org.spoofax.interpreter.core.VarScope varScope)
  { 
    compiledContext.registerComponent("compare");
    compare.init(compiledContext);
    varScope.addSVar("main_0_0", new InteropSDefT(main_0_0.instance, context));
    varScope.addSVar("compare_0_0", new InteropSDefT(compare_0_0.instance, context));
    varScope.addSVar("compare_term_0_0", new InteropSDefT(compare_term_0_0.instance, context));
    varScope.addSVar("peq_1_0", new InteropSDefT(peq_1_0.instance, context));
    varScope.addSVar("hs_string_to_int_0_0", new InteropSDefT(hs_string_to_int_0_0.instance, context));
    varScope.addSVar("hs_string_to_float_0_0", new InteropSDefT(hs_string_to_float_0_0.instance, context));
    varScope.addSVar("norm_char_char_0_0", new InteropSDefT(norm_char_char_0_0.instance, context));
    varScope.addSVar("norm_char_0_0", new InteropSDefT(norm_char_0_0.instance, context));
    varScope.addSVar("norm_string_0_0", new InteropSDefT(norm_string_0_0.instance, context));
    varScope.addSVar("DecimalEsc_1_0", new InteropSDefT($Decimal$Esc_1_0.instance, context));
  }

  private void registerLazy(org.spoofax.interpreter.core.IContext context, Context compiledContext, ClassLoader classLoader, org.spoofax.interpreter.core.VarScope varScope)
  { 
    compiledContext.registerComponent("compare");
    compare.init(compiledContext);
    varScope.addSVar("main_0_0", new InteropSDefT(classLoader, "org.spoofax.jsglr.tests.haskell.compare.main_0_0", context));
    varScope.addSVar("compare_0_0", new InteropSDefT(classLoader, "org.spoofax.jsglr.tests.haskell.compare.compare_0_0", context));
    varScope.addSVar("compare_term_0_0", new InteropSDefT(classLoader, "org.spoofax.jsglr.tests.haskell.compare.compare_term_0_0", context));
    varScope.addSVar("peq_1_0", new InteropSDefT(classLoader, "org.spoofax.jsglr.tests.haskell.compare.peq_1_0", context));
    varScope.addSVar("hs_string_to_int_0_0", new InteropSDefT(classLoader, "org.spoofax.jsglr.tests.haskell.compare.hs_string_to_int_0_0", context));
    varScope.addSVar("hs_string_to_float_0_0", new InteropSDefT(classLoader, "org.spoofax.jsglr.tests.haskell.compare.hs_string_to_float_0_0", context));
    varScope.addSVar("norm_char_char_0_0", new InteropSDefT(classLoader, "org.spoofax.jsglr.tests.haskell.compare.norm_char_char_0_0", context));
    varScope.addSVar("norm_char_0_0", new InteropSDefT(classLoader, "org.spoofax.jsglr.tests.haskell.compare.norm_char_0_0", context));
    varScope.addSVar("norm_string_0_0", new InteropSDefT(classLoader, "org.spoofax.jsglr.tests.haskell.compare.norm_string_0_0", context));
    varScope.addSVar("DecimalEsc_1_0", new InteropSDefT(classLoader, "org.spoofax.jsglr.tests.haskell.compare.$Decimal$Esc_1_0", context));
  }
}