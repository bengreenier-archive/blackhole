package com.bengreenier.blackhole.util;

import java.io.File;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;



public class SyncronizedDirectory {
	public static class SyncronizedDirectoryException extends Exception {
		/**
		 * suid, so this can be serialized properly if needed
		 */
		private static final long serialVersionUID = 6187747069322362175L;

		public SyncronizedDirectoryException(String message) {
			super(message);
		}
	}

	public static interface SyncInterface {
		public void createFile(File f) throws SyncronizedDirectoryException;
		public void updateFile(File f) throws SyncronizedDirectoryException;
		public void removeFile(File f) throws SyncronizedDirectoryException;
	}


	private Timer syncer;//the scheduler timer for the process
	private long delay,wait;//the args for the timer
	private File root; //the root directory to sync
	private HashMap<File,String> hashes;//store the file hashes here
	private SyncInterface si;//the interface where we make calls to mod the file stuct

	public SyncronizedDirectory(long delay,String directory, SyncInterface si) throws SyncronizedDirectoryException {
		if (!new File(directory).exists())
			throw new SyncronizedDirectoryException("Invalid SyncronizedDirectory: directory doesn't exist.");
		if (!new File(directory).isDirectory())
			throw new SyncronizedDirectoryException("Invalid SyncronizedDirectory: invalid directory.");
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
							} catch (SyncronizedDirectoryException e) {
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
								} catch (SyncronizedDirectoryException e) {
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
					for (File f : temp.keySet())
						try {
							si.removeFile(f);
						} catch (SyncronizedDirectoryException e) {
							e.printStackTrace();
						}
				}
				temp.clear();//just to be sure. even though we WILL recreate it

			}

		}, wait, delay);
	}

	public void stop() {
		syncer.cancel();
	}


	/**
	 * test case, shows example usage of this class.
	 * @param args
	 * @throws SyncronizedDirectoryException 
	 */
	public static void main(String[] args) throws SyncronizedDirectoryException {
		
		
		SyncronizedDirectory sd = new SyncronizedDirectory(1000, ".", new SyncInterface(){

			@Override
			public void createFile(File f) throws SyncronizedDirectoryException {
				System.out.println("this method would create "+f.getPath()+" in the file system.");
			}

			@Override
			public void updateFile(File f) throws SyncronizedDirectoryException {
				System.out.println("this method would update, or possibly remove and create "+f.getPath()+" in the file system.");
			}

			@Override
			public void removeFile(File f) throws SyncronizedDirectoryException {
				System.out.println("this method would remove "+f.getPath()+" from the file system.");
			}});
		sd.start();
	}
}
