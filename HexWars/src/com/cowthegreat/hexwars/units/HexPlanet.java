package com.cowthegreat.hexwars.units;

import com.badlogic.gdx.graphics.Color;
import com.cowthegreat.hexwars.hex.HexEntity;
import com.cowthegreat.hexwars.hex.HexKey;
import com.cowthegreat.hexwars.hex.HexBuffer.HexDescriptor;

public class HexPlanet extends HexEntity {
	
	HexKey position = HexKey.obtainKey();
	ResourceNode resources = new ResourceNode();
	
	public static HexDescriptor defaultDesc = new HexDescriptor(Color.BLACK, new Color(0.5f, 0f, 0f, 1f));
	
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
	
	@Override
	public HexDescriptor getHexDescriptor() {
		return defaultDesc;
	}
}
