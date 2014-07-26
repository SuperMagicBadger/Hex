package com.cowthegreat.hexwars.component;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

public class RenderComponent implements Component {

	@Override
	public componentType getComponentType() {
		return componentType.RENDER;
	}

	public ModelInstance model = null;
	
	public void render(ModelBatch batch, Environment environ){
		
	}
}
