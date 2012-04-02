package org.spoofax.jsglr.tests.haskell.compare;

import junit.framework.TestCase;

import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.terms.Term;
import org.strategoxt.lang.Context;


/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 *
 */
public class TestCompareAST extends TestCase {
  private ITermFactory factory;
  private Context context;

  @Override
  public void setUp() {
    context = CompareAST.init();
    factory = context.getFactory();
  }
  
  boolean compareTerms(IStrategoTerm term1, IStrategoTerm term2) {
    IStrategoTerm result = compare_0_0.instance.invoke(context, context.getFactory().makeTuple(term1, term2));
    return result != null && Term.isTermList(result) && ((IStrategoList) result).isEmpty();
  }
  
  public void testFloatNorm() {
    IStrategoTerm floatTerm = factory.makeAppl(factory.makeConstructor("Float", 1), factory.makeString("0.01001"));
    IStrategoTerm expectedResult = factory.makeAppl(factory.makeConstructor("Float", 1), factory.makeString("0.01"));
    
    assertFalse(compareTerms(floatTerm, expectedResult));
    assertFalse(compareTerms(expectedResult, floatTerm));
  }
  
  public void testFloatNorm2() {
    IStrategoTerm floatTerm = factory.makeAppl(factory.makeConstructor("Float", 1), factory.makeString("0.0100"));
    IStrategoTerm expectedResult = factory.makeAppl(factory.makeConstructor("Float", 1), factory.makeString("0.01"));
    
    assertTrue(compareTerms(floatTerm, expectedResult));
    assertTrue(compareTerms(expectedResult, floatTerm));
  }
  
  public void testFloatNorm3() {
    IStrategoTerm floatTerm = factory.makeAppl(factory.makeConstructor("Float", 1), factory.makeString("901"));
    IStrategoTerm expectedResult = factory.makeAppl(factory.makeConstructor("Float", 1), factory.makeString("000901"));
    
    assertTrue(compareTerms(floatTerm, expectedResult));
    assertTrue(compareTerms(expectedResult, floatTerm));
  }
}
