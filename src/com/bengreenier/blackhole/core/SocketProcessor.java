package com.bengreenier.blackhole.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.bengreenier.blackhole.util.Transmitable;

/**
 * Threaded object deserializer from
 * a network socket.
 * @author B3N
 *
 */
public class SocketProcessor extends Thread {

	private Socket socket;
	private ArrayList<Transmitable<?>> data;
	private volatile boolean completed;
	
	public SocketProcessor(Socket socket) {
		this.socket = socket;
		this.data = new ArrayList<Transmitable<?>>();
		this.completed = false;
	}
	
	@Override
	public void run() {
		if (socket != null)
			if (!socket.isClosed()) {
				try {
					InputStream is = socket.getInputStream();
					ObjectInputStream ois = new ObjectInputStream(is);
					Object o = null;
					while ((o = ois.readObject()) != null)
						if (o instanceof Transmitable)
							data.add((Transmitable<?>)o);
					ois.close();
					is.close();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		completed = true;
	}
	
	public boolean isDone() {
		return completed;
	}
	
	public ArrayList<Transmitable<?>> getData() {
		return data;
	}
}
