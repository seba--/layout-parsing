/*
 * Created on 04.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 * 
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.spoofax.jsglr.shared.Tools;


public class Frame implements Serializable {
    
    private static final long serialVersionUID = -4757644376472129935L;
            
    public static int framesCreated =0; //MJ: for testing

    public final State state;

    // Using a Vector and regular iteration takes parsing of file-test from 1100ms (min) to 2020ms (max)!
    // TODO: Just use an ArrayList for Frame.steps?
    //       which should have better performance than the obsolete, synchronized Vector classs
    
    

    private Link[] steps;
    private int stepsCount;
    
    // FIXME: All frames except the root must have a step with a label
    // that goes to the parent frame. Should we enforce this in this
    // constructor?
    public Frame(State s) {
        state = s;
        steps = new Link[20];
        stepsCount = 0;
        framesCreated +=1; //MJ: for testing 
    }

    public boolean allLinksRejected() {
        if(Tools.tracing) {
            SGLR.TRACE("SG_Rejected() - " + state.stateNumber);
        }
        
        if (stepsCount == 0)
            return false;

        for (int i = 0; i < stepsCount; i++) {
            if (!steps[i].isRejected())
                return false;
        }

        return true;
    }

    public State peek() {
        return state;
    }
    
    public Link peekLink() {
      if (stepsCount == 0)
        return null;
      return steps[0];
  }

  public Link[] peekLinks() {
    return steps;
  }

    
    public void findAllPaths(PooledPathList pool, int arity) throws InterruptedException {
    	doComputePathsToRoot(pool, null, arity, 0, 0);
    }

    private void doComputePathsToRoot(PooledPathList pool, Path node, int arity, int parentCount, int length) throws InterruptedException {
        
      if (Thread.currentThread().isInterrupted())
        throw new InterruptedException();

    	if(Tools.tracing) {
            SGLR.TRACE("SG_FindAllPaths() - " + arity + ", " + length);
        }

        if (arity == 0) {
            pool.rememberPath(node, null, this, length, parentCount);
            if(Tools.tracing) {
                SGLR.TRACE("SG_NewPath() - " + state.stateNumber + ", " + length);
            }
        } else { 
            for (int i = 1; i <= stepsCount; i++) {
                Link link = steps[stepsCount - i];
                
                Path n = pool.makePath(node, link, this, link.getLength(), parentCount);
                link.parent.doComputePathsToRoot(pool, n, arity - 1, parentCount + 1, length + link.getLength());
            }
        }
    }

    public Frame getRoot() {
        // FIXME: I'm not happy about the contract here. The assumption is
        // that the holder of the Frame class applies addStep correctly.
        if (stepsCount == 0)
            return this;
        return steps[0].parent.getRoot();
    }

    public Link findDirectLink(Frame st0) {
        if(Tools.tracing) {
            SGLR.TRACE("SG_FindDirectLink() - [" + state.stateNumber + ", " + st0.state.stateNumber + "]");
        }
        
        for (int i = 0; i < stepsCount; i++) {
            if (steps[i].parent == st0)
                return steps[i];
        }
        
        return null;
    }   
    
    /**
     * @deprecated Use the primitive types (or an iterator) to just iterate over the links.
     */
    public ArrayList<Link> getAllLinks() {
        ArrayList<Link> links=new ArrayList<Link>();
        for (int i = 0; i < stepsCount; i++) {
            links.add(steps[i]);
        }        
        return links;
    }   
    

//    static public int[] counter = new int[1000];
    
    public Link addLink(Frame st0, AbstractParseNode n, int length, int line, int column) {
        if(Tools.tracing) {
            SGLR.TRACE("SG_AddLink() - " + state.stateNumber + ", " + st0.state.stateNumber + ", " + length);
        }
        if(stepsCount >= steps.length) {
            resizeSteps();
        }
        
//        counter[stepsCount]++;
        return steps[stepsCount++] = new Link(st0, n, length, line, column); 
    }
    
    public Link addLink(Link ln) {
        if(Tools.tracing) {
            SGLR.TRACE("SG_AddLink() - " + state.stateNumber + " (recover node) ");
        }
        if(stepsCount >= steps.length) {
            resizeSteps();
        }
        
//        counter[stepsCount]++;
        return steps[stepsCount++] = ln; 
    }
    
    private void resizeSteps() {
        // Resize the steps array (not necessary for most grammars).
        // (see steps field)
        Link[] newSteps = new Link[steps.length * 2];
        System.arraycopy(steps, 0, newSteps, 0, steps.length);
        steps = newSteps;
    }

    public String dumpStack() {
        StringBuilder sb = new StringBuilder();

        sb.append("GSS [\n").append(doDumpStack(2)).append("\n  ]");
        return sb.toString();
    }

    public String doDumpStack(int indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(" ").append(state.stateNumber);
        if (stepsCount > 1) {
            sb.append(" ( ");
            for (Link s : steps) {
                if(s == null) {
                    break;
                }
                sb.append(s.parent.doDumpStack(indent + 1));
                sb.append(" | ");
            }
            sb.append(")");

        } else {
            for (Link s : steps) {
                if(s == null) {
                    break;
                }
                sb.append(" <").append(s.label).append("> ").append(s.parent.state.stateNumber).append("\n");
                if(indent<=5)//mj
                	sb.append(s.parent.doDumpStack(indent+1));
            }
        }
        return sb.toString();
    }

    public String dumpStackCompact() {
        StringBuilder sb = new StringBuilder();

        sb.append("GSS [").append(doDumpStackCompact()).append(" ]");
        return sb.toString();
    }

    public String doDumpStackCompact() {
        StringBuilder sb = new StringBuilder();
        sb.append(" ").append(state.stateNumber);
        if (stepsCount > 1) {
            sb.append(" ( ");
            for (Link s : steps) {
                if(s == null) {
                    break;
                }
                sb.append(s.parent.doDumpStackCompact());
                sb.append(" | ");
            }
            sb.append(")");

        } else {
            for (Link s : steps) {
                if(s == null) {
                    break;
                }
                sb.append(", ").append(s.parent.doDumpStackCompact());
            }
        }
        return sb.toString();
    }

    public void findLimitedPaths(PooledPathList pool, int arity, Link l) throws InterruptedException {
        if(Tools.tracing) {
            SGLR.TRACE("SG_FindLimitedPaths() - " + arity + ", " + l.getLength() + ", " + l.parent.state.stateNumber);
            TRACE_DumpLinks(steps);
        }
        if(findLink(arity, l)) { 
            doComputePathsToRoot(pool, null, l, false, arity, 0, 0);
        } 
    }

    private void TRACE_DumpLinks(Link[] st) {
        for(int i = 0; i < stepsCount; i++) {
            Link l = st[stepsCount - i - 1];
            if(Tools.tracing) {
                SGLR.TRACE("SG_ - dump link: " + l.parent.state.stateNumber);
            }
        }
    }

    private boolean findLink(int arity, Link l0) {
        if(Tools.tracing) {
            SGLR.TRACE("SG_FindLink() - " + arity);
            SGLR.TRACE("SG_ - links: " + stepsCount);
        }
        
        if(arity > 0) {
            for(int i = 0; i < stepsCount; i++) {
                Link l1 = steps[stepsCount - i - 1];
                if(l0 == l1) {
                    if(Tools.tracing) {
                        SGLR.TRACE("SG_ - l0 == l1");
                    }
                    return true;
                } else if (arity > 1 && l1.parent.findLink(arity - 1, l0)) {
                    if(Tools.tracing) {
                        SGLR.TRACE("SG_ - findlink");
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private void doComputePathsToRoot(PooledPathList pool, Path node, Link l,
      boolean seen, int arity, int parentCount, int length) throws InterruptedException {
        if (Thread.currentThread().isInterrupted())
          throw new InterruptedException();
      
        if(Tools.tracing) {
            SGLR.TRACE("SG_FindPaths() - " + arity);
        }

        if (arity == 0 && seen) {
            pool.rememberPath(node, null, this, length, parentCount);
            if(Tools.tracing) {
                SGLR.TRACE("SG_NewPath() - " + state.stateNumber + ", " + length);
            }
        } else if(arity > 0) {
            for (int i = 0; i < stepsCount; i++) {
                Link ln = steps[stepsCount - i - 1];
                boolean seenIt = seen || (ln == l);
                Path n = pool.makePath(node, ln, this, ln.getLength(), parentCount);
                ln.parent.doComputePathsToRoot(pool, n, l, seenIt, arity - 1, parentCount + 1, length + ln.getLength());
            }
        }
    }

    public void clear() {

        if (this.steps != null) {
            for (int i = 0; i < stepsCount; i++) {
                steps[i].clear();
                steps[i] = null;
            }
            this.steps = null;
            this.stepsCount = 0;
        }
    }
    
    //mj debug info function
    public int minAvoidValue()
    {
        int result = 0;
        for (int i = 0; i < stepsCount; i++) {
            if(i==0){
                result = steps[i].recoverCount;
            result = Math.min(result, steps[i].recoverCount);
            }            
        }
        return result;
    }
    
  //mj debug info function
    public List<String> getStackPaths(String frontEnd, boolean avoidFree)
    {
        String front = this.state.stateNumber + frontEnd;
        List<String> stackStrings=new ArrayList<String>();
        if(stepsCount == 0){
            stackStrings.add(front);
            return stackStrings;
        }
        for (int i = 0; i < stepsCount; i++) {
            Link ln = steps[i];
            List<String> childColl;
            if(ln.recoverCount ==0 || (avoidFree==false)) {
                if(ln.recoverCount ==0)
                    childColl= ln.parent.getStackPaths(" - "+front, avoidFree); 
                else {
                    String frnt = "-$"+ ln.recoverCount +"$-" + front;
                    childColl=ln.parent.getStackPaths(frnt, avoidFree);
                }
                stackStrings.addAll(childColl);
            }            
        }            
        return stackStrings;
    }

    //mj debug
    public String[] getStackRepresentation(boolean avoidFree){
        List<String> stackStrings=this.getStackPaths("", avoidFree);
        return stackStrings.toArray(new String[stackStrings.size()]);
    }
}
