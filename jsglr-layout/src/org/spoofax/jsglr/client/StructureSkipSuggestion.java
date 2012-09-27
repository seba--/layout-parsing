package org.spoofax.jsglr.client;



public class StructureSkipSuggestion {
    
    private IndentInfo startSkip;
    private IndentInfo endSkip; 
    private int indexHistoryStart;
    private int indexHistoryEnd;
    private char[] additionalTokens;
    
    public int getIndexHistoryStart() {
        return indexHistoryStart;
    }
    
    public boolean canBeDecomposed() {
        if(!isPreviousRegion())
            return false;
        return getIndexHistoryEnd()-getIndexHistoryStart() > 3;
    }
    
    private boolean isPreviousRegion(){
        return indexHistoryStart>=0 && indexHistoryEnd>=0;
    }

    public int getIndexHistoryEnd() {
        return indexHistoryEnd;
    }

    public void setStartSkip(IndentInfo startSkip) {
        this.startSkip = startSkip;
        if(endSkip!=null)
            getEndSkip().fillStackNodes(getStartSkip().getStackNodes());
    }

    public void setEndSkip(IndentInfo endSkip) {
        this.endSkip = endSkip;
        if(startSkip!=null)
            getEndSkip().fillStackNodes(getStartSkip().getStackNodes());
    }
    
    public void setStartSkip(IndentInfo startSkip, int indexStart) {
        indexHistoryStart=indexStart;
        setStartSkip(startSkip);        
    }

    public void setEndSkip(IndentInfo endSkip, int indexEnd) {
        indexHistoryEnd=indexEnd;
        setEndSkip(endSkip);
    }

    public StructureSkipSuggestion(){
        indexHistoryEnd=-1;
        indexHistoryStart=-1;
        additionalTokens=new char[0];
    }   
    
    public IndentInfo getStartSkip() {
        return startSkip;
    }
    
    public IndentInfo getEndSkip() {
        return endSkip;
    }
    
    public void setSkipLocations(IndentInfo startSkip, IndentInfo endSkip) {
        this.startSkip = startSkip;
        this.endSkip = endSkip;
        getEndSkip().fillStackNodes(getStartSkip().getStackNodes());
    }
    
    public void setSkipLocations(IndentInfo startSkip, IndentInfo endSkip, int indexStart, int indexEnd) {
        indexHistoryStart= indexStart;
        indexHistoryEnd=indexEnd;
        setSkipLocations(startSkip, endSkip);
    }
    
    public void setAdditionalTokens(char[] toParse) {
        additionalTokens=toParse;
    }

    public char[] getAdditionalTokens() {
        return additionalTokens;
    }    
}
