package com.bengreenier.blackhole.update;

import java.beans.XMLEncoder;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * responsible for wrapping
 * the behavior of connecting
 * to a location to get an update
 * file, and verifying if the update
 * file contains newer material then
 * what is currently "installed"
 * 
 * 
 * @author B3N
 *
 */
public class Updater {

	private String remoteLocation;
	private String localLocation;
	private ArrayList<UpdateComponent> list;
	
	
	public Updater(String localLocation,String remoteLocation) {
		this.localLocation = localLocation;
		this.remoteLocation = remoteLocation;
		list = new ArrayList<UpdateComponent>();
	}

	public void update() throws IOException {
		URL url = new URL(remoteLocation);

		//download file
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] byteChunk = new byte[1024]; 
		int n;

		InputStream is = url.openStream();

		while ( (n = is.read(byteChunk)) > 0 ) {
			os.write(byteChunk, 0, n);
		}

		byte[] arr = os.toByteArray();
		
		
		

		//determine content>current content
		
		
		
	}
	
	/**
	 * track a component to be updated
	 * @param c
	 */
	public void registerComponent(UpdateComponent c) {
		list.add(c);
	}
	
	/**
	 * write the contents of list to xml
	 * @param os
	 * @throws IOException
	 */
	public void storeToXML(OutputStream os) throws IOException {
		
		XMLEncoder xenc = new XMLEncoder(os);
		
		for (UpdateComponent c : list)
			xenc.writeObject(c);
		xenc.close();
		os.close();
	}
	
	
	public static void main(String[] args) {
		Updater u = new Updater("","");
		u.registerComponent(new UpdateComponent(){

			private static final long serialVersionUID = 7355930828436500930L;

			@Override
			public String getPath() {
				// TODO Auto-generated method stub
				return "./res/";
			}

			@Override
			public float getVersion() {
				// TODO Auto-generated method stub
				return 0.01f;
			}

			@Override
			public ArrayList<UpdateComponent> getChildren() {
				// TODO Auto-generated method stub
				return null;
			}});
		
		try {
			FileOutputStream fos = new FileOutputStream("test.xml");
			u.storeToXML(fos);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
