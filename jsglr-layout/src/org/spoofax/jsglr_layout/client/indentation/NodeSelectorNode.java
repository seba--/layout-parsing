package org.spoofax.jsglr_layout.client.indentation;

import java.util.Map;

import org.spoofax.jsglr_layout.client.AbstractParseNode;

public class NodeSelectorNode extends NodeOperatorNode<AbstractParseNode>
    implements AbstractParseNodeNode {

  public static enum Position {
    LEFT {
      @Override
      public AbstractParseNode select(AbstractParseNode node) {
        return node.getLeft();
      }

      @Override
      public String getCode(String nodeCode) {
        return nodeCode + ".getLeft()";
      }
    },
    RIGHT {
      @Override
      public AbstractParseNode select(AbstractParseNode node) {
        return node.getRight();
      }

      @Override
      public String getCode(String nodeCode) {
        return nodeCode + ".getRight()";
      }
    },
    FIRST {
      @Override
      public AbstractParseNode select(AbstractParseNode node) {
        return node;
      }

      @Override
      public String getCode(String nodeCode) {
        return nodeCode;
      }
    },
    LAST {
      @Override
      public AbstractParseNode select(AbstractParseNode node) {
        return node.getLast();
      }

      @Override
      public String getCode(String nodeCode) {
        return nodeCode + ".getLast()";
      }
    };

    public abstract AbstractParseNode select(AbstractParseNode node);

    public abstract String getCode(String nodeCode);
    
  }

  private final Position position;

  private static Position fromParseSymbol(String symbol) {
    if (symbol.equals("first")) {
      return Position.FIRST;
    }
    if (symbol.equals("left")) {
      return Position.LEFT;
    }
    if (symbol.equals("right")) {
      return Position.RIGHT;
    }
    if (symbol.equals("last")) {
      return Position.LAST;
    }
    return null;
  }

  public NodeSelectorNode(AbstractParseNodeNode operand, String parseSymbol) {
    this(operand, fromParseSymbol(parseSymbol));
  }

  public NodeSelectorNode(AbstractParseNodeNode operand, Position position) {
    super(operand);
    this.position = position;
  }

  @Override
  public AbstractParseNode evaluate(AbstractParseNode[] kids,
      Map<String, Object> env, boolean parseTime) {
    if (parseTime
        && (this.position == Position.LEFT || this.position == Position.RIGHT)) {
      return null;
    }
    AbstractParseNode node = this.operand.evaluate(kids, env, parseTime);
    if (!parseTime) {
      assert node != null;
    }
    if (node == null) {
      return null;
    }
    AbstractParseNode result =  this.position.select(node);
    return result;
  }

  @Override
  public String getCompiledParseTimeCode(LocalVariableManager manager) {
    if (this.getParseTimeInvokeType() == ParseTimeInvokeType.NOT_INVOKABLE) {
      return "null";
    } else {
      return this.position.getCode(this.operand.getCompiledParseTimeCode(manager));
    }
  }

  @Override
  public String getCompiledDisambiguationTimeCode(LocalVariableManager manager) {
    return this.position.getCode(this.operand
        .getCompiledDisambiguationTimeCode(manager));
  }

  @Override
  public ParseTimeInvokeType getParseTimeInvokeType() {
    if (this.position == Position.LEFT || this.position == Position.RIGHT) {
      return ParseTimeInvokeType.NOT_INVOKABLE;
    } else {
      return ParseTimeInvokeType.SAFELY_INVOKABLE;
    }
  }

}
