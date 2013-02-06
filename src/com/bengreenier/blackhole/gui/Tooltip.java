package com.bengreenier.blackhole.gui;

import java.awt.Component;

/**
 * this class is animated
 * from a start point,
 * to and end point and 
 * reversed once focus is lost
 * 
 * it can contain swing contents.
 * 
 * this is really quite ambitious.
 * 
 * @author B3N
 *
 */
public class Tooltip {

	private Component anchor;
	
	//defines where the popup "grows" from
	public static enum PopupStyle { CENTER,LEFT,RIGHT };
	
	public Tooltip() {
		
	}
	
	public void setAnchor(Component anchor) {
		this.anchor = anchor;
	}
}
