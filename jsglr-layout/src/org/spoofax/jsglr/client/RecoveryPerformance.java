package org.spoofax.jsglr.client;

import java.util.ArrayList;

public class RecoveryPerformance {
    
    private long startParse;
    private long parseTime;
    private long startCG;
    private ArrayList<Integer> CGTimes;
    private long startFG;
    private ArrayList<Integer> FGTimes;
    private long startBP;
    private ArrayList<Integer> BPTimes;
    private long startRecovery;
    private ArrayList<Integer> recoveryTimes;
    
    private ArrayList<Boolean> CGResults;
    private ArrayList<Boolean> FGResults;
    private ArrayList<Boolean> BPResults;
    private ArrayList<Boolean> recoveryResults;
    private boolean parseResult;
    
    public RecoveryPerformance(){
        CGTimes=new ArrayList<Integer>();
        FGTimes=new ArrayList<Integer>();
        BPTimes=new ArrayList<Integer>();
        recoveryTimes=new ArrayList<Integer>();
        
        CGResults=new ArrayList<Boolean>();
        FGResults=new ArrayList<Boolean>();
        BPResults=new ArrayList<Boolean>();
        recoveryResults=new ArrayList<Boolean>();
    }
    
    public boolean isParseSucceeded() {
		return parseResult;
	}

	public ArrayList<Boolean> getCGResults() {
		return CGResults;
	}

	public ArrayList<Boolean> getFGResults() {
		return FGResults;
	}

	public ArrayList<Boolean> getBPResults() {
		return BPResults;
	}

	public ArrayList<Boolean> getRecoveryResults() {
		return recoveryResults;
	}

	public long getParseTime() {
        return parseTime;
    }

    public ArrayList<Integer> getCGTimes() {
        return CGTimes;
    }

    public ArrayList<Integer> getFGTimes() {
        return FGTimes;
    }

    public ArrayList<Integer> getBPTimes() {
        return BPTimes;
    }

    public ArrayList<Integer> getRecoveryTimes() {
        return recoveryTimes;
    }

    void startParse(){
        startParse=System.currentTimeMillis();
    }

    void endParse(boolean succeeded){
        parseTime=System.currentTimeMillis()-startParse;
        parseResult=succeeded;
    }
    
    void startCG(){
        startCG=System.currentTimeMillis();
    }

    void endCG(boolean succeeded){
        long CGTime=System.currentTimeMillis()-startCG;
        CGTimes.add((int) CGTime);
        CGResults.add(succeeded);
    }
    
    void startFG(){
        startFG=System.currentTimeMillis();
    }

    void endFG(boolean succeeded){
        long FGTime=System.currentTimeMillis()-startFG;
        FGTimes.add((int)FGTime);
        FGResults.add(succeeded);
    }
    
    void startBP(){
        startBP=System.currentTimeMillis();
    }

    void endBP(boolean succeeded){
        long BPTime=System.currentTimeMillis()-startBP;
        BPTimes.add((int)BPTime);
        BPResults.add(succeeded);
    }
    
    void startRecovery(){
        startRecovery=System.currentTimeMillis();
    }

    void endRecovery(boolean succeeded){
        long recoveryTime=System.currentTimeMillis()-startRecovery;
        recoveryTimes.add((int)recoveryTime);
        recoveryResults.add(succeeded);
    }
}