package com.bengreenier.blackhole.util;

import java.io.File;
import java.io.IOException;
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
}
