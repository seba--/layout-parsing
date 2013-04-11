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
import javassist.Translator;

import org.spoofax.jsglr_layout.client.AbstractParseNode;
import org.spoofax.jsglr_layout.client.indentation.LocalVariableManager.LocalVariable;

public class LayoutNodeCompiler {
  
  private static int compiledClassCount = 0;
  private static CtClass compiledInterfaceClass;
  private static CtClass booleanClass;
  private static Loader loader;
  
  static {
    try {
      ClassPool pool = ClassPool.getDefault();
      compiledInterfaceClass = pool.get(CompiledLayoutConstraint.class.getName());
       loader = new Loader(pool.getDefault());
      loader.delegateLoadingOf(CompiledLayoutConstraint.class.getName());
      loader.delegateLoadingOf(AbstractParseNode.class.getName());
      booleanClass = pool.get(Integer.class.getName());
    } catch (NotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public CompiledLayoutConstraint compile(BooleanNode node) throws NotFoundException, CannotCompileException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
    ClassPool classPool = ClassPool.getDefault();
    
    //Create the class
    CtClass newClass = classPool.makeClass("CompiledLayoutConstraint"+compiledClassCount);
    compiledClassCount ++;
    newClass.setInterfaces(new CtClass[]{compiledInterfaceClass});
    createMethod(node, true, newClass);
    createMethod(node, false, newClass);
    newClass.setModifiers(newClass.getModifiers() & ~Modifier.ABSTRACT);
    newClass.writeFile();
    

    //Load the class
   
    Class<? extends CompiledLayoutConstraint> loadedClass = (Class<? extends CompiledLayoutConstraint>) loader.loadClass(newClass.getName());
    newClass.freeze();
    //  System.out.println(java.util.Arrays.toString(loadedClass.getInterfaces()));
   // System.out.println(java.util.Arrays.toString(loadedClass.getDeclaredMethods()));
  //  System.out.println(java.util.Arrays.toString(loadedClass.getMethods()));
  //  System.out.println(java.lang.reflect.Modifier.isAbstract(loadedClass.getModifiers()));
    //Create an instance
  //  System.out.println("Compiled " + loadedClass);
    CompiledLayoutConstraint constraint =  loadedClass.newInstance();
 //   System.out.println("Read instance" + constraint);
    return constraint;
  }
  
  private void createMethod(BooleanNode node, boolean parse, CtClass newClass) throws NotFoundException, CannotCompileException {
    String methodName;
    String sourceCode;
    LocalVariableManager manager = new LocalVariableManager();
    if (parse) {
      methodName = "evaluateParseTime";
      sourceCode = node.getCompiledParseTimeCode(manager);
    } else {
      methodName = "evaluateDisambiguationTime";
      sourceCode = node.getCompiledDisambiguationTimeCode(manager);
    }
    //Append variable declarations to code
    sourceCode = getCodeForVariables(manager) + "return " + sourceCode +";";
    
    //Create the method
    ClassPool classPool = ClassPool.getDefault();
    CtClass parseNodeArrayClass = classPool.get(AbstractParseNode.class.getName()+"[]");
    CtClass mapClass = classPool.get(Map.class.getName());
    CtMethod parseMethod = new CtMethod(CtClass.intType, methodName, new CtClass[]{parseNodeArrayClass, mapClass}, newClass);
   // System.out.println("Parse: "+ parse + " " + methodName);
   // System.out.println(sourceCode);
    parseMethod.setBody(sourceCode);
    newClass.addMethod(parseMethod);
  }
  
  private String getCodeForVariables(LocalVariableManager manager) {
    String code = "";
    List<LocalVariable> variables = manager.getDeclaredVariables();
    for (LocalVariable var : variables) {
      code += var.getType().getName() + " " + var.getName()+";";
    }
    return code;
  }

}
