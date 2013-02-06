package com.bengreenier.blackhole.sockproc;

/**
 * K is a processed Data type
 * @author B3N
 *
 * @param <K>
 */
public abstract class AbstractSocketProcessor<K> extends Thread implements SocketWorker<K>  {

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
		cleanCloseSocket();
	}
	
	/**
	 * should be implemented in a way that
	 * the socket we are processing with
	 * is safely closed on call.
	 */
	public abstract void cleanCloseSocket();
	
	/**
	 * do the socket work (ie read data)
	 * in here, as this is what runs in the thread.
	 * 
	 */
	public abstract void run();

}
