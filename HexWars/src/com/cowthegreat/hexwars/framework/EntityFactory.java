package com.cowthegreat.hexwars.framework;

import com.badlogic.gdx.graphics.Color;
import com.cowthegreat.hexwars.component.HexPositionComponent;
import com.cowthegreat.hexwars.component.PositionComponent;

public class EntityFactory {

	public static Entity createPlanet(){
		Entity e = new Entity();
		
		HexPositionComponent hexPos = new HexPositionComponent();
		PositionComponent pos = new PositionComponent();
		
		hexPos.position.setOddR(0, 0);
		hexPos.descritor.inner = Color.GRAY;
		hexPos.descritor.outer = Color.GREEN;
		
		e.addComponent(pos);
		e.addComponent(hexPos);
		
		return e;
	}
}
