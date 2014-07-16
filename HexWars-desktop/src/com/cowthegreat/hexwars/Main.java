package com.cowthegreat.hexwars;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		
		AtlasBuilder.traverse("../images");
		
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "HexWars";
		cfg.useGL20 = true;
		cfg.width = 480;
		cfg.height = 320;
		cfg.resizable = true;

		new LwjglApplication(new HexWars(), cfg);
	}
}
