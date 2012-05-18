package org.spoofax.jsglr_layout.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr_layout.shared.RemoteParseTableService;
import org.spoofax.terms.ParseError;
import org.spoofax.terms.TermFactory;
import org.spoofax.terms.io.binary.TermReader;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class RemoteParseTableServiceImpl extends RemoteServiceServlet implements RemoteParseTableService {

	public IStrategoTerm fetchParseTable(String resourceName) throws ParseError {
		final TermFactory f = new TermFactory();
		try {
			final InputStream is = new FileInputStream(resourceName);
			final IStrategoTerm e = new TermReader(f).parseFromStream(is);
			is.close();
			System.out.println("Loaded term, serializing");
			return e;
		} catch(final FileNotFoundException e) {
			e.printStackTrace();
		} catch(final IOException e) {
			e.printStackTrace();
		}
		System.err.println("Failed to load parse table " + resourceName);
		return null;
	}

	public IStrategoTerm readTermFromFile(String string) {
		return new TermFactory().makeInt(0);
	}

	public String fetchText(String string) {
		return "abc";
	}
}
