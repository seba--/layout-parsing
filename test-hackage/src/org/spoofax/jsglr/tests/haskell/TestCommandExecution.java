package org.spoofax.jsglr.tests.haskell;

import java.util.Arrays;

import junit.framework.TestCase;

/**
 * @author seba
 */
public class TestCommandExecution extends TestCase {
  public Object[] testEcho() {
    Object[] os = CommandExecution.execute("", new String[] {"echo", "'abc'"});
    System.out.println(Arrays.toString(os));
    return os;
  }
  
  public void testEchos() {
    int number = 1000;
    long sum = 0;
    for (int i = 0; i < number; i++)
      sum += (Long) testEcho()[0];
    double mean = sum / number;
    System.out.println(mean + " ns");
    System.out.println(mean/1000/1000 + " ms");
  }
}
