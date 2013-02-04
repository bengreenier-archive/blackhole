package com.bengreenier.blackhole.util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

/**
 * define the logic of 
 * configuring private and public
 * keys using rsa encrpytion
 * @author B3N
 *
 */
public class KeyCryptography {

	//secure is wicked secure (slower), default is damn good though.
	public static final int DEFAULT_KEY_SIZE = 1024;
	public static final int SECURE_KEY_SIZE = 2048;
	
	
	private PrivateKey priv;
	private PublicKey pub;
	private int keySize;
	
	public KeyCryptography() {
		this.keySize = DEFAULT_KEY_SIZE;
	}
	
	public KeyCryptography(int keySize) {
		this.keySize = keySize;
	}
	
	public KeyCryptography generateKeys() {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			kpg.initialize(keySize, random);
			KeyPair pair = kpg.generateKeyPair();
			priv = pair.getPrivate();
			pub = pair.getPublic();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this; //chainability
	}
	
	public PrivateKey getPrivate() {
		return priv;
	}
	
	public PublicKey getPublic() {
		return pub;
	}
	
}
