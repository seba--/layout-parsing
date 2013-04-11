package org.spoofax.jsglr_layout.client.indentation;

import org.spoofax.jsglr_layout.client.indentation.LocalVariableManager.LocalVariable;

public abstract class BinaryArithNode<V> extends ArithmeticNode<V> {
  
  public BinaryArithNode(IntegerNode node1, IntegerNode node2) {
    super(new IntegerNode[] {node1, node2});
  }
  
  protected abstract String getOperatorString();
  protected abstract String convertToInt(String val, LocalVariableManager manager);
  @Override
  public String getCompiledParseTimeCode(LocalVariableManager manager) {
    ParseTimeInvokeType type0 = this.operands[0].getParseTimeInvokeType();
    ParseTimeInvokeType type1 = this.operands[1].getParseTimeInvokeType();
    if (type0 == ParseTimeInvokeType.NOT_INVOKABLE
        || type1 == ParseTimeInvokeType.NOT_INVOKABLE) {
      return "Integer.MIN_VALUE";
    }
    if (type0 == ParseTimeInvokeType.UNSAFELY_INVOKABLE) {
      if (type1 == ParseTimeInvokeType.UNSAFELY_INVOKABLE) {
        // Noth unsafe,, local variables for both
        LocalVariable var0 = manager.getFreeLocalVariable(Integer.class);
        LocalVariable var1 = manager.getFreeLocalVariable(Integer.class);
        String code = "((" +"("+var0.getName() + " = " + this.operands[0].getCompiledParseTimeCode(manager) + ")"
            + " == Integer.MIN_VALUE || " + "(" + var1.getName() + " = " + this.operands[1].getCompiledParseTimeCode(manager) + ")"
            + " == Integer.MIN_VALUE) ? Integer.MIN_VALUE : "+this.convertToInt(var0.getName() + this.getOperatorString() + var1.getName(),manager)  + ")";
        manager.releaseLocalVariable(var0);
        manager.releaseLocalVariable(var1);
        return code;
      } else {
        return this.getCodeForSafeAndUnsafeType(this.operands[0], manager);
      }
    } else {
      if (type1 == ParseTimeInvokeType.UNSAFELY_INVOKABLE) {
        return this.getCodeForSafeAndUnsafeType(this.operands[1], manager);
      } else {
        return "("+this.convertToInt(this.operands[0].getCompiledParseTimeCode(manager)
            + this.getOperatorString()
            + this.operands[1].getCompiledParseTimeCode(manager), manager)+")";
      }
    }
  }

  private String getCodeForSafeAndUnsafeType(IntegerNode unsafe, LocalVariableManager manager) {
    // Local variable for unsafe
    LocalVariable var0 = manager.getFreeLocalVariable(Integer.class);
    String code = "("+"(" + var0.getName() + " = " +unsafe.getCompiledParseTimeCode(manager) + ")" + " == Integer.MIN_VALUE ? Integer.MIN_VALUE : (";
    String executeCode;
    if (unsafe ==this.operands[0]) {
      executeCode = "("+var0.getName() + this.getOperatorString() + this.operands[1].getCompiledParseTimeCode(manager)+")";
    } else {
      executeCode = "("+this.operands[0].getCompiledParseTimeCode(manager) + this.getOperatorString() + var0.getName()+")";
    }
    code += this.convertToInt(executeCode, manager)+"))";
    manager.releaseLocalVariable(var0);
    return code;
  }
  
  @Override
  public String getCompiledDisambiguationTimeCode(LocalVariableManager manager) {
    return "("+this.convertToInt("("+this.operands[0].getCompiledDisambiguationTimeCode(manager)
        + this.getOperatorString()
        + this.operands[1].getCompiledDisambiguationTimeCode(manager)+")", manager)+")"
        ;
  }

  @Override
  public ParseTimeInvokeType getParseTimeInvokeType() {
    return this.operands[0].getParseTimeInvokeType().combine(
        this.operands[1].getParseTimeInvokeType());
  }

}
