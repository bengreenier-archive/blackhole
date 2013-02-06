package com.bengreenier.blackhole.natives;

/**
 * This class makes JNI native
 * method calls to the included
 * c++ library to access the 
 * windows registry.
 * 
 * @author B3N
 * @version 1
 */
public class WinRegistry {

	public static native boolean writeKey(int root, String key);
	public static native boolean writeValue(int root, String key,String value,int type);
	public static native String getValue(int root, String key);
	
	/**
	 * the constant int id's used for the 
	 * registry key types. 
	 * 
	 * @author B3N
	 *
	 */
	public static interface Types {
		public static final int DWORD = 1;
		public static final int QWORD = 2;
		public static final int STRING = 3;
		public static final int BINARY = 4;
		public static final int MULTISTRING = 5;
		public static final int EXPANDABLESTRING = 6;
	}
	
	/**
	 * the constant int id's used for
	 * the registry root keys
	 * @author B3N
	 *
	 */
	public static interface Keys {
		public static final int HKEY_CLASSES_ROOT = 1;
		public static final int HKEY_CURRENT_CONFIG = 2;
		public static final int HKEY_CURRENT_USER = 3;
		public static final int HKEY_LOCAL_MACHINE = 4;
		public static final int HKEY_USERS = 5;
	}
}
