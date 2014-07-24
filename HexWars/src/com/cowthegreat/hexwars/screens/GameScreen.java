package com.cowthegreat.hexwars.screens;

import java.util.HashMap;
import java.util.HashSet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.cowthegreat.hexwars.HexWars;
import com.cowthegreat.hexwars.controlers.CameraController;
import com.cowthegreat.hexwars.graphics.Unit;
import com.cowthegreat.hexwars.graphics.Unit.MovementUpdateable;
import com.cowthegreat.hexwars.hex.HexBuffer;
import com.cowthegreat.hexwars.hex.HexEntity;
import com.cowthegreat.hexwars.hex.HexKey;
import com.cowthegreat.hexwars.hex.HexMap;
import com.cowthegreat.hexwars.hex.HexMath;
import com.cowthegreat.hexwars.hex.HexMath.Orientation;

public class GameScreen extends InputAdapter implements Screen{
	public static String SCREEN_TAG = "game_screen";
	HexWars game;

	// Camera
	PerspectiveCamera camera;
	CameraController cameraCon;
	
	// Batches
	Environment environ;
	ModelBatch modelBuffer;
	HexBuffer hexBuffer;
	
	// Map Data
	HexMap map;
	
	// Model render list
	HashMap<HexEntity, Unit> unitList;
	ModelInstance planetInstance;
	
	// Lua
//	LuaState state;
	
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
		
		unitList = new HashMap<HexEntity, Unit>();
		
		modelBuffer = new ModelBatch();
		hexBuffer = new HexBuffer(200);
		
		environ = new Environment();
		environ.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environ.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 1f, -1f, 1f));
		
//		state =  LuaStateFactory.newLuaState();/
	}
	
	HashSet<HexEntity> hen = new HashSet<HexEntity>();
	
	@Override
	public void render(float delta) {		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		cameraCon.update();
		
		// DETERMINE SCREEN HEX DIMENSIONS
		Rectangle r = cameraCon.getDimensions();
		HexKey u_left = HexMath.get().pixelToHex(r.x, r.y);
		HexKey b_right = HexMath.get().pixelToHex(r.x + r.width, r.y + r.height);
		
		// RENDER THE HEX MAP
		hexBuffer.begin(camera.combined);
		hexBuffer.setGlowStrength(0.5f);
		hexBuffer.setInnerRadius(0.15f);
		hexBuffer.setOuterRadius(0.95f);
		hexBuffer.setOutlineColor(0.75f, 0.75f, 0.75f, 1);
		hen.clear();
		HexKey hk = HexKey.obtainKey();
		for(int i = u_left.oddr_Q() - 2; i < b_right.oddr_Q() + 2; i++){
			for(int j = u_left.oddr_R() - 2; j < b_right.oddr_R() + 2; j++){
				hk.setOddR(i, j);
				hexBuffer.draw(hk, HexMath.get(), map.getDescriptor(hk));
				if(map.get(hk) != null){
					hen.add(map.get(hk));
				}
			}
		}
		hk.release();
		hexBuffer.end();
		
		// RENDER 3D MODELS
		modelBuffer.begin(camera);
		for(HexEntity he : unitList.keySet()){
			Unit u = unitList.get(he);
			u.update(delta);
			if(hen.contains(he)){
				modelBuffer.render(u, environ);
			}
		}
		modelBuffer.end();
	}

	@Override
	public void resize(int width, int height) {
		
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(cameraCon);
		map.generate(10, 10, 4, 0);
		
		for(HexEntity he : map.enteties){
			Unit u = new Unit(game.miPool.obtainInstance("planet.g3db"));
			Vector2 v2 = HexMath.get().hexToPixel(he.getPosition());
			u.setPosition(v2.x, 0, v2.y);
			MovementUpdateable mover = new MovementUpdateable(u);
			mover.finalPosition.set(50, 0, 50);
			mover.duration = 25;
			u.updateables.add(mover);
			u.updateables.add(new Unit.RotationUpdateable(u, Vector3.Y, 20));
			unitList.put(he, u);
		}
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
