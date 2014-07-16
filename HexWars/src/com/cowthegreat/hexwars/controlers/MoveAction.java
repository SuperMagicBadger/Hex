package com.cowthegreat.hexwars.controlers;

import com.badlogic.gdx.math.Vector3;
import com.cowthegreat.hexwars.graphics.Updateable;

public class MoveAction implements Updateable{
	
	public Vector3 source, destination, value;
	public float duration, timeElapsed;
	
	public MoveAction(){
		source = new Vector3();
		destination = new Vector3();
		value = new Vector3();
		
		duration = 1;
		timeElapsed = 1;
	}

	// --------------------------------
	// set up--------------------------
	// --------------------------------
	public void setDestination(Vector3 d){
		setDestination(d.x, d.y, d.z);
	}
	
	public void setDestination(float x, float y, float z){
		destination.set(x, y, z);
	}
	
	public void setSource(Vector3 s){
		setSource(s.x, s.y, s.z);
	}
	
	public void setSource(float x, float y, float z){
		source.set(x, y, z);
	}
	
	public void setValue(Vector3 v){
		value = v;
	}
	
	public void reset(){
		timeElapsed = 0;
	}

	// --------------------------------	
	// control-------------------------
	// --------------------------------
	
	public Vector3 getPosition(float i){
		return value.set(source).lerp(destination, i);
	}
	
	public Vector3 getPosition(){
		return value;
	}
	
	@Override
	public void update(float delta) {
		timeElapsed += delta;
		timeElapsed = timeElapsed > duration ? duration : timeElapsed;
		getPosition(timeElapsed / duration);
	}
	
	// --------------------------------
	// flags---------------------------
	// --------------------------------
	
	public boolean isComplete(){
		return timeElapsed >= duration;
	}
}
