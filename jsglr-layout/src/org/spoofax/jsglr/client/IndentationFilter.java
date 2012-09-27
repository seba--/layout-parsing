package org.spoofax.jsglr.client;

import static org.spoofax.terms.Term.termAt;

import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoNamed;
import org.spoofax.interpreter.terms.IStrategoTerm;

public class IndentationFilter {

    public static void resolveAmbiguitiesByIndentation(IStrategoTerm node)
    {
        if (node.getSubtermCount()>1)
        {
            IStrategoList contents;
            if ("amb".equals(((IStrategoNamed) node).getName())){
                contents = termAt(node, 0);
            }
            else{
                contents = termAt(node, 1);
            }
            for (int i = 0; i < contents.getSubtermCount(); i++) {
                resolveAmbiguitiesByIndentation(contents.getSubterm(i));
            }
        }

        if ("amb".equals(((IStrategoNamed) node).getName())){
            IStrategoList ambs = termAt(node, 0);
            node = ambs.getSubterm(0);
        }
    }

}
