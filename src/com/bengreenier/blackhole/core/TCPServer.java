package com.bengreenier.blackhole.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

import com.bengreenier.blackhole.util.Transmitable;

public class TCPServer extends Thread{

	private ServerSocket serverSocket;
	private CopyOnWriteArrayList<SocketProcessor> connections;
	private volatile boolean isListening;
	
	public TCPServer() {
		connections = new CopyOnWriteArrayList<SocketProcessor>();
		isListening = true;
	}
	
	@Override
	public void run() {
		try{
			
			//create a server socket for all recieves.
			serverSocket = new ServerSocket(Port.DEFAULT);
			while (isListening) {
				try{
					//when a connection comes in, Socket t is made
					Socket t = serverSocket.accept();
					
					if (t != null) {
						
						//create a processer for p, register it as a connection, and start it.
						SocketProcessor sp = new SocketProcessor(t);
						connections.add(sp);
						sp.start();
					}
					
				}catch(IOException e){
					if (isListening)
						e.printStackTrace();
				}
				
			}
			serverSocket.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Gracefully stop server asap
	 */
	public void stopListening() {
		if (serverSocket != null){
			isListening = false;
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public CopyOnWriteArrayList<SocketProcessor> getConnections() {
		return connections;
	}
	
	public static void main(String[] args) {
		TCPServer tcp = new TCPServer();
		tcp.start();
		
		System.out.print("Server is up\n\nType 'stop' to shutdown server: ");
		
		Scanner in = new Scanner(System.in);
		boolean more = true;
		while (more && in.hasNext()) {
			//output all data recieved on all sockets
			for (SocketProcessor sp : tcp.getConnections())
				if (sp.isDone())
					for (Transmitable<?> t : sp.getData())
						System.out.println(t.toString());
			
			if (in.next().toLowerCase().equals("stop"))
				more=!more;
		}
		in.close();
		
		tcp.stopListening();
	}
}
