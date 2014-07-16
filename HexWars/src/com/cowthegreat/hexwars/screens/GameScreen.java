package com.cowthegreat.hexwars.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Rectangle;
import com.cowthegreat.hexwars.HexWars;
import com.cowthegreat.hexwars.controlers.CameraController;
import com.cowthegreat.hexwars.hex.HexBuffer;
import com.cowthegreat.hexwars.hex.HexKey;
import com.cowthegreat.hexwars.hex.HexMath;
import com.cowthegreat.hexwars.hex.HexMath.Orientation;
import com.cowthegreat.hexwars.units.HexMap;

public class GameScreen extends InputAdapter implements Screen{
	public static String SCREEN_TAG = "game_screen";
	HexWars game;

	// Camera
	PerspectiveCamera camera;
	CameraController cameraCon;
	
	// Batches
	ModelBatch modelBuffer;
	HexBuffer hexBuffer;
	
	// Map Data
	HexMap map;
	
	// Model render list
	ArrayList<ModelInstance> list;
	
	public GameScreen(HexWars hexgame){
		game = hexgame;
		
		// HexMath
		HexMath hm = HexMath.get();
		hm.o = Orientation.POINTY;
		hm.side = 10;
		
		// Camera
		camera = new PerspectiveCamera();
		camera.fieldOfView = 67;
		camera.viewportWidth = Gdx.graphics.getWidth();
		camera.viewportHeight = Gdx.graphics.getHeight();
		camera.position.set(10,  10,  10);
		camera.lookAt(0, 0, 0);
		camera.near = 1;
		camera.far = 500;
		cameraCon = new CameraController(camera);
		cameraCon.setScrollBounds(
				hm.getWidth() / -2, 
				hm.getHeight() / -2,
				10 * hm.getWidth() - hm.getWidth() / 2,
				10 * hm.getHeight() - hm.getHeight() / 2);
		
		map = new HexMap();
		map.generate(10, 10, 4, 0);
		
		modelBuffer = new ModelBatch();
		hexBuffer = new HexBuffer(200);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		cameraCon.update();
		
		Rectangle r = cameraCon.getDimensions();
		
		HexKey u_left = HexMath.get().pixelToHex(r.x, r.y);
		HexKey b_right = HexMath.get().pixelToHex(r.x + r.width, r.y + r.height);
		
		hexBuffer.begin(camera.combined);
		hexBuffer.setGlowStrength(0.5f);
		hexBuffer.setInnerRadius(0.15f);
		hexBuffer.setOuterRadius(0.95f);
		hexBuffer.setOutlineColor(0.75f, 0.75f, 0.75f, 1);
		HexKey hk = HexKey.obtainKey();
		for(int i = u_left.oddr_Q() - 1; i < b_right.oddr_Q() + 1; i++){
			for(int j = u_left.oddr_R() - 1; j < b_right.oddr_R() + 1; j++){
				hk.setOddR(i, j);
				if(!map.inBounds(hk)){
					hexBuffer.draw(hk, HexMath.get(), Color.BLUE, Color.BLACK);
				} else {
					if(map.get(hk) != null){
						hexBuffer.draw(hk, HexMath.get(), Color.BLUE, Color.GREEN);
					} else {
						hexBuffer.draw(hk, HexMath.get(), Color.BLUE, Color.RED);
					}
				}
			}
		}
		hk.release();
		hexBuffer.end();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(cameraCon);
		System.out.println();
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
	public void dispose() {

	}

}
