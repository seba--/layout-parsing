/*
 * Created on 05.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 * 
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr.client;

import java.io.Serializable;

public abstract class ActionItem implements Serializable {

    static final long serialVersionUID = -340934714889427356L;

    public final int type;

    public static final int REDUCE = 1;

    public static final int SHIFT = 2;

    public static final int ACCEPT = 3;

    public static final int REDUCE_LOOKAHEAD = 4;

	public static final int ERROR = 5;

    public ActionItem(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
    	switch(type) {
    	case REDUCE: return "reduce";
    	case SHIFT: return "shift";
    	case ACCEPT: return "accept";
    	case REDUCE_LOOKAHEAD: return "reduce_la";
    	}
        return null;
    }
}
