package com.cowthegreat.hexwars.component;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.cowthegreat.hexwars.hex.HexBuffer.HexDescriptor;

public class RenderComponent implements Component {

	@Override
	public componentType getComponentType() {
		return componentType.RENDER;
	}

	public ModelInstance model = null;
	public HexDescriptor descriptor = null;
}
