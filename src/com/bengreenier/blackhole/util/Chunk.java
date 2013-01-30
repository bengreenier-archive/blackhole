package com.bengreenier.blackhole.util;

/**
 * a generic interface to define
 * the pieces of data that
 * are send within a packet
 * over the network.
 * @author B3N
 *
 */
public interface Chunk<T> {
	
	/**
	 * how big this chunk is.
	 * @return
	 */
	public int getSize();
	
	/**
	 * get the data from
	 * the chunk
	 * @return
	 */
	public T getData();
	
	/**
	 * set the data for
	 * the chunk
	 */
	public void setData(T data);
	
}
