package com.cowthegreat.hexwars.framework;

import com.cowthegreat.hexwars.component.HexPositionComponent;
import com.cowthegreat.hexwars.component.PositionComponent;

public class EntityFactory {

	public Entity createPlanet(){
		Entity e = new Entity();
		
		HexPositionComponent hexPos = new HexPositionComponent();
		PositionComponent pos = new PositionComponent();
		
		e.addComponent(pos);
		e.addComponent(hexPos);
		
		return e;
	}
}
