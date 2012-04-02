package org.spoofax.jsglr_orig.shared;

import org.spoofax.interpreter.terms.IStrategoTerm;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("parsetable")
public interface RemoteParseTableService extends RemoteService {

	IStrategoTerm fetchParseTable(String resourceName);

	IStrategoTerm readTermFromFile(String string);

	String fetchText(String string);
}
