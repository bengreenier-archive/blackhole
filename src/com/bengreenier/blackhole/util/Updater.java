package com.bengreenier.blackhole.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;

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
	private Properties prop;

	public Updater(String localLocation,String remoteLocation) {
		this.localLocation = localLocation;
		this.remoteLocation = remoteLocation;
		this.prop = new Properties();
		
		try {
			prop.loadFromXML(new FileInputStream(localLocation));
		} catch (InvalidPropertiesFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			loadDefaults();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void storeToXML() throws IOException {
		FileOutputStream os = new FileOutputStream(localLocation);
		prop.storeToXML(os, null);
		os.close();
	}

	public void update() throws IOException {
		URL url = new URL(remoteLocation);

		InputStream is = url.openStream();
		Properties t = new Properties();
		t.loadFromXML(is);		

		for (Map.Entry<Object, Object> entry : t.entrySet()) {
			if (entry.getKey() instanceof String && entry.getValue() instanceof String) {
				String key = (String)entry.getKey();
				String value = (String)entry.getValue();
				
				if (prop.containsKey(key))
					if (key.contains("-version")) {
						Float f = Float.parseFloat(prop.getProperty(key));
						if (f < Float.parseFloat(value))
							callUpdate(t);
						
						prop.setProperty(key, value);
					}

			}
		}

	}

	public Properties prop() {
		return prop;
	}

	private void callUpdate(Properties remote) throws IOException {
		URL url = new URL(remote.getProperty("archive-url"));
		InputStream is = url.openStream();
		byte[] arr = FileIO.getByteArray(is);
		
		File file = new File(StaticStrings.getString("archive-location"));
		if (file.exists())
			file.delete();
		
		FileIO.makeFileAtLocation(file.getPath());
			
		
		FileOutputStream fis = new FileOutputStream(StaticStrings.getString("archive-location"));
		fis.write(arr);
		fis.close();
		is.close();
	}
	
	
	private void loadDefaults() {
		prop.setProperty("core-version", "0");
	}
	
	public static void main(String[] args) {
		Updater u = new Updater("updater.config","http://bengreenier.com/blackhole/update.xml");
		try {
			u.update();
			u.storeToXML();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}
