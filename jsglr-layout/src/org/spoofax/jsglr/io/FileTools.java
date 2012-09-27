package org.spoofax.jsglr.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class FileTools {

    private static char[] asyncBuffer = new char[4096];

    @Deprecated
    public static String loadFileAsString(String fn) {
        return tryLoadFileAsString(fn);
    }

    public static String tryLoadFileAsString(String fn) {
        try {
            return loadFileAsString(new FileReader(fn));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String loadFileAsString(Reader reader) throws IOException {
    	try {
	        StringBuilder result = new StringBuilder();
	        BufferedReader bufferedReader = new BufferedReader(reader);
	        synchronized (asyncBuffer) {
	            int read;
	            while ((read = bufferedReader.read(asyncBuffer)) > 0) {
	                result.append(asyncBuffer, 0, read);
	            }
	            return result.toString();
	        }
    	} finally {
    		if (reader != null) reader.close();
    	}
    }

}
