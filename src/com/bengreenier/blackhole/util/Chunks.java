package com.bengreenier.blackhole.util;

import java.net.DatagramPacket;

/**
 * some chunk helpers
 * @author B3N
 *
 */
public class Chunks {
	
	/**
	 * static method to make a chunk from a packet
	 * @param packet
	 * @return
	 */
	public static Chunk<ByteArray> getByteChunk(final DatagramPacket packet) {
		return new Chunk<ByteArray>(){

			private ByteArray ba = new ByteArray(packet.getData(),packet.getLength());
			
			
			@Override
			public int getSize() {
				// TODO Auto-generated method stub
				return ba.getArray().length;
			}

			@Override
			public ByteArray getData() {
				// TODO Auto-generated method stub
				return ba;
			}

			@Override
			public void setData(ByteArray data) {
				ba = data;
				
			}
			
		};
	}
}
