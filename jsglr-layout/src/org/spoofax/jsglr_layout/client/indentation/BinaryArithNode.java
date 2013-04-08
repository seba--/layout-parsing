package org.spoofax.jsglr_layout.client.indentation;

public abstract class BinaryArithNode<V> extends ArithmeticNode<V> {
  
  public BinaryArithNode(IntegerNode node1, IntegerNode node2) {
    super(new IntegerNode[] {node1, node2});
  }
  
  protected abstract String getOperatorString();
  
  @Override
  public String getCompiledParseTimeCode() {
    ParseTimeInvokeType type0 = this.operands[0].getParseTimeInvokeType();
    ParseTimeInvokeType type1 = this.operands[1].getParseTimeInvokeType();
    if (type0 == ParseTimeInvokeType.NOT_INVOKABLE
        || type1 == ParseTimeInvokeType.NOT_INVOKABLE) {
      return "null";
    }
    if (type0 == ParseTimeInvokeType.UNSAFELY_INVOKABLE) {
      if (type1 == ParseTimeInvokeType.UNSAFELY_INVOKABLE) {
        return "((" + this.operands[0].getCompiledParseTimeCode()
            + " == null || " + this.operands[1].getCompiledParseTimeCode()
            + " == null) ? null : "
            + this.getOperateParseTimeCode()+")";
      } else {
        return this.getCodeForSafeAndUnsafeType(this.operands[0]);
      }
    } else {
      if (type1 == ParseTimeInvokeType.UNSAFELY_INVOKABLE) {
        return this.getCodeForSafeAndUnsafeType(this.operands[1]);
      } else {
        return this.getOperateParseTimeCode();
      }
    }
  }

  private String getCodeForSafeAndUnsafeType(IntegerNode unsafe) {
    return "("+unsafe.getCompiledParseTimeCode() + " == null ? null : "
        + this.getOperateParseTimeCode() +")";
  }
  
  private String getOperateParseTimeCode() {
    return "("+this.operands[0].getCompiledParseTimeCode()
        + this.getOperatorString()
        + this.operands[1].getCompiledParseTimeCode()+")";
  }

  @Override
  public String getCompiledDisambiguationTimeCode() {
    return "("+this.operands[0].getCompiledDisambiguationTimeCode()
        + this.getOperatorString()
        + this.operands[1].getCompiledDisambiguationTimeCode()+")";
  }

  @Override
  public ParseTimeInvokeType getParseTimeInvokeType() {
    return this.operands[0].getParseTimeInvokeType().combine(
        this.operands[1].getParseTimeInvokeType());
  }

}
