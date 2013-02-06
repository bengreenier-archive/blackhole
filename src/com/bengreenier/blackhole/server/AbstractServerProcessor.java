package com.bengreenier.blackhole.server;

/**
 * Where K is a data type we are
 * working with here.
 * @author B3N
 *
 * @param <K>
 */
public abstract class AbstractServerProcessor<K> extends Thread implements ServerWorker<K> {

	//a protected volatile boolean to shutdown the server
	protected volatile boolean isListening = true;

	/**
	 * calling this method safely
	 * shuts down the thread, and
	 * calls cleanCloseSocket
	 * to safely close the socket
	 */
	public void stopListening() {
		isListening = false;
		cleanCloseServer();
	}

	/**
	 * should be implemented in a way that
	 * the socket we are processing with
	 * is safely closed on call, also any
	 * other server processes need to be
	 * smoothly shutdown here.
	 */
	public abstract void cleanCloseServer();

	/**
	 * do the server work (ie listen for data, spawn threads)
	 * in here, as this is what runs in the main server thread.
	 * 
	 */
	public abstract void run();

}
