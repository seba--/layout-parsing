package org.spoofax.client;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr_orig.client.InvalidParseTableException;
import org.spoofax.jsglr_orig.client.ParseException;
import org.spoofax.jsglr_orig.client.ParseTable;
import org.spoofax.jsglr_orig.client.SGLR;
import org.spoofax.jsglr_orig.shared.BadTokenException;
import org.spoofax.jsglr_orig.shared.SGLRException;
import org.spoofax.jsglr_orig.shared.TokenExpectedException;
import org.spoofax.terms.TermFactory;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class JSGLREntryPoint implements EntryPoint {

	private void fetchParseTable(String parseTable) {
		final RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, parseTable);
		try {
			builder.sendRequest( null,  new RequestCallback() {
				public void onError(Request request, Throwable exception)
				{
					GWT.log( "error", exception );
				}
				public void onResponseReceived(Request request, Response response) {
					parseAndGo(response.getText());
				}
			});
		} catch (final RequestException e) {
			GWT.log( "error", e);
		}
	}

	public void onModuleLoad() {
		RootPanel.get().add(new Label("Loading parse table from server"));
		fetchParseTable("/Stratego.tbl");
		//		RemoteParseTableServiceAsync rpts = GWT.create(RemoteParseTableService.class);
		//		rpts.fetchParseTable("Stratego2.tbl", new AsyncCallback<ATerm>() {
		//
		//			@Override
		//			public void onSuccess(IStrategoTerm result) {
		//				parseAndGo(result);
		//			}
		//
		//			@Override
		//			public void onFailure(Throwable caught) {
		//				RootPanel.get().add(new Label("Failed to fetch parse table"));
		//			}
		//		});
	}

	void parseAndGo(String table) {
		try {
			System.out.println(table.length());
			final TermFactory af = new TermFactory();
			final IStrategoTerm pt = af.parseFromString(table);
			System.out.println(pt.toString().length());
			final SGLR sglr = new SGLR(af, new ParseTable(pt, af));
			long now = System.currentTimeMillis();
			final IStrategoTerm r = (IStrategoTerm) sglr.parse(strategoSampleCode(), null, null);
			now = System.currentTimeMillis() - now;
			if(r != null) {
				RootPanel.get().add(new Label(r.toString()));
				RootPanel.get().add(new Label("Parse time : " + now + " ms"));
			} else {
				RootPanel.get().add(new Label("Parsing failed"));
			}
		} catch (final InvalidParseTableException e) {
			wrap(e);
		} catch (final TokenExpectedException e) {
			wrap(e);
		} catch (final BadTokenException e) {
			wrap(e);
		} catch (final ParseException e) {
			wrap(e);
		} catch (final SGLRException e) {
			wrap(e);
		}

	}
	private static void wrap(Exception e) {
		System.err.println(e);
		throw new RuntimeException(e);
	}

	private String strategoSampleCode() {
		return "module common\n" +
		"imports\n" +
		"  include/Stratify\n" +
		"strategies\n" +
		"collect-meta-prop =\n" +
		"         ?Prop(n, v)\n" +
		"       ; rules(meta-prop : n -> v)\n" +
		"       ; <debug> (\"registered\", n, \"->\", v)\n" +
		"       ; <meta-prop> n\n" +
		"       ; <debug> (\"looked up\", n, \"->\", <id>)\n" +
		" apply-with-props(s | props) =\n" +
		"        {meta-props:\n" +
		"              where(<map(collect-meta-prop)> props)\n" +
		"          ; s\n" +
		"        }\n" +
		" string-join(|c) =\n" +
		"          <foldl(\\(x,y) -> <concat-strings> [y, c, x]\\)> (<id>, \"\")\n" +
		"        ; trim-chars(?',')";
	}

	private String booleanSampleCode() {
		return "(T&T|F)&not(F)|Bool293";
	}
}
