package com.bengreenier.blackhole.util;

/**
 * A generic interface to define 
 * data that is Transmit-able over
 * the Blackhole network. 
 * 
 * This is more useful for the client side of things.
 * 
 * if you so desire, you could reconstruct
 * this interface after a transmit (on the recieving end)
 * but its kinda silly right now since all we need to do
 * is rewrite it to the File.
 * 
 * @author B3N
 *
 * @param <T>
 */
public interface Transmitable<T> extends Iterable<Chunk<T>> {

	/**
	 * get an array of chunks
	 * 
	 * @return
	 */
	public Chunk<T>[] getChunks();
	
	/**
	 * get the local (ex: ../file/path)
	 * path to the file being described
	 * @return
	 */
	public String getLocalFilename();
	
	/**
	 * get the absolute (ex: c:/path/to/file)
	 * path to the file being described
	 * @return
	 */
	public String getAbsoluteFilename();
}
