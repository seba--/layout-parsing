package org.spoofax.jsglr_layout.client;

import java.util.ArrayList;

public class FineGrainedOnRegion {

    private static final int MAX_NR_OF_EXPLORED_LINES = 75;
	private int acceptRecoveryPosition;
    private int regionEndPosition;
    private ArrayList<BacktrackPosition> choicePoints;
    private static int MAX_BACK_JUMPS=5;
    private SGLR mySGLR;
    
    private ParserHistory getHistory() {
        return mySGLR.getHistory();
    }
   
    public void setInfoFGOnly(){
        regionEndPosition=mySGLR.tokensSeen+5;
        acceptRecoveryPosition=regionEndPosition+15;
        int lastIndex=getHistory().getIndexLastLine();       
        for (int i = Math.max(0, lastIndex-MAX_NR_OF_EXPLORED_LINES); i < lastIndex; i++) {
            IndentInfo line= getHistory().getLine(i);
            if(line.getStackNodes()!=null && line.getStackNodes().size()>0){
                BacktrackPosition btPoint=new BacktrackPosition(line.getStackNodes(), line.getTokensSeen());
                btPoint.setIndexHistory(i);
                choicePoints.add(btPoint);
            }            
        }
    }
    
    public void setRegionInfo(StructureSkipSuggestion erroneousRegion, int acceptPosition){
        regionEndPosition=erroneousRegion.getEndSkip().getTokensSeen();
        acceptRecoveryPosition=acceptPosition;
        int lastIndex=Math.min(erroneousRegion.getIndexHistoryEnd(), getHistory().getIndexLastLine());
        assert(
        	lastIndex >= 0 &&
        	erroneousRegion.getIndexHistoryStart()>=0 && 
        	erroneousRegion.getIndexHistoryStart()<=erroneousRegion.getIndexHistoryEnd()
        );
        int btStartIndex=Math.max(erroneousRegion.getIndexHistoryStart(), lastIndex-MAX_NR_OF_EXPLORED_LINES);
        for (int i = btStartIndex; i < lastIndex; i++) {
            IndentInfo line= getHistory().getLine(i);
            if (line.getStackNodes() != null && line.getStackNodes().size()>0){
                BacktrackPosition btPoint=new BacktrackPosition(line.getStackNodes(), line.getTokensSeen());
                btPoint.setIndexHistory(i);
                choicePoints.add(btPoint);
            }            
        }
    }
    
    public boolean recover() throws InterruptedException {
        int btIndex=choicePoints.size()-1;
        if(btIndex>=0)
        	return recoverFrom(btIndex, new ArrayList<RecoverNode>());
        return false;
    }

	private boolean recoverFrom(int btIndex, ArrayList<RecoverNode> unexplored_branches) throws InterruptedException {
        ArrayList<RecoverNode> rec_Branches=new ArrayList<RecoverNode>();
        if(btIndex>=0){
        	rec_Branches=collectRecoverBranches(btIndex); //collect permissive branches at btIndex line
        	resetSGLR(btIndex);
        }
        else
        	resetSGLR(0);
        rec_Branches.addAll(unexplored_branches);
    	
        ArrayList<RecoverNode> newbranches=recoverParse(rec_Branches, regionEndPosition); //explore and collect

    	if(acceptParse())
        	return true;
        if(choicePoints.size()-1-btIndex > MAX_BACK_JUMPS){
        	if(btIndex>0){
        		//Permissive branches constructed by one recovery are always explored
	        	ArrayList<RecoverNode> rec_Branches_prefix=new ArrayList<RecoverNode>();
	        	rec_Branches_prefix=collectRecoverBranches(0, btIndex);
	        	resetSGLR(0);
	        	ArrayList<RecoverNode> oneRecoverBranches = recoverParse(rec_Branches_prefix, regionEndPosition);
	        	if(!acceptParse()){
	        		resetSGLR(0);
	        		recoverParse(oneRecoverBranches, regionEndPosition);
	        	}
	        	return acceptParse();
        	}
        	return false;
        }
        return recoverFrom(btIndex-1, newbranches);
	}

	private void resetSGLR(int btIndex) {
		BacktrackPosition btrPosition=choicePoints.get(btIndex);    
    	mySGLR.activeStacks.clear(true); //only permissive branches are explored
        getHistory().setTokenIndex(btrPosition.tokensSeen);
	}

	private ArrayList<RecoverNode> collectRecoverBranches(int btIndex) throws InterruptedException {
		resetSGLR(btIndex);
        mySGLR.activeStacks.addAll(choicePoints.get(btIndex).recoverStacks);
        int endPos=btIndex<choicePoints.size()-1 ? choicePoints.get(btIndex+1).tokensSeen-1 : regionEndPosition;
    	ArrayList<RecoverNode> rec1_Branches=recoverParse(new ArrayList<RecoverNode>(), endPos);
		return rec1_Branches;
	}
	
	private ArrayList<RecoverNode> collectRecoverBranches(int btIndex, int btIndex_end) throws InterruptedException {
		resetSGLR(btIndex);
        mySGLR.activeStacks.addAll(choicePoints.get(btIndex).recoverStacks);
        int endPos=btIndex_end < choicePoints.size() ? choicePoints.get(btIndex_end).tokensSeen-1 : regionEndPosition;
    	ArrayList<RecoverNode> rec1_Branches=recoverParse(new ArrayList<RecoverNode>(), endPos);
		return rec1_Branches;
	}
    
    /** 
     * Explores permissive branches, and collects derived branches with higher recover count
     * @throws InterruptedException 
     */
    private ArrayList<RecoverNode> recoverParse(ArrayList<RecoverNode> candidates, int endRecoverSearchPos) throws InterruptedException {
    	mySGLR.setFineGrainedOnRegion(true);
        ArrayList<RecoverNode> newCandidates=new ArrayList<RecoverNode>();
        int curTokIndex;
        do {
            curTokIndex=getHistory().getTokenIndex();
            addCurrentCandidates(candidates, curTokIndex);
            getHistory().readRecoverToken(mySGLR, false);
            //System.out.print((char)mySGLR.currentToken);
            mySGLR.doParseStep();
            newCandidates.addAll(collectNewRecoverCandidates(curTokIndex));
            mySGLR.getRecoverStacks().clear();
        } while(getHistory().getTokenIndex()<= endRecoverSearchPos && mySGLR.acceptingStack==null && mySGLR.getCurrentToken()!=SGLR.EOF);
    	mySGLR.setFineGrainedOnRegion(false);
        return newCandidates;
    }
    
    /**
     * Permissive branches are accepted if they are still alive at the accepting position
     * @throws InterruptedException 
     */
    private boolean acceptParse() throws InterruptedException{
        while(getHistory().getTokenIndex()<= acceptRecoveryPosition && mySGLR.acceptingStack==null && mySGLR.activeStacks.size()>0){
            getHistory().readRecoverToken(mySGLR, false);
            //System.out.print((char)mySGLR.currentToken);
            mySGLR.doParseStep();
        }
    	return mySGLR.activeStacks.size()>0 || mySGLR.acceptingStack!=null;
    }

    private ArrayList<RecoverNode> collectNewRecoverCandidates(int tokenIndex) {
        ArrayList<RecoverNode> results=new ArrayList<RecoverNode>();
        for (Frame recoverStack : mySGLR.getRecoverStacks()) {
            RecoverNode rn = new RecoverNode(recoverStack, tokenIndex);
            results.add(rn);
        }
        return results;
    }
    
    private void addCurrentCandidates(ArrayList<RecoverNode> candidates, int tokenPosition) {
        for (RecoverNode recoverNode : candidates) {
            if(tokenPosition==recoverNode.tokensSeen){
            	Frame st =mySGLR.findStack(mySGLR.activeStacks, recoverNode.recoverStack.state);
                if(st!=null) {
                	for (Link ln : recoverNode.recoverStack.getAllLinks()) {
                		st.addLink(ln);
					}                	
                }
                else
                	mySGLR.addStack(recoverNode.recoverStack);
            }
        }
    }

    public FineGrainedOnRegion(SGLR parser){
        mySGLR=parser;
        choicePoints=new ArrayList<BacktrackPosition>();
    }

    public boolean parseRemainingTokens() throws InterruptedException {
        // TODO what if parsing fails here???
        while(!getHistory().hasFinishedRecoverTokens() && mySGLR.activeStacks.size()>0 && mySGLR.acceptingStack==null){        
            getHistory().readRecoverToken(mySGLR, true);
            //System.out.print((char)mySGLR.currentToken);
            mySGLR.doParseStep();            
        }  
        return mySGLR.activeStacks.size()>0 || mySGLR.acceptingStack!=null;        
    }
}
