package com.cowthegreat.hexwars.framework;

import com.cowthegreat.hexwars.component.HealthComponent;
import com.cowthegreat.hexwars.component.InventoryComponent;
import com.cowthegreat.hexwars.component.RenderComponent;
import com.cowthegreat.hexwars.component.WeaponComponent;
import com.cowthegreat.hexwars.hex.HexKey;

public class Unit extends Entity{
	public String unitName;
	public int team;
	public int ap, maxAP;
	
	public HexKey hexPosition;
	
	RenderComponent renderComponent;
	HealthComponent health;
	WeaponComponent wep;
	InventoryComponent inv;
}
