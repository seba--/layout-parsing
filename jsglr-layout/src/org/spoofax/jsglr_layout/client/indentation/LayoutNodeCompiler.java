package org.spoofax.jsglr_layout.client.indentation;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Loader;
import javassist.Modifier;
import javassist.NotFoundException;

import org.spoofax.jsglr_layout.client.AbstractParseNode;
import org.spoofax.jsglr_layout.client.indentation.LocalVariableManager.LocalVariable;

/**
 * The {@link LayoutNodeCompiler} compiles {@link CompilableLayoutNode}s to
 * {@link CompiledLayoutConstraint}. The compiler takes a {@link BooleanNode} as
 * main node and uses the generates code of the node to create a new class
 * implementing the {@link CompiledLayoutConstraint} at runtime which performs
 * the job of the given node. The generated class will perform this job faster.
 * Also the ram usage of the compiled class should be less.
 * 
 * @author moritzlichter
 * 
 */
public class LayoutNodeCompiler {

  /**
   * Static counter of the compiled classes used to generate the name of the
   * next compiled class
   */
  private static int compiledClassCount = 0;
  /**
   * Static instance of the {@link Loader} used to load the compiled classes
   */
  private static Loader loader;

  static {
    ClassPool pool = ClassPool.getDefault();
    // Initialize the loader
    loader = new Loader(pool);
    // Delegate loading classes used in the generated code to the default
    // class loader
    // This prohibits getting the classes loaded by both class loaders (and
    // then both classes are distinct and instances of it cannot be assigned
    // to references of each other)
    loader.delegateLoadingOf(CompiledLayoutConstraint.class.getName());
    loader.delegateLoadingOf(AbstractParseNode.class.getName());
  }

  /**
   * Compiles the given {@link BooleanNode} to a new class implementing
   * {@link CompiledLayoutConstraint} and returns an instance of the class
   * created.
   * 
   * @param node
   *          the {@link BooleanNode} to compile
   * @return an instance of the compiled class
   * @throws NotFoundException
   * @throws CannotCompileException
   * @throws ClassNotFoundException
   * @throws InstantiationException
   * @throws IllegalAccessException
   * @throws IOException
   */
  public CompiledLayoutConstraint compile(BooleanNode node)
      throws NotFoundException, CannotCompileException, ClassNotFoundException,
      InstantiationException, IllegalAccessException, IOException {
    ClassPool classPool = ClassPool.getDefault();
    // Load the CompiledLayoutNode
    CtClass compiledInterfaceClass = classPool
        .get(CompiledLayoutConstraint.class.getName());

    // Create the class
    CtClass newClass = classPool.makeClass(LayoutNodeCompiler.class
        .getPackage().getName()
        + ".compiledConstraints.CompiledLayoutConstraint" + compiledClassCount);
    compiledClassCount++;
    // Let it implement the desired interface
    newClass.setInterfaces(new CtClass[] { compiledInterfaceClass });
    // Add methods for parse and disambiguation time
    createMethod(node, true, newClass);
    createMethod(node, false, newClass);
    // Mark the class as not abstract
    newClass.setModifiers(newClass.getModifiers() & ~Modifier.ABSTRACT);

    // Load the class (casting is unchecked but we know that the class
    // implements the interface)
    @SuppressWarnings("unchecked")
    Class<? extends CompiledLayoutConstraint> loadedClass = (Class<? extends CompiledLayoutConstraint>) loader
        .loadClass(newClass.getName());
    newClass.freeze(); // No further modifications
    System.out.println("Compiled " + loadedClass);
    // Create an instance
    CompiledLayoutConstraint constraint = loadedClass.newInstance();
    return constraint;
  }

  /**
   * Creates the method for evaluating for layout constraint and add it to the
   * given class.
   * 
   * @param node
   *          the node to generate the code
   * @param parse
   *          whether to create a method for parse or disambiguation time
   * @param newClass
   *          the new class to add the method to
   * @throws NotFoundException
   * @throws CannotCompileException
   */
  private void createMethod(BooleanNode node, boolean parse, CtClass newClass)
      throws NotFoundException, CannotCompileException {
    // Generate code and method name depending on parse argument
    String methodName;
    String sourceCode;
    LocalVariableManager manager = new LocalVariableManager();
    if (parse) {
      methodName = "evaluateParseTime";
      sourceCode = node.getCompiledCode(manager, true);
    } else {
      methodName = "evaluateDisambiguationTime";
      sourceCode = node.getCompiledCode(manager, false);
    }

    // Complete method body: Add brackets, variable declarations and return
    // statement
    sourceCode = "{" + getCodeForVariables(manager) + "\nreturn " + sourceCode
        + ";\n}";
    System.out.println("Code for method: " + methodName);
    System.out.println(sourceCode);

    // Load class for arguments
    ClassPool classPool = ClassPool.getDefault();
    CtClass parseNodeArrayClass = classPool.get(AbstractParseNode.class
        .getName() + "[]");
    CtClass mapClass = classPool.get(Map.class.getName());
    // Create the method
    CtMethod parseMethod = new CtMethod(CtClass.intType, methodName,
        new CtClass[] { parseNodeArrayClass, mapClass }, newClass);
    parseMethod.setBody(sourceCode);
    newClass.addMethod(parseMethod);
  }

  /**
   * Generates the code for the variable declarations of the given
   * {@link LocalVariableManager}.
   * 
   * @param manager
   *          the {@link LocalVariableManager} which holds the local variables
   *          which should be declared
   * @return the generates source code
   */
  private String getCodeForVariables(LocalVariableManager manager) {
    String code = "";
    // Iterate through all variables
    List<LocalVariable> variables = manager.getDeclaredVariables();
    for (LocalVariable var : variables) {
      code += "\n";
      // Need to check whether it is primitive or not
      if (var.isPrimitive()) {
        if (var.getType() == Integer.class) {
          code += "int";
        } else if (var.getType() == Boolean.class) {
          code += "boolean;";
        }
      } else {
        code += var.getType().getName();
      }
      code += " " + var.getName() + ";";
    }
    return code;
  }

}
