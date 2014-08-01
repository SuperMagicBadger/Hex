package com.cowthegreat.hexwars.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.cowthegreat.hexwars.CameraController;
import com.cowthegreat.hexwars.HexWars;
import com.cowthegreat.hexwars.framework.Entity;
import com.cowthegreat.hexwars.framework.EntityFactory;
import com.cowthegreat.hexwars.framework.HexPositionMap;
import com.cowthegreat.hexwars.framework.MovementSystem;
import com.cowthegreat.hexwars.framework.RenderSystem;
import com.cowthegreat.hexwars.hex.HexKey;
import com.cowthegreat.hexwars.hex.HexMath;
import com.cowthegreat.hexwars.hex.HexMath.Orientation;
import com.cowthegreat.hexwars.ui.InventoryTable;
import com.cowthegreat.hexwars.ui.WeaponTable;

public class ComponentScreen implements Screen {

	public static final String SCREEN_TAG = "comonent_screen";
	
	RenderSystem renderSys;

	ArrayList<Entity> entityList;
	
	PerspectiveCamera raw;
	CameraController con;
	
	GestureDetector gestures;
	
	HexPositionMap hexPMap;

	EntityFactory ef;
	
	HexKey hk;
	
	// menus and overlays
	Stage uiStage;
	InventoryTable inv;
	WeaponTable wep;
	
	public ComponentScreen(HexWars game) {
		// systems
		ef = new EntityFactory(game.assets);
		
		renderSys = new RenderSystem();
		entityList = new ArrayList<Entity>();

		hexPMap = new HexPositionMap(15, 15);
		
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

		// menus and overlays
		uiStage = new Stage();
		inv = new InventoryTable(game);
		wep = new WeaponTable(game);
		wep.setPosition(inv.getWidth() + 10, 0);
		
		uiStage.addActor(inv);
		uiStage.addActor(wep);
		
		hk = HexKey.obtainKey();
		
		gestures = new GestureDetector(new GestureAdapter(){
			@Override
			public boolean tap(float x, float y, int count, int button) {
				Vector3 v3 = new Vector3();
				con.pick(x, y, v3);
				hk.release();
				hk = HexMath.get().pixelToHex(v3.x, v3.z);
				
				inv.display(hexPMap.get(hk));
				wep.display(hexPMap.get(hk));
				return true;
			}
		});
	}
	
	@Override
	public void show() {
		// generate map
		for(int i = 0; i < 3; i++){
			Entity e = ef.createPlanet();
			HexKey k = HexKey.obtainKey();
			k.setOddR((int) (Math.random() * hexPMap.hexWidth), (int) (Math.random() * hexPMap.hexHeight));
			MovementSystem.place(e, k);;
			hexPMap.add(e);
			entityList.add(e);
		}
		
		Entity e = ef.createFleet();
		MovementSystem.place(e, HexKey.obtainKey(0, 0));
		entityList.add(e);
		hexPMap.add(e);
		
		Gdx.input.setInputProcessor(new InputMultiplexer(con, gestures, uiStage));
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
		uiStage.act(delta);
		con.update();
		
		for(Entity e : entityList){
			MovementSystem.update(e, delta);
		}
		hexPMap.update();
		
		// RENDER HEX
		Rectangle r = con.getDimensions();
		HexKey u_left = HexMath.get().pixelToHex(r.x, r.y);
		HexKey b_right = HexMath.get().pixelToHex(r.x + r.width, r.y + r.height);
		renderSys.renderHex(raw, hexPMap, u_left, b_right);
		renderSys.renderModel(raw, entityList);
		u_left.release();
		b_right.release();
		
		renderSys.hexBuffer.setGlowStrength(0);
		renderSys.hexBuffer.setOutlineColor(0f, 1f, 0f, 1);
		renderSys.hexBuffer.setOuterRadius(0.85f);
		renderSys.hexBuffer.begin(raw.combined);
		renderSys.hexBuffer.draw(hk, HexMath.get(), Color.CLEAR, Color.CLEAR);
		renderSys.hexBuffer.end();
		
		// RENDER MODELS
		renderSys.renderModel(raw, entityList);
		
		// RENDER UI
		uiStage.draw();
	}

	@Override
	public void dispose() {
	}
}
