package com.bengreenier.blackhole.server;

public interface ServerWorker<K> extends Runnable {

	public void run();
	
	public K[] getData();
}
