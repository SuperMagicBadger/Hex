package com.cowthegreat.hexwars.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Rectangle;
import com.cowthegreat.hexwars.controlers.CameraController;
import com.cowthegreat.hexwars.framework.Entity;
import com.cowthegreat.hexwars.framework.MovementSystem;
import com.cowthegreat.hexwars.framework.RenderSystem;
import com.cowthegreat.hexwars.hex.HexKey;
import com.cowthegreat.hexwars.hex.HexMap;
import com.cowthegreat.hexwars.hex.HexMath;

public class ComponentScreen implements Screen {

	HexMap map;
	
	RenderSystem renderSys;
	MovementSystem movementSys;

	ArrayList<Entity> entityList;
	
	PerspectiveCamera raw;
	CameraController con;

	public ComponentScreen() {
		renderSys = new RenderSystem();
		movementSys = new MovementSystem();
		
		entityList = new ArrayList<Entity>();
		
		raw = new PerspectiveCamera();
		con = new CameraController(raw);
		
		map = new HexMap();
	}
	
	@Override
	public void show() {
		map.generate(5, 5, 4, 0);
		Gdx.input.setInputProcessor(con);
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void render(float delta) {
		// update camera
		con.update();
		
		// update entities
		for(Entity e : entityList){
			movementSys.update(e, delta);
		}
		
		// render hex's
		Rectangle r = con.getDimensions();
		HexKey u_left = HexMath.get().pixelToHex(r.x, r.y);
		HexKey b_right = HexMath.get().pixelToHex(r.x + r.width, r.y + r.height);
		
		renderSys.renderHex(raw, map, u_left, b_right);
		
		// render models
		renderSys.renderModel(raw, entityList);
	}

	@Override
	public void dispose() {
	}
}
