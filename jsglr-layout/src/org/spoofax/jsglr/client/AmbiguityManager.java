/*
 * Created on 01.apr.2006
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr.client;

import java.util.HashMap;
import java.util.Map;

import org.spoofax.jsglr.shared.Tools;

public class AmbiguityManager {

    private int injectionFilterSucceededCount;
    private int eagernessFilterSucceededCount;
    private int injectionCallsCount;
    private int eagernessGreaterCallsCount;
    //private int preferAndAvoidCallsCount;
    private int ambiguityCallsCount;
    private int maxNumberOfAmbiguitiesCount;
    private int ambiguitiesCount;
    protected int clustersVisitedCount;
    
    private Map<AmbKey,Integer> indexTable;

    AmbiguityManager(int inputLength) {
        indexTable = new HashMap<AmbKey, Integer>();
    }

    public int increaseAmbiguityCalls() {
        return ambiguityCallsCount++;
    }

    public int getAmbiguityCallsCount() {
        return ambiguityCallsCount;
    }
    
    public int getAmbiguitiesCount() {
        return ambiguitiesCount;
    }

    public int getMaxNumberOfAmbiguities() {
        return maxNumberOfAmbiguitiesCount;
    }

    public void resetClustersVisitedCount() {
        clustersVisitedCount = 0;
    }

    public void resetAmbiguityCount() {
        ambiguitiesCount = 0;
    }

    public void increaseAmbiguityCount() {
        ambiguitiesCount++;
    }

    public void decreaseAmbiguityCount() {
      ambiguitiesCount--;
    }

    public void decreaseAmbiguityCount(int c) {
      ambiguitiesCount -= c;
    }
    
    public int getClusterIndex(AbstractParseNode t, int pos) {
        if(Tools.debugging) {
            Tools.debug("getClusterIndex()");
            Tools.debug(" t - " + t);
            Tools.debug(" pos - " + pos);
            //Tools.debug(indexTable);
        }
        AmbKey k = new AmbKey(t, pos);
        Integer r = indexTable.get(k);
        return r == null ? -1 : r.intValue();
    }

    public void dumpIndexTable() {
        Tools.debug(indexTable);
    }

    public void increaseEagernessFilterSucceededCount() {
        eagernessFilterSucceededCount++;
    }

    public void increaseInjectionFilterSucceededCount() {
        injectionFilterSucceededCount++;
    }

    public int getEagernessComparisonCount() {
        return eagernessGreaterCallsCount;
    }

    public void increaseEagernessFilterCalledCount() {
        eagernessGreaterCallsCount++;
        
    }

    public int getEagernessSucceededCount() {
        return eagernessFilterSucceededCount;
    }

    public void increaseInjectionCount() {
        injectionCallsCount++;
        
    }

    public int getInjectionCount() {
        return injectionCallsCount;
    }

}
