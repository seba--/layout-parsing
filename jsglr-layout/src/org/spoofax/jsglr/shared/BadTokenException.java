package org.spoofax.jsglr.shared;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.jsglr.client.SGLR;

/**
 * Exception thrown when a specific token was unexpected by the parser.
 *
 * @author Lennart Kats <L.C.L.Kats add tudelft.nl>
 */
public class BadTokenException extends SGLRException {
    private static final long serialVersionUID = -2050581505177108272L;

    private final int token, offset, lineNumber, columnNumber;

    public int getOffset() {
        return offset;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getToken() {
        return token;
    }

    public boolean isEOFToken() {
        return token == SGLR.EOF;
    }

    @Override
    public String getMessage() {
        return getShortMessage() + " at line " + (lineNumber + 1)  + ", column " + (columnNumber - 1);
    }

    @Override
    public String getShortMessage() {
        if (isEOFToken())
            return "Unexpected end of file";
        else
            return "Syntax error near unexpected character '" + escape(token) + "'";
    }

    protected String escape(int ch) {
    	switch(ch) {
    	case 0: return "\\0";
    	case '\n': return "\\n";
    	case '\r': return "\\r";
    	default: return ""+ (char) ch;
    	}
	}

	public BadTokenException(SGLR parser, int token, int offset, int lineNumber, int columnNumber) {
        super(parser);
        this.token = token;
        this.offset = offset;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }


    @Override
    protected IStrategoTerm toLocationTerm() {
        ITermFactory factory = getParser().getParseTable().getFactory();
        return factory.makeAppl(
            factory.makeConstructor("area", 6),
            factory.makeInt(getLineNumber()),
            factory.makeInt(getColumnNumber()),
            factory.makeInt(getLineNumber()),
            factory.makeInt(getColumnNumber()),
            factory.makeInt(getOffset() + 1),
            factory.makeInt(0)
        );
    }
    
    @Override
    public String toString() {
    	return getMessage();
    }
}
