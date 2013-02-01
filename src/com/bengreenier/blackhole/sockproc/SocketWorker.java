package com.bengreenier.blackhole.sockproc;

/**
 * threaded interface to work with
 * a socket.
 * @author B3N
 *
 */
public interface SocketWorker<T> extends Runnable {

	
	public void run();
	
	public T[] getData();
}
