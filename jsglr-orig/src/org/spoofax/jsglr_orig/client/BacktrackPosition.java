package org.spoofax.jsglr_orig.client;

import java.util.ArrayList;

import org.spoofax.jsglr_orig.shared.ArrayDeque;

public class BacktrackPosition {
    public final int tokensSeen;
    public final ArrayDeque<Frame> recoverStacks;
    public final ArrayList<RecoverNode> recoverNodes; 
    public boolean isVisited;
    private int indexHistory;    
    
    public int getIndexHistory() {
        return indexHistory;
    }
    
    public BacktrackPosition( ArrayDeque<Frame> activeStacks, int tokSeen)
    {
        tokensSeen=tokSeen;
        recoverStacks = new ArrayDeque<Frame>(activeStacks);        
        recoverNodes=new ArrayList<RecoverNode>();        
    }

    public void setIndexHistory(int i) {
        indexHistory=i;
    }

}
