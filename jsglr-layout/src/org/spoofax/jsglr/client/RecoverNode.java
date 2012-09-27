package org.spoofax.jsglr.client;



public class RecoverNode {
    public final int tokensSeen;
    public Frame recoverStack;    
    public Frame parentStack;
    
    /*
    public RecoverNode(Frame st, int tokSeen, int line, int col, Frame parent)
    {
        super(tokSeen, line, col);
        recoverStack =st;        
        parentStack=parent;
    }*/
    
    public RecoverNode(Frame st, int tokSeen)
    {
        tokensSeen=tokSeen;
        recoverStack=st;        
        parentStack=null;
    }

}
