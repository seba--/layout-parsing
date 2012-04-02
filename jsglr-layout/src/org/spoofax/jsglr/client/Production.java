/*
 * Created on 04.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 * 
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr.client;

import static org.spoofax.jsglr.client.ProductionType.AVOID;
import static org.spoofax.jsglr.client.ProductionType.NO_TYPE;
import static org.spoofax.jsglr.client.ProductionType.PREFER;
import static org.spoofax.jsglr.client.ProductionType.REJECT;

import java.io.Serializable;

public class Production implements Serializable {

  static final long serialVersionUID = 8767621343854666185L;

  public final int arity;

  public final int label;

  public final int status;

  private final boolean isRecover;

  public Production(int arity, int label, int status, boolean isRecover) {
    this.arity = arity;
    this.label = label;
    this.status = status;
    this.isRecover = isRecover;
  }

  public AbstractParseNode apply(AbstractParseNode[] kids, int line, int column, boolean isLayout) {
    switch (status) {
    case REJECT:
      return new ParseNode(label, kids, AbstractParseNode.REJECT, line, column, isLayout);
    case AVOID:
      return new ParseNode(label, kids, AbstractParseNode.AVOID, line, column, isLayout);
    case PREFER:
      return new ParseNode(label, kids, AbstractParseNode.PREFER, line, column, isLayout);
    case NO_TYPE:
      return new ParseNode(label, kids, AbstractParseNode.PARSENODE, line, column, isLayout);
    }
    throw new IllegalStateException();
  }

  public boolean isRejectProduction() {
    return status == REJECT;
  }

  public boolean isRecoverProduction() {
    return isRecover;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Production))
      return false;
    Production o = (Production) obj;
    return arity == o.arity && label == o.label && status == o.status;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + arity;
    result = prime * result + label;
    result = prime * result + status;
    return result;
  }
}
