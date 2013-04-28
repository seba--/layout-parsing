package org.spoofax.jsglr.client;

import java.util.Set;

import org.spoofax.jsglr.shared.BadTokenException;

/**
 * Exception thrown when the parser times out.
 * 
 * @author Lennart Kats <L.C.L.Kats add tudelft.nl>
 */
public class ParseTimeoutException extends MultiBadTokenException {
    
    private static final long serialVersionUID = -8773024983956495431L;

    @Override
    public String getShortMessage() {
        return "Parser time out";
    }
    
    public ParseTimeoutException(SGLR parser, int token, int offset, int lineNumber, int columnNumber, Set<BadTokenException> causes) {
        super(parser, causes, token, offset, lineNumber, columnNumber);
    }
}