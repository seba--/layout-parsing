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

import org.spoofax.interpreter.terms.IStrategoConstructor;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr_layout.client.AbstractParseNode;
import org.spoofax.jsglr_layout.client.indentation.LocalVariableManager.LocalVariable;
import org.spoofax.terms.Term;

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
  public CompiledLayoutConstraint compile(IStrategoTerm constraintTerm)
      throws NotFoundException, CannotCompileException, ClassNotFoundException,
      InstantiationException, IllegalAccessException, IOException {
    
    //First build the layout tree
    BooleanNode node = buildConstraintTree(constraintTerm, BooleanNode.class);
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
//    System.out.println("Compiled " + loadedClass);
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
 //   System.out.println("Code for method: " + methodName);
 //   System.out.println(sourceCode);

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

  static Object buildConstraintTree(IStrategoTerm constraint) {
    switch (constraint.getTermType()) {
    case IStrategoTerm.INT: {
      int i = Term.asJavaInt(constraint);
      return new KidsSelectorNode(i);
    }
    case IStrategoTerm.STRING:
      // TODO implement that
      throw new UnsupportedOperationException(
          "Variables are not implemented in compiled constraints");
      // String v = Term.asJavaString(constraint);
      // return new VariableNode<Object>(v);
    case IStrategoTerm.APPL:
      IStrategoConstructor cons = Term.tryGetConstructor(constraint);
      String consName = cons.getName();
      if (consName.equals("num")) {
        String num = Term.asJavaString(constraint.getSubterm(0));
        int i = Integer.parseInt(num);
        return new ConstantNode(i);
      }
      if (consName.equals("tree")) {
        String num = Term.asJavaString(constraint.getSubterm(0));
        int i = Integer.parseInt(num);
        return new KidsSelectorNode(i);
      }
      if (consName.equals("eq") || consName.equals("gt")
          || consName.equals("ge") || consName.equals("lt")
          || consName.equals("le")) {
        IntegerNode operand1 = buildConstraintTree(constraint.getSubterm(0),
             IntegerNode.class);
        IntegerNode operand2 = buildConstraintTree(constraint.getSubterm(1),
             IntegerNode.class);
        return new ArithComparatorNode(operand1, operand2, consName);
      }
      if (consName.equals("add") || consName.equals("sub")
          || consName.equals("mul") || consName.equals("div")) {
        IntegerNode operand1 = buildConstraintTree(constraint.getSubterm(0),
             IntegerNode.class);
        IntegerNode operand2 = buildConstraintTree(constraint.getSubterm(1),
             IntegerNode.class);
        return new ArithOperatorNode(operand1, operand2, consName);
      }
      if (consName.equals("first") || consName.equals("left")
          || consName.equals("right") || consName.equals("last")) {
        AbstractParseNodeNode n = buildConstraintTree(constraint.getSubterm(0),
             AbstractParseNodeNode.class);
        return new NodeSelectorNode(n, consName);
      }
      if (consName.equals("or") || consName.equals("and")) {
        BooleanNode b1 = buildConstraintTree(constraint.getSubterm(0),
            BooleanNode.class);
        BooleanNode b2 = buildConstraintTree(constraint.getSubterm(1),
            BooleanNode.class);
        return new LogOperationNode(b1, b2, consName);
      }
      if (consName.equals("not")) {
        BooleanNode b1 = buildConstraintTree(constraint.getSubterm(0),
            BooleanNode.class);
        return new NegationNode(b1);
      }
      if (consName.equals("all")) {
        // TODO implement that
        throw new UnsupportedOperationException(
            "All is not implemented in compiled layouts");
      }
      if (consName.equals("col")) {
        AbstractParseNodeNode child = buildConstraintTree(
            constraint.getSubterm(0), AbstractParseNodeNode.class);
        return new MethodNode(child, MethodNode.Method.GET_COLUMN);
      }
      if (consName.equals("line")) {
        AbstractParseNodeNode child = buildConstraintTree(
            constraint.getSubterm(0), AbstractParseNodeNode.class);
        return new MethodNode(child, MethodNode.Method.GET_LINE);
      }
      throw new IllegalStateException("unhandeled constructor " + consName);
    default:
      throw new IllegalStateException("unhandeled constraint " + constraint);
    }
  }

  @SuppressWarnings("unchecked")
  public static <T extends LayoutNode<?>> T buildConstraintTree(IStrategoTerm constraint, Class<T> cl) {
    return (T) LayoutNodeCompiler.buildConstraintTree(constraint);
  }

}
