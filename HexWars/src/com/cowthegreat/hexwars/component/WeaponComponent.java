package com.cowthegreat.hexwars.component;

public class WeaponComponent implements Component {
	@Override
	public componentType getComponentType() {
		return componentType.WEAPONS;
	}
	
	public int range;
	public int damagePerPoint;
	public int ammoCost;
	public int accuracy;
}
