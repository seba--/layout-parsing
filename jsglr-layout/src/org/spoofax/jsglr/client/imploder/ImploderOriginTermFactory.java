package org.spoofax.jsglr.client.imploder;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.terms.attachments.OriginTermFactory;

/** 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class ImploderOriginTermFactory extends OriginTermFactory {

	public ImploderOriginTermFactory(ITermFactory baseFactory) {
		super(baseFactory);
	}

	@Override
	public boolean isOriginRoot(IStrategoTerm term) {
		return term.getAttachment(ImploderAttachment.TYPE) != null;
	}

}
