package org.spoofax.jsglr.tests.haskell.normalize;

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
    compiledContext.registerComponent("normalize");
    normalize.init(compiledContext);
    varScope.addSVar("main_0_0", new InteropSDefT(main_0_0.instance, context));
    varScope.addSVar("normalize_0_0", new InteropSDefT(normalize_0_0.instance, context));
    varScope.addSVar("norm_0_0", new InteropSDefT(norm_0_0.instance, context));
    varScope.addSVar("drop_tailings_0_1", new InteropSDefT(drop_tailings_0_1.instance, context));
    varScope.addSVar("insert_dot_zero_0_0", new InteropSDefT(insert_dot_zero_0_0.instance, context));
  }

  private void registerLazy(org.spoofax.interpreter.core.IContext context, Context compiledContext, ClassLoader classLoader, org.spoofax.interpreter.core.VarScope varScope)
  { 
    compiledContext.registerComponent("normalize");
    normalize.init(compiledContext);
    varScope.addSVar("main_0_0", new InteropSDefT(classLoader, "org.spoofax.jsglr.tests.haskell.normalize.main_0_0", context));
    varScope.addSVar("normalize_0_0", new InteropSDefT(classLoader, "org.spoofax.jsglr.tests.haskell.normalize.normalize_0_0", context));
    varScope.addSVar("norm_0_0", new InteropSDefT(classLoader, "org.spoofax.jsglr.tests.haskell.normalize.norm_0_0", context));
    varScope.addSVar("drop_tailings_0_1", new InteropSDefT(classLoader, "org.spoofax.jsglr.tests.haskell.normalize.drop_tailings_0_1", context));
    varScope.addSVar("insert_dot_zero_0_0", new InteropSDefT(classLoader, "org.spoofax.jsglr.tests.haskell.normalize.insert_dot_zero_0_0", context));
  }
}