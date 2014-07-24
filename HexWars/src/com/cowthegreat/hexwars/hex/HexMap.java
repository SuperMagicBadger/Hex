package com.cowthegreat.hexwars.hex;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.cowthegreat.hexwars.hex.HexBuffer.HexDescriptor;
import com.cowthegreat.hexwars.units.HexPlanet;

public class HexMap {
	public HashSet<HexEntity> enteties;
	public HashMap<HexKey, HexEntity> locations;
	public HashMap<HexKey, HexEntity> staticLocations;
	
	public int hexWidth;
	public int hexHeight;
	
	private HexDescriptor defaultDescriptor;
	private HexDescriptor invalidDescriptor;
	
	public HexMap(){
		enteties = new HashSet<HexEntity>();
		locations = new HashMap<HexKey, HexEntity>();
		staticLocations = new HashMap<HexKey, HexEntity>();
		
		defaultDescriptor = new HexDescriptor(Color.BLACK, Color.BLUE);
		invalidDescriptor = new HexDescriptor(Color.BLACK, new Color(0.1f, 0.1f, 0.1f, 1f));
	}
	
	public void add(HexEntity ent){
		enteties.add(ent);
		if(ent.isStatic()){
			staticLocations.put(ent.getPosition(), ent);
		} else {
			locations.put(ent.getPosition(), ent);
		}
	}
	
	public void remove(HexEntity ent){
		enteties.remove(ent);
		if(ent.isStatic()){
			staticLocations.remove(ent.getPosition());
		} else {
			locations.remove(ent.getPosition());
		}
	}
	
	public HexEntity get(HexKey position){
		if(locations.containsKey(position)){
			return locations.get(position);
		} else {
			return staticLocations.get(position);
		}
	}
	
	public HexDescriptor getDescriptor(HexKey hk){
		if(!inBounds(hk)){
			return invalidDescriptor;
		} else if (get(hk) != null){
			return get(hk).getHexDescriptor();
		}
		return defaultDescriptor;
	}
	
	public void update(){
		locations.clear();
		for(HexEntity hent : enteties){
			locations.put(hent.getPosition(), hent);
		}
	}
	
	public boolean inBounds(HexKey hk){
		return (hk.oddr_Q() >= 0 && hk.oddr_Q() < hexWidth) && (hk.oddr_R() >= 0 && hk.oddr_R() < hexHeight);
	}
	
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
}
