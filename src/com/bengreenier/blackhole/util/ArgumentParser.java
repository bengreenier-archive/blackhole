package com.bengreenier.blackhole.util;

import java.util.HashMap;

/**
 * parses arguments in the form of
 * -flag value
 * or --flag value
 * or /flag value
 * 
 * allowing you to query the parsed
 * arguments by calling find("-flag")
 * @author B3N
 *
 */
public class ArgumentParser {

	private HashMap<String,String> map;
	
	public ArgumentParser() {
		map = new HashMap<String,String>();
	}
	
	public ArgumentParser(String[] args) {
		map = new HashMap<String,String>();
		for (int i=0; i< args.length; i++) {
			
			//this is actually way too basic to be PERFECT TODO
			if (args[i].startsWith("-")
					|| args[i].startsWith("--")
					|| args[i].startsWith("/"))
				if (args.length>i+1)
					map.put(args[i],args[i+1]);
		}
	}
	
	public void feed(String key,String argument) {
		map.put(key, argument);
	}
	
	public String find(String key) {
		return map.get(key);
	}
}
