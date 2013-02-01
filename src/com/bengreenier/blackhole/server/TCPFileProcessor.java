package com.bengreenier.blackhole.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bengreenier.blackhole.sockproc.FileSocketProcessor;
import com.bengreenier.blackhole.util.Port;

/**
 * we don't even use the Generic here,
 * it should really be deprecated and removed.
 * 
 * @author B3N
 *
 */
public class TCPFileProcessor extends AbstractServerProcessor<Object>{

	private ServerSocket serverSocket;
	private int port;

	public TCPFileProcessor() {
		port = Port.DEFAULT;
	}

	public TCPFileProcessor(int port) {
		this.port = port;
	}

	@Override
	public Object[] getData() {
		return null;
	}

	@Override
	public void cleanCloseServer() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		//create a server socket for all recieves.
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (serverSocket != null)
			while (isListening) {
				try{
					//when a connection comes in, Socket t is made
					Logger.getLogger("com.bengreenier.blackhole").log(Level.INFO, "hanging for a socket to be accepted");
					Socket t = serverSocket.accept();
					System.out.println("socket accepted");

					if (t != null) {

						Logger.getLogger("com.bengreenier.blackhole").log(Level.INFO,"non null, processing socket");
						//create a processer for p, register it as a connection, and start it.
						FileSocketProcessor sp = new FileSocketProcessor(t);
						sp.start();

						Logger.getLogger("com.bengreenier.blackhole").log(Level.INFO,"socket processing");
					}

				}catch(IOException e){
					if (isListening)
						e.printStackTrace();
				}

			}
		cleanCloseServer();

	}

}
