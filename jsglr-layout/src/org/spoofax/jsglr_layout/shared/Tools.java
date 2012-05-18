/*
 * Created on 04.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 *
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr_layout.shared;

import org.spoofax.jsglr_layout.client.Measures;


public class Tools {

    public static final boolean tracing = false;
    public static final boolean debugging = false;
    public static boolean logging = false;
    public static boolean measuring = false;

    private static Measures measures;

    public static void debug(Object ...s) {
    	if(debugging)
    		System.err.println(s);
    }

    public static void logger(Object ...s) {
    	if(logging)
    		System.out.println(s);
    }

    public static void setLogging(boolean enableLogging) {
        logging = enableLogging;
    }

    // Measuring

    public static void setMeasuring(boolean enableMeasuring) {
        measuring = enableMeasuring;
    }

    public static void setMeasures(Measures m) {
        measures = m;
    }

    public static Measures getMeasures() {
        return measures;
    }


}
