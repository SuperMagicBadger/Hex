package com.cowthegreat.hexwars.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Rectangle;
import com.cowthegreat.hexwars.CameraController;
import com.cowthegreat.hexwars.framework.Entity;
import com.cowthegreat.hexwars.framework.HexPositionMap;
import com.cowthegreat.hexwars.framework.MovementSystem;
import com.cowthegreat.hexwars.framework.RenderSystem;
import com.cowthegreat.hexwars.hex.HexKey;
import com.cowthegreat.hexwars.hex.HexMath;
import com.cowthegreat.hexwars.hex.HexMath.Orientation;

public class ComponentScreen implements Screen {

	public static final String SCREEN_TAG = "comonent_screen";
	
	RenderSystem renderSys;

	ArrayList<Entity> entityList;
	
	PerspectiveCamera raw;
	CameraController con;
	
	HexPositionMap hexPMap;

	public ComponentScreen() {
		// systems
		renderSys = new RenderSystem();
		entityList = new ArrayList<Entity>();

		hexPMap = new HexPositionMap(15, 15);
		hexPMap.gen(0, 0, 0);
		
		HexMath.get().o = Orientation.POINTY; 
		HexMath.get().side = 5;
		
		// camera
		raw = new PerspectiveCamera();
		raw = new PerspectiveCamera();
		raw.fieldOfView = 67;
		raw.viewportWidth = Gdx.graphics.getWidth();
		raw.viewportHeight = Gdx.graphics.getHeight();
		raw.position.set(0,  0,  15);
		raw.lookAt(0, 0, 0);
		raw.near = 1;
		raw.far = 500;
		con = new CameraController(raw);
		con.setScrollBounds(
						HexMath.get().getWidth() / -2, 
						HexMath.get().getHeight() / -2,
						hexPMap.hexWidth * HexMath.get().getWidth() - HexMath.get().getWidth() / 2,
						hexPMap.hexHeight * HexMath.get().getHeight() - HexMath.get().getHeight() / 2);

	}
	
	@Override
	public void show() {
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
		// CLEAN UP
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		// UPDATE
		con.update();
		hexPMap.update();
		for(Entity e : entityList){
			MovementSystem.update(e, delta);
		}
		
		// RENDER HEX
		Rectangle r = con.getDimensions();
		HexKey u_left = HexMath.get().pixelToHex(r.x, r.y);
		HexKey b_right = HexMath.get().pixelToHex(r.x + r.width, r.y + r.height);
		renderSys.renderHex(raw, hexPMap, u_left, b_right);
		u_left.release();
		b_right.release();
		
		// RENDER MODELS
		renderSys.renderModel(raw, entityList);
	}

	@Override
	public void dispose() {
	}
}
