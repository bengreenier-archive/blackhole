package com.bengreenier.blackhole.util;

import java.io.Serializable;

/**
 * classical representation
 * of a byte[]
 * @author B3N
 *
 */
public class ByteArray implements Serializable{

	/**
	 * generated sid
	 */
	private static final long serialVersionUID = 8877785805220009383L;
	private byte[] array;
	
	public ByteArray(int length) {
		array = new byte[length];
	}
	
	public ByteArray(byte[] data) {
		array = new byte[data.length];
		for (int i=0;i<data.length;i++)
			array[i] = data[i];
	}
	
	private ByteArray(ByteArray other) {
		array = new byte[other.getArray().length];
		for (int i=0;i<other.getArray().length; i++)
			array[i] = other.getArray()[i];
	}
	
	public byte[] getArray() {
		return array;
	}
	
	@Override
	public ByteArray clone() {
		return new ByteArray(this);
	}
}
