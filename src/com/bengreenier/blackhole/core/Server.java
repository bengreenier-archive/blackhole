package com.bengreenier.blackhole.core;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Scanner;

import com.bengreenier.blackhole.util.ByteArray;
import com.bengreenier.blackhole.util.Chunk;
import com.bengreenier.blackhole.util.Chunks;

/**
 * Define the behavior 
 * of the "server" aspect
 * of a Blackhole application
 * 
 * Note that catching any exception within the run() method will
 * close the Thread. This is BY DESIGN, as exceptions should
 * only be thrown when indicating a problem with i/o
 * @author B3N
 *
 */
public class Server extends Thread{

	private volatile boolean isListening;
	private ArrayList<Chunk<ByteArray>> chunkList;
	private volatile DatagramSocket serverSocket;
	
	public Server() {
		isListening = true;
		
		try {
			serverSocket = new DatagramSocket(Port.DEFAULT);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			
		//this is our central listening loop. the server thread will reside here
		//until it is told to stop listening.
		while(isListening) {
			System.out.println(isListening);
			//generate a chunk, to recieve the data into
			Chunk<ByteArray> recievedChunk = new Chunk<ByteArray>(){

				//note our chunk is a wrapped ByteArray
				//this DOES create a smidgen more overhead
				//that should be addressed at a later date
				private ByteArray ba = new ByteArray(1024);
				
				@Override
				public int getSize() {
					return ba.getArray().length;
				}

				@Override
				public ByteArray getData() {
					return ba;
				}

				@Override
				public void setData(ByteArray data) {
					ba = data;
				}
				
			};
			
			//build a packet from that chunk to recieve data into
			DatagramPacket receivedPacket = new DatagramPacket(recievedChunk.getData().getArray(),recievedChunk.getSize());
			
			//receive the packet
			serverSocket.receive(receivedPacket);
			
			//process the packet if it isn't a connection packet
			if (receivedPacket.getData()[0] != 2)
				processPacket(receivedPacket);
			else {
				//reply to the connection packet here
				serverSocket.send(new DatagramPacket(new byte[]{2},1,receivedPacket.getAddress(),receivedPacket.getPort()));
			}
				
			//repeat until told to stopListening() //TODO damn, this would work, but receive() blocks.
		}
		
		serverSocket.close();
		}catch(SocketException e) {
			
			//otherwise ignore it
			if (isListening)
				e.printStackTrace();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This is the most effective and smooth
	 * way to request the server shutdown.
	 */
	public void stopListening() {
		isListening = false;
		if (serverSocket != null)
			serverSocket.close();
	}
	
	/**
	 * This method does what needs
	 * to happen to the packet
	 * @param packet
	 * @throws Exception
	 */
	private void processPacket(DatagramPacket packet) throws Exception {
		//this case should only be tripped in the event of a parse error (invalid transfer order)
		if (packet.getData()[0] != 0 && chunkList == null)
			throw new Exception("Packet Processor: Very bad data parser error (0x1)");
		
		//this is hit when we are getting the filename and extension
		if (packet.getData()[0] == 0) {
			chunkList = new ArrayList<Chunk<ByteArray>>();
			chunkList.add(Chunks.getByteChunk(packet));
		
		//this is hit when we are getting actual file data
		}else if (packet.getData()[0] == 1){
			chunkList.add(Chunks.getByteChunk(packet));
			
		//this should never be hit (malformed data)
		}else{
			throw new Exception("Packet Processor: Very bad data parser (0x2)");
		}
	}
	
	/**
	 * Test entry point for Server
	 * @param args
	 */
	public static void main(String[] args) {
		Server svr = new Server();
		svr.start();
		System.out.print("Server is up\n\nType 'stop' to shutdown server: ");

		Scanner in = new Scanner(System.in);
		boolean more = true;
		while (more && in.hasNext())
			if (in.next().toLowerCase().equals("stop"))
				more=!more;
		in.close();
		
		svr.stopListening();
	}
}
