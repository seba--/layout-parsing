package NormalizeAST_str;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.imp.debug.stratego.runtime.trans.*;
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
    compiledContext.registerComponent("NormalizeAST_str");
    NormalizeAST_str.init(compiledContext);
    varScope.addSVar("main_0_0", new InteropSDefT(main_0_0.instance, context));
    varScope.addSVar("normalize_0_0", new InteropSDefT(normalize_0_0.instance, context));
    varScope.addSVar("norm_0_0", new InteropSDefT(norm_0_0.instance, context));
    varScope.addSVar("flatten_seqs_0_0", new InteropSDefT(flatten_seqs_0_0.instance, context));
    varScope.addSVar("append_seqs_0_1", new InteropSDefT(append_seqs_0_1.instance, context));
    varScope.addSVar("is_seq_0_0", new InteropSDefT(is_seq_0_0.instance, context));
    varScope.addSVar("norm_char_char_0_0", new InteropSDefT(norm_char_char_0_0.instance, context));
    varScope.addSVar("norm_string_char_0_0", new InteropSDefT(norm_string_char_0_0.instance, context));
    varScope.addSVar("String_1_0", new InteropSDefT($String_1_0.instance, context));
    varScope.addSVar("Char_1_0", new InteropSDefT($Char_1_0.instance, context));
    varScope.addSVar("StringHash_1_0", new InteropSDefT($String$Hash_1_0.instance, context));
    varScope.addSVar("CharHash_1_0", new InteropSDefT($Char$Hash_1_0.instance, context));
  }

  private void registerLazy(org.spoofax.interpreter.core.IContext context, Context compiledContext, ClassLoader classLoader, org.spoofax.interpreter.core.VarScope varScope)
  { 
    compiledContext.registerComponent("NormalizeAST_str");
    NormalizeAST_str.init(compiledContext);
    varScope.addSVar("main_0_0", new InteropSDefT(classLoader, "NormalizeAST_str.main_0_0", context));
    varScope.addSVar("normalize_0_0", new InteropSDefT(classLoader, "NormalizeAST_str.normalize_0_0", context));
    varScope.addSVar("norm_0_0", new InteropSDefT(classLoader, "NormalizeAST_str.norm_0_0", context));
    varScope.addSVar("flatten_seqs_0_0", new InteropSDefT(classLoader, "NormalizeAST_str.flatten_seqs_0_0", context));
    varScope.addSVar("append_seqs_0_1", new InteropSDefT(classLoader, "NormalizeAST_str.append_seqs_0_1", context));
    varScope.addSVar("is_seq_0_0", new InteropSDefT(classLoader, "NormalizeAST_str.is_seq_0_0", context));
    varScope.addSVar("norm_char_char_0_0", new InteropSDefT(classLoader, "NormalizeAST_str.norm_char_char_0_0", context));
    varScope.addSVar("norm_string_char_0_0", new InteropSDefT(classLoader, "NormalizeAST_str.norm_string_char_0_0", context));
    varScope.addSVar("String_1_0", new InteropSDefT(classLoader, "NormalizeAST_str.$String_1_0", context));
    varScope.addSVar("Char_1_0", new InteropSDefT(classLoader, "NormalizeAST_str.$Char_1_0", context));
    varScope.addSVar("StringHash_1_0", new InteropSDefT(classLoader, "NormalizeAST_str.$String$Hash_1_0", context));
    varScope.addSVar("CharHash_1_0", new InteropSDefT(classLoader, "NormalizeAST_str.$Char$Hash_1_0", context));
  }
}