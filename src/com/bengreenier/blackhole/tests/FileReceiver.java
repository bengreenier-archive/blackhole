package com.bengreenier.blackhole.tests;

import java.util.Scanner;

import com.bengreenier.blackhole.server.TCPFileProcessor;


public class FileReceiver {
	public static void main(String[] args) {
		TCPFileProcessor tcp = new TCPFileProcessor();
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
