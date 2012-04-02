package org.spoofax.jsglr.shared;

import org.spoofax.jsglr.client.SGLR;

/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 */
public class BadIndentationException extends BadTokenException {

  private static final long serialVersionUID = 8916741860327644938L;

  private final int indentation;
  private final int expectedIndentation;

  public BadIndentationException(SGLR parser, int token, int offset, int lineNumber, int columnNumber, int indentation, int expectedIndentation) {
    super(parser, token, offset, lineNumber, columnNumber);
    this.indentation = indentation;
    this.expectedIndentation = expectedIndentation;
  }

  @Override
  public String getShortMessage() {
    return "Unexpected indentation of character '" + escape(getToken()) + "': was " + indentation + ", expected " + expectedIndentation;
  }

}
