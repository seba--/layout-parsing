/*
 * Created on 03.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 *
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr_layout.client;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.spoofax.PushbackStringIterator;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.jsglr_layout.client.indentation.LayoutFilter;
import org.spoofax.jsglr_layout.shared.ArrayDeque;
import org.spoofax.jsglr_layout.shared.BadTokenException;
import org.spoofax.jsglr_layout.shared.SGLRException;
import org.spoofax.jsglr_layout.shared.TokenExpectedException;
import org.spoofax.jsglr_layout.shared.Tools;

public class SGLR {

  private static final boolean ENFORCE_NEWLINE_FITER = true;
  private static final boolean PARSE_TIME_LAYOUT_FITER = true;
  
  private int layoutFiltering;
  
  private int enforcedNewlineSkip = 0;
  
  private RecoveryPerformance performanceMeasuring;

	private final Set<BadTokenException> collectedErrors = new LinkedHashSet<BadTokenException>();

	public static final int EOF = ParseTable.NUM_CHARS;

	static final int TAB_SIZE = 8;

	protected static boolean WORK_AROUND_MULTIPLE_LOOKAHEAD;

	//Performance testing
	private static long parseTime=0;
	private static int parseCount=0;

	protected Frame startFrame;

	private long startTime;

	protected volatile boolean asyncAborted;

	protected Frame acceptingStack;

	protected final ArrayDeque<Frame> activeStacks;

	private ParseTable parseTable;

	private int currentToken;
	
	private int currentIndentation;
	
	protected int tokensSeen;

	protected int lineNumber;

	protected int columnNumber;

  protected int lastLineNumber;

  protected int lastColumnNumber;

  private int startOffset;

	private final ArrayDeque<ActionState> forShifter;

	private ArrayDeque<Frame> forActor;

	private ArrayDeque<Frame> forActorDelayed;

	private int maxBranches;

	private int maxToken;

	private int maxLine;

	private int maxColumn;

	private int maxTokenNumber;

	private AmbiguityManager ambiguityManager;

	protected Disambiguator disambiguator;

	private int rejectCount;

	private int reductionCount;

	protected PushbackStringIterator currentInputStream;

	private PathListPool pathCache = PathListPool.getInstance();
	private ArrayDeque<Frame> activeStacksWorkQueue = new ArrayDeque<Frame>();
	private ArrayDeque<Frame> recoverStacks;

	private ParserHistory history;

	private RecoveryConnector recoverIntegrator;
	
	private ITreeBuilder treeBuilder;
	
	private AbstractParseNode parseTree;

	protected boolean useIntegratedRecovery;
	
	public ParserHistory getHistory() {
		return history;
	}

	private boolean fineGrainedOnRegion;
	
	private LayoutFilter layoutFilter;


	protected ArrayDeque<Frame> getRecoverStacks() {
		return recoverStacks;
	}

	public Set<BadTokenException> getCollectedErrors() {
		return collectedErrors;
	}
	
	public int getEnforcedNewlineSkips() {
	  return enforcedNewlineSkip;
	}
   
	public int getLayoutFilteringCount() {
    return layoutFiltering;
  }
	
	 public int getLayoutFilterCallCount() {
	    return layoutFilter.getFilterCallCount();
	  }
	  
    /**
     * Attempts to set a timeout for parsing.
     * Default implementation throws an
     * {@link UnsupportedOperationException}.
     * 
     * @see org.spoofax.jsglr_layout.io.SGLR#setTimeout(int)
     */
    public void setTimeout(int timeout) {
        throw new UnsupportedOperationException("Use org.spoofax.jsglr.io.SGLR for setting a timeout");
    }
    
    protected void initParseTimer() {
        // Default does nothing (not supported by GWT)
    }

	@Deprecated
	public SGLR(ITermFactory pf, ParseTable parseTable) {
		this(new Asfix2TreeBuilder(pf), parseTable);
	}
	
	@Deprecated
	public SGLR(ParseTable parseTable) {
		this(new Asfix2TreeBuilder(), parseTable);
	}
	
	public SGLR(ITreeBuilder treeBuilder, ParseTable parseTable) {
		assert parseTable != null;
		// Init with a new factory for both serialized or BAF instances.
		this.parseTable = parseTable;

		activeStacks = new ArrayDeque<Frame>();
		forActor = new ArrayDeque<Frame>();
		forActorDelayed = new ArrayDeque<Frame>();
		forShifter = new ArrayDeque<ActionState>();
		disambiguator = new Disambiguator();
		useIntegratedRecovery = false;
		recoverIntegrator = null;
		history = new ParserHistory();
		setTreeBuilder(treeBuilder);
		layoutFilter = new LayoutFilter(parseTable, true);
	}

	public void setUseStructureRecovery(boolean useRecovery, IRecoveryParser parser) {
		useIntegratedRecovery = useRecovery;
		recoverIntegrator = new RecoveryConnector(this, parser);
	}

	/**
	 * Enables error recovery based on region recovery and, if available, recovery rules.
	 * Does not enable bridge parsing.
	 *
	 * @see ParseTable#hasRecovers()   Determines if the parse table supports recovery rules
	 */
	public final void setUseStructureRecovery(boolean useRecovery) {
		setUseStructureRecovery(useRecovery, null);
	}
	
    protected void setFineGrainedOnRegion(boolean fineGrainedMode) {
        fineGrainedOnRegion = fineGrainedMode;
        recoverStacks = new ArrayDeque<Frame>();
    }

    @Deprecated
    protected void setUseFineGrained(boolean useFG) {
        recoverIntegrator.setUseFineGrained(useFG);
    }

    // FIXME: we have way to many of these accessors; does this have to be public?
    //        if not for normal use, it should at least be 'internalSet....'
    @Deprecated
    public void setCombinedRecovery(boolean useBP, boolean useFG,
            boolean useOnlyFG) {
        recoverIntegrator.setOnlyFineGrained(useOnlyFG);
        recoverIntegrator.setUseBridgeParser(useBP);
        recoverIntegrator.setUseFineGrained(useFG);
    }

    public RecoveryPerformance getPerformanceMeasuring() {
        return performanceMeasuring;
    }

	/**
	 * @deprecated Use {@link #asyncCancel()} instead.
	 */
	@Deprecated
	public void asyncAbort() {
		asyncCancel();
	}

	/**
	 * Aborts an asynchronously running parse job, causing it to throw an exception.
	 *
	 * (Provides no guarantee that the parser is actually cancelled.)
	 */
	public void asyncCancel() {
		asyncAborted = true;
	}

	public void asyncCancelReset() {
		asyncAborted = false;
	}

	@Deprecated
	public static boolean isDebugging() {
		return Tools.debugging;
	}

	public static boolean isLogging() {
		return Tools.logging;
	}

	/**
	 * Initializes the active stacks. At the start of parsing there is only one
	 * active stack, and this stack contains the start symbol obtained from the
	 * parse table.
	 *
	 * @return top-level frame of the initial stack
	 */
	private Frame initActiveStacks() {
		activeStacks.clear();
		final Frame st0 = newStack(parseTable.getInitialState());
		addStack(st0);
		return st0;
	}
	
	@Deprecated
	public Object parse(String input) throws BadTokenException,
    TokenExpectedException, ParseException, SGLRException, InterruptedException {
	    return parse(input, null, null);
	}

    @Deprecated
	public final Object parse(String input, String filename) throws BadTokenException,
    TokenExpectedException, ParseException, SGLRException, InterruptedException {

        return parse(input, filename, null);
    }

	/**
	 * Parses a string and constructs a new tree using the tree builder.
	 * 
	 * @param input        The input string.
	 * @param filename     The source filename of the string, or null if not available.
	 * @param startSymbol  The start symbol to use, or null if any applicable.
	 * @throws InterruptedException 
	 */
    public Object parse(String input, String filename, String startSymbol) throws BadTokenException, TokenExpectedException, ParseException,
	SGLRException, InterruptedException {
		logBeforeParsing();
		initParseVariables(input, filename);
		startTime = System.currentTimeMillis();
		initParseTimer();
        Object result = sglrParse(startSymbol);
        return result;
	}

	private Object sglrParse(String startSymbol)
	throws BadTokenException, TokenExpectedException,
	ParseException, SGLRException, InterruptedException {
		getPerformanceMeasuring().startParse();
		try {
			do {
		     if (Thread.currentThread().isInterrupted())
		        throw new InterruptedException();

				readNextToken();
				//System.out.print((char)currentToken);
				history.keepTokenAndState(this);
				doParseStep();
			} while (currentToken != SGLR.EOF && activeStacks.size() > 0);

			if (acceptingStack == null) {
				collectedErrors.add(createBadTokenException());
			}

			if(useIntegratedRecovery && acceptingStack==null){
				recoverIntegrator.recover();
				if(acceptingStack==null && activeStacks.size()>0) {
					return sglrParse(startSymbol);
				}
			}
			getPerformanceMeasuring().endParse(acceptingStack!=null);
		} catch (final TaskCancellationException e) {
			throw new ParseTimeoutException(this, currentToken, tokensSeen - 1, lineNumber,
					columnNumber, collectedErrors);
		} finally {
			activeStacks.clear();
			activeStacksWorkQueue.clear();
			forShifter.clear();
			history.clear();
			if (recoverStacks != null) recoverStacks.clear();
		}

		logAfterParsing();

		final Link s = acceptingStack.findDirectLink(startFrame);

		if (s == null) {
			throw new ParseException(this, "Accepting stack has no link");
		}

		logParseResult(s);
		Tools.debug("avoids: ", s.recoverCount);
		//Tools.debug(s.label.toParseTree(parseTable));

		if (getTreeBuilder() instanceof NullTreeBuilder) {
			return null;
		} else {
		  this.parseTree = s.label;
			return disambiguator.applyFilters(this, s.label, startSymbol, tokensSeen);
		}
	}

	void readNextToken() {
		logCurrentToken();
		setCurrentToken(getNextToken());
	}
	
	protected void setCurrentToken(int tok) {
	  if (currentToken == -1)
	    currentIndentation = 0;
	  else
	    switch (currentToken) {
	    case '\n':
	      currentIndentation = 0;
	      break;
	    case '\t':
	      currentIndentation = (currentIndentation / TAB_SIZE + 1) * TAB_SIZE;
	      break;
	    case -1:
	      break;
	    default:
	      currentIndentation++;
	    }
	  
	  this.currentToken = tok;
	}
	
	protected int getCurrentToken() {
	  return this.currentToken;
	}

	protected void doParseStep() throws InterruptedException {
		logBeforeParseCharacter();
		parseCharacter(); //applies reductions on active stack structure and fills forshifter
		shifter(); //renewes active stacks with states in forshifter
	}

	private void initParseVariables(String input, String filename) {
		forActor.clear();
		forActorDelayed.clear();
		forShifter.clear();
		history.clear();
		startFrame = initActiveStacks();
		currentToken = -1;
		currentIndentation = 0;
		tokensSeen = 0;
		currentInputStream = new PushbackStringIterator(input);
		acceptingStack = null;
		collectedErrors.clear();
		history=new ParserHistory();
		performanceMeasuring=new RecoveryPerformance();
		getTreeBuilder().initializeInput(input, filename);
		PooledPathList.resetPerformanceCounters();
		pathCache.resetPerformanceCounters();
		ambiguityManager = new AmbiguityManager(input.length());
		parseTree = null;
		enforcedNewlineSkip = 0;
		layoutFiltering = 0;
		if (getTreeBuilder().getTokenizer() != null) {
			// Make sure we use the same starting offsets as the tokenizer, if any
			// (crucial for parsing fragments at a time)
			lineNumber = getTreeBuilder().getTokenizer().getEndLine();
			columnNumber = getTreeBuilder().getTokenizer().getEndColumn();
			startOffset = getTreeBuilder().getTokenizer().getStartOffset();
		} else {
			lineNumber = 1;
			columnNumber = 0;
		}
	}

	private BadTokenException createBadTokenException() {

		final Frame singlePreviousStack = activeStacks.size() == 1 ? activeStacks.get(0) : null;

		if (singlePreviousStack != null) {
			Action action = singlePreviousStack.peek().getSingularAction();

			if (action != null && action.getActionItems().length == 1) {
				final StringBuilder expected = new StringBuilder();

				do {
					final int token = action.getSingularRange();
					if (token == -1) {
						break;
					}
					expected.append((char) token);

					final ActionItem[] items = action.getActionItems();

					if (!(items.length == 1 && items[0].type == ActionItem.SHIFT)) {
						break;
					}

					final Shift shift = (Shift) items[0];
					action = parseTable.getState(shift.nextState).getSingularAction();

				} while (action != null);

				if (expected.length() > 0) {
					return new TokenExpectedException(this, expected.toString(), currentToken,
							tokensSeen + startOffset - 1, lineNumber, columnNumber);
				}
			}
		}

		return new BadTokenException(this, currentToken, tokensSeen + startOffset - 1, lineNumber,
				columnNumber);
	}

	private void shifter() {
		logBeforeShifter();
		activeStacks.clear();
		final AbstractParseNode prod = new ParseProductionNode(currentToken, lastLineNumber, lastColumnNumber);

		while (forShifter.size() > 0) {
			final ActionState as = forShifter.remove();
			if (!parseTable.hasRejects() || !as.st.allLinksRejected()) {				
				Frame	st1=findStack(activeStacks, as.s);
				if(st1==null){				
					st1 = newStack(as.s);
					addStack(st1);
				}
				st1.addLink(as.st, prod, 1, lastLineNumber, lastColumnNumber);
			} else {
				if (Tools.logging) {
					Tools.logger("Shifter: skipping rejected stack with state ",
							as.st.state.stateNumber);
				}
			}
		}
		logAfterShifter();
	}

	protected void addStack(Frame st1) {
		if(Tools.tracing) {
			TRACE("SG_AddStack() - " + st1.state.stateNumber);
		}
		activeStacks.addFirst(st1);
	}

	private void parseCharacter() throws InterruptedException {
		logBeforeParseCharacter();

		activeStacksWorkQueue.clear();
		activeStacksWorkQueue.addAll(activeStacks);

		forActorDelayed.clear();
		forShifter.clear();

		while (activeStacksWorkQueue.size() > 0 || forActor.size() > 0) {
		  if (Thread.currentThread().isInterrupted())
		    throw new InterruptedException();
		  
			final Frame st = pickStackNodeFromActivesOrForActor(activeStacksWorkQueue);
			if (!st.allLinksRejected()) {
				actor(st);
			}

			if(activeStacksWorkQueue.size() == 0 && forActor.size() == 0) {
				fillForActorWithDelayedFrames(); //Fills foractor, clears foractor delayed
			}
		}
	}

	private void fillForActorWithDelayedFrames() {
		if(Tools.tracing) {
			TRACE("SG_ - both empty");
		}
		final ArrayDeque<Frame> empty = forActor;
		forActor = forActorDelayed;
		empty.clear();
		forActorDelayed = empty;
	}

	private Frame pickStackNodeFromActivesOrForActor(ArrayDeque<Frame> actives) {
		Frame st;
		if(actives.size() > 0) {
			if(Tools.tracing) {
				TRACE("SG_ - took active");
			}
			st = actives.remove();
		} else {
			if(Tools.tracing) {
				TRACE("SG_ - took foractor");
			}
			st = forActor.remove();
		}
		return st;
	}

	private void actor(Frame st) throws InterruptedException {

		final State s = st.peek();
		logBeforeActor(st, s);

		for (final Action action : s.getActions()) {
			if (action.accepts(currentToken)) {
				for (final ActionItem ai : action.getActionItems()) {
					switch (ai.type) {
					case ActionItem.SHIFT: {
						final Shift sh = (Shift) ai;
						final ActionState actState = new ActionState(st, parseTable.getState(sh.nextState));
						actState.currentToken = currentToken;
						addShiftPair(actState); //Adds StackNode to forshifter
						statsRecordParsers(); //sets some values un current parse state
						break;
					}
					case ActionItem.REDUCE: {
						final Reduce red = (Reduce) ai;
						doReductions(st, red.production);
						break;
					}
					case ActionItem.REDUCE_LOOKAHEAD: {
						final ReduceLookahead red = (ReduceLookahead) ai;
						if(checkLookahead(red)) {
							if(Tools.tracing) {
								TRACE("SG_ - ok");
							}
							doReductions(st, red.production);
						}
						break;
					}
					case ActionItem.ACCEPT: {
						if (!st.allLinksRejected()) {
							acceptingStack = st;
							if (Tools.logging) {
								Tools.logger("Reached the accept state");
							}
						}
						break;
					}
					default:
						throw new IllegalStateException("Unknown action type: " + ai.type);
					}
				}
			}
		}

		if(Tools.tracing) {
			TRACE("SG_ - actor done");
		}
	}

	private boolean checkLookahead(ReduceLookahead red) {
		return doCheckLookahead(red, red.getCharRanges());
	}

	private boolean doCheckLookahead(ReduceLookahead red, RangeList[] charClass) {
		if(Tools.tracing) {
			TRACE("SG_CheckLookAhead() - ");
		}

		if (charClass.length == 0)
		  return true;
		
    boolean permit = false;
    int offset = -1;
    int[] readChars = new int[charClass.length];
		
    int i;
		for (i = 0; i < charClass.length; i++) {
  		int c = currentInputStream.read();
  		offset++;
  		readChars[offset] = c; 
  
  		// EOF
  		if(c == -1) {
  			permit = true;
  			break;
  		}
  
  		if (!charClass[i].within(c)) {
  		  permit = true;
  		  break;
  		}
		}

		for (int j = offset; j >= 0; j--)
		  currentInputStream.unread(readChars[j]);

		return permit;
	}

	private void addShiftPair(ActionState state) {
		if(Tools.tracing) {
			TRACE("SG_AddShiftPair() - " + state.s.stateNumber);
		}
		forShifter.add(state);
	}

	private void statsRecordParsers() {
		if (forShifter.size() > maxBranches) {
			maxBranches = forShifter.size();
			maxToken = currentToken;
			maxColumn = columnNumber;
			maxLine = lineNumber;
			maxTokenNumber = tokensSeen;
		}
	}


	private void doReductions(Frame st, Production prod) throws InterruptedException {

	  if (Thread.currentThread().isInterrupted())
	    throw new InterruptedException();
	  
		if(!recoverModeOk(st, prod)) {
			return;
		}
		
				PooledPathList paths = pathCache.create();
		try {
			st.findAllPaths(paths, prod.arity);
			logBeforeDoReductions(st, prod, paths.size());
			reduceAllPaths(prod, paths);
			logAfterDoReductions();
		} finally {
			pathCache.endCreate(paths);
		}
	}

	private boolean recoverModeOk(Frame st, Production prod) {
		return !prod.isRecoverProduction() || fineGrainedOnRegion;
	}

	private void doLimitedReductions(Frame st, Production prod, Link l) throws InterruptedException { //Todo: Look add sharing code with doReductions
		if(!recoverModeOk(st, prod)) {
			return;
		}
		PooledPathList limitedPool = pathCache.create();
		try {
			st.findLimitedPaths(limitedPool, prod.arity, l); //find paths containing the link
			logBeforeLimitedReductions(st, prod, l, limitedPool);
			reduceAllPaths(prod, limitedPool);
		} finally {
			pathCache.endCreate(limitedPool);
		}
	}

	private void reduceAllPaths(Production prod, PooledPathList paths) throws InterruptedException {

		for(int i = 0; i < paths.size(); i++) {
			final Path path = paths.get(i);
			final AbstractParseNode[] kids = path.getParseNodes();
			final Frame st0 = path.getEnd();
			final State next = parseTable.go(st0.state, prod.label);
			logReductionPath(prod, path, st0, next);
			
			if (SGLR.PARSE_TIME_LAYOUT_FITER &&
			    !layoutFilter.hasValidLayout(prod.label, kids)) {
			  layoutFiltering++;
			  continue;
			}
			else
			  layoutFiltering += layoutFilter.getDisambiguationCount();
			
			if (SGLR.ENFORCE_NEWLINE_FITER && 
          parseTable.getLabel(prod.label).getAttributes().isNewlineEnforced()) {
        boolean hasNewline = false;
        for (int j = kids.length - 1; j >= 0; j--) {
          int status = kids[j].getLayoutStatus();
          
          if (status == AbstractParseNode.NEWLINE_LAYOUT) {
            hasNewline = true;
            break;
          }
          if (status == AbstractParseNode.OTHER_LAYOUT) {
            hasNewline = false;
            break;
          }
        }
        
        if (!hasNewline) {
          enforcedNewlineSkip++;
          continue;
        }
      }


			if(!prod.isRecoverProduction())
				reducer(st0, next, prod, kids, path);
			else
				reducerRecoverProduction(st0, next, prod, kids, path);				
		}

		if (asyncAborted) {
			// Rethrown as ParseTimeoutException in SGLR.sglrParse()
			throw new TaskCancellationException("Long-running parse job aborted");
		}
	}

	
	private void reducer(Frame st0, State s, Production prod, AbstractParseNode[] kids, Path path) throws InterruptedException {
		assert(!prod.isRecoverProduction());
		logBeforeReducer(s, prod, path.getLength());
		increaseReductionCount();

//		final boolean illegalLayout = !layoutFilter.hasValidLayout(prod, kids, parseTable);
		final int length = path.getLength();
		final int numberOfRecoveries = calcRecoverCount(prod, path);
		final AbstractParseNode t = prod.apply(kids, 
		                                       path.getParentCount() > 0 ? path.getParent().getLink().getLine() : lineNumber, 
		                                       path.getParentCount() > 0 ? path.getParent().getLink().getColumn() : columnNumber,
		                                       parseTable.getLabel(prod.label).isLayout(),
		                                       parseTable.getLabel(prod.label).getAttributes().isIgnoreLayout());
		final Frame st1 = findStack(activeStacks, s);

		if (st1 == null) {
			// Found no existing stack with for state s; make new stack
			Link nl = addNewStack(st0, s, prod, length, numberOfRecoveries, t);
//			if (illegalLayout) {
//			  nl.reject();
//			}
		} else {
			/* A stack with state s exists; check for ambiguities */
			Link nl = st1.findDirectLink(st0);

			if (nl != null) {
        logAmbiguity(st0, prod, st1, nl);
        if (prod.isRejectProduction()) {
          nl.reject();
        } 
//        else if (illegalLayout) {
//          nl.reject();
//        }
        if(numberOfRecoveries == 0 && nl.recoverCount == 0 || nl.isRejected()) {
          createAmbNode(t, nl);
        } else if (numberOfRecoveries < nl.recoverCount) {
          nl.label = t;
          nl.recoverCount = numberOfRecoveries;
          actorOnActiveStacksOverNewLink(nl);
        } else if (numberOfRecoveries == nl.recoverCount) {
          nl.label = t;
        }
      } else {
        nl = st1.addLink(st0, t, length, t.getLine(), t.getColumn());
        nl.recoverCount = numberOfRecoveries;
        if (prod.isRejectProduction()) {
          nl.reject();
          increaseRejectCount();
        }
//        else if (illegalLayout) {
//          nl.reject();
//        }
        logAddedLink(st0, st1, nl);
        actorOnActiveStacksOverNewLink(nl);
      }
		}
	}
	
	private void reducerRecoverProduction(Frame st0, State s, Production prod, AbstractParseNode[] kids, Path path) {
		assert(prod.isRecoverProduction());
		final int length = path.getLength();
		final int numberOfRecoveries = calcRecoverCount(prod, path);
		final AbstractParseNode t = prod.apply(kids, lineNumber, columnNumber, parseTable.getLabel(prod.label).isLayout(), parseTable.getLabel(prod.label).getAttributes().isIgnoreLayout());
		final Frame stActive = findStack(activeStacks, s);
		if(stActive!=null){
			Link lnActive=stActive.findDirectLink(st0);
			if(lnActive!=null){
				return; //TODO: ambiguity
			}
		}
		final Frame stRecover = findStack(recoverStacks, s);
		if(stRecover!=null){
			Link nlRecover = stRecover.findDirectLink(st0);
			if(nlRecover!=null){
				return; //TODO: ambiguity
			}
			nlRecover = stRecover.addLink(st0, t, length, t.getLine(), t.getColumn());
			nlRecover.recoverCount = numberOfRecoveries;
			return;
		}
		addNewRecoverStack(st0, s, prod, length, numberOfRecoveries, t);
	}

	private void createAmbNode(AbstractParseNode t, Link nl) {
		nl.addAmbiguity(t, tokensSeen);
		ambiguityManager.increaseAmbiguityCalls();
	}
	
	/**
	 * Found no existing stack with for state s; make new stack
	 */
	private Link addNewStack(Frame st0, State s, Production prod, int length,
			int numberOfRecoveries, AbstractParseNode t) {

		final Frame st1 = newStack(s);
		final Link nl = st1.addLink(st0, t, length, t.getLine(), t.getColumn());

		nl.recoverCount = numberOfRecoveries;
		addStack(st1);
		forActorDelayed.addFirst(st1);

		if(Tools.tracing) {
			TRACE("SG_AddStack() - " + st1.state.stateNumber);
		}

		if (prod.isRejectProduction()) {
			if (Tools.logging) {
				Tools.logger("Reject [new]");
			}
			nl.reject();
			increaseRejectCount();
		}
		
		return nl;
	}

	/**
	 *  Found no existing stack with for state s; make new stack
	 */
	private void addNewRecoverStack(Frame st0, State s, Production prod, int length,
			int numberOfRecoveries, AbstractParseNode t) {
		if (!(fineGrainedOnRegion && !prod.isRejectProduction())) {
			return;
		}
		final Frame st1 = newStack(s);
		final Link nl = st1.addLink(st0, t, length, t.getLine(), t.getColumn());
		nl.recoverCount = numberOfRecoveries;
		recoverStacks.addFirst(st1);
	}

	private void actorOnActiveStacksOverNewLink(Link nl) throws InterruptedException {
		// Note: ActiveStacks can be modified inside doLimitedReductions
		// new elements may be inserted at the beginning
		final int sz = activeStacks.size();
		for (int i = 0; i < sz; i++) {
			//                for(Frame st2 : activeStacks) {
			if(Tools.tracing) {
				TRACE("SG_ activeStack - ");
			}
			final int pos = activeStacks.size() - sz + i;
			final Frame st2 = activeStacks.get(pos);
			if (st2.allLinksRejected() || inReduceStacks(forActor, st2) || inReduceStacks(forActorDelayed, st2))
			{
				continue; //stacknode will find reduction in regular process
			}

			for (final Action action : st2.peek().getActions()) {
				if (action.accepts(currentToken)) {
					for (final ActionItem ai : action.getActionItems()) {
						switch(ai.type) {
						case ActionItem.REDUCE:
							final Reduce red = (Reduce) ai;
							doLimitedReductions(st2, red.production, nl);
							break;
						case ActionItem.REDUCE_LOOKAHEAD:
							final ReduceLookahead red2 = (ReduceLookahead) ai;
							if(checkLookahead(red2)) {
								doLimitedReductions(st2, red2.production, nl);
							}
							break;
						}
					}
				}
			}
		}
	}

	private int calcRecoverCount(Production prod, Path path) {
		int result = path.getRecoverCount();
		if (prod.isRecoverProduction()){
			result += 1;
			if (path.getLength() > 0)
				result += 1; //Hack: insertion rules (length 0) should be preferred above water rules.
		}
		return result;
	}

	
	int count = 0;
	int count2 = 0;
	
	
//	private class LongestMatchKey {
//	  private AbstractParseNode n1, n2;
//	  public LongestMatchKey(AbstractParseNode n1, AbstractParseNode n2) { this.n1 = n1; this.n2 = n2; }
//	  @Override public int hashCode() { return (9 << n1.hashCode()) + n2.hashCode(); }
//	  @Override public boolean equals(Object o) { 
//	    return o instanceof LongestMatchKey && ((LongestMatchKey) o).n1 == n1 && ((LongestMatchKey) o).n2 == n2;
//	  }
//	}
//	private Map<LongestMatchKey, Integer> longestMatchCache = new HashMap<LongestMatchKey, Integer>();
//	
//	@SuppressWarnings("null")
//  private AbstractParseNode filterLongestMatch(AbstractParseNode t1, AbstractParseNode t2) {
//	  if (t1.isParseRejectNode() || t2.isParseRejectNode())
//	    return null;
//	  
//	  System.out.println(t1.toString());
//	  System.out.println(t2.toString());
//	  
//	  Stack<AbstractParseNode[]> stack = new Stack<AbstractParseNode[]>();
//	  stack.push(new AbstractParseNode[] {t1, t2});
//
//	  LinkedList<LongestMatchKey> done = new LinkedList<LongestMatchKey>();
//	  
//	  AbstractParseNode res = null;
//	  
//	  while (!stack.isEmpty()) {
//	    AbstractParseNode[] ns = stack.pop();
//	    AbstractParseNode n1 = ns[0];
//	    AbstractParseNode n2 = ns[1];
//	  
//	    if (n1.equals(n2))
//	      continue;
//	    
//	    LongestMatchKey key = new LongestMatchKey(n1, n2);
//	    Integer prevRes = longestMatchCache.get(key);
//	    if (prevRes != null) {
//	      AbstractParseNode newres = prevRes == -1 ? null : (prevRes == 0 ? t1 : t2);
//	      if (res != null && res != newres) {
//	        res = null;
//	        break;
//	      }
//	      if (newres == null)
//	        continue;
//	      
//	      res = newres;
//	      break;
//	    }
//	    
//	    Label l1 = n1.isAmbNode() ? null : parseTable.getLabel(n1.getLabel());
//	    Label l2 = n2.isAmbNode() ? null : parseTable.getLabel(n2.getLabel());
//	    
//      if (n1.isAmbNode() || n2.isAmbNode()) {
//        AbstractParseNode[] n1Array = n1.isAmbNode() ? n1.getChildren() : new AbstractParseNode[] {n1};
//        AbstractParseNode[] n2Array = n2.isAmbNode() ? n2.getChildren() : new AbstractParseNode[] {n2};
//        
//        for (int i = 0; i < n1Array.length; i++)
//          for (int j = 0; j < n2Array.length; j++) {
//            if (!n1Array[i].isParseRejectNode() && !n2Array[j].isParseRejectNode())
//              stack.push(new AbstractParseNode[] {n1Array[i], n2Array[j]});
//          }
//        continue;
//      }
//
//	    else if (l1 != null && l1.getAttributes().isLongestMatch() && 
//	             (l2 == null || !l2.getAttributes().isLongestMatch())) {
//	      if (res == t2) {
//	        res = null;
//	        break;
//	      }
//	      res = t1;
//	      break;
//	    }
//
//      else if (l2 != null && l2.getAttributes().isLongestMatch() &&
//               (l1 == null || !l1.getAttributes().isLongestMatch())) {
//        if (res == t1) {
//          res = null;
//          break;
//        }
//        res = t2;
//        break;
//      }
//	    
//      else if (n1.getLabel() != n2.getLabel())
//        continue;
//
//      else if (l1 != null && l2 != null &&
//          l1.equals(l2) && l1.getAttributes().isLongestMatch()) {
//        int[] end1 = n1.getEnd();
//        int[] end2 = n2.getEnd();
//        if (end1[0] > end2[0] || end1[0] == end2[0] && end1[1] > end2[1]) {
//          if (res == t2) {
//            res = null;
//            break;
//          }
//          res = t1;
//          break;
//        }
//        else if (end2[0] > end1[0] || end2[0] == end1[0] && end2[1] > end1[1]) {
//          if (res == t1) {
//            res = null;
//            break;
//          }
//          res = t2;
//          break;
//        }
//      }
//	    
//      done.add(key);
//
//      for (int i = n1.getChildren().length - 1; i >= 0; i--)
//        stack.push(new AbstractParseNode[] {n1.getChildren()[i], n2.getChildren()[i]});
//	  }
//	  
//	  int val = res == t1 ? 0 : (res == t2 ? 1 : -1);
//	  for (LongestMatchKey key : done)
//	    longestMatchCache.put(key, val);
//	  
//	  return res;
//	}
	
	
  private boolean inReduceStacks(Queue<Frame> q, Frame frame) {
		if(Tools.tracing) {
			TRACE("SG_InReduceStacks() - " + frame.state.stateNumber);
		}
		return q.contains(frame);
	}

	protected Frame newStack(State s) {
		if(Tools.tracing) {
			TRACE("SG_NewStack() - " + s.stateNumber);
		}
		return new Frame(s);
	}

	private void increaseReductionCount() {
		reductionCount++;
	}

	protected void increaseRejectCount() {
		rejectCount++;
	}

	protected int getRejectCount() {
		return rejectCount;
	}

	Frame findStack(ArrayDeque<Frame> stacks, State s) {
		int desiredState = s.stateNumber;
		if(Tools.tracing) {
			TRACE("SG_FindStack() - " + desiredState);
		}

		// We need only check the top frames of the active stacks.
		if (Tools.debugging) {
			Tools.debug("findStack() - ", dumpActiveStacks());
			Tools.debug(" looking for ", desiredState);
		}

		final int size = stacks.size();
		for (int i = 0; i < size; i++) {
			Frame stack = stacks.get(i);
			if (stack.state.stateNumber == desiredState) {
				if(Tools.tracing) {
					TRACE("SG_ - found stack");
				}
				return stack;
			}
		}
		if(Tools.tracing) {
			TRACE("SG_ - stack not found");
		}
		return null;
	}


	private int getNextToken() {
		if(Tools.tracing) {
			TRACE("SG_NextToken() - ");
		}

		final int ch = currentInputStream.read();
		updateLineAndColumnInfo(ch);
		
		if(ch == -1) {
			return SGLR.EOF;
		}
		return ch;
	}

	protected void updateLineAndColumnInfo(int ch) {
		tokensSeen++;

		if (Tools.debugging) {
			Tools.debug("getNextToken() - ", ch, "(", (char) ch, ")");
		}
		
		lastLineNumber = lineNumber;
		lastColumnNumber = columnNumber;

		switch (ch) {
		case '\n':
			lineNumber++;
			columnNumber = 0;
			break;
		case '\t':
			columnNumber = (columnNumber / TAB_SIZE + 1) * TAB_SIZE;
			break;
		case -1:
			break;
		default:
			columnNumber++;
		}
	}
	
	public ParseTable getParseTable() {
		return parseTable;
	}

	public void setTreeBuilder(ITreeBuilder treeBuilder) {
		this.treeBuilder = treeBuilder;
		parseTable.initializeTreeBuilder(treeBuilder);
	}

	public ITreeBuilder getTreeBuilder() {
		return treeBuilder;
	}

	AmbiguityManager getAmbiguityManager() {
	  return ambiguityManager;
	}
	
	public Disambiguator getDisambiguator() {
		return disambiguator;
	}

	public void setDisambiguator(Disambiguator disambiguator) {
		this.disambiguator = disambiguator;
	}

	@Deprecated
	public ITermFactory getFactory() {
		return parseTable.getFactory();
	}

	protected int getReductionCount() {
		return reductionCount;
	}

	protected int getRejectionCount() {
		return rejectCount;
	}





	////////////////////////////////////////////////////// Log functions ///////////////////////////////////////////////////////////////////////////////
	
	// TODO: cleanup, this doesn't belong here!

	private static int traceCallCount = 0;

	static void TRACE(String string) {
		System.out.println("[" + traceCallCount + "] " + string + "\n");
		traceCallCount++;
	}

	private String dumpActiveStacks() {
		final StringBuffer sb = new StringBuffer();
		boolean first = true;
		if (activeStacks == null) {
			sb.append(" GSS unitialized");
		} else {
			sb.append("{").append(activeStacks.size()).append("} ");
			for (final Frame f : activeStacks) {
				if (!first) {
					sb.append(", ");
				}
				sb.append(f.dumpStack());
				first = false;
			}
		}
		return sb.toString();
	}


	private void logParseResult(Link s) {
		if (Tools.debugging) {
			Tools.debug("internal parse tree:\n", s.label);
		}

		if(Tools.tracing) {
			TRACE("SG_ - internal tree: " + s.label);
		}

		if (Tools.measuring) {
			final Measures m = new Measures();
			//Tools.debug("Time (ms): " + (System.currentTimeMillis()-startTime));
			m.setTime(System.currentTimeMillis() - startTime);
			//Tools.debug("Red.: " + reductionCount);
			m.setReductionCount(reductionCount);
			//Tools.debug("Nodes: " + Frame.framesCreated);
			m.setFramesCreated(Frame.framesCreated);
			//Tools.debug("Links: " + Link.linksCreated);
			m.setLinkedCreated(Link.linksCreated);
			//Tools.debug("avoids: " + s.avoidCount);
			m.setAvoidCount(s.recoverCount);
			//Tools.debug("Total Time: " + parseTime);
			m.setParseTime(parseTime);
			//Tools.debug("Total Count: " + parseCount);
			Measures.setParseCount(++parseCount);
			//Tools.debug("Average Time: " + (int)parseTime / parseCount);
			m.setAverageParseTime((int)parseTime / parseCount);
			m.setRecoverTime(-1);
			Tools.setMeasures(m);
		}
	}


	private void logBeforeParsing() {
		if(Tools.tracing) {
			TRACE("SG_Parse() - ");
		}

		if (Tools.debugging) {
			Tools.debug("parse() - ", dumpActiveStacks());
		}
	}

	private void logAfterParsing()
	throws BadTokenException, TokenExpectedException {
		if (isLogging()) {
			Tools.logger("Number of lines: ", lineNumber);
			Tools.logger("Maximum ", maxBranches, " parse branches reached at token ",
					logCharify(maxToken), ", line ", maxLine, ", column ", maxColumn,
					" (token #", maxTokenNumber, ")");

			final long elapsed = System.currentTimeMillis() - startTime;
			Tools.logger("Parse time: " + elapsed / 1000.0f + "s");
		}

		if (Tools.debugging) {
			Tools.debug("Parsing complete: all tokens read");
		}

		if (acceptingStack == null) {
			final BadTokenException bad = createBadTokenException();
			if (collectedErrors.isEmpty()) {
				throw bad;
			} else {
				collectedErrors.add(bad);
				throw new MultiBadTokenException(this, collectedErrors);
			}
		}


		if (Tools.debugging) {
			Tools.debug("Accepting stack exists");
		}
	}

	private void logCurrentToken() {
		if (isLogging()) {
			Tools.logger("Current token (#", tokensSeen, "): ", logCharify(currentToken));
		}
	}

	private void logAfterShifter() {
		if(Tools.tracing) {
			TRACE("SG_DiscardShiftPairs() - ");
			TRACE_ActiveStacks();
		}
	}

	private void logBeforeShifter() {
		if(Tools.tracing) {
			TRACE("SG_Shifter() - ");
			TRACE_ActiveStacks();
		}

		if (Tools.logging) {
			Tools.logger("#", tokensSeen, ": shifting ", forShifter.size(), " parser(s) -- token ",
					logCharify(currentToken), ", line ", lineNumber, ", column ", columnNumber);
		}

		if (Tools.debugging) {
			Tools.debug("shifter() - " + dumpActiveStacks());

			Tools.debug(" token   : " + currentToken);
			Tools.debug(" parsers : " + forShifter.size());
		}
	}

	private void logBeforeParseCharacter() {
		if(Tools.tracing) {
			TRACE("SG_ParseToken() - ");
		}

		if (Tools.debugging) {
			Tools.debug("parseCharacter() - " + dumpActiveStacks());
			Tools.debug(" # active stacks : " + activeStacks.size());
		}

		/* forActor = *///computeStackOfStacks(activeStacks);

		if (Tools.debugging) {
			Tools.debug(" # for actor     : " + forActor.size());
		}
	}

	private String logCharify(int currentToken) {
		switch (currentToken) {
		case 32:
			return "\\32";
		case SGLR.EOF:
			return "EOF";
		case '\n':
			return "\\n";
		case 0:
			return "\\0";
		default:
			return "" + (char) currentToken;
		}
	}

	@SuppressWarnings("deprecation")
	private void logBeforeActor(Frame st, State s) {
		List<ActionItem> actionItems = null;

		if (Tools.debugging || Tools.tracing) {
			actionItems = s.getActionItems(currentToken);
		}

		if(Tools.tracing) {
			TRACE("SG_Actor() - " + st.state.stateNumber);
			TRACE_ActiveStacks();
		}

		if (Tools.debugging) {
			Tools.debug("actor() - ", dumpActiveStacks());
		}

		if (Tools.debugging) {
			Tools.debug(" state   : ", s.stateNumber);
			Tools.debug(" token   : ", currentToken);
		}

		if (Tools.debugging) {
			Tools.debug(" actions : ", actionItems);
		}

		if(Tools.tracing) {
			TRACE("SG_ - actions: " + actionItems.size());
		}
	}

	private void logAfterDoReductions() {
		if (Tools.debugging) {
			Tools.debug("<doReductions() - " + dumpActiveStacks());
		}

		if(Tools.tracing) {
			TRACE("SG_ - doreductions done");
		}
	}

	private void logReductionPath(Production prod, Path path, Frame st0, State next) {
		if (Tools.debugging) {
			Tools.debug(" path: ", path);
			Tools.debug(st0.state);
		}

		if (Tools.logging) {
			Tools.logger("Goto(", st0.peek().stateNumber, ",", prod.label + ") == ",
					next.stateNumber);
		}
	}


	private void logBeforeDoReductions(Frame st, Production prod,
			final int pathsCount) {
		if(Tools.tracing) {
			TRACE("SG_DoReductions() - " + st.state.stateNumber);
		}

		if (Tools.debugging) {
			Tools.debug("doReductions() - " + dumpActiveStacks());
			logReductionInfo(st, prod);
			Tools.debug(" paths : " + pathsCount);
		}
	}

	private void logBeforeLimitedReductions(Frame st, Production prod, Link l,
			PooledPathList paths) {
		if(Tools.tracing) {
			TRACE("SG_ - back in reducer ");
			TRACE_ActiveStacks();
			TRACE("SG_DoLimitedReductions() - " + st.state.stateNumber + ", " + l.parent.state.stateNumber);
		}

		if (Tools.debugging) {
			Tools.debug("doLimitedReductions() - ", dumpActiveStacks());
			logReductionInfo(st, prod);
			Tools.debug(Arrays.asList(paths));
		}
	}

	private void logReductionInfo(Frame st, Production prod) {
		Tools.debug(" state : ", st.peek().stateNumber);
		Tools.debug(" token : ", currentToken);
		Tools.debug(" label : ", prod.label);
		Tools.debug(" arity : ", prod.arity);
		Tools.debug(" stack : ", st.dumpStack());
	}

	private void logAddedLink(Frame st0, Frame st1, Link nl) {
		if (Tools.debugging) {
			Tools.debug(" added link ", nl, " from ", st1.state.stateNumber, " to ",
					st0.state.stateNumber);
		}

		if(Tools.tracing) {
			TRACE_ActiveStacks();
		}
	}

	private void logBeforeReducer(State s, Production prod, int length) {
		if(Tools.tracing) {
			TRACE("SG_Reducer() - " + s.stateNumber + ", " + length + ", " + prod.label);
			TRACE_ActiveStacks();
		}

		if (Tools.logging) {
			Tools.logger("Reducing; state ", s.stateNumber, ", token: ", logCharify(currentToken),
					", production: ", prod.label);
		}

		if (Tools.debugging) {
			Tools.debug("reducer() - ", dumpActiveStacks());

			Tools.debug(" state      : ", s.stateNumber);
			Tools.debug(" token      : ", logCharify(currentToken) + " (" + currentToken + ")");
			Tools.debug(" production : ", prod.label);
		}
	}

	private void TRACE_ActiveStacks() {
		TRACE("SG_ - active stacks: " + activeStacks.size());
		TRACE("SG_ - for_actor stacks: " + forActor.size());
		TRACE("SG_ - for_actor_delayed stacks: " + forActorDelayed.size());
	}


	private void logAmbiguity(Frame st0, Production prod, Frame st1, Link nl) {
		if (Tools.logging) {
			Tools.logger("Ambiguity: direct link ", st0.state.stateNumber, " -> ",
					st1.state.stateNumber, " ", (prod.isRejectProduction() ? "{reject}" : ""));
			if (nl.label.isParseNode()) {
				Tools.logger("nl is ", nl.isRejected() ? "{reject}" : "", " for ",
						((ParseNode) nl.label).getLabel());
			}
		}

		if (Tools.debugging) {
			Tools.debug("createAmbiguityCluster - ", tokensSeen - nl.getLength() - 1, "/",
					nl.getLength());
		}
	}

  public AbstractParseNode getParseTree() {
    return parseTree;
  }
}
