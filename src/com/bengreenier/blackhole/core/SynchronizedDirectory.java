package com.bengreenier.blackhole.core;

import java.io.File;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.bengreenier.blackhole.util.FileIO;
import com.bengreenier.blackhole.util.MD5;


public class SynchronizedDirectory {
	public static class SynchronizedDirectoryException extends Exception {
		/**
		 * suid, so this can be serialized properly if needed
		 */
		private static final long serialVersionUID = 6187747069322362175L;

		public SynchronizedDirectoryException(String message) {
			super(message);
		}
	}

	/**
	 * the interface that defines the methods that execute sync requests
	 * 
	 * @author B3N
	 *
	 */
	public static interface SyncInterface {
		public void createFile(File f) throws SynchronizedDirectoryException;
		public void updateFile(File f) throws SynchronizedDirectoryException;
		public void removeFile(File f) throws SynchronizedDirectoryException;
	}


	private Timer syncer;//the scheduler timer for the process
	private long delay,wait;//the args for the timer
	private File root; //the root directory to sync
	private HashMap<File,String> hashes;//store the file hashes here
	private SyncInterface si;//the interface where we make calls to mod the file stuct

	public SynchronizedDirectory(long delay,String directory, SyncInterface si) throws SynchronizedDirectoryException {
		if (!new File(directory).exists())
			throw new SynchronizedDirectoryException("Invalid SyncronizedDirectory: directory doesn't exist.");
		if (!new File(directory).isDirectory())
			throw new SynchronizedDirectoryException("Invalid SyncronizedDirectory: invalid directory.");
		this.si = si;
		this.root = new File(directory);
		this.hashes = new HashMap<File,String>();
		this.delay = delay;
		this.wait = 0;
		syncer = new Timer();
	}

	//call this to begin execution
	public void start() {
		syncer.scheduleAtFixedRate(new TimerTask(){

			private HashMap<File,String> temp;
			
			private void recurse(File dir) {
				for (File f : dir.listFiles()) {
					if (f.isDirectory()) {
						recurse(f);
					} else {
						if (!hashes.containsKey(f)) {
							hashes.put(f, new MD5(FileIO.getByteArray(f.getPath())).getStringDigest());//eww
							try {
								si.createFile(f);
							} catch (SynchronizedDirectoryException e) {
								e.printStackTrace();
							}
						} else {
							String td = new MD5(FileIO.getByteArray(f.getPath())).getStringDigest();//temp digest
							if (!hashes.get(f).equals(td)) {
								//if file has changed
								hashes.remove(f);
								hashes.put(f, td);
								try {
									si.updateFile(f);
								} catch (SynchronizedDirectoryException e) {
									e.printStackTrace();
								}
							}
						}
						temp.remove(f);
						
					}
				}
			}
			
			@Override
			public void run() {
				temp = new HashMap<File,String>(hashes);

				recurse(root);
				
				if (temp.size()>0) {
					for (File f : temp.keySet()) {
						hashes.remove(f);//delete the hash
						try {
							si.removeFile(f);
						} catch (SynchronizedDirectoryException e) {
							e.printStackTrace();
						}
					}
				}
				temp.clear();//just to be sure. even though we WILL recreate it

			}

		}, wait, delay);
	}

	/**
	 * call this to stop the synchronizing process
	 */
	public void stop() {
		syncer.cancel();
	}


	/**
	 * test case, shows example usage of this class.
	 * 
	 * unfortunately, this method for syncdir is not
	 * as efficient (memory wise) as i would like. so
	 * it will need to be readdressed at some point TODO
	 * 
	 * @param args
	 * @throws SynchronizedDirectoryException 
	 */
	public static void main(String[] args) throws SynchronizedDirectoryException {
		SynchronizedDirectory sd = new SynchronizedDirectory(1000, ".", new SyncInterface(){

			@Override
			public void createFile(File f) throws SynchronizedDirectoryException {
				System.out.println("this method would create "+f.getPath()+" in the file system.");
			}

			@Override
			public void updateFile(File f) throws SynchronizedDirectoryException {
				System.out.println("this method would update, or possibly remove and create "+f.getPath()+" in the file system.");
			}

			@Override
			public void removeFile(File f) throws SynchronizedDirectoryException {
				System.out.println("this method would remove "+f.getPath()+" from the file system.");
			}});
		sd.start();
	}
}
