package com.bengreenier.blackhole.sockproc;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import com.bengreenier.blackhole.util.ByteArray;
import com.bengreenier.blackhole.util.FileIO;
import com.bengreenier.blackhole.util.Marker;

/**
 * This class is designed to
 * only process File data from TCP sockets,
 * all other data is ignored.
 * 
 * out return type is object, since it isn't used/is null
 * 
 * @author B3N
 *
 */
public class FileSocketProcessor extends AbstractSocketProcessor<Object>{

	private Socket socket; 
	
	public FileSocketProcessor(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void cleanCloseSocket() {
		if (socket != null)
			if (!socket.isClosed())
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
							if (header instanceof Marker.FileHeaderMarker && o instanceof ByteArray) {
								if (!FileIO.exists(((Marker.FileHeaderMarker)header).getProperties().getProperty("filename")))
									FileIO.writeByteArray(((ByteArray)o).getArray(), ((Marker.FileHeaderMarker)header).getProperties().getProperty("filename"));
							}
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
		
	}

	@Override
	public Object[] getData() {
		//since we write files to the system, there is no return data.
		return null;
	}

}
