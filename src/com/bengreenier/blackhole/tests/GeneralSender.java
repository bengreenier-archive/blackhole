package com.bengreenier.blackhole.tests;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

import com.bengreenier.blackhole.util.Marker;

/**
 * use this to prompt for
 * data from the console to 
 * send to a specified server.
 * 
 * IE: to test server configurations
 * 
 * @author B3N
 *
 */
public class GeneralSender {

	public static void main(String[] args) throws NumberFormatException, IOException {
		Scanner in = new Scanner(System.in);
		
		System.out.print("GeneralSender: v1   Type 'stop' to shutdown server\n");
		System.out.print("Server ip: ");
		String serverIP = in.next();
		System.out.print("Server port: ");
		String serverPort = in.next();
		
		boolean more = true;
		
		
		System.out.println("Please wait...attempting to connect");
		
		Socket socket = new Socket();
		try{
			
		socket.connect(new InetSocketAddress(serverIP,Integer.parseInt(serverPort)));
		}catch(IOException e) {
			System.out.println("Unable to connect.");
			System.exit(-1);
		}
		
		System.out.println("connected");
		ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
		oout.writeObject(new Marker.GenericHeaderMarker());
		
		//the packet sending loop
		while (more && in.hasNext()) {
			String data = in.next();
			if (data.toLowerCase().equals("stop")) {
				more=!more;
				oout.writeObject(new Marker.GenericFooterMarker());
			}else {
				System.out.println("writing: "+data);
				//recall that the server is listening for a Header, then data, then a footer
				oout.writeObject(data);
				
			}
				
		}
		oout.close();
		socket.close();
		in.close();
	}
	
	
	/*
	 * 
	i kinda wish that it wasn't glitching like that
	writing: i
	Exception in thread "main" java.net.SocketException: Software caused connection abort: socket write error
	at java.net.SocketOutputStream.socketWrite0(Native Method)
	at java.net.SocketOutputStream.socketWrite(Unknown Source)
	at java.net.SocketOutputStream.write(Unknown Source)
	at java.io.ObjectOutputStream$BlockDataOutputStream.drain(Unknown Source)
	at java.io.ObjectOutputStream$BlockDataOutputStream.setBlockDataMode(Unknown Source)
	at java.io.ObjectOutputStream.writeNonProxyDesc(Unknown Source)
	at java.io.ObjectOutputStream.writeClassDesc(Unknown Source)
	at java.io.ObjectOutputStream.writeOrdinaryObject(Unknown Source)
	at java.io.ObjectOutputStream.writeObject0(Unknown Source)
	at java.io.ObjectOutputStream.writeFatalException(Unknown Source)
	at java.io.ObjectOutputStream.writeObject(Unknown Source)
	at com.bengreenier.blackhole.tests.GeneralSender.main(GeneralSender.java:57)


	this is because the socket on the GeneralSender has been connected to a server socket, but it tries to send
	socket open { data } socket close socket open { part2 } socket close all to one socket listener, which naturally,
	doesn't work. TEST THIS MORE
	
	ok, i think my new placement of the Marker's should work
	 */
}
