package org.spoofax.jsglr.client;

import org.spoofax.jsglr.shared.ArrayDeque;
import org.spoofax.jsglr.shared.BadTokenException;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr.shared.TokenExpectedException;

public class RecoveryConnector {
    private SGLR mySGLR;
    private IRecoveryParser recoveryParser;
    private RegionRecovery skipRecovery;
    private boolean useBridgeParser;
    private boolean useFineGrained;
    private boolean onlyFineGrained;
    
    
    public void setOnlyFineGrained(boolean onlyFG) {
        onlyFineGrained=onlyFG;        
    }
    
    public void setUseFineGrained(boolean useFG) {
        useFineGrained=useFG;        
    }
    
    public void setUseBridgeParser(boolean useBridgeParser) {
        this.useBridgeParser = useBridgeParser;
    }

    public RecoveryConnector(SGLR parser, IRecoveryParser recoveryParser){
        mySGLR=parser;        
        skipRecovery = new RegionRecovery(mySGLR); 
        useFineGrained=true;
        onlyFineGrained=false;
        if(recoveryParser!=null){
            this.recoveryParser = recoveryParser;
            useBridgeParser=true;
        }
        else
            useBridgeParser=false;
        
    }    

    private ParserHistory getHistory() {
        return mySGLR.getHistory();
    }
    public void recover() {
        mySGLR.getPerformanceMeasuring().startRecovery();
        combinedRecover();
        mySGLR.getPerformanceMeasuring().endRecovery(recoverySucceeded());
    }

    private void combinedRecover() {
        if(onlyFineGrained){
            mySGLR.getPerformanceMeasuring().startFG();
            boolean fg=tryFineGrainedRepair();
            if(fg){
            	System.out.println("FG-only Succeeded");
            }
            mySGLR.getPerformanceMeasuring().endFG(fg);
            return;
        }
        mySGLR.getPerformanceMeasuring().startCG();
        boolean skipSucceeded = skipRecovery.selectErroneousFragment(); //decides whether whitespace parse makes sense
        /*
        System.out.println();
        System.out.println("------------------------------");
        System.out.println("SKIP-RESULT: "+skipSucceeded);
        System.out.print(skipRecovery.getErrorFragment());
        System.out.println();
        System.out.println("------------------------------");
        */
        mySGLR.getPerformanceMeasuring().endCG(skipSucceeded);
        mySGLR.acceptingStack=null;
        mySGLR.activeStacks.clear();
        //BRIDGE REPAIR
        if(useBridgeParser){            
            String errorFragment = skipRecovery.getErrorFragmentWithLeftMargin();
            mySGLR.getPerformanceMeasuring().startBP();
            boolean succeeded = tryBridgeRepair(errorFragment);
            mySGLR.getPerformanceMeasuring().endBP(succeeded);
            if(succeeded){
            	//System.out.println("BP-Succeeded");
                return;
            }
        }
        //FINEGRAINED REPAIR 
        if(useFineGrained){
            mySGLR.getPerformanceMeasuring().startFG();
            boolean FGSucceeded=tryFineGrainedRepair();
            mySGLR.getPerformanceMeasuring().endFG(FGSucceeded);
            if(FGSucceeded){ //FG succeeded  
                addSkipOption(skipSucceeded);
                //System.out.println("FG-Succeeded");
                return;
            }
        }
        //WHITESPACE REPAIR
        if (skipSucceeded) { 
            getHistory().deleteLinesFrom(skipRecovery.getStartIndexErrorFragment());//TODO: integrate with FG and BP
            getHistory().resetRecoveryIndentHandler(skipRecovery.getStartLineErrorFragment().getIndentValue());
            parseErrorFragmentAsWhiteSpace(false);
            boolean rsSucceeded=parseRemainingTokens(true);
            /*
            if(rsSucceeded)
            	System.out.println("RS-Succeeded");
            else
            	System.err.println("RS failed");
            */
            
        }
    }

    private void addSkipOption(boolean skipSucceeded) {
        ArrayDeque<Frame> fgStacks=new ArrayDeque<Frame>();
        fgStacks.addAll(mySGLR.activeStacks);
        if(skipSucceeded && parseErrorFragmentAsWhiteSpace(false) && parseRemainingTokens(false)){
            for (Frame frame : mySGLR.activeStacks) {
                for (Link l : frame.getAllLinks()) {
                    l.recoverCount = 5;
                }
            }                        
            for (Frame frame : fgStacks) {
                mySGLR.addStack(frame);
            } 
        }
    }
    
    private boolean recoverySucceeded() {
        return (mySGLR.activeStacks.size()>0 || mySGLR.acceptingStack!=null);
    }

    private boolean tryFineGrainedRepair() {
        FineGrainedOnRegion fgRepair=new FineGrainedOnRegion(mySGLR); 
        if(!onlyFineGrained){
            fgRepair.setRegionInfo(skipRecovery.getErroneousRegion(), skipRecovery.getAcceptPosition());
        }
        else{
            fgRepair.setInfoFGOnly();
        }
        fgRepair.recover();
        fgRepair.parseRemainingTokens();
        return recoverySucceeded();
    }

    private boolean tryBridgeRepair(String errorFragment) {
        String repairedFragment = repairBridges(errorFragment);
        mySGLR.activeStacks.addAll(skipRecovery.getStartLineErrorFragment().getStackNodes());   
        tryParsing(repairedFragment, false);      
        return parseRemainingTokens(true);
    }

    private String repairBridges(String errorFragment) {        
        try {            
            IRecoveryResult bpResult = null;
            bpResult = recoveryParser.recover(errorFragment);
            return bpResult.getResult();
        } catch (TokenExpectedException e) {
            e.printStackTrace();
        } catch (BadTokenException e) {
            e.printStackTrace();
        } catch (SGLRException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  errorFragment;
    }
    
    private void tryParsing(String fragment, boolean asLayout) {
        // Skip any leading whitespace, since we already parsed up to that point
        int indexFragment = findFirstNonLayoutToken(fragment);
        while(indexFragment<fragment.length() && mySGLR.activeStacks.size()>0) {                        
            mySGLR.setCurrentToken(fragment.charAt(indexFragment));
            indexFragment++;
            if(!asLayout)
                mySGLR.doParseStep();
            else
                parseAsLayout();
        }       
    }
    
    public boolean parseErrorFragmentAsWhiteSpace(boolean keepLines) {
        //System.out.println("---------- Start WhiteSpace Parsing ----------");
        mySGLR.activeStacks.clear();
        mySGLR.activeStacks.addAll(skipRecovery.getStartLineErrorFragment().getStackNodes());
        getHistory().setTokenIndex(skipRecovery.getStartPositionErrorFragment());
        while((getHistory().getTokenIndex()<skipRecovery.getEndPositionErrorFragment()) && mySGLR.activeStacks.size()>0 && mySGLR.acceptingStack==null){        
            getHistory().readRecoverToken(mySGLR, keepLines);
            //System.out.print((char)mySGLR.currentToken);
            parseAsLayout();           
        }
        //System.out.println("----------- End WhiteSpace Parsing ---------");
        return recoverySucceeded();
    }
    
    public boolean parseRemainingTokens(boolean keepHistory) {
        //System.out.println("------------- REMAINING CHARACTERS --------------- ");
        //System.out.println();
        getHistory().setTokenIndex(skipRecovery.getEndPositionErrorFragment());
        while(
        		(!getHistory().hasFinishedRecoverTokens()) 
        		&& mySGLR.activeStacks.size()>0 
        		&& mySGLR.acceptingStack==null
        )
        {        
            getHistory().readRecoverToken(mySGLR, keepHistory);
            //System.out.print((char)mySGLR.currentToken);
            //System.out.print("("+mySGLR.currentToken+")");
            mySGLR.doParseStep();            
        }  
        return recoverySucceeded();
    }
    
    private void parseAsLayout() {
        if(!isLayoutCharacter((char)mySGLR.getCurrentToken()) && mySGLR.getCurrentToken()!=SGLR.EOF){
            mySGLR.setCurrentToken(' ');
        }
        mySGLR.doParseStep();
    }
    
    public static boolean isLayoutCharacter(char aChar) {
        // TODO: Move this to the parse table class; only it truly can now layout characters
        return aChar==' ' || aChar == '\t' || aChar=='\n';
    }

    private int findFirstNonLayoutToken(String repairedFragment) {
        int indexFragment=0;
        while(indexFragment<repairedFragment.length()-1 && isLayoutCharacter(repairedFragment.charAt(indexFragment)))
            indexFragment++;
        return indexFragment;
    }

}
