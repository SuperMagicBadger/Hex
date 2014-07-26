package com.cowthegreat.hexwars.component;

public class HealthComponent implements Component {

	@Override
	public componentType getComponentType() {
		return componentType.HEALTH;
	}

	public int health;
	public int maxHealth;
	public int armor;
}
