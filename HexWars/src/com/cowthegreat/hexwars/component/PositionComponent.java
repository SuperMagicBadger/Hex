package com.cowthegreat.hexwars.component;

import com.badlogic.gdx.math.Vector3;

public class PositionComponent implements Component{
	@Override
	public componentType getComponentType() {
		return componentType.POSITION;
	}
	
	public Vector3 position = new Vector3();
	public float rotation = 0;
	
	public void face(Vector3 direction){
		float value = position.x * direction.y + position.y * direction.x;
		rotation = (float) Math.toDegrees(Math.acos(value));
	}
}
