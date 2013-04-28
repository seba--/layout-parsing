package org.spoofax.jsglr.client;

import org.spoofax.jsglr.shared.ArrayDeque;

/*
 * Should be created before parsing the first character of a line
 * Keeps information about indentation of a line.
 * Keeps parser state in order to support backtracking
 */
public class IndentInfo {
    
    private final int lineNumber;
    private int tokensSeen;
    private final int indentValue;
    private final ArrayDeque<Frame> stackNodes;    
    
    public ArrayDeque<Frame> getStackNodes() {
        return stackNodes;
    }   
    
    public void fillStackNodes(ArrayDeque<Frame> nodes) {
        stackNodes.clear();        
        stackNodes.addAll(nodes);        
    } 

    public int getLineNumber() {
        return lineNumber;
    }

    public int getTokensSeen() {
        return tokensSeen;
    }

    public void setTokensSeen(int tokenPos) {
        tokensSeen=tokenPos;
    }
    
    public int getIndentValue() {
        return indentValue;
    }
    
    public static IndentInfo cloneIndentInfo(IndentInfo original){
        IndentInfo cloneResult = new IndentInfo(original.getLineNumber(), original.getTokensSeen(), original.getIndentValue());
        cloneResult.fillStackNodes(original.getStackNodes());
        return cloneResult;    
    }
    
    public IndentInfo(int line, int tok, int indent)
    {
        lineNumber=line;
        tokensSeen=tok;
        indentValue=indent;
        stackNodes=new ArrayDeque<Frame>();
    }

    public IndentInfo() {
        this(-1, -1, -1);
    }

    /*
     * Calculates the biggest reduce belonging to this backtrack point.
     */
    private PooledPathList indentPathCache = new PooledPathList(512, false);
    
    public int maxReduceLength() throws InterruptedException {
        int maxPathLength = 0;
        for (Frame activeStack : stackNodes) {
        	indentPathCache.start();
        	try {
	        	activeStack.findAllPaths(indentPathCache, 2);
	        	for(int i = 0; i < indentPathCache.size(); i++) {
	        		// 3=> shifted_LO, reduced_LO, ReducedCodeFragment
	                int length = indentPathCache.get(i).getLength(); //length => total_length, p => reduce_length, p.p => layout_length (-shift), p.p.p => shift_length (=1)                 
	                if(length > maxPathLength){
	                    maxPathLength = length;                   
	                }
	            }
        	} finally {
        		indentPathCache.end();
        	}
        }
        return maxPathLength;
    }    
    
    //Calculates the start position of the biggest reduce
    public int structureStartPosition() throws InterruptedException
    {
        return tokensSeen - maxReduceLength();        
    }
    
    public Link getReductionLink() throws InterruptedException {
        int maxPathLength = -1;
        Link result=null;
        for (Frame activeStack : stackNodes) {
        	activeStack.findAllPaths(indentPathCache, 3);
            for(int i = 0; i < indentPathCache.size(); i++ ) {//3=> shifted_LO, reduced_LO, ReducedCodeFragment
            	Path p = indentPathCache.get(i);
                int length = p.getLength(); //length => total_length, p => reduce_length, p.p => layout_length (-shift), p.p.p => shift_length (=1)                 
                if(length > maxPathLength){
                    maxPathLength = length;
                    if(p.getParent().getLabel() != null)
                        result = p.getParent().getLink();
                }
            }
        }
        return result;
    }

    
}
