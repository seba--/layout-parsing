package org.spoofax.jsglr.client;

import org.spoofax.jsglr.shared.SGLRException;

/**
 * Exception thrown when the specified start symbol could not be found.
 * 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class StartSymbolException extends SGLRException {

    private static final long serialVersionUID = 7793294984269018514L;

    public StartSymbolException(SGLR parser, String message) {
        super(parser, message);
    }

}
