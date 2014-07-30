package com.cowthegreat.hexwars.component;

public class InventoryComponent implements Component{
	@Override
	public componentType getComponentType() {
		return componentType.INVENTORY;
	}
	
	public int supplies = 0;
	public int ammo = 0;
	public int fuel = 0;
	public int materials = 0;
	public int chem = 0;
	
	public int maxSupplies = -1;
	public int maxAmmo = -1;
	public int maxFuel = -1;
	public int maxMaterials = -1;
	public int maxChem = -1;
	
	public void setInventory(int s, int m, int c, int a, int f){
		supplies = s;
		materials = m;
		chem = c;
		ammo = a;
		fuel = f;
	}
	
	public void setMax(int s, int m, int c, int a, int f){
		maxSupplies = s;
		maxMaterials = m;
		maxChem = c;
		maxAmmo = a;
		maxFuel = f;
	}
}
