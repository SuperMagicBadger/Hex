package com.cowthegreat.hexwars.framework;

import java.util.Collection;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.cowthegreat.hexwars.component.Component.componentType;
import com.cowthegreat.hexwars.component.HexPositionComponent;
import com.cowthegreat.hexwars.component.PositionComponent;
import com.cowthegreat.hexwars.component.RenderComponent;
import com.cowthegreat.hexwars.hex.HexBuffer;
import com.cowthegreat.hexwars.hex.HexKey;
import com.cowthegreat.hexwars.hex.HexMap;
import com.cowthegreat.hexwars.hex.HexMath;

public class RenderSystem {
	public HexBuffer hexBuffer;
	public ModelBatch modelBuffer;
	public Environment environ;
	
	public RenderSystem(){
		hexBuffer = new HexBuffer(100);
		modelBuffer = new ModelBatch();
		
		environ = new Environment();
		environ.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environ.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 1f, -1f, 1f));
	}
	
	public void renderModel(Entity entity){
		RenderComponent ren = (RenderComponent) entity.getComponent(componentType.RENDER);
		PositionComponent pos = (PositionComponent) entity.getComponent(componentType.POSITION);
		
		if(ren != null && pos != null){
			ren.model.transform.idt();
			ren.model.transform.translate(pos.position);
			ren.model.transform.rotate(Vector3.Y, pos.rotation);
			modelBuffer.render(ren.model, environ);
		}
	}
	
	public void renderModel(Camera cam, Collection<Entity> entityList){
		modelBuffer.begin(cam);
		for(Entity e : entityList){
			renderModel(e);
		}
		modelBuffer.end();
	}
	
	public void renderHex(Camera camera, HexMap map, HexKey u_left, HexKey b_right){
		hexBuffer.begin(camera.combined);
		hexBuffer.setGlowStrength(0.5f);
		hexBuffer.setInnerRadius(0.15f);
		hexBuffer.setOuterRadius(0.95f);
		hexBuffer.setOutlineColor(0.75f, 0.75f, 0.75f, 1);

		HexKey hk = HexKey.obtainKey();
		for(int i = u_left.oddr_Q() - 2; i < b_right.oddr_Q() + 2; i++){
			for(int j = u_left.oddr_R() - 2; j < b_right.oddr_R() + 2; j++){
				hk.setOddR(i, j);
				hexBuffer.draw(hk, HexMath.get(), map.getDescriptor(hk));
			}
		}
		hk.release();
		hexBuffer.end();
	}
	
	public void renderHex(Entity entity){
		RenderComponent ren = (RenderComponent) entity.getComponent(componentType.RENDER);
		HexPositionComponent pos = (HexPositionComponent) entity.getComponent(componentType.HEX_POSITION);
		
		if(ren != null && pos != null){
			hexBuffer.draw(pos.position, HexMath.get(), ren.descriptor);
		}
	}
}