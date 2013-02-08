package com.bengreenier.blackhole.update;

import java.io.Serializable;
import java.util.ArrayList;

public interface UpdateComponent extends Serializable {

	public String getPath();
	public float getVersion();
	public ArrayList<UpdateComponent> getChildren();
	
	
	
}
