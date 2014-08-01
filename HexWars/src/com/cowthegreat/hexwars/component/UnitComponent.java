package com.cowthegreat.hexwars.component;

public class UnitComponent implements Component{
	@Override
	public componentType getComponentType() {
		return componentType.UNIT_DATA;
	}
	
	public String unitName;
	public int team;
	public int AP;
	public int maxAP;
}
