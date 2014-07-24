package com.cowthegreat.hexwars.graphics;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class Unit implements Updateable, RenderableProvider{
	
	public ModelInstance modelInst;
	public ArrayList<Updateable> updateables;
	
	public Vector3 position;
	public float rotation;
	
	public Unit(ModelInstance m){
		modelInst = m;
		updateables = new ArrayList<Updateable>();
		
		position = new Vector3();
		rotation = 0;
	}
	
	@Override
	public void update(float delta){
		for(Updateable u : updateables){
			u.update(delta);
		}
		modelInst.transform.idt();
		modelInst.transform.translate(position);
		modelInst.transform.rotate(Vector3.Y, rotation);
	}
	
	public void setPosition(float x, float y, float z){
		position.set(x, y, z);
	}
	
	@Override
	public void getRenderables(Array<Renderable> renderables,
			Pool<Renderable> pool) {
		modelInst.getRenderables(renderables, pool);
	}
	
	public static class RotationUpdateable implements Updateable{
		Unit unit;
		Vector3 axis;
		float rotation;
		
		public RotationUpdateable(Unit unit, Vector3 axis, float rotationSpeed) {
			this.unit = unit;
			this.axis = axis;
			this.rotation = rotationSpeed;
		}
		
		@Override
		public void update(float delta) {
			unit.rotation = delta * rotation;
		}
	}

	public static class MovementUpdateable implements Updateable{
		Unit unit;
		public float elapsedTime, duration;
		public Vector3 initialPosition, finalPosition, temp;

		public MovementUpdateable(Unit unit) {
			this.unit = unit;
			
			initialPosition = new Vector3();
			finalPosition = new Vector3();
			temp = new Vector3();
			
			elapsedTime = 1;
			duration = 1;

			initialPosition.set(unit.position);
		}
		
		@Override
		public void update(float delta) {
			elapsedTime += delta;
			float t = elapsedTime / duration;
			if(t > 1) return;
			
			temp.set(initialPosition);
			temp.lerp(finalPosition, t);

			unit.position.set(temp);
		}
	}
}
