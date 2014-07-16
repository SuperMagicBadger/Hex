package com.cowthegreat.hexwars.units;

import com.cowthegreat.hexwars.hex.HexKey;

public class HexUnit implements HexEntity {
	
	private HexKey pos;
	public ResourceNode res;
	
	public int teamID;
	
	@Override
	public HexKey getPosition() {
		return HexKey.obtainKey(pos);
	}

	@Override
	public void setPosition(HexKey key) {
		pos.set(key);
	}

	public boolean isStatic() {
		return false;
	}
	
	public int team(){
		return teamID;
	}
	
	public void setTeam(int teamID){
		this.teamID = teamID;
	}
}
