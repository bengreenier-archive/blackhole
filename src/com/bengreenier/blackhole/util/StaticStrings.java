package com.bengreenier.blackhole.util;

import java.util.HashMap;

/**
 * Static class to 
 * get static strings,
 * that are used throughout the application for
 * things like resource paths, etc. 
 * 
 * Node: this is hardcoded for now,
 * but should probably be xml in the future
 * @author B3N
 *
 */
public class StaticStrings {

	
	static private StaticStrings pointer;
	private HashMap<String,String> map;
	
	private StaticStrings() {
		map = new HashMap<String,String>();
		
		//register our hardcoded static strings
		map.put("config", "blackhole.config");
		map.put("blackhole", "res/drawable/blackhole.png");
		map.put("blackhole_whole", "res/drawable/blackhole_whole.png");
		map.put("syncer","res/drawable/syncer.png");
		
	}
	
	static private StaticStrings get() {
		if (pointer == null)
			pointer = new StaticStrings();
		
		return pointer;
	}
	
	static public void init() {
		if (pointer == null)
			pointer = new StaticStrings();
	}
	
	static public String getString(String key) {
		return get().map.get(key);
	}
	
	
	
}
