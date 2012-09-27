package org.spoofax.jsglr.client;



public class PooledPathList {

	int rememberIndex;
	int allocIndex;
	Path[] rememberPool;
	Path[] makePool;
	int usage;
	
	public static int maxRemembered;
	public static int maxAllocated;
	
	public PooledPathList(int capacity, boolean deepInit) {
		allocIndex = 1;
		rememberIndex = 0;
		usage = 0;
		rememberPool = new Path[capacity];
		makePool = new Path[capacity];
		if (deepInit) {
			for(int i = 0; i < capacity; i++) {
				rememberPool[i] = new Path();
			}
			for(int i = 0; i < capacity; i++) {
				makePool[i] = new Path();
			}			
		}
	}
	
	/**
	 * Store a path for later retrieval.
	 * 
	 * @see #get(int)
	 * @see #size()
	 */
	public Path rememberPath(Path parent, Link link, Frame frame, int length, int parentCount) {
		Path p;
		if (rememberIndex == rememberPool.length)
			rememberPool = resizeArray(rememberPool);
		if(rememberPool[rememberIndex] == null) {
			p = new Path();
			rememberPool[rememberIndex] = p;
		} else {
			p = rememberPool[rememberIndex];
		}
		rememberIndex++;
		return p.reuse(parent, link, frame, length, parentCount);
    }
	
	/**
	 * Create a path, either reusing one from this pool, or creating a fresh instance.
	 */
	public Path makePath(Path parent, Link link, Frame frame, int length, int parentCount) {
		Path p;
		if (allocIndex == makePool.length)
			makePool = resizeArray(makePool);
		if(makePool[allocIndex] == null) {
			p = new Path();
			makePool[allocIndex] = p;
		} else {
			p = makePool[allocIndex];
		}
		allocIndex++;
		return p.reuse(parent, link, frame, length, parentCount);
    }

	private Path[] resizeArray(Path[] array) {
		Path[] result = new Path[array.length * 2];
		System.arraycopy(array, 0, result, 0, array.length);
		return result;
	}

	public int size() {
		return rememberIndex;
	}
	
	public Path get(int index) {
		return rememberPool[index];
	}

	public void end() {
		usage--;
		if(usage != 0)
			throw new IllegalStateException("Must always end() the PooledPathList after use");
		reset();
	}
	
	
	public PooledPathList start() {
		if(usage == 0) {
			usage++;
			rememberIndex = 0;
			allocIndex = 1;
			return this;
		}
		// TODO: return unpooled pathlist?
		throw new IllegalStateException("PooledPathList may not be used recursively");
	}
	
	public static void resetPerformanceCounters() {
		maxRemembered = 0;
		maxAllocated = 0;
	}

	public void reset() {
		maxRemembered = Math.max(maxRemembered, rememberIndex);
		maxAllocated = Math.max(maxAllocated, allocIndex);
		usage = 0;
		rememberIndex = 0;
		allocIndex = 0;
	}
}
