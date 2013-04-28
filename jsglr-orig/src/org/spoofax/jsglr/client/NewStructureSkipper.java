package org.spoofax.jsglr.client;

import java.util.ArrayList;
import java.util.Collections;


public class NewStructureSkipper {

    private final static int MAX_NR_OF_LINES=15;
    private static final int MAX_NR_OF_STRUCTURES = 8;

    private SGLR myParser;    
    private StructuralTokenRecognizer structTokens;

    enum indentShift{
        INDENT,
        DEDENT,
        SAME_INDENT
    }    

    public ParserHistory getHistory() {
        return myParser.getHistory();
    }

    public NewStructureSkipper(SGLR sglr){   
        myParser=sglr;
        structTokens=new StructuralTokenRecognizer();
    }

    public ArrayList<StructureSkipSuggestion> getPreviousSkipSuggestions(int failureIndex) {
        return selectPrevRegion(failureIndex);
    }
    
    public ArrayList<StructureSkipSuggestion> getCurrentSkipSuggestions(int failureIndex) {
        ArrayList<StructureSkipSuggestion> result = getCurrentRegionSkips(failureIndex);  
        addNextRegionMerges(result);
        return result;
    }

    private void addNextRegionMerges(ArrayList<StructureSkipSuggestion> result) {
        ArrayList<StructureSkipSuggestion> includeNexts=new ArrayList<StructureSkipSuggestion>();
        for (StructureSkipSuggestion skip : result) {            
            for (StructureSkipSuggestion skipFW : selectRegion(skip.getIndexHistoryEnd())) {
                includeNexts.add(mergeRegions(skipFW, skip));
            }                            
        }        
        result.addAll(includeNexts);
    }

    private ArrayList<StructureSkipSuggestion> getCurrentRegionSkips(int failureIndex) {
        ArrayList<StructureSkipSuggestion> result=new ArrayList<StructureSkipSuggestion>();
        if (failureIndex>0 && isScopeOpeningLine(failureIndex) && getHistory().getLine(failureIndex-1).getIndentValue()==getHistory().getLine(failureIndex).getIndentValue())
            result.addAll(selectRegion(failureIndex-1));
        result.addAll(selectRegion(failureIndex));
        return result;
    }
    
    public ArrayList<StructureSkipSuggestion> getPriorSkipSuggestions(int failureIndex) {
        return getPriorRegions(failureIndex);
    }
    
    public ArrayList<StructureSkipSuggestion> getSibblingBackwardSuggestions(int failureIndex) {
        ArrayList<StructureSkipSuggestion> bwSkips=new ArrayList<StructureSkipSuggestion>();
        ArrayList<StructureSkipSuggestion> priorSiblings=getPriorRegions(failureIndex);
        ArrayList<StructureSkipSuggestion> currentRegionSuggestions=selectRegion(failureIndex);
        if(currentRegionSuggestions.isEmpty())
            currentRegionSuggestions=selectPrevRegion(failureIndex);
        for (StructureSkipSuggestion currSugestion : currentRegionSuggestions) {
            for (int i = 0; i < priorSiblings.size(); i++) {
                StructureSkipSuggestion priorSuggestion=priorSiblings.get(i);
                if(currSugestion.getAdditionalTokens().length==0){//ignore suggestions based on adding the separator
                    StructureSkipSuggestion mergedSkip=mergeRegions(currSugestion, priorSuggestion);
                    bwSkips.add(mergedSkip);
                }
            }
        }
        return bwSkips;
    }
    
    public ArrayList<StructureSkipSuggestion> getSibblingForwardSuggestions(int failureIndex) {
        ArrayList<StructureSkipSuggestion> fwSkips=new ArrayList<StructureSkipSuggestion>();
        ArrayList<StructureSkipSuggestion> nextSiblings=getCurrentAndNextSkipSuggestions(failureIndex);
        ArrayList<StructureSkipSuggestion> prevRegionSuggestions=selectPrevRegion(failureIndex);
        if(prevRegionSuggestions.isEmpty() && nextSiblings.size()> 0){
            prevRegionSuggestions=getCurrentRegionSkips(failureIndex);
            nextSiblings.remove(0);
        }
        for (StructureSkipSuggestion priorSuggestion : prevRegionSuggestions) {
            for (int i = 0; i < nextSiblings.size(); i++) {
                StructureSkipSuggestion nextSuggestion=nextSiblings.get(i);
                StructureSkipSuggestion mergedSkip=mergeRegions(nextSuggestion, priorSuggestion);
                fwSkips.add(mergedSkip);                
            }
        }
        return fwSkips;
    }
    
    public ArrayList<StructureSkipSuggestion> getSibblingSurroundingSuggestions(int failureIndex) {
        ArrayList<StructureSkipSuggestion> surroundingSkips=new ArrayList<StructureSkipSuggestion>();
        ArrayList<StructureSkipSuggestion> priorSiblings=getPriorRegions(failureIndex);
        ArrayList<StructureSkipSuggestion> nextSiblings=getCurrentAndNextSkipSuggestions(failureIndex);
        if(nextSiblings.size()>1 && priorSiblings.size()>0){
            nextSiblings.remove(0);
            StructureSkipSuggestion nextSuggestion=null;
            StructureSkipSuggestion priorSuggestion=null;
            int j=0;
            int i = 0;            
            while(i < nextSiblings.size() || j < priorSiblings.size()) {                
                if (i<nextSiblings.size()) {
                    nextSuggestion = nextSiblings.get(i); 
                    i++;
                }                         
                if (j<priorSiblings.size()) {
                    priorSuggestion = priorSiblings.get(j);  
                    j++;
                }
                StructureSkipSuggestion mergedSkip=mergeRegions(nextSuggestion, priorSuggestion);
                surroundingSkips.add(mergedSkip);
                if (j<priorSiblings.size()) {
                    priorSuggestion = priorSiblings.get(j); 
                    if(priorSuggestion.getAdditionalTokens().length!=0){
                        StructureSkipSuggestion mergedSkipPlus=mergeRegions(nextSuggestion, priorSuggestion);
                        surroundingSkips.add(mergedSkipPlus);
                        j++;
                    }
                }
            }
        }
        return surroundingSkips;
    }

    public ArrayList<StructureSkipSuggestion> getParentSkipSuggestions(int failureIndex) {
        ArrayList<StructureSkipSuggestion> parentSkips = new ArrayList<StructureSkipSuggestion>();
        int errorLineIndex=failureIndex;
        int maxBW=Math.max(failureIndex-MAX_NR_OF_LINES, 0);
        int nrOfStructs=0;
        while (errorLineIndex > maxBW && nrOfStructs<MAX_NR_OF_STRUCTURES) { 
            nrOfStructs++;
            int startSkipIndex = findParentBegin(errorLineIndex);
            ArrayList<StructureSkipSuggestion> skips=selectRegion(startSkipIndex);
            if(skips.isEmpty()){
                StructureSkipSuggestion closingSkip=new StructureSkipSuggestion();
                closingSkip.setSkipLocations(IndentInfo.cloneIndentInfo(getHistory().getLine(startSkipIndex)), IndentInfo.cloneIndentInfo(getHistory().getLine(failureIndex)), startSkipIndex, failureIndex);
                parentSkips.add(closingSkip);
            }
            parentSkips.addAll(skips);
            if(skips.size()>0)
                errorLineIndex=skips.get(0).getIndexHistoryStart();
            else
                errorLineIndex=-1;
        }
        addNextRegionMerges(parentSkips);
        return parentSkips;
    }

    public StructureSkipSuggestion getErroneousPrefix(int failureIndex) {
        StructureSkipSuggestion prefix=new StructureSkipSuggestion();
        if(getHistory().getIndexLastLine()>=0)
            prefix.setSkipLocations(IndentInfo.cloneIndentInfo(getHistory().getLine(0)), IndentInfo.cloneIndentInfo(getHistory().getLine(failureIndex)), 0, failureIndex);
        return prefix;
    }

    public ArrayList<StructureSkipSuggestion> getZoomOnPreviousSuggestions(StructureSkipSuggestion prevRegion) {
        ArrayList<StructureSkipSuggestion> result = new ArrayList<StructureSkipSuggestion>();
        if(!prevRegion.canBeDecomposed()){
            return result;
        }
        ArrayList<Integer> indentLevels=new ArrayList<Integer>(); 
        int startIndexZoom=Math.max(prevRegion.getIndexHistoryStart(), prevRegion.getIndexHistoryEnd()-MAX_NR_OF_LINES);
        for (int i = startIndexZoom; i < prevRegion.getIndexHistoryEnd(); i++) {
            int indentOfLine=getHistory().getLine(i).getIndentValue();
            if(!indentLevels.contains(indentOfLine))
                indentLevels.add(indentOfLine);
        }
        Collections.sort(indentLevels);
        indentLevels.remove(0);       
        //System.out.println(indentLevels);        
        int indentOfLevel;
        int lineIndex;
        for (int level = 0; level < indentLevels.size(); level++) {
            indentOfLevel=indentLevels.get(level);
            lineIndex = startIndexZoom;            
            while (lineIndex < prevRegion.getIndexHistoryEnd()) {
                int indentOfLine=getHistory().getLine(lineIndex).getIndentValue();
                if(indentOfLine==indentOfLevel){                    
                    ArrayList<StructureSkipSuggestion> regions = selectRegion(lineIndex); 
                    if(regions.size()>0){
                        //System.out.println("index: "+lineIndex +" indent: "+indentOfLevel);
                        lineIndex=regions.get(0).getIndexHistoryEnd();
                        //System.out.println();
                        //System.out.println(getHistory().getFragment(regions.get(0)));
                        //System.out.println();
                        //System.out.println("new index: "+lineIndex +" end indent: "+getHistory().getLine(lineIndex).getIndentValue());
                        Collections.reverse(regions);
                        result.addAll(regions);
                    }
                    else
                        lineIndex++;
                }
                else
                    lineIndex++;
            }
        }    
        Collections.reverse(result);
        return result;
    } 

    private ArrayList<StructureSkipSuggestion> selectRegion(int indexLine) {           
        if (isScopeClosingLine(indexLine))
            return new ArrayList<StructureSkipSuggestion>();        
        ArrayList<Integer> endLocations=findCurrentEnd(indexLine);
        ArrayList<StructureSkipSuggestion> skipSuggestions=new ArrayList<StructureSkipSuggestion>();
        IndentInfo startLine=IndentInfo.cloneIndentInfo(getHistory().getLine(indexLine));
        for (Integer endSkipIndex : endLocations) {
            if (endSkipIndex>indexLine) {
                IndentInfo endSkip = IndentInfo.cloneIndentInfo(getHistory()
                        .getLine(endSkipIndex));
                StructureSkipSuggestion skipConstruct = new StructureSkipSuggestion();
                skipConstruct.setSkipLocations(startLine, endSkip, indexLine,
                        endSkipIndex);
                skipSuggestions.add(skipConstruct);
                addSeparatorIncludingRegion_Forwards(skipSuggestions,
                        skipConstruct);
                addSeperatorIncludingRegion_Backwards(skipSuggestions,
                        skipConstruct);
            }
        }        
        return skipSuggestions;
    }

    private ArrayList<StructureSkipSuggestion> selectPrevRegion(int indexEnd) {
        ArrayList<StructureSkipSuggestion> prevRegions=new ArrayList<StructureSkipSuggestion>();       
        boolean onClosing=isScopeClosingLine(indexEnd);
        int indexStart = findPreviousBegin(indexEnd, onClosing);
        if(onClosing){
            if(indexEnd>0){
                if(isScopeClosingLine(indexEnd-1))
                    prevRegions.addAll(selectPrevRegion(indexEnd-1));
                else
                    prevRegions.addAll(selectRegion(indexEnd-1));
            }
            indexEnd++;
        }         
        IndentInfo endSkip=IndentInfo.cloneIndentInfo(getHistory().getLine(indexEnd));
        if(indexStart<0)
            return prevRegions;
        IndentInfo startSkip=IndentInfo.cloneIndentInfo(getHistory().getLine(indexStart));
        StructureSkipSuggestion previousRegion=new StructureSkipSuggestion();
        previousRegion.setSkipLocations(startSkip, endSkip, indexStart, indexEnd);
        prevRegions.add(previousRegion);
        addSeperatorIncludingRegion_Backwards(prevRegions, previousRegion);
        addSeparatorIncludingRegion_Forwards(prevRegions, previousRegion);            
        return prevRegions;
    }

    private void addSeperatorIncludingRegion_Backwards(
    		ArrayList<StructureSkipSuggestion> prevRegions, 
            StructureSkipSuggestion previousRegion) {
        int indexStart=previousRegion.getIndexHistoryStart();
        if(indexStart>0 && isSeparatorEndingLine(indexStart-1)){
            char[] toParse = structTokens.removeSeparatorAtTheEnd(readLine(indexStart-1));
            IndentInfo startSkip2=IndentInfo.cloneIndentInfo(getHistory().getLine(indexStart-1));
            IndentInfo endSkip2=IndentInfo.cloneIndentInfo(previousRegion.getEndSkip());
            StructureSkipSuggestion previousRegion2=new StructureSkipSuggestion();
            previousRegion2.setSkipLocations(startSkip2, endSkip2, indexStart-1, previousRegion.getIndexHistoryEnd());
            previousRegion2.setAdditionalTokens(toParse);
            prevRegions.add(previousRegion2);
        }
    }

    private void addSeparatorIncludingRegion_Forwards(
            ArrayList<StructureSkipSuggestion> regions,
            StructureSkipSuggestion aRegion) {
        if(isSeparatorStartingLine(aRegion.getIndexHistoryEnd())){
            IndentInfo startSkip=IndentInfo.cloneIndentInfo(aRegion.getStartSkip());
            IndentInfo endSkip=IndentInfo.cloneIndentInfo(aRegion.getEndSkip());
            int indentShift=separatorIndent(aRegion.getIndexHistoryEnd())- endSkip.getIndentValue();
            endSkip.setTokensSeen(endSkip.getTokensSeen()+indentShift);
            StructureSkipSuggestion previousRegion3=new StructureSkipSuggestion();
            previousRegion3.setSkipLocations(startSkip, endSkip, aRegion.getIndexHistoryStart(), aRegion.getIndexHistoryEnd());
            regions.add(previousRegion3);
        }
    }

    private ArrayList<StructureSkipSuggestion> getPriorRegions(int pos) {
        ArrayList<StructureSkipSuggestion> priorRegions= new ArrayList<StructureSkipSuggestion>();
        ArrayList<StructureSkipSuggestion> prevRegions=selectPrevRegion(pos);
        int bwMax=Math.max(pos-MAX_NR_OF_LINES, 0);
        int nrOfStructures=0;
        do{
            nrOfStructures+=1;
            if(!prevRegions.isEmpty())
                pos=prevRegions.get(0).getIndexHistoryStart();
            prevRegions=selectPrevRegion(pos);
            priorRegions.addAll(prevRegions);
        }while (pos>bwMax && nrOfStructures< MAX_NR_OF_STRUCTURES && !prevRegions.isEmpty());            
        return priorRegions;
    }

    private ArrayList<StructureSkipSuggestion> getCurrentAndNextSkipSuggestions(int failureIndex) {
        ArrayList<StructureSkipSuggestion> nextRegions= new ArrayList<StructureSkipSuggestion>();
        ArrayList<StructureSkipSuggestion> currRegions=selectRegion(failureIndex);
        int fwMax=failureIndex+MAX_NR_OF_LINES;
        int lineIndex=failureIndex;
        int nrOfStructs=0;
        int indentValueStart=-1;
        if(currRegions.size()>0)
            indentValueStart=currRegions.get(0).getStartSkip().getIndentValue();
        do{   
            nrOfStructs++;
            for (StructureSkipSuggestion r : currRegions) {
                if(r.getAdditionalTokens().length==0)
                    nextRegions.add(r);
            }            
            if(!currRegions.isEmpty()){
                lineIndex=currRegions.get(0).getIndexHistoryEnd();
                if (currRegions.get(0).getStartSkip().getIndentValue()>=indentValueStart) {
                    //System.out.println(currRegions.get(0).getIndexHistoryStart()+" => "+ currRegions.get(0).getIndexHistoryEnd());
                    currRegions = selectRegion(currRegions.get(0)
                            .getIndexHistoryEnd());
                }
                else
                    currRegions.clear(); //no dedent allowed
            }
        }while (nrOfStructs<MAX_NR_OF_STRUCTURES && lineIndex<fwMax && !currRegions.isEmpty());
        return nextRegions;
    }
    
    private ArrayList<Integer> findCurrentEnd(int indexStartLine) {
        int indentStartLine=separatorIndent(indexStartLine);        
        boolean hasIndentChilds=false;
        boolean isSecondLine=true;
        ArrayList<Integer> endLocations=new ArrayList<Integer>();
        int indexNextLine=skipLine(indexStartLine);        
        while(myParser.currentToken!=SGLR.EOF && 0<=indexNextLine && indexNextLine<=getHistory().getIndexLastLine()){            
            IndentInfo nextLine = getHistory().getLine(indexNextLine);
            // FIXME: get nextLine may be null 
            int indentSkipPosition=nextLine.getIndentValue();
            indentShift shift=calculateShift(indentStartLine, indentSkipPosition);
            switch (shift) {
            case DEDENT:               
                endLocations.add(indexNextLine);                
                return endLocations;                
            case INDENT:
                hasIndentChilds=true;
                break;
            case SAME_INDENT:
                if(hasIndentChilds && isScopeClosingLine(indexNextLine)){
                    indexNextLine = skipLine(indexNextLine);
                    endLocations.add(indexNextLine);
                    return endLocations;
                }
                if((!isSecondLine || !isScopeOpeningLine(indexNextLine)) && !isSeparatorStartingLine(indexNextLine)){
                    endLocations.add(indexNextLine);
                    return endLocations;
                }
                break;
            default:
                break;
            }
            isSecondLine=false;
            indexNextLine=skipLine(indexNextLine);            
        }
        endLocations.add(getHistory().getIndexLastLine()); //EOF
        return endLocations;
    }
    
    private int findPreviousBegin(int indexLine, boolean onClosing) { 
        int indentValue = getHistory().getLine(indexLine).getIndentValue();
        boolean sawChilds=false;
        boolean closingSeen=onClosing;
        boolean ignoreSeps=!isSeparatorStartingLine(indexLine);
        int indexHistoryLines=indexLine;
        while(indexHistoryLines>0){
            indexHistoryLines-=1;
            int indentSkipPosition=getHistory().getLine(indexHistoryLines).getIndentValue();
            indentShift shift=calculateShift(indentValue, indentSkipPosition);
            switch (shift) {
            case DEDENT:
                if(!sawChilds)
                    return -1;
                return indexHistoryLines;              
            case INDENT:      
                if(!ignoreSeps && !isSeparatorStartingLine(indexHistoryLines) && separatorIndent(indexLine)==indentSkipPosition)
                    return indexHistoryLines;
                sawChilds=true;
                break;
            case SAME_INDENT: 
                if(!sawChilds && isScopeClosingLine(indexHistoryLines)){
                    if(closingSeen)
                        return indexHistoryLines+1;
                    closingSeen=true;
                }
                else
                    if(!sawChilds || !isScopeOpeningLine(indexHistoryLines)){                       
                        if(!(ignoreSeps && isSeparatorStartingLine(indexHistoryLines)))
                            return indexHistoryLines;
                    }        
                break;
            default:
                break;
            }
        }  
        return 0;
    }  
    
    private int findParentBegin(int startLineIndex) {
        int indentStartLine=separatorIndent(startLineIndex); 
        int indexHistoryLines=startLineIndex;
        while(indexHistoryLines > 0){
            indexHistoryLines-=1;            
            int indentSkipPosition=separatorIndent(indexHistoryLines); //currentLine.getIndentValue();
            indentShift shift=calculateShift(indentStartLine, indentSkipPosition);
            if (shift==indentShift.DEDENT){
                if(isScopeOpeningLine(indexHistoryLines))
                {
                    IndentInfo prevLine = getHistory().getLine(indexHistoryLines-1);
                    if((!isScopeClosingLine(indexHistoryLines-1)) && calculateShift(indentSkipPosition, prevLine.getIndentValue())==indentShift.SAME_INDENT){                            
                        return indexHistoryLines-1;
                    }                        
                }                
                return indexHistoryLines;
            }            
        }        
        return 0; //SOF
    }

    private int separatorIndent(int indexLine) {
        int indentValue = getHistory().getLine(indexLine).getIndentValue();
        String lineContent = readLine(indexLine);
        return indentValue+structTokens.separatorIndent(lineContent);
    }

    private boolean isScopeOpeningLine(int index) {
        String lineContent = readLine(index);
        return structTokens.isScopeOpeningLine(lineContent);
    }

    private boolean isScopeClosingLine(int index) {
        String lineContent = readLine(index);
        return structTokens.isScopeClosingLine(lineContent);
    }

    private boolean isSeparatorStartingLine(int index) {
        String lineContent = readLine(index);
        return structTokens.isSeparatorStartedLine(lineContent);
    }

    private boolean isSeparatorEndingLine(int index) {
        String lineContent = readLine(index);
        return structTokens.isSeparatorEndingLine(lineContent);
    }

    private String readLine(int index) {
        while(getHistory().getIndexLastLine()<=index && myParser.currentToken!=SGLR.EOF)
            getHistory().readRecoverToken(myParser, false);
        if(index<=getHistory().getIndexLastLine()){
            IndentInfo line=getHistory().getLine(index);
            return readLine(line);
        }
        return "";
    }

    private String readLine(IndentInfo line) {
        int startTok = line.getTokensSeen();
        String lineContent=getHistory().readLine(startTok, myParser.currentInputStream);
        return lineContent;
    }

    private indentShift calculateShift(int indentStartLine, int indentSkipPosition) {
        int difference=indentStartLine-indentSkipPosition;
        if(difference>0)
            return indentShift.DEDENT;
        if(difference<0)
            return indentShift.INDENT;
        return indentShift.SAME_INDENT;
    }

    private int skipLine(int indexLine) {
        IndentInfo line =getHistory().getLine(indexLine);
        IndentationHandler skipIndentHandler=new IndentationHandler();
        getHistory().setTokenIndex(Math.max(0, line.getTokensSeen()-1));        
        skipIndentHandler.setInLeftMargin(false);
        getHistory().readRecoverToken(myParser, false);
        while(myParser.currentToken!=SGLR.EOF){
            getHistory().readRecoverToken(myParser, false);            
            skipIndentHandler.updateIndentation(myParser.currentToken);
            if(skipIndentHandler.lineMarginEnded()){
                return indexLine+=1;
            }            
        }
        return getHistory().getIndexLastLine();// EOF
    }
    
    private StructureSkipSuggestion mergeRegions(StructureSkipSuggestion fwSuggestion,
            StructureSkipSuggestion bwSuggestion) {
        StructureSkipSuggestion mergedSkip=new StructureSkipSuggestion();
        mergedSkip.setSkipLocations(IndentInfo.cloneIndentInfo(bwSuggestion.getStartSkip()), IndentInfo.cloneIndentInfo(fwSuggestion.getEndSkip()), bwSuggestion.getIndexHistoryStart(), fwSuggestion.getIndexHistoryEnd());
        mergedSkip.setAdditionalTokens(bwSuggestion.getAdditionalTokens());
        return mergedSkip;
    }

    public ArrayList<StructureSkipSuggestion> getBlockSuggestions(int structStartIndex)  {
        ArrayList<StructureSkipSuggestion> result=getCurrentRegionSkips(structStartIndex);
        int endIndex=Math.min(getHistory().getIndexLastLine()+1, structStartIndex + MAX_NR_OF_LINES);
        for (int i = structStartIndex; i < endIndex; i++) {
            IndentInfo startSkip=IndentInfo.cloneIndentInfo(getHistory().getLine(structStartIndex));
            IndentInfo endSkip=IndentInfo.cloneIndentInfo(getHistory().getLine(i));
            StructureSkipSuggestion block=new StructureSkipSuggestion();
            block.setSkipLocations(startSkip, endSkip, structStartIndex, i);
            result.add(block);
        }        
        return result;
    }

}
