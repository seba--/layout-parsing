package org.spoofax.jsglr_layout.client.indentation;

import org.spoofax.jsglr_layout.client.indentation.LocalVariableManager.LocalVariable;
import static org.spoofax.jsglr_layout.client.indentation.CompilableLayoutNode.InvokeState.*;

public abstract class BinaryArithNode<V> extends ArithmeticNode<V> {
  
  public BinaryArithNode(IntegerNode node1, IntegerNode node2) {
    super(new IntegerNode[] {node1, node2});
  }
  
  protected abstract String getOperatorString();
  protected abstract String convertToInt(String val, LocalVariableManager manager);
  @Override
  public String getCompiledCode(LocalVariableManager manager, boolean atParseTime) {
    InvokeState type0 = this.operands[0].getInvokeState(atParseTime);
    InvokeState type1 = this.operands[1].getInvokeState(atParseTime);
    if (type0 == NOT_INVOKABLE
        || type1 == NOT_INVOKABLE) {
      return "Integer.MIN_VALUE";
    }
    if (type0 == UNSAFELY_INVOKABLE) {
      if (type1 == UNSAFELY_INVOKABLE) {
        // Noth unsafe,, local variables for both
        LocalVariable var0 = manager.getFreeLocalVariable(Integer.class);
        LocalVariable var1 = manager.getFreeLocalVariable(Integer.class);
        String code = "((" +"("+var0.getName() + " = " + this.operands[0].getCompiledCode(manager, atParseTime) + ")"
            + " == Integer.MIN_VALUE || " + "(" + var1.getName() + " = " + this.operands[1].getCompiledCode(manager, atParseTime) + ")"
            + " == Integer.MIN_VALUE) ? Integer.MIN_VALUE : "+this.convertToInt(var0.getName() + this.getOperatorString() + var1.getName(),manager)  + ")";
        manager.releaseLocalVariable(var0);
        manager.releaseLocalVariable(var1);
        return code;
      } else {
        return this.getCodeForSafeAndUnsafeType(this.operands[0], manager, atParseTime);
      }
    } else {
      if (type1 == UNSAFELY_INVOKABLE) {
        return this.getCodeForSafeAndUnsafeType(this.operands[1], manager, atParseTime);
      } else {
        return "("+this.convertToInt(this.operands[0].getCompiledCode(manager, atParseTime)
            + this.getOperatorString()
            + this.operands[1].getCompiledCode(manager, atParseTime), manager)+")";
      }
    }
  }

  private String getCodeForSafeAndUnsafeType(IntegerNode unsafe, LocalVariableManager manager, boolean atParseTime) {
    // Local variable for unsafe
    LocalVariable var0 = manager.getFreeLocalVariable(Integer.class);
    String code = "("+"(" + var0.getName() + " = " +unsafe.getCompiledCode(manager, atParseTime) + ")" + " == Integer.MIN_VALUE ? Integer.MIN_VALUE : (";
    String executeCode;
    if (unsafe ==this.operands[0]) {
      executeCode = "("+var0.getName() + this.getOperatorString() + this.operands[1].getCompiledCode(manager, atParseTime)+")";
    } else {
      executeCode = "("+this.operands[0].getCompiledCode(manager, atParseTime) + this.getOperatorString() + var0.getName()+")";
    }
    code += this.convertToInt(executeCode, manager)+"))";
    manager.releaseLocalVariable(var0);
    return code;
  }

  @Override
  public InvokeState getInvokeState(boolean atParseTime) {
    return this.operands[0].getInvokeState(atParseTime).combine(
        this.operands[1].getInvokeState(atParseTime));
  }

}
