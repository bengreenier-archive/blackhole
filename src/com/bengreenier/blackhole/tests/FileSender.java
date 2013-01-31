package com.bengreenier.blackhole.tests;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.bengreenier.blackhole.util.Marker;

public class FileSender {

	public static void main(String[] args) {
		
		String serverAddress = "";
		String serverPort = "";
		String filePath = "";
		
		for (int i=0;i<args.length; i++) {
			if (args[i].toLowerCase().equals("--server"))
				serverAddress = args[i+1];
			if (args[i].toLowerCase().equals("--port"))
				serverPort = args[i+1];
			if (args[i].toLowerCase().equals("--file"))
				filePath = args[i+1];
		}
		
		if (serverAddress.length() == 0
				|| serverPort.length() == 0
				|| filePath.length() == 0 )
			System.exit(-1);
		
		if (!new File(filePath).exists())
			System.exit(-1);
		
		Socket socket = new Socket();
		try {
			socket.connect(new InetSocketAddress(serverAddress,Integer.parseInt(serverPort)));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		
		try {
			ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
			oout.writeObject(new Marker.FileHeaderMarker(filePath));
			oout.writeObject(new File(filePath));
			oout.writeObject(new Marker.GenericFooterMarker());
			oout.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
