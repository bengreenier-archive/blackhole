package com.bengreenier.blackhole.util;

import java.io.Serializable;
import java.util.Properties;

/**
 * lightweight interface used to mark
 * head / foot of blackhole
 * transmits.
 * @author B3N
 *
 */
public interface Marker extends Serializable {

	public static class FileHeaderMarker implements Marker {

		/**
		 * 
		 */
		private static final long serialVersionUID = 3778423273633125161L;

		private Properties prop;
		
		public FileHeaderMarker(String filename) {
			prop = new Properties();
			prop.setProperty("filename", filename);
		}
		
		
		@Override
		public Properties getProperties() {
			return prop;
		}

		@Override
		public Type getType() {
			return Type.HEADER;
		}
		
	}

	public static class GenericHeaderMarker implements Marker {

		/**
		 * 
		 */
		private static final long serialVersionUID = -3195759607695097314L;

		@Override
		public Properties getProperties() {
			return null;
		}

		@Override
		public Type getType() {
			return Type.HEADER;
		}
		
	}
	
	public static class GenericFooterMarker implements Marker {

		/**
		 * 
		 */
		private static final long serialVersionUID = 4456730454239523115L;

		@Override
		public Properties getProperties() {
			return null;
		}

		@Override
		public Type getType() {
			return Type.FOOTER;
		}
		
	}
	
	public enum Type{HEADER,FOOTER};
	
	/**
	 * used to store filename/type/etc
	 * this is IGNORED in getType() == Type.FOOTER
	 * 
	 * @return
	 */
	public Properties getProperties();
	
	/**
	 * what type of marker is this?
	 * before or after a file?
	 * 
	 * @return
	 */
	public Type getType();
}
