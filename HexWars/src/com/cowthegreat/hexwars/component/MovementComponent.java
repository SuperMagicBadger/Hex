package com.cowthegreat.hexwars.component;

import com.badlogic.gdx.math.Vector3;

public class MovementComponent implements Component {
	@Override
	public componentType getComponentType() {
		return componentType.MOVEMENT;
	}

	public Vector3 source = new Vector3();
	public Vector3 destination = new Vector3();
	public float elapsed;
	public float duration;
	
	public boolean faceMovement = false;
	
	public void reset(){
		elapsed = 0;
		duration = -1;
	}
	
	public Vector3 getPosition(Vector3 pos, float delta){
		if(elapsed == duration) return destination;
		elapsed += delta;
		elapsed = elapsed > duration ? duration : elapsed;
		
		pos.set(source);
		pos.lerp(destination, elapsed / duration);
		return pos;
	}
}
