package com.cowthegreat.hexwars.graphics;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class GameSprite extends Sprite implements Comparable<GameSprite>{
	int z;
	
	@Override
	public int compareTo(GameSprite o) {
		return z - o.z;
	}
}
