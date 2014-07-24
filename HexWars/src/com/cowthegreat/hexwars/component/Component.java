package com.cowthegreat.hexwars.component;

public interface Component {	
	public enum componentType{
		POSITION, 
		HEX_POSITION,
		MOVEMENT,
		INVENTORY,
		WEAPONS,
		HEALTH,
		RENDER;
	}
	
	public componentType getComponentType();
}
