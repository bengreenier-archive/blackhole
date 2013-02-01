package com.bengreenier.blackhole.sockproc;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.bengreenier.blackhole.util.Marker;

/**
 * The generalized TCP Socket Processor,
 * for working with Object Data.
 * @author B3N
 *
 */
public class SocketProcessor extends AbstractSocketProcessor<Object> {

	private Socket socket;
	private volatile ArrayList<Object> list;
	private volatile boolean isComplete;
	
	public SocketProcessor(Socket socket) {
		this.socket = socket;
		this.list = new ArrayList<Object>();
		this.isComplete = false;
	}
	
	@Override
	public void cleanCloseSocket() {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void run() {
		//do the file logic reading code here
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
									list.add(o);
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
				isComplete = true;
		
	}

	@Override
	public Object[] getData() {
		// TODO Auto-generated method stub
		return list.toArray();
	}
	
	/**
	 * call this before getData, as
	 * a precaution to be sure to
	 * prevent concurrent ArrayList
	 * access
	 * @return
	 */
	public boolean isComplete() {
		return isComplete;
	}
	
}