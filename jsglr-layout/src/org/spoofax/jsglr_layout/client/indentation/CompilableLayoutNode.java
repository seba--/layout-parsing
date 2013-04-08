package org.spoofax.jsglr_layout.client.indentation;

public interface CompilableLayoutNode<V> extends LayoutNode<V> {
  
  public static enum ParseTimeInvokeType {
    SAFELY_INVOKABLE {

      @Override
      public ParseTimeInvokeType combine(ParseTimeInvokeType type) {
        // return the given type
        return type;
      }
      
    },
    UNSAFELY_INVOKABLE {

      @Override
      public ParseTimeInvokeType combine(ParseTimeInvokeType type) {
        // Stays UNSAFLEY when SAFLY is given, otherwise given type
        if (type == SAFELY_INVOKABLE) {
          return this;
        } else {
          return type;
        }
      }
      
    },
    NOT_INVOKABLE {

      @Override
      public ParseTimeInvokeType combine(ParseTimeInvokeType type) {
        // Stays not invokable
        return this;
      }
      
    };
    
    public abstract ParseTimeInvokeType combine(ParseTimeInvokeType type);
    
  }
  
  public String getCompiledParseTimeCode(LocalVariableManager manager);
  public String getCompiledDisambiguationTimeCode(LocalVariableManager manager);
  public ParseTimeInvokeType getParseTimeInvokeType();

}
