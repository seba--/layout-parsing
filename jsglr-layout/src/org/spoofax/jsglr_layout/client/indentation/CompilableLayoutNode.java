package org.spoofax.jsglr_layout.client.indentation;

public interface CompilableLayoutNode<V> extends LayoutNode<V> {
  
  public static enum InvokeState {
    SAFELY_INVOKABLE {

      @Override
      public InvokeState combine(InvokeState type) {
        // return the given type
        return type;
      }
      
    },
    UNSAFELY_INVOKABLE {

      @Override
      public InvokeState combine(InvokeState type) {
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
      public InvokeState combine(InvokeState type) {
        // Stays not invokable
        return this;
      }
      
    };
    
    public abstract InvokeState combine(InvokeState type);
    
  }
  
  public String getCompiledCode(LocalVariableManager manager, boolean atParseTime);
  public InvokeState getInvokeState(boolean atParseTime);

}
