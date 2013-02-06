package com.bengreenier.blackhole.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * this class is designed to
 * implement a lock, on a file,
 * so that only one instance of
 * this executable is allowed to run.
 * @author B3N
 *
 */
public class Lock {

	private ServerSocket socket;
	
	public Lock(int port) {
		try {
			socket = new ServerSocket(port,10,InetAddress.getLocalHost());
			
		} catch (java.net.BindException e) {
			socket = null;
		} catch (IOException e) {
		
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isLocked() {
		if (socket == null)
			return false;
	
		
		Logger.getLogger("com.bengreenier.blackhole").log(Level.INFO,"isClosed "+socket.isClosed());
		
		if (!socket.isClosed())
			return true;
		else
			return false;
	}
	
	public void release() {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
