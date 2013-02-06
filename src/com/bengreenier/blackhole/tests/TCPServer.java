package com.bengreenier.blackhole.tests;

import java.util.Scanner;

import com.bengreenier.blackhole.server.TCPProcessor;

/**
 * a test of TCPProcessor
 * @author B3N
 *
 */
public class TCPServer extends Thread{
	public static void main(String[] args) {
		TCPProcessor tcp = new TCPProcessor();
		tcp.start();
		
		System.out.print("Server is up\n\nType 'stop' to shutdown server: ");
		
		Scanner in = new Scanner(System.in);
		boolean more = true;
		
		
		while (more && in.hasNext()) {
			if (in.next().toLowerCase().equals("stop"))
				more=!more;
		}
		in.close();
		
		tcp.stopListening();
	}
}
