package com.cowthegreat.hexwars.framework;

import java.util.HashMap;
import java.util.HashSet;

import com.badlogic.gdx.graphics.Color;
import com.cowthegreat.hexwars.component.Component.componentType;
import com.cowthegreat.hexwars.component.HexPositionComponent;
import com.cowthegreat.hexwars.hex.HexBuffer.HexDescriptor;
import com.cowthegreat.hexwars.hex.HexKey;

public class HexPositionMap {

	HashSet<Entity> entitySet;
	HashMap<HexKey, Entity> entityLocations;
	
	public int hexWidth;
	public int hexHeight;
	
	private HexDescriptor defaultDescriptor;
	private HexDescriptor invalidDescriptor;
	
	public HexPositionMap(int width, int height){
		hexWidth = width;
		hexHeight = height;
		
		entitySet = new HashSet<Entity>();
		entityLocations = new HashMap<HexKey, Entity>();
		
		defaultDescriptor = new HexDescriptor(Color.BLACK, Color.BLUE);
		invalidDescriptor = new HexDescriptor(Color.BLACK, new Color(0.1f, 0.1f, 0.1f, 1f));
	}
	
	public void add(Entity ent){
		if(entitySet.contains(ent)) return;
		HexPositionComponent hexPos = (HexPositionComponent) ent.getComponent(componentType.HEX_POSITION);
		if(hexPos != null){
			entitySet.add(ent);
		} else {
			System.out.println("did not contain hex position");
		}
	}
	
	public void remove(Entity ent){
		HexPositionComponent hexPos = (HexPositionComponent) ent.getComponent(componentType.HEX_POSITION);
		if(hexPos != null){
			entityLocations.remove(hexPos.position);
		}
		entitySet.remove(ent);
		
	}
	
	public Entity get(HexKey position){
		return entityLocations.get(position);
	}
	
	public HexDescriptor getDescriptor(HexKey hk){
		if(!inBounds(hk)){
			return invalidDescriptor;
		}
		
		if(entityLocations.containsKey(hk)){
			Entity e = entityLocations.get(hk);
			HexPositionComponent hexPos =
					(HexPositionComponent) e.getComponent(componentType.HEX_POSITION);
			return hexPos.descritor;
		}
		
		return defaultDescriptor;
	}
	
	public void update(){
		entityLocations.clear();
		for(Entity e : entitySet){
			HexPositionComponent hexPos = 
					(HexPositionComponent) e.getComponent(componentType.HEX_POSITION);
			entityLocations.put(hexPos.position, e);
		}
	}
	
	public boolean inBounds(HexKey hk){
		return (hk.oddr_Q() >= 0 && hk.oddr_Q() < hexWidth) && (hk.oddr_R() >= 0 && hk.oddr_R() < hexHeight);
	}
	
	public void gen(int w, int h, int count){
		Entity e = EntityFactory.createPlanet();
		HexKey hk = HexKey.obtainKey();
		hk.setOddR(0, 0);
		MovementSystem.place(e, hk);
		add(e);
		
		e = EntityFactory.createPlanet();
		hk = HexKey.obtainKey();
		hk.setOddR(1, 1);
		MovementSystem.place(e, hk);
		add(e);
	}
	
	/*
	public void generate(int w, int h, int pCount, int uCount){
		Random rng = new Random();
		HexKey temKey = HexKey.obtainKey();
		HexPlanet hp;
		
		// set dims
		hexWidth = w;
		hexHeight = h;
		
		// gen units
		
		// gen planets
		temKey.setOddR(0, 0);
		hp = new HexPlanet();
		hp.setPosition(temKey);
		add(hp);
		
		for(int i = 1; i < pCount; i++){
			hp = new HexPlanet();
			int x = rng.nextInt(hexWidth);
			int y = rng.nextInt(hexHeight);
			temKey.setOddR(x, y);
			hp.setPosition(temKey);
			add(hp);
			System.out.println(temKey + " --- " + x + " " + y);
		}
		
		temKey.release();
	}
	*/
}
