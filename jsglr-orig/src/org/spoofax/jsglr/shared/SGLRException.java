/*
 * Created on 3. aug.. 2006
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 *
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr.shared;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.jsglr.client.SGLR;

public class SGLRException extends Exception {

    private static final long serialVersionUID = -8467572969588110480L;

    private final SGLR parser;

    public SGLRException(SGLR parser, String message, Throwable cause) {
        super(message, cause);
        this.parser = parser;
    }

    public SGLRException(SGLR parser, String message) {
        this(parser, message, null);
    }

    public SGLRException(SGLR parser) {
        this(parser, null, null);
    }

    public final SGLR getParser() {
        return parser;
    }

    public final IStrategoTerm toTerm() {
        return toTerm("-");
    }

    protected String getShortMessage() {
        return getMessage();
    }

    public IStrategoTerm toTerm(String filename) {
        if (parser == null)
            throw new UnsupportedOperationException();

        ITermFactory factory = parser.getParseTable().getFactory();
        return factory.makeAppl(
            factory.makeConstructor("error", 2),
            factory.makeString("Parse error"),
            factory.makeList(
                factory.makeAppl(
                    factory.makeConstructor("localized", 2),
                    factory.makeString(getShortMessage()),
                    factory.makeAppl(
                        factory.makeConstructor("area-in-file", 2),
                        factory.makeString(filename),
                        toLocationTerm()
                    )
                )
            )
        );
    }

    protected IStrategoTerm toLocationTerm() {
        ITermFactory factory = parser.getParseTable().getFactory();
        return factory.makeAppl(
            factory.makeConstructor("area", 6),
            factory.makeInt(0),
            factory.makeInt(0),
            factory.makeInt(0),
            factory.makeInt(0),
            factory.makeInt(0),
            factory.makeInt(0)
        );
    }
}
