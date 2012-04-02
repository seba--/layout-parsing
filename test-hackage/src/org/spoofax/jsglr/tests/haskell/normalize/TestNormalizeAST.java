package org.spoofax.jsglr.tests.haskell.normalize;

import junit.framework.TestCase;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.strategoxt.lang.Context;


/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 *
 */
public class TestNormalizeAST extends TestCase {
  private ITermFactory factory;
  private Context context;

  @Override
  public void setUp() {
    context = NormalizeAST.init();
    factory = context.getFactory();
  }
  
  public void testFloatNorm() {
    IStrategoTerm floatTerm = factory.makeAppl(factory.makeConstructor("Float", 1), factory.makeString("0.0100"));
    IStrategoTerm expectedResult = factory.makeAppl(factory.makeConstructor("Float", 1), factory.makeString("0.01"));
    
    IStrategoTerm actualResult = normalize_0_0.instance.invoke(context, floatTerm);
    
    assertEquals(expectedResult, actualResult);
  }
  
  public void testFloatNorm2() {
    IStrategoTerm floatTerm = factory.makeAppl(factory.makeConstructor("Float", 1), factory.makeString("0.01001"));
    IStrategoTerm expectedResult = floatTerm;
    
    IStrategoTerm actualResult = normalize_0_0.instance.invoke(context, floatTerm);
    
    assertEquals(expectedResult, actualResult);
  }
  
  public void testFloatNorm3() {
    IStrategoTerm floatTerm = factory.makeAppl(factory.makeConstructor("Float", 1), factory.makeString("901"));
    IStrategoTerm expectedResult = floatTerm;
    
    IStrategoTerm actualResult = normalize_0_0.instance.invoke(context, floatTerm);
    
    assertEquals(expectedResult, actualResult);
  }

  public void testFloatNorm4() {
    IStrategoTerm floatTerm = factory.makeAppl(factory.makeConstructor("Float", 1), factory.makeString("1.0"));
    IStrategoTerm expectedResult = floatTerm;
    
    IStrategoTerm actualResult = normalize_0_0.instance.invoke(context, floatTerm);
    
    assertEquals(expectedResult, actualResult);
  }

  public void testFloatNorm5() {
    IStrategoTerm floatTerm = factory.makeAppl(factory.makeConstructor("Float", 1), factory.makeString("1e1"));
    IStrategoTerm expectedResult = factory.makeAppl(factory.makeConstructor("Float", 1), factory.makeString("1.0e1"));
    
    IStrategoTerm actualResult = normalize_0_0.instance.invoke(context, floatTerm);
    
    assertEquals(expectedResult, actualResult);
  }
  
  public void testFloatNorm6() {
    IStrategoTerm floatTerm = factory.makeAppl(factory.makeConstructor("Float", 1), factory.makeString("1"));
    IStrategoTerm expectedResult = factory.makeAppl(factory.makeConstructor("Float", 1), factory.makeString("1.0"));;
    
    IStrategoTerm actualResult = normalize_0_0.instance.invoke(context, floatTerm);
    
    assertEquals(expectedResult, actualResult);
  }
  
  public void testModuleDeclNorm() {
//    Program(Body([Import(None,None,QModId("Distribution","Simple"),None,None)],[Valdef(Var("main"),Var("defaultMain"))]))
//    Module("Main",Some(Exportlist(["main"])),Body([Import(None,None,QModId("Distribution","Simple"),None,None)],[Valdef(Var("main"),Var("defaultMain"))]))
  }
}
