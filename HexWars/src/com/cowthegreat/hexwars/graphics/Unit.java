package com.cowthegreat.hexwars.graphics;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.cowthegreat.hexwars.hex.HexMath;
import com.cowthegreat.hexwars.units.HexEntity;

public class Unit implements Updateable{
	
	public ModelInstance modelInst;
	public HexEntity entity;
	
	public ArrayList<Updateable> updateables;
	
	public HexMath hexMath;
	
	float f;
	
	public Unit(ModelInstance m, HexMath hm, HexEntity ent){
		modelInst = m;
		entity = ent;
		updateables = new ArrayList<Updateable>();
		
		hexMath = hm;
	}
	
	@Override
	public void update(float delta){
		f += delta * 100;
		f = f > 360 ? f - 360 : f;
		
		Vector2 v2 = hexMath.hexToPixel(entity.getPosition());

		modelInst.transform.setToTranslation(v2.x, 0, v2.y);
		modelInst.transform.rotate(Vector3.Y, f);
		
		for(Updateable u : updateables){
			u.update(delta);
		}
	}
}
