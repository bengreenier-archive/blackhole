package com.bengreenier.blackhole.tests;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;


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
		
		//the packet sending loop
		while (more && in.hasNext()) {
			String data = in.next();
			if (data.toLowerCase().equals("stop"))
				more=!more;
			else{
				OutputStream out = socket.getOutputStream();
				ObjectOutputStream oout = new ObjectOutputStream(out);
				oout.writeObject(data);
				oout.close();
				out.close();
			}
				
		}
		socket.close();
		in.close();
	}
}
