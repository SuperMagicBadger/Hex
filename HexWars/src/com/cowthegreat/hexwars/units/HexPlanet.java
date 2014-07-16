package com.cowthegreat.hexwars.units;

import com.cowthegreat.hexwars.hex.HexKey;

public class HexPlanet implements HexEntity {
	
	HexKey position = HexKey.obtainKey();
	ResourceNode resources = new ResourceNode();
	
	@Override
	public HexKey getPosition() {
		return (position);
	}

	@Override
	public void setPosition(HexKey key) {
		position.set(key);
	}

	@Override
	public boolean isStatic() {
		return true;
	}
}
