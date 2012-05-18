package org.spoofax.jsglr_layout.client;

import java.util.ArrayList;

import org.spoofax.PushbackStringIterator;

public class ParserHistory {
    
    private IndentationHandler indentHandler;
    private IndentationHandler recoveryIndentHandler;
    
    private ArrayList<IndentInfo> newLinePoints;
    private int recoverTokenCount;
    private int tokenIndex;
    
    public int getTokenIndex() {
        return tokenIndex;
    }
    
    public int getIndexLastToken() {
        return recoverTokenCount-1;
    }

    public void setTokenIndex(int tokenIndex) {
        this.tokenIndex = tokenIndex;
    }
    
    public ParserHistory(){    
        newLinePoints=new ArrayList<IndentInfo>();        
        clear();
    }
     
    public void clear(){
        newLinePoints.clear();
        recoverTokenCount = 0;
        tokenIndex=0;
        indentHandler = new IndentationHandler();
        recoveryIndentHandler=new IndentationHandler();
    }
    
    public void resetRecoveryIndentHandler(int indentValue){
        recoveryIndentHandler=new IndentationHandler();
        recoveryIndentHandler.setInLeftMargin(true);
        recoveryIndentHandler.setIndentValue(indentValue);
    }
    /*
     * Set current token of parser based on recover tokens or read from new tokens
     */
    public void readRecoverToken(SGLR myParser, boolean keepRecoveredLines) {  
        if (hasFinishedRecoverTokens()) {             
            if(myParser.getCurrentToken()!=SGLR.EOF){                
                if(getIndexLastToken()>=0){
                    myParser.readNextToken();
                    indentHandler.updateIndentation(myParser.getCurrentToken());
                    recoverTokenCount++;   
                    if(indentHandler.lineMarginEnded() || myParser.getCurrentToken()==SGLR.EOF)
                        keepNewLinePoint(myParser, myParser.tokensSeen-1, true, indentHandler);
                }
            }
        }
        else{
            myParser.setCurrentToken(readCharAt(tokenIndex, myParser.currentInputStream));
            if(myParser.getCurrentToken() == -1) {
            	myParser.setCurrentToken(SGLR.EOF);
    		}
            if(keepRecoveredLines){
                recoveryIndentHandler.updateIndentation(myParser.getCurrentToken());
                if(recoveryIndentHandler.lineMarginEnded() || myParser.getCurrentToken()==SGLR.EOF)
                    keepNewLinePoint(myParser, tokenIndex, false, recoveryIndentHandler);
            }    
        }
        tokenIndex++;
        
    }
    
    public boolean hasFinishedRecoverTokens() {
        return tokenIndex >= recoverTokenCount;
    }
    
    public int getTokensSeenStartLine(int tokPosition, PushbackStringIterator chars){
        int tokIndexLine=tokPosition;
        while (readCharAt(tokIndexLine, chars) != '\n' && tokIndexLine>0) {
            tokIndexLine-=1;
        }
        return tokIndexLine;
    }
    
    private int readCharAt(int offset, PushbackStringIterator chars){
    	chars.setOffset(offset);
        return chars.read();
    }

    public void keepTokenAndState(SGLR myParser) {
        indentHandler.updateIndentation(myParser.getCurrentToken());
        recoverTokenCount++;
        tokenIndex++;
        if(indentHandler.lineMarginEnded() || myParser.getCurrentToken()==SGLR.EOF || tokenIndex == 1)
            keepNewLinePoint(myParser, myParser.tokensSeen-1, false, indentHandler);
    }
    
    public void keepInitialState(SGLR myParser) {        
        IndentInfo newLinePoint= new IndentInfo(0, 0, 0);
        newLinePoint.fillStackNodes(myParser.activeStacks);
        newLinePoints.add(newLinePoint);
    }
    
    private void keepNewLinePoint(SGLR myParser, int tokSeen ,boolean inRecoverMode, IndentationHandler anIndentHandler) {
        int indent = anIndentHandler.getIndentValue();
        IndentInfo newLinePoint= new IndentInfo(myParser.lineNumber, tokSeen, indent);
        newLinePoints.add(newLinePoint);
        //System.out.println(newLinePoints.size()-1+" NEWLINE ("+newLinePoint.getIndentValue()+")"+newLinePoint.getTokensSeen());
        if(!inRecoverMode){
            newLinePoint.fillStackNodes(myParser.activeStacks);           
        }
    }

    public String getFragment(int startTok, int endTok, PushbackStringIterator chars) {
        String fragment="";
        for (int i = startTok; i <= endTok; i++) {
            if(i >= recoverTokenCount)
                break;
            fragment+= (char)readCharAt(i, chars);
        }        
        return fragment;
    }
    
    public String readLine(int StartTok, PushbackStringIterator chars) {
        String fragment="";
        int pos=StartTok;
        int currentTok=' ';
        while(currentTok!='\n' && currentTok!=SGLR.EOF && pos<recoverTokenCount) {            
            currentTok=readCharAt(pos, chars);
            fragment+= (char)currentTok;
            pos++;
        }        
        return fragment;
    }
    
    public IndentInfo getLine(int index){
        // FIXME: throw an IndexOutOfBoundsException: this is indicates a programmer error
        if(index < 0 || index > getIndexLastLine())
            return null;
        return newLinePoints.get(index);
    }
    
    public IndentInfo getLastLine(){
        return newLinePoints.get(newLinePoints.size()-1);
    }
    
    public int getIndexLastLine(){
        return newLinePoints.size()-1;
    }
    
    public ArrayList<IndentInfo> getLinesFromTo(int startIndex, int endLocation) {
        int indexLine = startIndex;
        ArrayList<IndentInfo> result=new ArrayList<IndentInfo>();
        IndentInfo firstLine = newLinePoints.get(indexLine);
        while(indexLine < newLinePoints.size()){
             firstLine = newLinePoints.get(indexLine);
             if(firstLine.getTokensSeen() < endLocation){
                 result.add(firstLine);
                 indexLine++;
             }
             else{
                 indexLine=newLinePoints.size();
             }
        }
        return result;
    }

    public void deleteLinesFrom(int startIndexErrorFragment) {
        if(startIndexErrorFragment>=0 && startIndexErrorFragment<newLinePoints.size()-1){
            ArrayList<IndentInfo> shrinkedList=new ArrayList<IndentInfo>();
            shrinkedList.addAll(newLinePoints.subList(0, startIndexErrorFragment));
            newLinePoints=shrinkedList;
        }
        else if (startIndexErrorFragment > newLinePoints.size()-1){
            System.err.println("StartIndex Error Fragment: "+startIndexErrorFragment);
            System.err.println("Numeber Of Lines in History: : "+newLinePoints.size());
            System.err.println("Unexpected index of history new-line-points");            
        }
    }
    
    /*
    public void logHistory(){       
       for (int i = 0; i < newLinePoints.size()-1; i++) {
           IndentInfo currLine=newLinePoints.get(i);
           IndentInfo nextLine=newLinePoints.get(i+1);
           String stackDescription="";
           for (Frame node : currLine.getStackNodes()) {
               stackDescription+=node.state.stateNumber+";";
           }
           System.out.print("("+i+")"+"["+currLine.getIndentValue()+"]"+"{"+stackDescription+"}"+getFragment(currLine.getTokensSeen(), nextLine.getTokensSeen()-1));
       }
       IndentInfo currLine=newLinePoints.get(newLinePoints.size()-1);
       System.out.print("("+(newLinePoints.size()-1)+")"+"["+currLine.getIndentValue()+"]"+getFragment(currLine.getTokensSeen(), getIndexLastToken()-1));

    }*/

    public int getLineOfTokenPosition(int tokPos) {        
        for (int i = 1; i < newLinePoints.size(); i++) {
            IndentInfo line=newLinePoints.get(i);
            if(line.getTokensSeen()>tokPos)
                return i-1;
        }
        return newLinePoints.size()-1;
    }
    
}
