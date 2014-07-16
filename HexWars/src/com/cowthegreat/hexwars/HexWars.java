package com.cowthegreat.hexwars;


import java.util.HashMap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.cowthegreat.hexwars.hex.HexMath;
import com.cowthegreat.hexwars.hex.HexMath.Orientation;
import com.cowthegreat.hexwars.screens.GameScreen;
import com.cowthegreat.hexwars.screens.TestRenderScreen;

public class HexWars extends Game {
	
	public OrthographicCamera camera;
	public ShapeRenderer shapeBatch;
	public FPSLogger fpslogger;
	
	public AssetsPool assets;
	public AssetManager am;
	
	private HashMap<String, Screen> screenMap;
	
	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(w, h);
		shapeBatch = new ShapeRenderer();

		HexMath.get().o = Orientation.POINTY;
		HexMath.get().side = 30f;

		am = new AssetManager();
		assets = new AssetsPool(am);
		
		loadAssets();
		loadScreens();
		setScreen(TestRenderScreen.SCREEN_TAG);
		setScreen(GameScreen.SCREEN_TAG);
		
		fpslogger = new FPSLogger();
		
	}

	public void loadAssets(){
		am.load("untitled.g3db", Model.class);
		am.load("planet.g3db", Model.class);
		am.load("ui.atlas", TextureAtlas.class);
		am.load("fonts/small_font_25.fnt", BitmapFont.class);
		am.finishLoading();
	}
	
	public void loadScreens(){
		screenMap = new HashMap<String, Screen>();
		screenMap.put(GameScreen.SCREEN_TAG, new GameScreen(this));
		screenMap.put(TestRenderScreen.SCREEN_TAG, new TestRenderScreen(this));
	}
	
	@Override
	public void dispose() {
		shapeBatch.dispose();
	}
	
	public void setScreen(String screenName){
		if(screenMap.containsKey(screenName))
			setScreen(screenMap.get(screenName));
	}
}
