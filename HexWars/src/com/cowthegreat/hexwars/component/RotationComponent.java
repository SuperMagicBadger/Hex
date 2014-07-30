package com.cowthegreat.hexwars.component;

public class RotationComponent implements Component{
	public float rotationRate;
	public float elapsed;
	public float duration;
	
	@Override
	public componentType getComponentType() {
		return componentType.ROTATION;
	}
}
