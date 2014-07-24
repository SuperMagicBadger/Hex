package com.cowthegreat.hexwars.hex;

import com.cowthegreat.hexwars.hex.HexBuffer.HexDescriptor;


public abstract class HexEntity {	
	public abstract HexKey getPosition();
	public abstract void setPosition(HexKey key);
	public abstract boolean isStatic();
	public abstract HexDescriptor getHexDescriptor();
	
	boolean hasComponent(){
		return false;
	}
}
