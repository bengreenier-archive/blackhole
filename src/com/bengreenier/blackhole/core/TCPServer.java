package com.bengreenier.blackhole.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bengreenier.blackhole.sockproc.SocketProcessor;


public class TCPServer extends Thread{

	private ServerSocket serverSocket;
	private CopyOnWriteArrayList<SocketProcessor> connections;//bad data structure choice //TODO fix
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
				Logger.getLogger("com.bengreenier.blackhole").log(Level.INFO, "hanging for a socket to be accepted");
					Socket t = serverSocket.accept();
					System.out.println("socket accepted");
					
					if (t != null) {
						
						Logger.getLogger("com.bengreenier.blackhole").log(Level.INFO,"non null, processing socket");
						//create a processer for p, register it as a connection, and start it.
						SocketProcessor sp = new SocketProcessor(t);
						connections.add(sp);
						sp.start();
						
						Logger.getLogger("com.bengreenier.blackhole").log(Level.INFO,"socket processing");
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
		return connections; //somethings wrong with this //TODO fix
	}
	
	public static void main(String[] args) {
		TCPServer tcp = new TCPServer();
		tcp.start();
		
		System.out.print("Server is up\n\nType 'stop' to shutdown server: ");
		
		Scanner in = new Scanner(System.in);
		boolean more = true;
		
		
		while (more && in.hasNext()) {
			for (SocketProcessor sp : tcp.getConnections())
				if (sp.isComplete())
					System.out.println("done");
				else
					System.out.println("not done");
			
			if (in.next().toLowerCase().equals("stop"))
				more=!more;
		}
		in.close();
		
		tcp.stopListening();
	}
}
