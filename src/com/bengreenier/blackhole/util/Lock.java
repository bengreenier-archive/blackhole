package com.bengreenier.blackhole.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * this class is designed to
 * implement a lock, on a file,
 * so that only one instance of
 * this executable is allowed to run.
 * @author B3N
 *
 */
public class Lock {

	private FileChannel channel;
	private FileLock lock;
	
	public Lock(String filename) {
		try {
			File file = new File(filename);
			
			if (file.exists())
				file.delete();
			
			FileOutputStream lockFileOS = new FileOutputStream(file);
			lockFileOS.close();
			
			channel = new RandomAccessFile(file,"rw").getChannel();
		
			lock = channel.tryLock();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (OverlappingFileLockException e) {
			lock = null;
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public boolean isLocked() {
		if (lock == null)
			return false;
		
		Logger.getLogger("com.bengreenier.blackhole").log(Level.INFO, "Lock is valid: "+lock.isValid());
		Logger.getLogger("com.bengreenier.blackhole").log(Level.INFO, "Lock is shared: "+lock.isShared());
		
		return lock.isValid();
	}
	
	public void release() {
		try {
			lock.release();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
