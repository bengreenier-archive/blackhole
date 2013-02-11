package com.bengreenier.blackhole.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

	private byte[] digest; 

	public MD5(byte[] array) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (md5 != null)
			digest = md5.digest(array);
	}

	public byte[] getDigest() {
		return digest;
	}

	public String getStringDigest() {
		return new String(digest);
	}
}
