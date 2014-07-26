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
import com.cowthegreat.hexwars.hex.HexMath;
import com.cowthegreat.hexwars.hex.HexMath.Orientation;
import com.cowthegreat.hexwars.screens.ComponentScreen;

public class HexWars extends Game {
	
	public OrthographicCamera camera;
	public FPSLogger fpslogger;
	
	public ModelInstancePool miPool;
	public AssetManager assets;
	
	private HashMap<String, Screen> screenMap;
	
	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(w, h);

		HexMath.get().o = Orientation.POINTY;
		HexMath.get().side = 30f;

		assets = new AssetManager();
		miPool = new ModelInstancePool(assets);
		
		loadAssets();
		loadScreens();
		setScreen(ComponentScreen.SCREEN_TAG);
		
		fpslogger = new FPSLogger();
		
	}

	public void loadAssets(){
		assets.load("untitled.g3db", Model.class);
		assets.load("planet.g3db", Model.class);
		assets.load("ui.atlas", TextureAtlas.class);
		assets.load("fonts/small_font_25.fnt", BitmapFont.class);
		assets.finishLoading();
	}
	
	public void loadScreens(){
		screenMap = new HashMap<String, Screen>();
		screenMap.put(ComponentScreen.SCREEN_TAG, new ComponentScreen());
	}
	
	@Override
	public void dispose() {
		assets.dispose();
	}
	
	public void setScreen(String screenName){
		if(screenMap.containsKey(screenName))
			setScreen(screenMap.get(screenName));
	}
}
