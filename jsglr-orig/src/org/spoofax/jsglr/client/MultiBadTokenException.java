package org.spoofax.jsglr.client;

import java.util.Set;

import org.spoofax.jsglr.shared.BadTokenException;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class MultiBadTokenException extends BadTokenException {

    private static final long serialVersionUID = -1082868203048369423L;

    private final Set<BadTokenException> causes;
    
    public MultiBadTokenException(SGLR parser, Set<BadTokenException> causes) {
        this(parser, causes, getFirst(causes));
    }
    
    protected MultiBadTokenException(SGLR parser, Set<BadTokenException> causes, BadTokenException primary) {
        super(parser, primary.getToken(), primary.getOffset(), primary.getLineNumber(), primary.getColumnNumber());
        this.causes = causes;
    }
    
    protected MultiBadTokenException(SGLR parser, Set<BadTokenException> causes,
            int primaryToken, int primaryOffset,
            int primaryLineNumber, int primaryColumnNumber) {
        super(parser, primaryToken, primaryOffset, primaryLineNumber, primaryColumnNumber);
        this.causes = causes;
    }

    private static BadTokenException getFirst(Set<BadTokenException> causes) {
        if (causes.isEmpty()) throw new IllegalArgumentException("Set of causes is empty");
        return causes.iterator().next();
    }
    
    public Set<BadTokenException> getCauses() {
        return causes;
    }
    
    @Override
    @Deprecated
    /**
     * @deprecated Use getCauses() instead.
     */
    public Throwable getCause() {
        if (causes != null && !causes.isEmpty()) return causes.iterator().next();
        return null;
    }
    
}
