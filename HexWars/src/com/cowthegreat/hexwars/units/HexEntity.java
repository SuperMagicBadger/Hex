package com.cowthegreat.hexwars.units;

import com.cowthegreat.hexwars.hex.HexKey;

public interface HexEntity {
	HexKey getPosition();
	void setPosition(HexKey key);
	boolean isStatic();
}
