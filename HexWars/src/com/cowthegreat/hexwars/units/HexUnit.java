package com.cowthegreat.hexwars.units;

import com.badlogic.gdx.graphics.Color;
import com.cowthegreat.hexwars.hex.HexEntity;
import com.cowthegreat.hexwars.hex.HexKey;
import com.cowthegreat.hexwars.hex.HexBuffer.HexDescriptor;

public class HexUnit extends HexEntity {
	
	private static HexDescriptor defaultDescriptor = new HexDescriptor(new Color(0.25f, 0f, 0f, 1f), Color.BLACK);
	
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
	
	@Override
	public HexDescriptor getHexDescriptor() {
		return defaultDescriptor;
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
