package org.spoofax.jsglr.shared;

import org.spoofax.interpreter.terms.IStrategoTerm;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface RemoteParseTableServiceAsync {

	void fetchParseTable(String resourcePath, AsyncCallback<IStrategoTerm> callback);

	void readTermFromFile(String string, AsyncCallback<IStrategoTerm> callback);

	void fetchText(String string, AsyncCallback<String> callback);

}
