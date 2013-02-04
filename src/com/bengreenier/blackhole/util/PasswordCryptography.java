package com.bengreenier.blackhole.util;

/**
 * define the logic of 
 * configuring password
 * encrpytion schemes
 * 
 * Note: this code doesn't
 * enforce "good" passwords
 * 
 * @author B3N
 *
 */
public class PasswordCryptography {

	//TODO how should we implement this?
	/*
	 * will a company have a core password
	 * that is used to "encrpyt" everything,
	 * or perhaps every user would be prompted
	 * for a password. or, the blackhole interfaces
	 * could get a core password every x seconds
	 * and use that for encrpytion, and autodecrypt
	 * upon arrival. but, that means the connection
	 * from the inteface to the core is vulnerable.
	 *  
	 *  
	 *  for the record, i don't like this method.
	 *  if it where up to me, KeyCryptography would
	 *  be used on both ends, where sent data is
	 *  encrypted using local-private and remote-public
	 *  keys. then on data arrival, local-private and remote-public
	 *  is used again (but they're swapped, note the old remote 
	 *  is the new local) to decrypt.
	 */
}
