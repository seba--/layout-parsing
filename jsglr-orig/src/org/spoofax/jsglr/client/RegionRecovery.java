//TODO: samenwerking met recovery connector eenduidiger
package org.spoofax.jsglr.client;

import java.util.ArrayList;


/**
 * Supports error recovery by selecting the region containing the error.
 * This region can be discarded (parsed as layout) or can be inspected by a refined recover method
 */
public class RegionRecovery {

    private SGLR myParser;    
    private StructureSkipSuggestion erroneousRegion;    
    private int errorDetectionLocation;
    public static int NR_OF_LINES_TILL_SUCCESS=6;
    private int acceptPosition;

    public int getAcceptPosition() {        
        return acceptPosition;
    }

    /**
     * Supports error recovery by selecting the region containing the error
     */
    public RegionRecovery(SGLR sglr){
        myParser=sglr;        
    }
    
    private ParserHistory getHistory() {
        return myParser.getHistory();
    }
    
    /** *
     *  Returns info about the parser configuration at the start of the erroneous region 
     */
    public IndentInfo getStartLineErrorFragment() {
        return erroneousRegion.getStartSkip();
    }
    
    /**
     * returns the location of the first non-erroneous character
     */
    public int getEndPositionErrorFragment() {
        return erroneousRegion.getEndSkip().getTokensSeen();
    }

    public int getStartPositionErrorFragment() {
        return erroneousRegion.getStartSkip().getTokensSeen();
    }

    /**
     *  Returns error fragment including the left margin (needed for bridge-parsing)
     */
    public String getErrorFragmentWithLeftMargin() {
        int tokIndexLine=getHistory().getTokensSeenStartLine(getStartLineErrorFragment().getTokensSeen(), myParser.currentInputStream);
        return getHistory().getFragment(tokIndexLine, getEndPositionErrorFragment()-1, myParser.currentInputStream);
    }
    
    public String getErrorFragment() {
        int tokIndexLine=getStartPositionErrorFragment();//+erroneousRegion.getAdditionalTokens().length; 
        return getHistory().getFragment(tokIndexLine, getEndPositionErrorFragment()-1, myParser.currentInputStream);
    }
    
    /**
     * Returns location where erroneous region starts, including left margin
     */
    public int getStartPositionErrorFragment_InclLeftMargin() {
        int tokIndexLine=getHistory().getTokensSeenStartLine(getStartLineErrorFragment().getTokensSeen(), myParser.currentInputStream);
        return tokIndexLine;
    }

    public ArrayList<IndentInfo> getSkippedLines() {        
        return getHistory().getLinesFromTo(erroneousRegion.getIndexHistoryStart(), getEndPositionErrorFragment());
    }      

    /**
     * Selects erroneous region based on layout 
     * @throws InterruptedException 
     */
    public boolean selectErroneousFragment() throws InterruptedException { 
        boolean eofReached=myParser.currentToken==SGLR.EOF;
        acceptPosition=-1;
        NewStructureSkipper newRegionSelector=new NewStructureSkipper(myParser);
        int failureIndex=getHistory().getIndexLastLine();
        assert(failureIndex >= 0);
        errorDetectionLocation=getHistory().getIndexLastToken();
        ArrayList<StructureSkipSuggestion> prevRegions=newRegionSelector.getPreviousSkipSuggestions(failureIndex);
        //System.out.println("PREVIOUS REGION");        
        if(trySetErroneousRegion(prevRegions)){
            ArrayList<StructureSkipSuggestion> decomposedRegions=newRegionSelector.getZoomOnPreviousSuggestions(erroneousRegion);
            trySetErroneousRegion(decomposedRegions);
            return true;
        }        
        ArrayList<StructureSkipSuggestion> currentRegions=newRegionSelector.getCurrentSkipSuggestions(failureIndex);
        //System.out.println("CURRENT REGION");
        if(trySetErroneousRegion(currentRegions)){            
            return true;
        }
        //System.out.println("PRIOR REGIONS");
        ArrayList<StructureSkipSuggestion> priorRegions=newRegionSelector.getPriorSkipSuggestions(failureIndex);
        if(trySetErroneousRegion(priorRegions)){
            ArrayList<StructureSkipSuggestion> decomposedRegions=newRegionSelector.getZoomOnPreviousSuggestions(erroneousRegion);
            trySetErroneousRegion(decomposedRegions);
            return true;
        }
        //System.out.println("FW-SIB REGIONS");
        ArrayList<StructureSkipSuggestion> siblingForWardRegions=newRegionSelector.getSibblingForwardSuggestions(failureIndex);
        if(trySetErroneousRegion(siblingForWardRegions)){            
            return true;
        }
        //System.out.println("BW-SIB REGIONS");
        ArrayList<StructureSkipSuggestion> siblingBackWardRegions=newRegionSelector.getSibblingBackwardSuggestions(failureIndex);
        if(trySetErroneousRegion(siblingBackWardRegions)){            
            return true;
        }
        //System.out.println("SURROUNDING-SIB REGIONS");        
        ArrayList<StructureSkipSuggestion> siblingSurroundingRegions=newRegionSelector.getSibblingSurroundingSuggestions(failureIndex);
        if(trySetErroneousRegion(siblingSurroundingRegions)){            
            return true;
        }
        //System.out.println("PARENT REGION");
        ArrayList<StructureSkipSuggestion> parentRegion=newRegionSelector.getParentSkipSuggestions(failureIndex);
        if(trySetErroneousRegion(parentRegion)){            
            return true;
        }
        //System.out.println("PARENT AND SIB REGION");
        //ArrayList<StructureSkipSuggestion> parentsibRegion=newRegionSelector.getParentSibSkipSuggestions(failureIndex);
        //if(trySetErroneousRegion(parentRegion)){            
          //  return true;
        //}
        erroneousRegion=newRegionSelector.getErroneousPrefix(failureIndex);
        ArrayList<StructureSkipSuggestion> decomposedRegions=newRegionSelector.getZoomOnPreviousSuggestions(erroneousRegion);
        boolean findSmallerPart=trySetErroneousRegion(decomposedRegions);
        if(!findSmallerPart){
            if(eofReached){
                int structStart=getHistory().getLastLine().structureStartPosition();
                //System.out.print(getHistory().getFragment(structStart-10, structStart));
                //System.out.print("$$");
                //System.out.print(getHistory().getFragment(structStart, structStart+10));
                int structStartIndex=getHistory().getLineOfTokenPosition(structStart);
                ArrayList<StructureSkipSuggestion> structRegions=newRegionSelector.getBlockSuggestions(structStartIndex);
                if(trySetErroneousRegion(structRegions)){            
                    return true;
                }
            }
            
            int indexAccept;
            if(getHistory().getIndexLastLine()>=failureIndex+NR_OF_LINES_TILL_SUCCESS)
                indexAccept=failureIndex+NR_OF_LINES_TILL_SUCCESS;
            else
                indexAccept=getHistory().getIndexLastLine();            
            acceptPosition=getHistory().getLine(indexAccept).getTokensSeen();
            IndentInfo openEnd=new IndentInfo();
            openEnd.setTokensSeen(Integer.MAX_VALUE);
            erroneousRegion.setEndSkip(openEnd, Integer.MAX_VALUE);
            return false;
        }
        return true; 
    }

    private boolean trySetErroneousRegion(ArrayList<StructureSkipSuggestion> regions) throws InterruptedException {
        StructureSkipSuggestion aSkip=new StructureSkipSuggestion();
        int indexSkips=0;
        myParser.acceptingStack=null; 
        myParser.activeStacks.clear(); //undo success
        boolean hasFoundErroneousRegion=false;
        while (indexSkips < regions.size() && !hasFoundErroneousRegion) {
            aSkip = regions.get(indexSkips);            
            hasFoundErroneousRegion=testRegion(aSkip);
            indexSkips++;            
        }        
        if(hasFoundErroneousRegion){
            erroneousRegion=aSkip;   
            //System.out.println("Erroneous region set ");
        }
        return hasFoundErroneousRegion;
    }

    private boolean testRegion(StructureSkipSuggestion aSkip) throws InterruptedException {
       // System.out.println("%%%%%%%%%%% TEST REGION %%%%%%%%%%%");
        //System.out.println(getInputFragment(aSkip));
        IndentInfo endPos=aSkip.getEndSkip();
        getHistory().setTokenIndex(endPos.getTokensSeen());
        myParser.activeStacks.clear();
        myParser.acceptingStack=null;
        myParser.activeStacks.addAll(endPos.getStackNodes());        
        parseAdditionalTokens(aSkip);
        int nrOfParsedLines=0; 
        //System.out.println("%%%%%%%%%%% CONTINUE PARSING %%%%%%%%%%%");
        IndentationHandler indentHandler = new IndentationHandler();
        indentHandler.setInLeftMargin(false);
        //System.out.println();
        //System.out.println("-------------------------");
        while((myParser.activeStacks.size() > 0 && nrOfParsedLines<NR_OF_LINES_TILL_SUCCESS)) {//|| !getHistory().hasFinishedRecoverTokens() 
            getHistory().readRecoverToken(myParser,false); 
            indentHandler.updateIndentation(myParser.currentToken);           
            //System.out.print((char)myParser.currentToken);
            myParser.doParseStep();
            if(getHistory().getTokenIndex()>errorDetectionLocation && indentHandler.lineMarginEnded())
                nrOfParsedLines++;
        }
        if(successCriterion()){
            acceptPosition=getHistory().getTokenIndex();
            return true;
        }
        return false;
    }

    private void parseAdditionalTokens(
            StructureSkipSuggestion aSkip) throws InterruptedException {
        for (char aChar : aSkip.getAdditionalTokens()) {
            myParser.currentToken=aChar;           
            myParser.doParseStep();
        }
        if(aSkip.getAdditionalTokens().length>0){            
            aSkip.getStartSkip().fillStackNodes(myParser.activeStacks);
            aSkip.getEndSkip().fillStackNodes(myParser.activeStacks);
            aSkip.getStartSkip().setTokensSeen(aSkip.getStartSkip().getTokensSeen() + aSkip.getAdditionalTokens().length);
            aSkip.setAdditionalTokens(new char[0]);
        }
    }

    private String getInputFragment(StructureSkipSuggestion aSkip) {
        return getHistory().getFragment(aSkip.getStartSkip().getTokensSeen(), aSkip.getEndSkip().getTokensSeen()-1, myParser.currentInputStream);
    }

    private boolean successCriterion() {
        return myParser.activeStacks.size() > 0 || myParser.acceptingStack!=null;
    }

    public int getStartIndexErrorFragment() { //TODO: erroneous region to recovery connector
        return erroneousRegion.getIndexHistoryStart();
    }

    public StructureSkipSuggestion getErroneousRegion() {
        return erroneousRegion;
    }

}
