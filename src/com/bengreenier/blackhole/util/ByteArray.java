package com.bengreenier.blackhole.util;

/**
 * A wrapper for a Byte[]
 * @author B3N
 *
 */
public class ByteArray {

	private byte[] array;
	
	public ByteArray(int length) {
		array =  new byte[length];
	}
	
	private ByteArray(ByteArray other) {
		this.array = other.getArray();
	}
	
	public ByteArray(byte[] data, int length) {
		this.array = new byte[length];
		for (int i=0;i<length;i++)
			array[i] = data[i];
	}

	public byte[] getArray() {
		return this.array;
	}
	
	public void setArray(byte[] array) {
		this.array = array;
	}
	
	@Override
	public ByteArray clone() {
		return new ByteArray(this);
	}
}
