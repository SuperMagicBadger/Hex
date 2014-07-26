package com.cowthegreat.hexwars.component;

import com.cowthegreat.hexwars.hex.HexBuffer.HexDescriptor;
import com.cowthegreat.hexwars.hex.HexKey;

public class HexPositionComponent implements Component{
	@Override
	public componentType getComponentType() {
		return componentType.HEX_POSITION;
	}
	
	public HexKey position = HexKey.obtainKey();
	public HexDescriptor descritor = new HexDescriptor();
}
