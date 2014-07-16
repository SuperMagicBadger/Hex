package com.cowthegreat.hexwars.units;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import com.cowthegreat.hexwars.hex.HexKey;

public class HexMap {
	public HashSet<HexEntity> enteties;
	public HashMap<HexKey, HexEntity> locations;
	public HashMap<HexKey, HexEntity> staticLocations;
	
	public int hexWidth;
	public int hexHeight;
	
	public HexMap(){
		enteties = new HashSet<HexEntity>();
		locations = new HashMap<HexKey, HexEntity>();
		staticLocations = new HashMap<HexKey, HexEntity>();
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
	
	public void update(){
		locations.clear();
		for(HexEntity hent : enteties){
			locations.put(hent.getPosition(), hent);
		}
	}
	
	public boolean inBounds(HexKey hk){
		return (hk.oddr_Q() > 0 && hk.oddr_Q() <= hexWidth) && (hk.oddr_R() > 0 && hk.oddr_R() <= hexHeight);
	}
	
	public void generate(int w, int h, int pCount, int uCount){
		Random rng = new Random();
		HexKey temKey = HexKey.obtainKey();
		
		// set dims
		hexWidth = w;
		hexHeight = h;
		
		// gen units
		
		// gen planets
		temKey.setOddR(0, 0);
		HexPlanet hp = new HexPlanet();
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
