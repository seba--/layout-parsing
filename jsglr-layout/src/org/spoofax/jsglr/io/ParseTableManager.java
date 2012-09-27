/*
 * Created on 26. nov.. 2006
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.shared.Tools;
import org.spoofax.terms.ParseError;
import org.spoofax.terms.TermFactory;
import org.spoofax.terms.io.binary.TermReader;

public class ParseTableManager {
    
    private final Map<String, CachedTable> cache =
        new HashMap<String, CachedTable>();
    
    private final ITermFactory factory;
    
    private boolean useDiskCache;
    
    public ParseTableManager() {
        this(new TermFactory());
    }
    
    public ParseTableManager(ITermFactory factory) {
        this(factory, false);
    }
    
    public ParseTableManager(ITermFactory factory, boolean useDiskCache) {
        this.factory = factory;
        this.useDiskCache = useDiskCache;
    }
    
    public ParseTable loadFromFile(String filename) throws FileNotFoundException, IOException, InvalidParseTableException {
        
        ParseTable pt = null;
        
    	if(cache.containsKey(filename)) {
    	    CachedTable cached = cache.get(filename);
    	    pt = cached.get();
            if (pt != null && cached.isUpToDate()) return pt;
    	}
    	
        if(useDiskCache) {
        	final String cachedTable = hashFilename(filename);
        	File cached = new File(cachedTable);
        	File table = new File(filename);
        	if(cached.exists() && 
        			cached.lastModified() >= table.lastModified()) {
        		try {
					pt = loadFromDiskCache(cachedTable);
					pt.initTransientData(factory);
				} catch (ClassNotFoundException e) {
					// Fine, don't load from disk then
				}
        	}
        }

        if(pt == null) {
        	pt = loadFromStream(new FileInputStream(filename));
        	if(useDiskCache) {
        		storeInDiskCache(pt, filename);
        	}
        }
        
        cache.put(filename, new CachedTable(pt, filename));
        return pt;
    }
    
    private void storeInDiskCache(ParseTable pt, String filename) throws FileNotFoundException, IOException {
    	String storeName = hashFilename(filename);
    	File dir = new File(System.getProperty("user.home") + "/.jsglr/cache/");
    	if(!dir.exists()) {
    		dir.mkdirs();
    	}
    	ObjectOutputStream ous = new ObjectOutputStream(new FileOutputStream(storeName));
    	ous.writeObject(pt);
    	ous.close();
	}

	private ParseTable loadFromDiskCache(String cachedTable) throws FileNotFoundException, IOException, ClassNotFoundException {
		System.out.println("Loading " + cachedTable);
    	ObjectInputStream ois = new ObjectInputStream(new FileInputStream(cachedTable));
    	ParseTable pt = (ParseTable)ois.readObject();
    	return pt;
	}

	private String hashFilename(String filename) {
    	// FIXME use a real hash instead
    	return System.getProperty("user.home") + "/.jsglr/cache/" + String.format("%x", filename.hashCode());
	}

	public ParseTable loadFromStream(InputStream stream) throws IOException, InvalidParseTableException, ParseError {
        if(Tools.debugging) {
            Tools.debug("loadFromStream()");
        }
        
        if (stream == null) {
            throw new InvalidParseTableException("stream is null");
        }

        // TODO: optimize - load table directly from stream
        return initializeParseTable(factory, new TermReader(factory).parseFromStream(stream));
    }

	private ParseTable initializeParseTable(ITermFactory factory, IStrategoTerm pt) throws InvalidParseTableException {
        long start = System.currentTimeMillis();
		ParseTable parseTable = new ParseTable(pt, factory);
        long elapsed = System.currentTimeMillis() - start;

        if (SGLR.isLogging()) {
            Tools.logger("Loading parse table took " + elapsed/1000.0f + "s");
            Tools.logger("No. of states: ", parseTable.getStateCount());
            Tools.logger("No. of productions: ", parseTable.getProductionCount());
            Tools.logger("No. of action entries: ", parseTable.getActionCount());
            Tools.logger("No. of gotos entries: ", parseTable.getGotoCount());

            Tools.logger((parseTable.hasRejects() ? "Includes" : "Excludes"), " rejects");
            Tools.logger((parseTable.hasPriorities() ? "Includes" : "Excludes"), " priorities");
            Tools.logger((parseTable.hasPrefers() ? "Includes" : "Excludes"), " prefer actions");
            Tools.logger((parseTable.hasAvoids() ? "Includes" : "Excludes"), " avoid actions");
        }
        
        return parseTable;
	}

    public ITermFactory getFactory() {
        return factory;
    }

	public ParseTable loadFromTerm(IStrategoTerm term) throws InvalidParseTableException {
		return initializeParseTable(factory, term);
	}
	
	/**
	 * A parse table cache entry.
	 */
	private class CachedTable extends WeakReference<ParseTable> {
        
        private String path;
	    
        private long lastModified;
	    
	    public CachedTable(ParseTable table, String path) {
            super(table);
            this.path = path;
            this.lastModified = new File(path).lastModified();
        }
	    
	    public boolean isUpToDate() {
	        return new File(path).lastModified() == lastModified;
	    }
	}

}
