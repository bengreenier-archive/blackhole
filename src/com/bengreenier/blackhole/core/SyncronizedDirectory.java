package com.bengreenier.blackhole.core;

import java.io.File;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.bengreenier.blackhole.util.FileIO;
import com.bengreenier.blackhole.util.MD5;

public class SyncronizedDirectory {
	public interface SyncInterface {
		public void createFile(File f);
		public void updateFile(File f);
		public void removeFile(File f);
	}
	
	
	private Timer syncer;//the scheduler timer for the process
	private long delay,wait;//the args for the timer
	private File root; //the root directory to sync
	private HashMap<File,String> hashes;//store the file hashes here
	private SyncInterface si;//the interface where we make calls to mod the file stuct
	
	public SyncronizedDirectory(long delay,String directory, SyncInterface si) throws Exception {
		if (!new File(directory).exists())
			throw new Exception("Invalid SyncronizedDirectory: directory doesn't exist.");
		if (!new File(directory).isDirectory())
			throw new Exception("Invalid SyncronizedDirectory: invalid directory.");
		this.si = si;
		this.root = new File(directory);
		this.hashes = new HashMap<File,String>();
		this.delay = delay;
		this.wait = 0;
		syncer = new Timer();
	}
	
	public void start() {
		syncer.scheduleAtFixedRate(new TimerTask(){

			@Override
			public void run() {
				HashMap<File,String> temp = new HashMap<File,String>(hashes);
				
				
				for (File f : root.listFiles()) {
					if (!hashes.containsKey(f)) {
						hashes.put(f, new MD5(FileIO.getByteArray(f.getName())).getStringDigest());//eww
						si.createFile(f);
					} else {
						String td = new MD5(FileIO.getByteArray(f.getName())).getStringDigest();//temp digest
						if (!hashes.get(f).equals(td)) {
							//if file has changed
							hashes.remove(f);
							hashes.put(f, td);
							si.updateFile(f);
						}
					}
					temp.remove(f);
				}
				
				if (temp.size()>0) {
					for (File f : temp.keySet())
						si.removeFile(f);
				}
					
			}
			
		}, wait, delay);
	}
	
}
