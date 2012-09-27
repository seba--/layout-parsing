package org.spoofax.jsglr.client;




public class PathListPool {

	// Poor man's queue (?)
	private PooledPathList asyncP0 = new PooledPathList(512, false);
	private PooledPathList asyncP1 = new PooledPathList(512, false);
	private PooledPathList asyncP2 = new PooledPathList(512, false);
	private PooledPathList asyncP3 = new PooledPathList(512, false);
	private PooledPathList asyncP4 = new PooledPathList(512, false);
	
	public int asyncCacheMisses = 0;
	
	// this would be a weak reference, if GWT had one
//	private static PathListPool asyncInstance = new PathListPool();
	
	private PathListPool() {
		// singleton
	}
	
	private static Object getSyncRoot() {
		return PathListPool.class;
	}
	
	public static PathListPool getInstance() {
		return new PathListPool();
	}
	
	public PooledPathList create() {
		synchronized (getSyncRoot()) {
			if(asyncP0.usage == 0)
				return asyncP0.start();
			if(asyncP1.usage == 0)
				return asyncP1.start();
			if(asyncP2.usage == 0)
				return asyncP2.start();
			if(asyncP3.usage == 0)
				return asyncP3.start();
			if(asyncP4.usage == 0)
				return asyncP4.start();
		
			asyncCacheMisses++;
			PooledPathList paths = new PooledPathList(512, false);
			endCreate(paths);
			return asyncP0.start();
		}
	}
	
	public void resetPerformanceCounters() {
		synchronized (getSyncRoot()) {
			asyncCacheMisses = 0;
		}
	}

	public void endCreate(PooledPathList paths) {
		synchronized (getSyncRoot()) {
			paths.reset();
			if (asyncP0 == paths || asyncP1 == paths || asyncP2 == paths
					|| asyncP3 == paths || asyncP4 == paths)
				return;
			asyncP4 = asyncP3;
			asyncP3 = asyncP2;
			asyncP2 = asyncP1;
			asyncP1 = asyncP0;
			asyncP0 = paths;
		}
	}
}
