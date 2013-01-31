package com.bengreenier.blackhole.core;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.bengreenier.blackhole.util.Marker;

/**
 * Threaded object deserializer from
 * a network socket.
 * @author B3N
 *
 */
public class SocketProcessor extends Thread {

	private Socket socket;
	private ArrayList<Object> data;
	private volatile boolean completed;
	
	public SocketProcessor(Socket socket) {
		this.socket = socket;
		this.data = new ArrayList<Object>();
		this.completed = false;
	}
	
	@Override
	public void run() {
		if (socket != null)
			if (!socket.isClosed()) {
				try {
					ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
					Object o = null; Marker header = null; boolean noFooter = true;
					while ((o = ois.readObject()) != null && noFooter) {
						//we look for a Marker with type header
						if (o instanceof Marker) {
							if (((Marker)o).getType() == Marker.Type.HEADER && header == null)
								header = (Marker)o;
							if (((Marker)o).getType() == Marker.Type.FOOTER && header != null) {
								header = null;
								noFooter = false;
							}
						}else if (header != null)
							data.add(o);
								
						
					}
						
					ois.close();
				} catch (EOFException e) {
					//can we ignore this exception? i think we can and should safely be able to. but i've changed this like 100x times
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
		
		for (Object o : data) //TODO remove
			System.out.println(o.toString());
	}
	
	public boolean isDone() {
		return completed;
	}
	
	public ArrayList<Object> getData() {
		return data;
	}
}
