package com.bengreenier.blackhole.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * some utils to work
 * with File input
 * output.
 * 
 * @author B3N
 *
 */
public class FileIO {

	public static byte[] getByteArray(String filename) {
		byte[] arr = null;
		try {
			arr = Files.readAllBytes(Paths.get(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return arr;
	}

	public static void writeByteArray(byte[] array, String filename) {
		try {
			Files.write(Paths.get(filename), array);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean exists(String filename) {
		return new File(filename).exists();
	}

	/**
	 * read a byte array of a RandomAccessFile
	 * 
	 * Note that we DO NOT seek back to the 
	 * begining of the file, this might be necessary,
	 * and should be handled by the coder.
	 * 
	 * @param raf
	 * @return
	 */
	public static byte[] getByteArray(RandomAccessFile raf) {
		byte[] arr = null;

		if (raf!=null)
			try {
				arr = new byte[(int) raf.length()];
				raf.read(arr);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		return arr;
	}

	/**
	 * write a byte array to a RandomAccessFile
	 * 
	 * Note that we DO NOT seek back to the 
	 * begining of the file, this might be necessary,
	 * and should be handled by the coder.
	 * 
	 * @param raf
	 * @param arr
	 */
	public static void writeByteArray(RandomAccessFile raf, byte[] arr) {

		if (raf!=null)
			try {
				raf.write(arr);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public static byte[] getByteArray(InputStream is) throws IOException {
		byte[] byteChunk = new byte[1024]; // Or whatever size you want to read in at a time.
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		int n;

		while ( (n = is.read(byteChunk)) > 0 ) {
			os.write(byteChunk, 0, n);
		}

		return os.toByteArray();
	}
	
	public static boolean makeFileAtLocation(String path) throws IOException {
		String dir = (path.contains("/")) ? path.substring(0,path.lastIndexOf("/")) : path.substring(0,path.lastIndexOf("\\"))
		,file = (path.contains("/")) ? path.substring(path.lastIndexOf("/")) : path.substring(path.lastIndexOf("\\"));
		boolean result = false;
		result = (new File(dir)).mkdirs();
		if (result)
			result = (new File(file)).createNewFile();
		
		return result;
	}


}
