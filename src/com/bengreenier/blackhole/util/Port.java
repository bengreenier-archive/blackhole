package com.bengreenier.blackhole.util;

/**
 * Define some statics we need
 * pertaining to port data.
 * @author B3N
 *
 */
public class Port {

	public static final int DEFAULT = 53535;
	
	private static int nextPort = DEFAULT;
	
	public static int getNext() {
		nextPort++;
		return nextPort;
	}
	
}
